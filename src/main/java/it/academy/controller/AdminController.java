package it.academy.controller;

import it.academy.dto.*;

public interface AdminController {

    LoginDto logIn(UserDto userDto);

    LoginDto createAdmin(CreateRequestDto dto);

    DtoWithPageForUi<UserDto> createUser(String role);

    UserDto getUser(String email) throws Exception;

    DtoWithPageForUi<ContractorDto> getAllContractors(FilterPageDto dto);

    DtoWithPageForUi<UserDto> toMainPage(Object role);

    DtoWithPageForUi<UserDto> getAllAdministrators();

    DtoWithPageForUi<DeveloperDto> getAllDevelopers(FilterPageDto dto);

    DtoWithPageForUi<ProjectDto> getProjectsByDeveloper(FilterPageDto dto);

    DtoWithPageForUi<ChapterDto> getChaptersByProjectId(long projectId);

    DtoWithPageForUi<ChapterDto> getChaptersByContractorId(FilterPageDto dto);

    DtoWithPageForUi<MoneyTransferDto> getMoneyTransfers(long calculationId);

    DtoWithPageForUi<CalculationDto> getCalculationsByChapterId(FilterPageDto dto);

    DtoWithPageForUi<ProposalDto> getProposalsByChapterId(FilterPageDto dto);

    DtoWithPageForUi<ProposalDto> getProposalsByContractorId(FilterPageDto dto);

    DtoWithPageForUi<UserDto> changeUserStatus(ChangeRequestDto dto);

    DtoWithPageForUi<UserDto> deleteUser(ChangeRequestDto dto);

    DtoWithPageForUi<CalculationDto> deleteCalculation(long calculationId);

    DtoWithPageForUi<ChapterDto> deleteChapter(long chapterId);

    DtoWithPageForUi<ProjectDto> deleteMoneyTransfer(long moneyTransferId);

    DtoWithPageForUi<ProjectDto> deleteProject(long projectId);

    DtoWithPageForUi<ProposalDto> deleteProposal(ChangeRequestDto dto);
}
