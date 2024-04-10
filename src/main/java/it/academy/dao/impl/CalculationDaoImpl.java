package it.academy.dao.impl;

import it.academy.dao.CalculationDao;
import it.academy.pojo.Calculation;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;

public class CalculationDaoImpl extends DaoImpl<Calculation, Long> implements CalculationDao {

    public CalculationDaoImpl() {

        super(Calculation.class);
    }

    @Override
    public List<Calculation> getCalculationsByChapterId(Long chapterId, int page, int count)
        throws IOException {

        TypedQuery<Calculation> query = getEm().createQuery(
            "SELECT ca FROM Calculation ca WHERE ca.chapter.id=:chapterId ORDER BY ca.month DESC",
            Calculation.class);
        return query.setParameter("chapterId", chapterId)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public Long getCountOfCalculationsByChapterId(Long chapterId) throws NoResultException, IOException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT CONT(ca) FROM Calculation ca WHERE ca.chapter.id=:chapterId ",
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