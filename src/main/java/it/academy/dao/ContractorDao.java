package it.academy.dao;

import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Contractor;

import java.util.List;

public interface ContractorDao extends Dao<Contractor, Long> {

    List<Contractor> getContractors(UserStatus status, int page, int count);

    List<Contractor> getContractorsByDeveloperId(Long developerId, ProjectStatus status, int page, int count);
}
