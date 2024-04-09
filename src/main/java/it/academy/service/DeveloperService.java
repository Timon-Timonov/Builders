package it.academy.service;

import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.exceptions.RoleException;
import it.academy.pojo.*;
import it.academy.pojo.enums.ChapterStatus;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;

import java.io.IOException;
import java.util.List;

public interface DeveloperService {

    Developer createDeveloper(
        String email, String password, String name, String city, String street, Integer building)
        throws IOException, NotCreateDataInDbException, EmailOccupaidException;

    Developer getDeveloper(Long userId) throws IOException, RoleException;

    List<Project> getMyProjects(Long developerId, ProjectStatus status, int page, int count) throws IOException;

    List<Contractor> getMyContractors(Long developerId, ProjectStatus status, int page, int count) throws IOException;

    List<Proposal> getAllMyProposals(Long developerId, ProposalStatus status, int page, int count) throws IOException;

    Project createProject(Long developerId, String name, String city, String street, Integer building)
        throws IOException, NotCreateDataInDbException;

    Chapter createChapter(Long projectId, String name, Integer price) throws IOException, NotCreateDataInDbException;

    void cancelChapter(Long chapterId) throws IOException, NotUpdateDataInDbException;

    List<Chapter> getChaptersByProjectId(Long projectId) throws IOException;

    List<Chapter> getChaptersByContractorId(Long contractorId, ChapterStatus status, int page, int count)
        throws IOException;

    void rejectProposal(Long proposalId) throws IOException, NotUpdateDataInDbException;

    void approveProposal(Long proposalId) throws IOException, NotUpdateDataInDbException;

    List<Proposal> getProposalsByChapterId(Long chapterId, ProposalStatus status, int page, int count)
        throws IOException;

    void startProject(Long projectId) throws IOException, NotUpdateDataInDbException;

    void endProject(Long projectId) throws IOException, NotUpdateDataInDbException;

    void cancelProject(Long projectId) throws IOException, NotUpdateDataInDbException;

    List<Calculation> getCalculationsByChapterId(Long chapterId, int page, int count) throws IOException;

    MoneyTransfer payAdvance(Integer sum, Long calculationId) throws IOException, NotCreateDataInDbException;

    MoneyTransfer payForWork(Integer sum, Long calculationId) throws IOException, NotCreateDataInDbException;

}
