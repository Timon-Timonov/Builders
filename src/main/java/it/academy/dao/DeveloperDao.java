package it.academy.dao;

import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Developer;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;

public interface DeveloperDao extends Dao<Developer, Long> {

    List<Developer> getDevelopers(UserStatus status, int page, int count) throws NoResultException, IOException;

    Long getCountOfDevelopers(UserStatus status) throws NoResultException, IOException;

    List<Developer> getDevelopersByContractorId(Long contractorId, ProjectStatus status, int page, int count)
        throws NoResultException, IOException;

    Long getCountOfDevelopers(Long contractorId, ProjectStatus status) throws NoResultException, IOException;
}
