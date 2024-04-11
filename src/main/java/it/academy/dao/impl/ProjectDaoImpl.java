package it.academy.dao.impl;

import it.academy.dao.ProjectDao;
import it.academy.pojo.Project;
import it.academy.pojo.enums.ProjectStatus;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class ProjectDaoImpl extends DaoImpl<Project, Long> implements ProjectDao {

    public ProjectDaoImpl() {

        super(Project.class);
    }

    @Override
    public List<Project> getProjects(ProjectStatus status, int page, int count)
        throws NoResultException {

        TypedQuery<Project> query = getEm().createQuery(
            "SELECT p FROM Project p WHERE  p.status=:status ORDER BY p.developer.name ASC, p.name ASC",
            Project.class);
        return query.setParameter("status", status)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public List<Project> getProjectsByContractorId(Long contractorId, ProjectStatus status, int page, int count)
        throws NoResultException {

        TypedQuery<Project> query = getEm().createQuery(
            "SELECT p FROM Project p, Chapter ch WHERE p.status=:status AND ch MEMBER OF p.chapters AND ch.contractor.id=:contractorId ORDER BY p.developer.name ASC, p.name ASC",
            Project.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("status", status)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public List<Project> getProjectsByDeveloperIdContractorId
        (Long developerId, Long contractorId, ProjectStatus status, int page, int count)
        throws NoResultException {

        TypedQuery<Project> query = getEm().createQuery(
            "SELECT p FROM Project p, Chapter ch WHERE p.developer.id=:developerId AND p.status=:status AND ch MEMBER OF p.chapters AND ch.contractor.id=:contractorId ORDER BY p.developer.name ASC",
            Project.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("developerId", developerId)
                   .setParameter("status", status)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public List<Project> getProjectsByDeveloperId(Long developerId, ProjectStatus status, int page, int count)
        throws NoResultException {

        TypedQuery<Project> query = getEm().createQuery(
            "SELECT p FROM Project p WHERE p.developer.id=:developerId AND p.status=:ProjectStatus ORDER BY p.name ASC",
            Project.class);
        return query.setParameter("developerId", developerId)
                   .setParameter("ProjectStatus", status)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public Long getCountOfProjects(ProjectStatus status) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(p) FROM Project p WHERE p.status=:status",
            Long.class);
        return query.setParameter("status", status)
                   .getSingleResult();
    }

    @Override
    public Long getCountOfProjectsByContractorId(Long contractorId, ProjectStatus status) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(p) FROM Project p, Chapter ch WHERE p.status=:status AND ch MEMBER OF p.chapters AND ch.contractor.id=:contractorId",
            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("status", status)
                   .getSingleResult();
    }

    @Override
    public Long getCountOfProjectsByDeveloperIdContractorId(Long developerId, Long contractorId, ProjectStatus status) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(p) FROM Project p, Chapter ch WHERE p.developer.id=:developerId AND p.status=:status AND ch MEMBER OF p.chapters AND ch.contractor.id=:contractorId",
            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("developerId", developerId)
                   .setParameter("status", status)
                   .getSingleResult();
    }

    @Override
    public Long getCountOfProjectsByDeveloperId(Long developerId, ProjectStatus status) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(p) FROM Project p WHERE p.developer.id=:developerId AND p.status=:status",
            Long.class);
        return query.setParameter("developerId", developerId)
                   .setParameter("status", status)
                   .getSingleResult();
    }
}
