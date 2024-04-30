package it.academy.service;

import it.academy.service.dto.Page;
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

public interface ContractorService {

    Contractor createContractor(
        String email, String password, String name, String city, String street, String building)
        throws Exception;

    Contractor getContractor(long userId) throws Exception;

    Page<Project> getMyProjects(long contractorId, ProjectStatus status, int page, int count) throws Exception;

    Page<Project> getMyProjectsByDeveloper
        (long developerId, long contractorId, ProjectStatus status, int page, int count)
        throws Exception;

    Page<Chapter> getFreeChapters(long contractorId, String chapterName, ProjectStatus projectStatus, int page, int count) throws Exception;

    Page<Developer> getMyDevelopers(long contractorId, ProjectStatus status, int page, int count) throws Exception;

    Page<Proposal> getMyProposals(long contractorId, ProposalStatus status, int page, int count) throws Exception;

    Page<Calculation> getCalculationsByChapter(long chapterId, int page, int count) throws Exception;

    List<Chapter> getMyChaptersByProjectId(long ProjectId, long ContractorId) throws Exception;

    List<String> getAllChapterNames() throws IOException;

    void createCalculation(long chapterId, int YYYY, int MM, int workPricePlan) throws Exception;

    void createProposal(long chapterId, long contractorId) throws Exception;

    int getTotalDeptByDeveloper(long contractorId, long developerId) throws IOException;

    void updateWorkPriceFact(int workPrice, long calculationId) throws Exception;

    void setProposalStatus(long proposalId, ProposalStatus newStatus) throws Exception;
}
