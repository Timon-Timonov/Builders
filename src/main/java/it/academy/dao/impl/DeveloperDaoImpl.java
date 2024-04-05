package it.academy.dao.impl;

import it.academy.dao.DeveloperDao;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Developer;

import java.util.List;

public class DeveloperDaoImpl extends DaoImpl<Developer, Long> implements DeveloperDao {

    public DeveloperDaoImpl() {

        super(Developer.class);
    }

    @Override
    public List<Developer> getDevelopers(UserStatus status, int page, int count) {
        return null;
    }

    @Override
    public List<Developer> getDevelopersByContractorId(Long contractorId, ProjectStatus status, int page, int count) {
        return null;
    }
}