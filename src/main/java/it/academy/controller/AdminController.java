package it.academy.controller;

import it.academy.dto.CreateRequestDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.LoginDto;
import it.academy.dto.FilterPageDto;
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

    DtoWithPageForUi<ChapterDto> getChaptersByProjectId(FilterPageDto dto);

    DtoWithPageForUi<ChapterDto> getChaptersByContractorId(FilterPageDto dto);

    DtoWithPageForUi<MoneyTransferDto> getMoneyTransfers(FilterPageDto dto);

    DtoWithPageForUi<CalculationDto> getCalculationsByChapterId(FilterPageDto dto);

    DtoWithPageForUi<ProposalDto> getProposalsByChapterId(FilterPageDto dto);

    DtoWithPageForUi<ProposalDto> getProposalsByContractorId(FilterPageDto dto);

    DtoWithPageForUi<UserDto> changeUserStatus(FilterPageDto dto);

    DtoWithPageForUi<UserDto> deleteUser(FilterPageDto dto);

    DtoWithPageForUi<CalculationDto> deleteCalculation(FilterPageDto dto);

    DtoWithPageForUi<ChapterDto> deleteChapter(FilterPageDto dto);

    DtoWithPageForUi<ProjectDto> deleteMoneyTransfer(FilterPageDto dto);

    DtoWithPageForUi<ProjectDto> deleteProject(FilterPageDto dto);

    DtoWithPageForUi<ProposalDto> deleteProposal(FilterPageDto dto);
}
