package it.academy.service;

import it.academy.dto.Page;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.*;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;

public interface AdminService {

    User createAdmin(String email, String password) throws Exception;

    User getUser(String email) throws Exception;

    Page<Contractor> getAllContractors(UserStatus status, int page, int count) throws Exception;

    Page<Developer> getAllDevelopers(UserStatus status, int page, int count) throws Exception;

    Page<Project> getProjectsByDeveloper(long developerId, ProjectStatus status, int page, int count) throws Exception;

    List<Chapter> getChaptersByProjectId(long projectId) throws Exception;

    Page<Chapter> getChaptersByContractorId(long contractorId, int page, int count) throws Exception;

    Page<Calculation> getCalculationsByChapterId(long chapterId, int page, int count) throws Exception;

    List<MoneyTransfer> getMoneyTransfers(long calculationId) throws Exception;

    Page<Proposal> getProposalsByChapterId(long chapterId, ProposalStatus status, int page, int count) throws Exception;

    Page<Proposal> getProposalsByContractorId(long contractorId, ProposalStatus status, int page, int count) throws Exception;

    List<User> getAllAdministrators() throws Exception;

    List<Project> getAllProjects() throws Exception;


    void changeUserStatus(long userId, UserStatus newStatus) throws Exception;

    void deleteUser(long userId) throws IOException, NotUpdateDataInDbException;

    void deleteCalculation(long calculationId) throws IOException, NotUpdateDataInDbException;

    void deleteChapter(long chapterId) throws IOException, NotUpdateDataInDbException;

    void deleteMoneyTransfer(long transferId) throws IOException, NotUpdateDataInDbException;

    void deleteProject(long projectId) throws IOException, NotUpdateDataInDbException;

    void deleteProposal(long proposalId) throws IOException, NotUpdateDataInDbException;
}
