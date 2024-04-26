package it.academy.controller;

import it.academy.dto.*;
import it.academy.pojo.Project;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;

import java.io.IOException;
import java.util.List;

public interface DeveloperController {

    DeveloperDto createDeveloper(
        String email, String password, String name, String city, String street, String building)
        throws Exception;

    Page<ProjectDto> getMyProjects(long developerId, ProjectStatus status, int page, int count) throws Exception;

    Page<ContractorDto> getMyContractors(long developerId, ProjectStatus status, int page, int count) throws Exception;

    Page<ProposalDto> getAllMyProposals(long developerId, ProposalStatus status, int page, int count) throws Exception;

    ProjectDto createProject(long developerId, String name, String city, String street, String building)
        throws Exception;

    void createChapter(long projectId, String name, Integer price) throws Exception;

    void cancelChapter(long chapterId) throws Exception;

    List<ChapterDto> getChaptersByProjectId(long projectId) throws Exception;

    Page<ChapterDto> getChaptersByContractorIdAndDeveloperId(long developerId, long contractorId, ProjectStatus status, int page, int count)
        throws Exception;

    void changeStatusOfProposal(long proposalId, ProposalStatus newStaatus) throws Exception;

    Page<ProposalDto> getProposalsByChapterId(long chapterId, ProposalStatus status, int page, int count)
        throws Exception;

    void changeProjectStatus(long projectId, ProjectStatus newStatus) throws Exception;

    Page<CalculationDto> getCalculationsByChapterId(long chapterId, int page, int count) throws Exception;

    void payAdvance(int sum, long calculationId) throws Exception;

    void payForWork(int sum, long calculationId) throws Exception;

    int getProjectDept(Project project);

    int getTotalDeptByContractor(long contractorId, long developerId) throws IOException;
}
