package it.academy.controller;

import it.academy.dto.*;
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

public interface ContractorController {

    ContractorDto createContractor(
        String email, String password, String name, String city, String street, String building)
        throws IOException, NotCreateDataInDbException, EmailOccupaidException;

    ContractorDto getContractor(Long userId) throws IOException, RoleException;

    List<ProjectDto> getMyProjects(Long contractorId, ProjectStatus status, int page, int count) throws IOException;

    List<ProjectDto> getMyProjectsByDeveloper
        (Long developerId, Long contractorId, ProjectStatus status, int page, int count)
        throws IOException;

    List<String> getAllChapterNames() throws IOException;

    List<ChapterDto> getFreeChapters(String chapterName, int page, int count) throws IOException;

    List<DeveloperDto> getMyDevelopers(Long contractorId, ProjectStatus status, int page, int count) throws IOException;

    List<ProposalDto> getMyProposals(Long contractorId, ProposalStatus status, int page, int count) throws IOException;

    List<ChapterDto> getMyChaptersByProjectId(Long ProjectId, Long ContractorId) throws IOException;

    List<CalculationDto> getCalculationsByChapter(Long chapterId, int page, int count) throws IOException;

    void updateWorkPriceFact(Integer workPrice, Long calculationId) throws IOException, NotUpdateDataInDbException;

    CalculationDto createCalculation(Long chapterId, Integer YYYY, Integer MM, Integer workPricePlan) throws IOException, NotCreateDataInDbException;

    void startWork(Long proposalId) throws IOException, NotUpdateDataInDbException;

    void cancelProposal(Long proposalId) throws IOException, NotUpdateDataInDbException;

    ProposalDto createProposal(Long chapterId, Long contractorId) throws IOException, NotCreateDataInDbException;

}
