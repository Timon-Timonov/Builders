package it.academy.dao.impl;

import it.academy.dao.DeveloperDao;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Developer;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.*;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;

public class DeveloperDaoImpl extends DaoImpl<Developer, Long> implements DeveloperDao {

    public DeveloperDaoImpl() {

        super(Developer.class);
    }

    @Override
    public List<Developer> getDevelopers(UserStatus status, String search, int page, int count) {

        TypedQuery<Developer> query = getEm().createQuery(
            "SELECT DISTINCT dev FROM Developer dev " +
                "WHERE dev.user.status=:status " +

                "AND dev.name LIKE :searchString " +
                "OR dev.address.building LIKE :searchString " +
                "OR dev.address.street LIKE :searchString " +
                "OR dev.address.city LIKE :searchString " +

                "ORDER BY dev.name ",
            Developer.class);
        return query.setParameter("status", status)
                   .setParameter("searchString", search)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public Map<Developer, Integer[]> getDevelopersForContractor(Long contractorId, ProjectStatus status, String search, int page, int count) {

        Query queryPrice = getEm().createQuery(
            "SELECT dev, SUM(calc.workPriceFact), us " +
                "FROM Developer dev INNER JOIN User us " +
                "ON us.id=dev.id INNER JOIN Project proj " +
                "ON proj.developer.id=dev.id  INNER JOIN Chapter ch " +
                "ON proj.id=ch.project.id LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id " +

                "WHERE proj.status=:status " +
                "AND ch.contractor.id=:contractorId " +

                "GROUP BY dev " +

                "HAVING dev.name LIKE :searchString " +
                "OR dev.address.building LIKE :searchString " +
                "OR dev.address.street LIKE :searchString " +
                "OR dev.address.city LIKE :searchString " +

                "ORDER BY dev.name"
        );

        Query querySum = getEm().createQuery(
            "SELECT dev, SUM(tr.sum) " +
                "FROM Developer dev INNER JOIN User us " +
                "ON us.id=dev.id INNER JOIN Project proj " +
                "ON proj.developer.id=dev.id INNER JOIN Chapter ch " +
                "ON proj.id=ch.project.id LEFT JOIN Calculation calc " +
                "ON ch.id=calc.chapter.id LEFT JOIN MoneyTransfer tr " +
                "ON calc.id=tr.calculation.id " +

                "WHERE proj.status=:status " +
                "AND ch.contractor.id=:contractorId " +

                "GROUP BY dev " +

                "HAVING dev.name LIKE :searchString " +
                "OR dev.address.building LIKE :searchString " +
                "OR dev.address.street LIKE :searchString " +
                "OR dev.address.city LIKE :searchString " +

                "ORDER BY dev.name"
        );

        List<Query> queries = new ArrayList<>();
        queries.add(queryPrice);
        queries.add(querySum);

        queries.forEach(query -> query.setParameter("contractorId", contractorId)
                                     .setParameter("status", status)
                                     .setParameter("searchString", search)
                                     .setMaxResults(count)
                                     .setFirstResult((page - 1) * count));

        List<Object[]> listWorkPrice = (List<Object[]>) queryPrice.getResultList();
        List<Object[]> listTransferSum = (List<Object[]>) querySum.getResultList();

        Map<Developer, Integer[]> map = new TreeMap<>(Comparator.comparing(Developer::getName));
        listWorkPrice.forEach(res -> {
            Integer[] arr = new Integer[2];
            Arrays.fill(arr, ZERO_INT_VALUE);

            Developer dev = (Developer) res[0];
            long price = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;
            arr[0] = (int) price;

            map.put(dev, arr);
        });

        listTransferSum.forEach(res -> {
            Developer dev = (Developer) res[0];
            long sum = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;

            map.get(dev)[1] = (int) sum;
        });

        return map;
    }

    @Override
    public Long getCountOfDevelopers(UserStatus status, String search) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT (DISTINCT dev) FROM Developer dev " +
                "WHERE dev.user.status=:status " +

                "AND dev.name LIKE :searchString " +
                "OR dev.address.building LIKE :searchString " +
                "OR dev.address.street LIKE :searchString " +
                "OR dev.address.city LIKE :searchString ",
            Long.class);
        return query.setParameter("status", status)
                   .setParameter("searchString", search)
                   .getSingleResult();
    }

    @Override
    public Long getCountOfDevelopers(Long contractorId, ProjectStatus status, String search) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT (DISTINCT dev) " +
                "FROM Developer dev INNER JOIN Project proj " +
                "ON proj.developer.id=dev.id INNER JOIN Chapter chapt " +
                "ON chapt.project.id=proj.id " +

                "WHERE chapt.contractor.id=:contractorId " +
                "AND proj.status=:status " +

                "AND dev.name LIKE :searchString " +
                "OR dev.address.building LIKE :searchString " +
                "OR dev.address.street LIKE :searchString " +
                "OR dev.address.city LIKE :searchString ",
            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("status", status)
                   .setParameter("searchString", search)
                   .getSingleResult();
    }
}