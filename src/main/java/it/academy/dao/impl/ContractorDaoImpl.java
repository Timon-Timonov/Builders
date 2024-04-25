package it.academy.dao.impl;

import it.academy.dao.ContractorDao;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Contractor;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;

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
    public List<Contractor> getContractorsByDeveloperId(Long developerId, ProjectStatus status, int page, int count)
        throws IOException {

        TypedQuery<Contractor> query = getEm().createQuery(
            "SELECT DISTINCT (co) FROM Contractor co LEFT JOIN Chapter ch ON co.id=ch.contractor.id  WHERE  ch.project.developer.id=:developerId AND ch.project.status=:status ORDER BY co.name ASC ",
            Contractor.class);

        return query.setParameter("developerId", developerId)
                   .setParameter("status", status)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
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



