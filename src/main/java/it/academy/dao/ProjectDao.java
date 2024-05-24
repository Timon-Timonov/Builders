package it.academy.dao;

import it.academy.pojo.Project;
import it.academy.pojo.enums.ProjectStatus;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Map;

public interface ProjectDao extends Dao<Project, Long> {

    Map<Project, Integer[]> getProjectsByContractorId(Long contractorId, ProjectStatus status, int page, int count)
        throws NoResultException;

    Map<Project, Integer[]> getProjectsByDeveloperIdContractorId
        (Long developerId, Long contractorId, ProjectStatus status, int page, int count)
        throws NoResultException;

    Map<Project, Integer[]> getProjectsByDeveloperId(Long developerId, ProjectStatus status, int page, int count)
        throws NoResultException;

    Long getCountOfProjectsByContractorId(Long contractorId, ProjectStatus status)
        throws NoResultException;

    Long getCountOfProjectsByDeveloperIdContractorId
        (Long developerId, Long contractorId, ProjectStatus status)
        throws NoResultException;

    Long getCountOfProjectsByDeveloperId(Long developerId, ProjectStatus status)
        throws NoResultException;

    List<Project> getAll();
}
