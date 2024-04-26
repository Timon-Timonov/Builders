package it.academy.service;

import it.academy.dto.Page;
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
        throws Exception;

    Developer getDeveloper(long userId) throws Exception;

    Page<Project> getMyProjects(long developerId, ProjectStatus status, int page, int count) throws Exception;

    Page<Contractor> getMyContractors(long developerId, ProjectStatus status, int page, int count) throws Exception;

    Page<Proposal> getAllMyProposals(long developerId, ProposalStatus status, int page, int count) throws Exception;

    Project createProject(long developerId, String name, String city, String street, String building)
        throws Exception;

    void createChapter(long projectId, String name, int price) throws Exception;

    void cancelChapter(long chapterId) throws Exception;

    List<Chapter> getChaptersByProjectId(long projectId) throws Exception;

    Page<Chapter> getChaptersByContractorIdAndDeveloperId(long developerId, long contractorId, ProjectStatus status, int page, int count)
        throws Exception;

    void changeStatusOfProposal(long proposalId, ProposalStatus newStatus) throws Exception;

    Page<Proposal> getProposalsByChapterId(long chapterId, ProposalStatus status, int page, int count)
        throws Exception;

    void changeProjectStatus(long projectId, ProjectStatus newStatus) throws Exception;

    Page<Calculation> getCalculationsByChapterId(long chapterId, int page, int count) throws Exception;

    void payAdvance(int sum, long calculationId) throws Exception;

    void payForWork(int sum, long calculationId) throws Exception;

    int getProjectDept(Project project);

    int getTotalDeptByContractor(long contractorId, long developerId) throws IOException;
}
