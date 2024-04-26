package it.academy.dao;

import it.academy.pojo.Project;
import it.academy.pojo.enums.ProjectStatus;

import javax.persistence.NoResultException;
import java.util.List;

public interface ProjectDao extends Dao<Project, Long> {

    List<Project> getProjects(ProjectStatus status, int page, int count) throws NoResultException;

    List<Project> getProjectsByContractorId(Long contractorId, ProjectStatus status, int page, int count)
        throws NoResultException;

    List<Project> getProjectsByDeveloperIdContractorId
        (Long developerId, Long contractorId, ProjectStatus status, int page, int count)
        throws NoResultException;

    List<Project> getProjectsByDeveloperId(Long developerId, ProjectStatus status, int page, int count)
        throws NoResultException;

    Long getCountOfProjects(ProjectStatus status) throws NoResultException;

    Long getCountOfProjectsByContractorId(Long contractorId, ProjectStatus status)
        throws NoResultException;

    Long getCountOfProjectsByDeveloperIdContractorId
        (Long developerId, Long contractorId, ProjectStatus status)
        throws NoResultException;

    Long getCountOfProjectsByDeveloperId(Long developerId, ProjectStatus status)
        throws NoResultException;

    List<Project> getAll();
}
