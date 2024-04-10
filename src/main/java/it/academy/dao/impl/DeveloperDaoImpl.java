package it.academy.dao.impl;

import it.academy.dao.DeveloperDao;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Developer;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;

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
            "SELECT d FROM Developer d, Chapter c WHERE c.project.developer=d AND c.contractor.id=:contractorId AND c.project.status=:status ORDER BY d.name",
            Developer.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("status", status)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
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
            "SELECT COUNT (d) FROM Developer d, Chapter c WHERE c.project.developer=d AND c.contractor.id=:contractorId AND c.project.status=:status",

            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("status", status)
                   .getSingleResult();
    }

}