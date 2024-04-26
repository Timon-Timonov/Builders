package it.academy.controller;

import it.academy.dto.*;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.UserStatus;

import java.io.IOException;
import java.util.List;

public interface AdminController {

    void createAdmin(String email, String password) throws Exception;

    UserDto getUser(String email) throws Exception;

    Page<ContractorDto> getAllContractors(UserStatus status, int page, int count) throws Exception;

    List<UserDto> getAllAdministrators() throws Exception;

    Page<DeveloperDto> getAllDevelopers(UserStatus status, int page, int count) throws Exception;

    Page<ProjectDto> getProjectsByDeveloper(long developerId, ProjectStatus status, int page, int count) throws Exception;

    List<ChapterDto> getChaptersByProjectId(long projectId) throws Exception;

    Page<ChapterDto> getChaptersByContractorId(long contractorId, int page, int count) throws Exception;

    List<MoneyTransferDto> getMoneyTransfers(long calculationId) throws Exception;

    Page<CalculationDto> getCalculationsByChapterId(long chapterId, int page, int count) throws Exception;

    Page<ProposalDto> getProposalsByChapterId(long chapterId, ProposalStatus status, int page, int count) throws Exception;

    Page<ProposalDto> getProposalsByContractorId(long contractorId, ProposalStatus status, int page, int count) throws Exception;

    void changeUserStatus(long userId, UserStatus newStatus) throws Exception;

    void deleteUser(long userId) throws IOException, NotUpdateDataInDbException;

    void deleteCalculation(long calculationId) throws IOException, NotUpdateDataInDbException;

    void deleteChapter(long chapterId) throws IOException, NotUpdateDataInDbException;

    void deleteMoneyTransfer(long transferId) throws IOException, NotUpdateDataInDbException;

    void deleteProject(long projectId) throws IOException, NotUpdateDataInDbException;

    void deleteProposal(long proposalId) throws IOException, NotUpdateDataInDbException;
}
