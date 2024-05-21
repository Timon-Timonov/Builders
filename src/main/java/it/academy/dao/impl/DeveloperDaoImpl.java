package it.academy.dao.impl;

import it.academy.dao.DeveloperDao;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Developer;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DeveloperDaoImpl extends DaoImpl<Developer, Long> implements DeveloperDao {

    public DeveloperDaoImpl() {

        super(Developer.class);
    }

    @Override
    public List<Developer> getDevelopers(UserStatus status, int page, int count)
        throws IOException {

        TypedQuery<Developer> query = getEm().createQuery(
            "SELECT d FROM Developer d WHERE d.user.status=:status ORDER BY d.name",
            Developer.class);
        return query.setParameter("status", status)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public List<Developer> getDevelopersByContractorId(Long contractorId, ProjectStatus status, int page, int count)
        throws IOException {

        TypedQuery<Developer> query = getEm().createQuery(
            "SELECT DISTINCT (d) FROM Developer d, Chapter c WHERE c.project.developer.id=d.id AND c.contractor.id=:contractorId AND c.project.status=:status ORDER BY d.name",
            Developer.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("status", status)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    public Map<Developer, Integer> getDevelopersForContractor(Long contractorId, ProjectStatus status, int page, int count)
        throws IOException {

        Map<Developer, Integer> map = new TreeMap<>(Comparator.comparing(Developer::getName));

        // Query query = getEm().createQuery("SELECT le AS developer, (SUM(calc.workPriceFact)-SUM(tr.sum)) AS debt FROM LegalEntity le, Developer dev, Chapter  ch, Calculation calc, MoneyTransfer tr WHERE ch.project.developer.id=dev.id AND ch.contractor.id=:contractorId AND ch.project.status=:status AND calc.chapter=ch AND tr.calculation=calc AND dev.id=le.id GROUP BY dev ORDER BY dev.name");

        Query query = getEm().createQuery(
            "SELECT dev AS developer, (SUM(calc.workPriceFact)-SUM(tr.sum)) AS debt FROM " +
                " Chapter ch LEFT JOIN Contractor contr " +
                "ON ch.contractor.id=contr.id LEFT JOIN Developer dev " +
                "ON ch.project.developer.id=dev.id JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id JOIN MoneyTransfer tr " +
                "ON tr.calculation.id=calc.id  " +
                "WHERE contr.id=:contractorId AND ch.project.status=:status " +
                "GROUP BY dev " +
                "ORDER BY dev.name");


        query.setParameter("contractorId", contractorId)
            .setParameter("status", status)
            .setMaxResults(count)
            .setFirstResult((page - 1) * count);

        List<Object[]> list = (List<Object[]>) query.getResultList();

        list.forEach(result -> {
            Developer dev = (Developer) result[0];
            long i = (long) result[1];
            map.put(dev, (int) i);
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
            "SELECT COUNT (d) FROM Developer d, Chapter c WHERE c.project.developer.id=d.id AND c.contractor.id=:contractorId AND c.project.status=:status",

            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("status", status)
                   .getSingleResult();
    }
}