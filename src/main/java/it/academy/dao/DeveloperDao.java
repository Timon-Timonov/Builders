package it.academy.dao;

import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Developer;

import java.util.List;

public interface DeveloperDao extends Dao<Developer, Long> {

    List<Developer> getDevelopers(UserStatus status, int page, int count);
    List<Developer> getDevelopersByContractorId(Long contractorId, ProjectStatus status, int page, int count);
}
