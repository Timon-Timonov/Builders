package it.academy.controller;

import it.academy.dto.*;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.UserStatus;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;

public interface AdminController {

    UserDto createAdmin(String email, String password) throws Exception;

    UserDto getUser(String email) throws Exception;

    Page<ContractorDto> getAllContractors(UserStatus status, int page, int count) throws Exception;

    List<UserDto> getAllAdministrators() throws Exception;

    Page<DeveloperDto> getAllDevelopers(UserStatus status, int page, int count) throws Exception;

    Page<ProjectDto> getProjectsByDeveloper(Long developerId, ProjectStatus status, int page, int count) throws Exception;

    List<ChapterDto> getChaptersByProjectId(Long projectId) throws Exception;

    Page<ChapterDto> getChaptersByContractorId(Long contractorId, int page, int count) throws Exception;

    List<MoneyTransferDto> getMoneyTransfers(Long calculationId) throws Exception;

    Page<CalculationDto> getCalculationsByChapterId(Long chapterId, int page, int count) throws Exception;

    Page<ProposalDto> getProposalsByChapterId(long chapterId, ProposalStatus status, int page, int count) throws Exception;

    Page<ProposalDto> getProposalsByContractorId(long contractorId, ProposalStatus status, int page, int count) throws Exception;

    void changeUserStatus(Long userId, UserStatus newStatus) throws Exception;

    void deleteUser(Long userId) throws IOException, NotUpdateDataInDbException;

    void deleteCalculation(Long calculationId) throws IOException, NotUpdateDataInDbException;

    void deleteChapter(Long chapterId) throws IOException, NotUpdateDataInDbException;

    void deleteMoneyTransfer(Long transferId) throws IOException, NotUpdateDataInDbException;

    void deleteProject(Long projectId) throws IOException, NotUpdateDataInDbException;

    void deleteProposal(Long proposalId) throws IOException, NotUpdateDataInDbException;
}
