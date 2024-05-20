package it.academy.service;

import it.academy.dto.*;

public interface ContractorService {

    LoginDto createContractor(CreateRequestDto dto);

    DtoWithPageForUi<ProjectDto> getMyProjects(FilterPageDto dto);

    DtoWithPageForUi<ProjectDto> getMyProjectsByDeveloper(FilterPageDto dto);

    DtoWithPageForUi<ChapterDto> getAllChapterNames();

    DtoWithPageForUi<ChapterDto> getFreeChapters(FilterPageDto dto);

    DtoWithPageForUi<DeveloperDto> getMyDevelopers(FilterPageDto dto);

    DtoWithPageForUi<ProposalDto> getMyProposals(FilterPageDto dto);

    DtoWithPageForUi<ChapterDto> getMyChaptersByProjectId(FilterPageDto dto);

    DtoWithPageForUi<CalculationDto> getCalculationsByChapter(FilterPageDto dto);

    DtoWithPageForUi<CalculationDto> updateWorkPriceFact(ChangeRequestDto dto);

    DtoWithPageForUi<CalculationDto> createCalculation(CreateRequestDto dto);

    DtoWithPageForUi<ProposalDto> setProposalStatus(ChangeRequestDto dto);

    DtoWithPageForUi<ProposalDto> createProposal(CreateRequestDto dto);

}
