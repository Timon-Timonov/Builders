package it.academy.dao;

import it.academy.pojo.Project;
import it.academy.pojo.enums.ProjectStatus;

import java.util.List;

public interface ProjectDao extends Dao<Project, Long> {

    List<Project> getProjects(ProjectStatus status, int page, int count);

    List<Project> getProjectsByContractorId(Long contractorId, ProjectStatus status, int page, int count);

    List<Project> getProjectsByDeveloperIdContractorId(Long developerId, Long contractorId, int page, int count);

    List<Project> getProjectsByDeveloperId(Long developerId, ProjectStatus status, int page, int count);
}
