package it.academy.controller;

import it.academy.service.dto.Page;
import it.academy.dto.*;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;

import java.io.IOException;
import java.util.List;

public interface ContractorController {

    ContractorDto createContractor(
        String email, String password, String name, String city, String street, String building)
        throws Exception;

    Page<ProjectDto> getMyProjects(long contractorId, ProjectStatus status, int page, int count) throws Exception;

    Page<ProjectDto> getMyProjectsByDeveloper
        (long developerId, long contractorId, ProjectStatus status, int page, int count)
        throws Exception;

    List<String> getAllChapterNames() throws IOException;

    Page<ChapterDto> getFreeChapters(long contractorId, String chapterName, ProjectStatus projectStatus, int page, int count) throws Exception;

    Page<DeveloperDto> getMyDevelopers(long contractorId, ProjectStatus status, int page, int count) throws Exception;

    Page<ProposalDto> getMyProposals(long contractorId, ProposalStatus status, int page, int count) throws Exception;

    List<ChapterDto> getMyChaptersByProjectId(long projectId, long contractorId) throws Exception;

    Page<CalculationDto> getCalculationsByChapter(long chapterId, int page, int count) throws Exception;

    void updateWorkPriceFact(int workPrice, long calculationId) throws Exception;

    void createCalculation(long chapterId, int YYYY, int MM, int workPricePlan) throws Exception;

    void setProposalStatus(long proposalId, ProposalStatus newStatus) throws Exception;

    void createProposal(long chapterId, long contractorId) throws Exception;

}
