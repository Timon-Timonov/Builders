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
    public List<Developer> getDevelopers(UserStatus status, int page, int count)
        throws IOException {

        TypedQuery<Developer> query = getEm().createQuery(
            "SELECT d FROM Developer d " +
                "WHERE d.user.status=:status " +
                "ORDER BY d.name",
            Developer.class);
        return query.setParameter("status", status)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }


    public Map<Developer, Integer[]> getDevelopersForContractor(Long contractorId, ProjectStatus status, int page, int count)
        throws IOException {

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
                "ORDER BY dev.name"
        );

        List<Query> queries = new ArrayList<>();
        queries.add(queryPrice);
        queries.add(querySum);

        queries.forEach(query -> query.setParameter("contractorId", contractorId)
                                     .setParameter("status", status)
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
    public Long getCountOfDevelopers(UserStatus status) throws NoResultException, IOException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT (d) FROM Developer d WHERE d.user.status=:status",
            Long.class);
        return query.setParameter("status", status)
                   .getSingleResult();
    }

    @Override
    public Long getCountOfDevelopers(Long contractorId, ProjectStatus status) throws NoResultException, IOException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT (DISTINCT d) " +
                "FROM Developer d, Chapter c " +
                "WHERE c.project.developer.id=d.id " +
                "AND c.contractor.id=:contractorId " +
                "AND c.project.status=:status",

            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("status", status)
                   .getSingleResult();
    }
}