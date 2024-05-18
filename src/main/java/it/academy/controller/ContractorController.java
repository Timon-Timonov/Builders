package it.academy.controller;

import it.academy.dto.CreateRequestDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.LoginDto;
import it.academy.dto.FilterPageDto;
import it.academy.dto.*;

public interface ContractorController {

    LoginDto createContractor(CreateRequestDto dto);

    DtoWithPageForUi<ProjectDto> getMyProjects(FilterPageDto dto);

    DtoWithPageForUi<ProjectDto> getMyProjectsByDeveloper(FilterPageDto dto);

    DtoWithPageForUi<ChapterDto> getAllChapterNames();

    DtoWithPageForUi<ChapterDto> getFreeChapters(FilterPageDto dto);

    DtoWithPageForUi<DeveloperDto> getMyDevelopers(FilterPageDto dto);

    DtoWithPageForUi<ProposalDto> getMyProposals(FilterPageDto dto);

    DtoWithPageForUi<ChapterDto> getMyChaptersByProjectId(FilterPageDto dto);

    DtoWithPageForUi<CalculationDto> getCalculationsByChapter(FilterPageDto dto);

    DtoWithPageForUi<CalculationDto> updateWorkPriceFact(FilterPageDto dto);

    DtoWithPageForUi<CalculationDto> createCalculation(CreateRequestDto dto);

    DtoWithPageForUi<ProposalDto> setProposalStatus(FilterPageDto dto);

    DtoWithPageForUi<ProposalDto> createProposal(CreateRequestDto dto);

}
