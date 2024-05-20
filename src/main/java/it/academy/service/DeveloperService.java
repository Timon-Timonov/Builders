package it.academy.service;

import it.academy.dto.CreateRequestDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.LoginDto;
import it.academy.dto.FilterPageDto;
import it.academy.dto.*;

public interface DeveloperService {

    LoginDto createDeveloper(CreateRequestDto dto);

    DtoWithPageForUi<ProjectDto> getMyProjects(FilterPageDto dto);

    DtoWithPageForUi<ContractorDto> getMyContractors(FilterPageDto dto);

    DtoWithPageForUi<ProposalDto> getAllMyProposals(FilterPageDto dto);

    DtoWithPageForUi<ProjectDto> createProject(CreateRequestDto dto);

    DtoWithPageForUi<ChapterDto> createChapter(CreateRequestDto dto);

    DtoWithPageForUi<ChapterDto> cancelChapter(Long chapterId);

    DtoWithPageForUi<ChapterDto> getChaptersByProject(FilterPageDto dto);

    DtoWithPageForUi<ChapterDto> getChaptersByContractorIdAndDeveloperId(FilterPageDto dto);

    DtoWithPageForUi<ProposalDto> changeStatusOfProposal(ChangeRequestDto dto);

    DtoWithPageForUi<ProposalDto> getProposalsByChapter(FilterPageDto dto);

    DtoWithPageForUi<ProjectDto> changeProjectStatus(ChangeRequestDto dto);

    DtoWithPageForUi<CalculationDto> getCalculationsByChapterId(FilterPageDto dto);

    DtoWithPageForUi<MoneyTransferDto> payMoney(CreateRequestDto dto);
}
