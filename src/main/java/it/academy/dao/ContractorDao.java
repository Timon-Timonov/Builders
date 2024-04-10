package it.academy.dao;

import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Contractor;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;

public interface ContractorDao extends Dao<Contractor, Long> {

    List<Contractor> getContractors(UserStatus status, int page, int count) throws NoResultException, IOException;

    List<Contractor> getContractorsByDeveloperId(Long developerId, ProjectStatus status, int page, int count)
        throws NoResultException, IOException;

    Long getCountOfContractors(UserStatus status) throws NoResultException, IOException;

    Long getCountOfContractorsByDeveloperId(Long developerId, ProjectStatus status)
        throws NoResultException, IOException;
}
