package it.academy.controller;

import it.academy.controller.dto.CreateRequestDto;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.LoginDto;
import it.academy.controller.dto.PageRequestDto;
import it.academy.dto.*;

public interface AdminController {

    LoginDto logIn(UserDto userDto);

    LoginDto createAdmin(CreateRequestDto dto);

    UserDto getUser(String email) throws Exception;

    DtoWithPageForUi<ContractorDto> getAllContractors(PageRequestDto dto);

    DtoWithPageForUi<UserDto> getAllAdministrators();

    DtoWithPageForUi<DeveloperDto> getAllDevelopers(PageRequestDto dto);

    DtoWithPageForUi<ProjectDto> getProjectsByDeveloper(PageRequestDto dto);

    DtoWithPageForUi<ChapterDto> getChaptersByProjectId(PageRequestDto dto);

    DtoWithPageForUi<ChapterDto> getChaptersByContractorId(PageRequestDto dto);

    DtoWithPageForUi<MoneyTransferDto> getMoneyTransfers(PageRequestDto dto);

    DtoWithPageForUi<CalculationDto> getCalculationsByChapterId(PageRequestDto dto);

    DtoWithPageForUi<ProposalDto> getProposalsByChapterId(PageRequestDto dto);

    DtoWithPageForUi<ProposalDto> getProposalsByContractorId(PageRequestDto dto);

    DtoWithPageForUi<UserDto> changeUserStatus(PageRequestDto dto);

    DtoWithPageForUi<UserDto> deleteUser(PageRequestDto dto);

    DtoWithPageForUi<CalculationDto> deleteCalculation(PageRequestDto dto);

    DtoWithPageForUi<ChapterDto> deleteChapter(PageRequestDto dto);

    DtoWithPageForUi<ProjectDto> deleteMoneyTransfer(PageRequestDto dto);

    DtoWithPageForUi<ProjectDto> deleteProject(PageRequestDto dto);

    DtoWithPageForUi<ProposalDto> deleteProposal(PageRequestDto dto);
}
