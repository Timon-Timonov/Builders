package it.academy.controller;

import it.academy.controller.dto.CreateRequestDto;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.LoginDto;
import it.academy.controller.dto.PageRequestDto;
import it.academy.dto.*;
import it.academy.pojo.enums.ProposalStatus;

public interface ContractorController {

    LoginDto createContractor(CreateRequestDto dto);

    DtoWithPageForUi<ProjectDto> getMyProjects(PageRequestDto dto);

    DtoWithPageForUi<ProjectDto> getMyProjectsByDeveloper(PageRequestDto dto);

    DtoWithPageForUi<ChapterDto> getAllChapterNames();

    DtoWithPageForUi<ChapterDto> getFreeChapters(PageRequestDto dto);

    DtoWithPageForUi<DeveloperDto> getMyDevelopers(PageRequestDto dto);

    DtoWithPageForUi<ProposalDto> getMyProposals(PageRequestDto dto);

    DtoWithPageForUi<ChapterDto> getMyChaptersByProjectId(PageRequestDto dto);

    DtoWithPageForUi<CalculationDto> getCalculationsByChapter(PageRequestDto dto);

    DtoWithPageForUi<CalculationDto> updateWorkPriceFact(PageRequestDto dto);

    DtoWithPageForUi<CalculationDto> createCalculation(CreateRequestDto dto);

    DtoWithPageForUi<ProposalDto> setProposalStatus(PageRequestDto dto);

    DtoWithPageForUi<ProposalDto> createProposal(CreateRequestDto dto);

}
