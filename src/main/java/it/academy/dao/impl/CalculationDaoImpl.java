package it.academy.dao.impl;

import it.academy.dao.CalculationDao;
import it.academy.pojo.Calculation;
import it.academy.pojo.enums.PaymentType;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;

public class CalculationDaoImpl extends DaoImpl<Calculation, Long> implements CalculationDao {

    public CalculationDaoImpl() {

        super(Calculation.class);
    }

    @Override
    public Map<Calculation, Integer[]> getCalculationsByChapterId(Long chapterId, int page, int count) {

        TypedQuery<Calculation> query = getEm().createQuery(
            "SELECT calc " +
                "FROM Chapter ch, Calculation calc " +
                "WHERE ch.id=:chapterId " +
                "AND calc.chapter.id=ch.id " +

                "ORDER BY calc.month DESC ", Calculation.class);

        query.setParameter("chapterId", chapterId)
            .setMaxResults(count)
            .setFirstResult((page - 1) * count);

        List<Calculation> calculationList = query.getResultList();

        Query queryMtr = getEm().createQuery(
            "SELECT calc, SUM (tr.sum) " +
                "FROM Chapter ch INNER JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id LEFT JOIN MoneyTransfer tr " +
                "ON tr.calculation.id=calc.id " +

                "WHERE tr.type=:trType " +
                "AND calc IN :list " +

                "GROUP BY calc " +
                "ORDER BY calc.month DESC ");

        queryMtr.setParameter("trType", PaymentType.ADVANCE_PAYMENT)
            .setParameter("list", calculationList);

        List<Object[]> listTransferSumAdv = (List<Object[]>) queryMtr.getResultList();
        queryMtr.setParameter("trType", PaymentType.PAYMENT_FOR_WORK);
        List<Object[]> listTransferSumWork = (List<Object[]>) queryMtr.getResultList();

        Map<Calculation, Integer[]> map = new TreeMap<>(Comparator.comparing(Calculation::getMonth).reversed());
        calculationList.forEach(calculation -> {
            Integer[] arr = new Integer[2];
            Arrays.fill(arr, ZERO_INT_VALUE);

            map.put(calculation, arr);
        });
        listTransferSumAdv.forEach(res -> {
            Calculation calculation = (Calculation) res[0];
            long transferSumA = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;
            map.get(calculation)[0] = (int) transferSumA;
        });
        listTransferSumWork.forEach(res -> {
            Calculation calculation = (Calculation) res[0];
            long transferSumP = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;
            map.get(calculation)[1] = (int) transferSumP;
        });
        return map;
    }

    @Override
    public Long getCountOfCalculationsByChapterId(Long chapterId) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(ca) FROM Calculation ca WHERE ca.chapter.id=:chapterId ",
            Long.class);
        return query.setParameter("chapterId", chapterId)
                   .getSingleResult();
    }

    @Override
    public int updateWorkPriceFact(Integer workPriceFact, Long calculationId) {

        Query query = getEm().createQuery("UPDATE Calculation ca SET ca.workPriceFact=:workPriceFact WHERE ca.id=:calculationId");
        return query.setParameter("workPriceFact", workPriceFact)
                   .setParameter("calculationId", calculationId)
                   .executeUpdate();
    }
}