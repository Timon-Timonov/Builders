package it.academy.controller;

import it.academy.dto.CreateRequestDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.LoginDto;
import it.academy.dto.FilterPageDto;
import it.academy.dto.*;

public interface DeveloperController {

    LoginDto createDeveloper(CreateRequestDto dto);

    DtoWithPageForUi<ProjectDto> getMyProjects(FilterPageDto dto);

    DtoWithPageForUi<ContractorDto> getMyContractors(FilterPageDto dto);

    DtoWithPageForUi<ProposalDto> getAllMyProposals(FilterPageDto dto);

    DtoWithPageForUi<ProjectDto> createProject(CreateRequestDto dto);

    DtoWithPageForUi<ChapterDto> createChapter(CreateRequestDto dto);

    DtoWithPageForUi<ChapterDto> cancelChapter(FilterPageDto dto);

    DtoWithPageForUi<ChapterDto> getChaptersByProject(FilterPageDto dto);

    DtoWithPageForUi<ChapterDto> getChaptersByContractorIdAndDeveloperId(FilterPageDto dto);

    DtoWithPageForUi<ProposalDto> changeStatusOfProposal(FilterPageDto dto);

    DtoWithPageForUi<ProposalDto> getProposalsByChapter(FilterPageDto dto);

    DtoWithPageForUi<ProjectDto> changeProjectStatus(FilterPageDto dto);

    DtoWithPageForUi<CalculationDto> getCalculationsByChapterId(FilterPageDto dto);

    DtoWithPageForUi<MoneyTransferDto> payMoney(CreateRequestDto dto);
}
