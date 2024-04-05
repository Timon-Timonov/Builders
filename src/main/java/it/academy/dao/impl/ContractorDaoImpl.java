package it.academy.dao.impl;

import it.academy.dao.ContractorDao;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Contractor;

import java.util.List;

public class ContractorDaoImpl extends DaoImpl<Contractor, Long> implements ContractorDao {

    public ContractorDaoImpl() {

        super(Contractor.class);
    }

    @Override
    public List<Contractor> getContractors(UserStatus status, int page, int count) {
        return null;
    }

    @Override
    public List<Contractor> getContractorsByDeveloperId(Long developerId, ProjectStatus status, int page, int count) {
        return null;
    }
}