package it.academy.controller;

import it.academy.controller.dto.CreateRequestDto;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.LoginDto;
import it.academy.controller.dto.PageRequestDto;
import it.academy.dto.*;
import it.academy.pojo.enums.ProposalStatus;

public interface DeveloperController {

    LoginDto createDeveloper(CreateRequestDto dto);

    DtoWithPageForUi<ProjectDto> getMyProjects(PageRequestDto dto);

    DtoWithPageForUi<ContractorDto> getMyContractors(PageRequestDto dto);

    DtoWithPageForUi<ProposalDto> getAllMyProposals(PageRequestDto dto);

    DtoWithPageForUi<ProjectDto> createProject(CreateRequestDto dto);

    DtoWithPageForUi<ChapterDto> createChapter(CreateRequestDto dto);

    DtoWithPageForUi<ChapterDto> cancelChapter(PageRequestDto dto);

    DtoWithPageForUi<ChapterDto> getChaptersByProject(PageRequestDto dto);

    DtoWithPageForUi<ChapterDto> getChaptersByContractorIdAndDeveloperId(PageRequestDto dto);

    DtoWithPageForUi<ProposalDto> changeStatusOfProposal(PageRequestDto dto);

    DtoWithPageForUi<ProposalDto> getProposalsByChapter(PageRequestDto dto);

    DtoWithPageForUi<ProjectDto> changeProjectStatus(PageRequestDto dto);

    DtoWithPageForUi<CalculationDto> getCalculationsByChapterId(PageRequestDto dto);

    DtoWithPageForUi<MoneyTransferDto> payMoney(CreateRequestDto dto);
}
