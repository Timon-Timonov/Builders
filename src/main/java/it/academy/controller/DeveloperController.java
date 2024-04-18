package it.academy.controller;

import it.academy.dto.*;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.exceptions.RoleException;
import it.academy.pojo.Project;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;

import java.io.IOException;
import java.util.List;

public interface DeveloperController {

    DeveloperDto createDeveloper(
        String email, String password, String name, String city, String street, String building)
        throws IOException, NotCreateDataInDbException, EmailOccupaidException;

    DeveloperDto getDeveloper(Long userId) throws IOException, RoleException;

    Page<ProjectDto> getMyProjects(Long developerId, ProjectStatus status, int page, int count) throws IOException;

    Page<ContractorDto> getMyContractors(Long developerId, ProjectStatus status, int page, int count) throws IOException;

    Page<ProposalDto> getAllMyProposals(Long developerId, ProposalStatus status, int page, int count) throws IOException;

    ProjectDto createProject(Long developerId, String name, String city, String street, String building)
        throws IOException, NotCreateDataInDbException;

    void createChapter(Long projectId, String name, Integer price) throws IOException, NotCreateDataInDbException;

    void cancelChapter(Long chapterId) throws IOException, NotUpdateDataInDbException;

    List<ChapterDto> getChaptersByProjectId(Long projectId) throws IOException;

    Page<ChapterDto> getChaptersByContractorIdAndDeveloperId(Long developerId, Long contractorId, ProjectStatus status, int page, int count)
        throws IOException;

    void rejectProposal(Long proposalId) throws IOException, NotUpdateDataInDbException;

    void considerateProposal(Long proposalId) throws IOException, NotUpdateDataInDbException;

    void approveProposal(Long proposalId) throws IOException, NotUpdateDataInDbException;

    Page<ProposalDto> getProposalsByChapterId(Long chapterId, ProposalStatus status, int page, int count)
        throws IOException;

    void startProject(Long projectId) throws IOException, NotUpdateDataInDbException;

    void endProject(Long projectId) throws IOException, NotUpdateDataInDbException;

    void cancelProject(Long projectId) throws IOException, NotUpdateDataInDbException;

    Page<CalculationDto> getCalculationsByChapterId(Long chapterId, int page, int count) throws IOException;

    void payAdvance(int sum, Long calculationId) throws IOException, NotCreateDataInDbException;

    void payForWork(int sum, Long calculationId) throws IOException, NotCreateDataInDbException;

    Integer getProjectDept(Project project);

    Integer getTotalDeptByContractor(Long contractorId, Long developerId) throws IOException;
}
