package it.academy.dao.impl;

import it.academy.dao.ContractorDao;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Contractor;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.*;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;

public class ContractorDaoImpl extends DaoImpl<Contractor, Long> implements ContractorDao {

    public ContractorDaoImpl() {

        super(Contractor.class);
    }

    @Override
    public List<Contractor> getContractors(UserStatus status, int page, int count)
        throws IOException {

        TypedQuery<Contractor> query = getEm().createQuery(
            "SELECT co FROM Contractor co WHERE co.user.status=:status ORDER BY co.name ASC",
            Contractor.class);
        return query.setParameter("status", status)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public Map<Contractor, Integer[]> getContractorsByDeveloperId(Long developerId, ProjectStatus status, int page, int count)
        throws IOException {

        Query queryWorkPrice = getEm().createQuery(
            "SELECT contr, SUM(calc.workPriceFact), us " +
                "FROM Project proj INNER JOIN Chapter ch " +
                "ON ch.project.id=proj.id INNER JOIN Contractor contr " +
                "ON contr.id=ch.contractor.id LEFT JOIN User us " +
                "ON contr.id=us.id LEFT JOIN Developer dev " +
                "ON dev.id=proj.developer.id LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id " +

                "WHERE dev.id=:developerId " +
                "AND proj.status=:status " +

                "GROUP BY contr " +
                "ORDER BY contr.name ASC");

        Query queryTransferSum = getEm().createQuery(
            "SELECT contr, SUM(tr.sum) " +
                "FROM Project proj INNER JOIN Chapter ch " +
                "ON ch.project.id=proj.id INNER JOIN Contractor contr " +
                "ON contr.id=ch.contractor.id LEFT JOIN Developer dev " +
                "ON dev.id=proj.developer.id LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id LEFT JOIN MoneyTransfer tr " +
                "ON tr.calculation.id=calc.id " +

                "WHERE dev.id=:developerId " +
                "AND proj.status=:status " +

                "GROUP BY contr " +
                "ORDER BY contr.name ASC");

        List<Query> queries = new ArrayList<>();
        queries.add(queryWorkPrice);
        queries.add(queryTransferSum);

        queries.forEach(query -> query.setParameter("developerId", developerId)
            .setParameter("status", status)
            .setMaxResults(count)
            .setFirstResult((page - 1) * count));

        Map<Contractor, Integer[]> map = new TreeMap<>(Comparator.comparing(Contractor::getName));

        List<Object[]> listWorkPrice = (List<Object[]>) queryWorkPrice.getResultList();
        List<Object[]> listTransferSum = (List<Object[]>) queryTransferSum.getResultList();

        listWorkPrice.forEach(res -> {
            Integer[] arr = new Integer[2];
            Arrays.fill(arr, ZERO_INT_VALUE);

            Contractor contractor = (Contractor) res[0];
            long workPrice = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;
            arr[0] = (int) workPrice;

            map.put(contractor, arr);
        });

        listTransferSum.forEach(res -> {
            Contractor contractor = (Contractor) res[0];
            long transferSum = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;
            map.get(contractor)[1] = (int) transferSum;
        });

        return map;
    }

    @Override
    public Long getCountOfContractors(UserStatus status) throws NoResultException, IOException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(co) FROM Contractor co WHERE co.user.status=:status",
            Long.class);
        return query.setParameter("status", status)
                   .getSingleResult();
    }

    @Override
    public Long getCountOfContractorsByDeveloperId(Long developerId, ProjectStatus status) throws NoResultException, IOException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(DISTINCT co) FROM Contractor co LEFT JOIN Chapter ch ON co.id=ch.contractor.id  WHERE  ch.project.developer.id=:developerId AND ch.project.status=:status ",
            Long.class);
        return query.setParameter("developerId", developerId)
                   .setParameter("status", status)
                   .getSingleResult();
    }
}



