package it.academy.dao.impl;

import it.academy.dao.ProjectDao;
import it.academy.pojo.Project;
import it.academy.pojo.enums.ProjectStatus;

import java.util.List;

public class ProjectDaoImpl extends DaoImpl<Project,Long> implements ProjectDao {

    public ProjectDaoImpl() {

        super(Project.class);
    }

    @Override
    public List<Project> getProjects(ProjectStatus status, int page, int count) {
        return null;
    }

    @Override
    public List<Project> getProjectsByContractorId(Long contractorId, ProjectStatus status, int page, int count) {
        return null;
    }

    @Override
    public List<Project> getProjectsByDeveloperIdContractorId(Long developerId, Long contractorId, int page, int count) {
        return null;
    }

    @Override
    public List<Project> getProjectsByDeveloperId(Long developerId, ProjectStatus status, int page, int count) {
        return null;
    }
}
