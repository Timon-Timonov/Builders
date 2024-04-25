package it.academy.service;

import it.academy.dto.Page;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.exceptions.RoleException;
import it.academy.pojo.Calculation;
import it.academy.pojo.Chapter;
import it.academy.pojo.Project;
import it.academy.pojo.Proposal;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;

import java.io.IOException;
import java.util.List;

public interface DeveloperService {

    Developer createDeveloper(
        String email, String password, String name, String city, String street, String building)
        throws IOException, NotCreateDataInDbException, EmailOccupaidException;

    Developer getDeveloper(long userId) throws IOException, RoleException;

    Page<Project> getMyProjects(long developerId, ProjectStatus status, int page, int count) throws IOException;

    Page<Contractor> getMyContractors(long developerId, ProjectStatus status, int page, int count) throws IOException;

    Page<Proposal> getAllMyProposals(long developerId, ProposalStatus status, int page, int count) throws IOException;

    Project createProject(long developerId, String name, String city, String street, String building)
        throws IOException, NotCreateDataInDbException;

    void createChapter(long projectId, String name, int price) throws IOException, NotCreateDataInDbException;

    void cancelChapter(long chapterId) throws IOException, NotUpdateDataInDbException;

    List<Chapter> getChaptersByProjectId(long projectId) throws IOException;

    Page<Chapter> getChaptersByContractorIdAndDeveloperId(long developerId, long contractorId, ProjectStatus status, int page, int count)
        throws IOException;

    void rejectProposal(long proposalId) throws IOException, NotUpdateDataInDbException;

    void considerateProposal(long proposalId) throws IOException, NotUpdateDataInDbException;

    void approveProposal(long proposalId) throws IOException, NotUpdateDataInDbException;

    Page<Proposal> getProposalsByChapterId(long chapterId, ProposalStatus status, int page, int count)
        throws Exception;

    void startProject(long projectId) throws Exception;

    void endProject(long projectId) throws Exception;

    void cancelProject(long projectId) throws Exception;

    Page<Calculation> getCalculationsByChapterId(long chapterId, int page, int count) throws IOException, Exception;

    void payAdvance(int sum, long calculationId) throws Exception;

    void payForWork(int sum, long calculationId) throws Exception;

    int getProjectDept(Project project);

    int getTotalDeptByContractor(long contractorId, long developerId) throws IOException;

}
