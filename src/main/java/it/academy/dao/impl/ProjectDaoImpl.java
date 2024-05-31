package it.academy.dao.impl;

import it.academy.dao.ProjectDao;
import it.academy.pojo.Project;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

import static it.academy.util.constants.Constants.*;

public class ProjectDaoImpl extends DaoImpl<Project, Long> implements ProjectDao {

    public ProjectDaoImpl() {

        super(Project.class);
    }

    @Override
    public Map<Project, Integer[]> getProjectsByContractorId(
        Long contractorId, ProjectStatus status, int page, int count) throws NoResultException {

        TypedQuery<Object[]> queryTotalPrice = getEm().createQuery(
            "SELECT  p, SUM(ch.price), dev, us " +
                "FROM Project p INNER JOIN Chapter ch " +
                "ON p=ch.project LEFT JOIN Developer dev " +
                "ON p.developer=dev LEFT JOIN User us " +
                "ON dev.id=us.id " +

                "WHERE ch.contractor.id=:contractorId " +
                "AND p.status=:status " +

                "GROUP BY p " +
                "ORDER BY  p.name ", Object[].class);


        TypedQuery<Object[]> queryWorkPrice = getEm().createQuery(
            "SELECT  p, SUM(calc.workPriceFact) " +
                "FROM Project p INNER JOIN Chapter ch " +
                "ON p.id=ch.project.id LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id " +

                "WHERE ch.contractor.id=:contractorId " +
                "AND p.status=:status " +

                "GROUP BY p " +
                "ORDER BY  p.name ", Object[].class);


        TypedQuery<Object[]> queryTransferSum = getEm().createQuery(
            "SELECT  p, SUM(tr.sum) " +
                "FROM Project p INNER JOIN Chapter ch " +
                "ON p.id=ch.project.id LEFT JOIN Calculation calc " +
                "ON ch.id=calc.chapter.id LEFT JOIN MoneyTransfer tr " +
                "ON calc.id=tr.calculation.id " +

                "WHERE ch.contractor.id=:contractorId " +
                "AND p.status=:status " +

                "GROUP BY p " +
                "ORDER BY  p.name ", Object[].class);

        List<Query> queries = new ArrayList<>();
        queries.add(queryTotalPrice);
        queries.add(queryWorkPrice);
        queries.add(queryTransferSum);

        queries.forEach(query -> query.setParameter("contractorId", contractorId)
                                     .setParameter("status", status)
                                     .setMaxResults(count)
                                     .setFirstResult((page - 1) * count));
        return getProjectMap(queryTotalPrice, queryWorkPrice, queryTransferSum);
    }

    @Override
    public Map<Project, Integer[]> getProjectsByDeveloperIdContractorId(
        Long developerId, Long contractorId, ProjectStatus status, int page, int count)
        throws NoResultException {

        TypedQuery<Object[]> queryPrice = getEm().createQuery(
            "SELECT proj, SUM(ch.price), us, dev " +
                "FROM Project proj INNER JOIN Chapter ch " +
                "ON ch.project.id=proj.id INNER JOIN Developer dev " +
                "ON dev.id=proj.developer.id INNER JOIN User us " +
                "ON us.id =dev.id " +

                "WHERE ch.contractor.id=:contractorId " +
                "AND dev.id=:developerId " +
                "AND proj.status=:status " +

                "GROUP BY proj " +
                "ORDER BY proj.name ASC", Object[].class);

        TypedQuery<Object[]> queryWorkPrice = getEm().createQuery(
            "SELECT proj,SUM(calc.workPriceFact) " +
                "FROM Project proj INNER JOIN Developer dev " +
                "ON dev.id=proj.developer.id INNER JOIN Chapter ch " +
                "ON proj.id=ch.project.id LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id " +

                "WHERE ch.contractor.id=:contractorId " +
                "AND dev.id=:developerId " +
                "AND proj.status=:status " +

                "GROUP BY proj " +
                "ORDER BY proj.name ASC", Object[].class);

        TypedQuery<Object[]> queryTransferSum = getEm().createQuery(
            "SELECT proj,SUM(tr.sum) " +
                "FROM Project proj INNER JOIN Developer dev " +
                "ON dev.id=proj.developer.id INNER JOIN Chapter ch " +
                "ON proj.id=ch.project.id LEFT JOIN Calculation calc " +
                "ON ch.id=calc.chapter.id LEFT JOIN MoneyTransfer tr " +
                "ON tr.calculation.id=calc.id " +

                "WHERE ch.contractor.id=:contractorId " +
                "AND dev.id=:developerId " +
                "AND proj.status=:status " +

                "GROUP BY proj " +
                "ORDER BY proj.name ASC", Object[].class);

        List<Query> queries = new ArrayList<>();
        queries.add(queryPrice);
        queries.add(queryWorkPrice);
        queries.add(queryTransferSum);

        queries.forEach(query -> query.setParameter("contractorId", contractorId)
                                     .setParameter("developerId", developerId)
                                     .setParameter("status", status)
                                     .setMaxResults(count)
                                     .setFirstResult((page - 1) * count));

        return getProjectMap(queryPrice, queryWorkPrice, queryTransferSum);
    }

    @Override
    public Map<Project, Integer[]> getProjectsByDeveloperId(Long developerId, ProjectStatus status, int page, int count)
        throws NoResultException {

        TypedQuery<Object[]> queryPrice = getEm().createQuery(
            "SELECT proj, SUM(ch.price), us, dev " +
                "FROM Project proj LEFT JOIN Chapter ch " +
                "ON ch.project.id=proj.id INNER JOIN Developer dev " +
                "ON dev.id=proj.developer.id INNER JOIN User us " +
                "ON us.id =dev.id " +

                "WHERE proj.status=:status " +
                "AND dev.id=:developerId " +

                "GROUP BY proj " +
                "ORDER BY proj.name ASC", Object[].class);

        TypedQuery<Object[]> queryWorkPrice = getEm().createQuery(
            "SELECT proj,SUM(calc.workPriceFact) " +
                "FROM Project proj LEFT JOIN Chapter ch " +
                "ON ch.project.id=proj.id INNER JOIN Developer dev " +
                "ON dev.id=proj.developer.id LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id " +

                "WHERE proj.status=:status " +
                "AND dev.id=:developerId " +

                "GROUP BY proj " +
                "ORDER BY proj.name ASC", Object[].class);

        TypedQuery<Object[]> queryTransferSum = getEm().createQuery(
            "SELECT proj,SUM(tr.sum) " +
                "FROM Project proj LEFT JOIN Chapter ch " +
                "ON ch.project.id=proj.id INNER JOIN Developer dev " +
                "ON dev.id=proj.developer.id LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id LEFT JOIN MoneyTransfer tr " +
                "ON tr.calculation.id=calc.id " +

                "WHERE proj.status=:status " +
                "AND dev.id=:developerId " +

                "GROUP BY proj " +
                "ORDER BY proj.name ASC", Object[].class);

        List<Query> queries = new ArrayList<>();
        queries.add(queryPrice);
        queries.add(queryWorkPrice);
        queries.add(queryTransferSum);

        queries.forEach(query -> query.setParameter("developerId", developerId)
                                     .setParameter("status", status)
                                     .setMaxResults(count)
                                     .setFirstResult((page - 1) * count));

        return getProjectMap(queryPrice, queryWorkPrice, queryTransferSum);
    }


    @Override
    public Long getCountOfProjectsByContractorId(Long contractorId, ProjectStatus status) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(DISTINCT p) " +
                "FROM Project p JOIN Chapter ch " +
                "ON p.id=ch.project.id " +
                "WHERE p.status=:status " +
                "AND ch.contractor.id=:contractorId ",
            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("status", status)
                   .getSingleResult();
    }

    @Override
    public Long getCountOfProjectsByDeveloperIdContractorId(Long developerId, Long contractorId, ProjectStatus status) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(DISTINCT proj) " +
                "FROM Project proj INNER JOIN Chapter ch " +
                "ON ch.project.id=proj.id INNER JOIN Developer dev " +
                "ON proj.developer.id=dev.id " +
                "WHERE dev.id=:developerId " +
                "AND proj.status=:status " +
                "AND ch.contractor.id=:contractorId",
            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("developerId", developerId)
                   .setParameter("status", status)
                   .getSingleResult();
    }

    @Override
    public Long getCountOfProjectsByDeveloperId(Long developerId, ProjectStatus status) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(p) " +
                "FROM Project p " +
                "WHERE p.developer.id=:developerId " +
                "AND p.status=:status",
            Long.class);
        return query.setParameter("developerId", developerId)
                   .setParameter("status", status)
                   .getSingleResult();
    }

    @Override
    public List<Project> getAll() {

        TypedQuery<Project> query = getEm().createQuery(
            "SELECT p FROM Project p " +
                "WHERE  p.developer.user.status=:status " +
                "ORDER BY p.name ASC",
            Project.class);
        return query.setParameter("status", UserStatus.ACTIVE)
                   .getResultList();
    }

    private Map<Project, Integer[]> getProjectMap(TypedQuery<Object[]> queryPrice, TypedQuery<Object[]> queryWorkPrice, TypedQuery<Object[]> queryTransferSum) {

        Map<Project, Integer[]> map = new TreeMap<>(Comparator.comparing(Project::getName));

        List<Object[]> listPrice = queryPrice.getResultList();
        List<Object[]> listWorkPrice = queryWorkPrice.getResultList();
        List<Object[]> listTransferSum = queryTransferSum.getResultList();

        listPrice.forEach(res -> {
            Integer[] arr = new Integer[DEBT_ARRAY_LENGTH];
            Arrays.fill(arr, ZERO_INT_VALUE);

            Project project = (Project) res[0];
            long price = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;
            arr[0] = (int) price;

            map.put(project, arr);
        });
        listWorkPrice.forEach(res -> {
            Project project = (Project) res[0];
            long workPrice = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;
            map.get(project)[1] = (int) workPrice;
        });
        listTransferSum.forEach(res -> {
            Project project = (Project) res[0];
            long transferSum = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;
            map.get(project)[2] = (int) transferSum;
        });

        return map;
    }
}
