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
    public List<Contractor> getContractors(UserStatus status, String search, int page, int count) {

        TypedQuery<Contractor> query = getEm().createQuery(
            "SELECT contr FROM Contractor contr " +
                "WHERE contr.user.status=:status " +

                "GROUP BY contr " +

                "HAVING contr.name LIKE :searchString " +
                "OR contr.address.building LIKE :searchString " +
                "OR contr.address.street LIKE :searchString " +
                "OR contr.address.city LIKE :searchString " +

                "ORDER BY contr.name ASC",
            Contractor.class);
        return query.setParameter("status", status)
                   .setParameter("searchString", search)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public Map<Contractor, Integer[]> getContractorsByDeveloperId(Long developerId, ProjectStatus status, String search, int page, int count) {

        Query queryWorkPrice = getEm().createQuery(
            "SELECT contr, SUM(calc.workPriceFact), us " +
                "FROM Project proj INNER JOIN Chapter ch " +
                "ON ch.project.id=proj.id INNER JOIN Contractor contr " +
                "ON contr.id=ch.contractor.id JOIN Developer dev " +
                "ON dev.id=proj.developer.id LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id LEFT JOIN User us " +
                "ON us.id=contr.id " +

                "WHERE dev.id=:developerId " +
                "AND proj.status=:status " +

                "GROUP BY contr " +

                "HAVING contr.name LIKE :searchString " +
                "OR contr.address.building LIKE :searchString " +
                "OR contr.address.street LIKE :searchString " +
                "OR contr.address.city LIKE :searchString " +

                "ORDER BY contr.name ASC");

        Query queryTransferSum = getEm().createQuery(
            "SELECT contr, SUM(tr.sum) " +
                "FROM Project proj INNER JOIN Chapter ch " +
                "ON ch.project.id=proj.id INNER JOIN Contractor contr " +
                "ON contr.id=ch.contractor.id JOIN Developer dev " +
                "ON dev.id=proj.developer.id LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id LEFT JOIN MoneyTransfer tr " +
                "ON tr.calculation.id=calc.id " +

                "WHERE dev.id=:developerId " +
                "AND proj.status=:status " +

                "GROUP BY contr " +
                "HAVING contr.name LIKE :searchString " +
                "OR contr.address.building LIKE :searchString " +
                "OR contr.address.street LIKE :searchString " +
                "OR contr.address.city LIKE :searchString " +

                "ORDER BY contr.name ASC");

        List<Query> queries = new ArrayList<>();
        queries.add(queryWorkPrice);
        queries.add(queryTransferSum);

        queries.forEach(query -> query.setParameter("developerId", developerId)
                                     .setParameter("status", status)
                                     .setParameter("searchString", search)
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
    public Long getCountOfContractors(UserStatus status, String search) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(DISTINCT contr) " +
                "FROM Contractor contr " +
                "WHERE contr.user.status=:status " +

                "AND contr.name LIKE :searchString " +
                "OR contr.address.building LIKE :searchString " +
                "OR contr.address.street LIKE :searchString " +
                "OR contr.address.city LIKE :searchString ",
            Long.class);
        return query.setParameter("status", status)
                   .setParameter("searchString", search)
                   .getSingleResult();
    }

    @Override
    public Long getCountOfContractorsByDeveloperId(Long developerId, ProjectStatus status, String search) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(DISTINCT contr) " +
                "FROM Contractor contr INNER JOIN Chapter ch " +
                "ON contr.id=ch.contractor.id  INNER JOIN Project proj " +
                "ON ch.project.id=proj.id JOIN Developer dev " +
                "ON dev.id=proj.developer.id " +

                "WHERE dev.id=:developerId " +
                "AND proj.status=:status " +

                "AND contr.name LIKE :searchString " +
                "OR contr.address.building LIKE :searchString " +
                "OR contr.address.street LIKE :searchString " +
                "OR contr.address.city LIKE :searchString ",
            Long.class);
        return query.setParameter("developerId", developerId)
                   .setParameter("status", status)
                   .setParameter("searchString", search)
                   .getSingleResult();
    }
}



