package it.academy.service;

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
import java.sql.Date;
import java.util.List;

public interface ContractorService {

    Contractor createContractor(
        String email, String password, String name, String city, String street, Integer building)
        throws IOException, NotCreateDataInDbException, EmailOccupaidException;

    Contractor getContractor(Long userId) throws IOException, RoleException;

    List<Project> getMyProjects(Long contractorId, ProjectStatus status, int page, int count) throws IOException;

    List<Project> getMyProjectsByDeveloper(Long developerId, Long contractorId, int page, int count) throws IOException;

    List<String> getAllChapterNames() throws IOException;

    List<Chapter> getFreeChapters(String chapterName, int page, int count) throws IOException;

    List<Developer> getMyDevelopers(Long contractorId, ProjectStatus status, int page, int count) throws IOException;

    List<Proposal> getMyProposals(Long contractorId, ProposalStatus status, int page, int count) throws IOException;

    List<Chapter> getMyChaptersByProjectId(Long ProjectId, Long ContractorId) throws IOException;

    List<Calculation> getCalculationsByChapter(Long chapterId, int page, int count) throws IOException;

    void updateWorkPrice(Integer workPrice, Long calculationId) throws IOException, NotUpdateDataInDbException;

    Calculation createCalculation(Long chapterId, Date month) throws IOException, NotCreateDataInDbException;

    void starWork(Long proposalId) throws IOException, NotUpdateDataInDbException;

    void cancelProposal(Long proposalId) throws IOException, NotUpdateDataInDbException;

    Proposal createProposal(Long chapterId, Long contractorId) throws IOException, NotCreateDataInDbException;
}
