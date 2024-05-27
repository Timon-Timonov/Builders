package it.academy.dao;

import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Developer;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DeveloperDao extends Dao<Developer, Long> {

    List<Developer> getDevelopers(UserStatus status, String search, int page, int count) throws NoResultException, IOException;

    Long getCountOfDevelopers(UserStatus status, String search) throws NoResultException, IOException;

    Long getCountOfDevelopers(Long contractorId, ProjectStatus status, String search) throws NoResultException, IOException;

    Map<Developer, Integer[]> getDevelopersForContractor(Long contractorId, ProjectStatus status, String search, int page, int count)
        throws IOException;
}
