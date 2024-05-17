package it.academy.controller.impl;

import it.academy.controller.ContractorController;
import it.academy.controller.dto.CreateRequestDto;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.LoginDto;
import it.academy.controller.dto.PageRequestDto;
import it.academy.dto.*;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.Calculation;
import it.academy.pojo.Chapter;
import it.academy.pojo.Project;
import it.academy.pojo.Proposal;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;
import it.academy.service.ContractorService;
import it.academy.service.dto.Page;
import it.academy.service.impl.ContractorServiceImpl;
import it.academy.util.Util;
import it.academy.util.converters.*;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.academy.util.constants.Constants.FIRST_PAGE_NUMBER;
import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.JspURLs.*;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
public class ContractorControllerImpl implements ContractorController {


    private final ContractorService service = new ContractorServiceImpl();

    @Override
    public LoginDto createContractor(CreateRequestDto dto) {

        String exceptionMessage = null;
        UserDto userFromDb = null;

        try {
            Contractor newContractor = service.createContractor(dto.getEmail(), dto.getPassword(), dto.getName(),
                dto.getCity(), dto.getStreet(), dto.getBuilding());
            if (newContractor == null) {
                throw new NotCreateDataInDbException();
            }
            userFromDb = UserConverter.convertToDto(newContractor.getUser());
            log.trace(ACCOUNT + CREATED_SUCCESSFUL + newContractor.getId());
        } catch (EmailOccupaidException e) {
            exceptionMessage = EMAIL + IS_OCCUPIED;
            log.debug(EMAIL + IS_OCCUPIED, e);
        } catch (NotCreateDataInDbException e) {
            exceptionMessage = ACCOUNT_NOT_CREATE;
            log.debug(ACCOUNT_NOT_CREATE + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }

        LoginDto loginDto;
        if (exceptionMessage != null) {
            loginDto = LoginDto.builder()
                           .exceptionMessage(exceptionMessage)
                           .build();
        } else {
            loginDto = LoginDto.builder()
                           .userFromDb(userFromDb)
                           .url(CONTRACTOR_PAGES_MAIN_JSP)
                           .build();
        }
        return loginDto;
    }

    @Override
    public DtoWithPageForUi<ProjectDto> getMyProjects(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ProjectDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        ProjectStatus status = null;
        try {
            status = (ProjectStatus) dto.getStatus();
            Page<Project> projectPage = service.getMyProjects(dto.getId(), status, page, count);
            page = projectPage.getPageNumber();
            lastPageNumber = projectPage.getLastPageNumber();
            list.addAll(projectPage.getList()
                            .stream()
                            .map(project -> getProjectDtoForContractor(dto.getId(), project))
                            .collect(Collectors.toList()));
        } catch (ClassCastException e) {
            exceptionMessage = INVALID_VALUE;
            log.error(INVALID_VALUE + dto.getStatus().toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        DtoWithPageForUi<ProjectDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<ProjectDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<ProjectDto>builder()
                                   .list(list)
                                   .page(page)
                                   .countOnPage(count)
                                   .lastPageNumber(lastPageNumber)
                                   .status(status)
                                   .url(CONTRACTOR_PAGES_LIST_WITH_PROJECTS_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProjectDto> getMyProjectsByDeveloper(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ProjectDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        ProjectStatus status = null;
        try {
            status = (ProjectStatus) dto.getStatus();
            Page<Project> projectPage = service.getMyProjectsByDeveloper(dto.getId(), dto.getSecondId(), status, page, count);
            page = projectPage.getPageNumber();
            lastPageNumber = projectPage.getLastPageNumber();
            list.addAll(projectPage.getList().stream()
                            .map(project -> getProjectDtoForContractor(dto.getSecondId(), project))
                            .collect(Collectors.toList()));
        } catch (ClassCastException e) {
            exceptionMessage = INVALID_VALUE;
            log.error(INVALID_VALUE + dto.getStatus().toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        DtoWithPageForUi<ProjectDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<ProjectDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<ProjectDto>builder()
                                   .list(list)
                                   .status(status)
                                   .page(page)
                                   .countOnPage(count)
                                   .lastPageNumber(lastPageNumber)
                                   .id(dto.getId())
                                   .name(dto.getName())
                                   .url(CONTRACTOR_PAGES_LIST_WITH_PROJECTS_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ChapterDto> getAllChapterNames() {

        String exceptionMessage = null;
        List<ChapterDto> list = new ArrayList<>();

        try {
            list.addAll(service.getAllChapterNames().stream()
                            .map(name -> ChapterDto.builder().chapterName(name).build())
                            .collect(Collectors.toList()));
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }

        DtoWithPageForUi<ChapterDto> dtoWithListForUi;
        if (exceptionMessage != null) {
            dtoWithListForUi = DtoWithPageForUi.<ChapterDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithListForUi = DtoWithPageForUi.<ChapterDto>builder()
                                   .list(list)
                                   .url(LIST_WITH_CHAPTER_NAMES_JSP)
                                   .build();
        }
        return dtoWithListForUi;
    }

    @Override
    public DtoWithPageForUi<ChapterDto> getFreeChapters(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ChapterDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        ProjectStatus status = null;

        try {
            status = (ProjectStatus) dto.getStatus();
            Page<Chapter> chapterPage = service.getFreeChapters(dto.getId(), dto.getName(), status, page, count);
            page = chapterPage.getPageNumber();
            lastPageNumber = chapterPage.getLastPageNumber();
            list.addAll(chapterPage.getList()
                            .stream()
                            .map(chapter -> ChapterConverter.getChapterDtoForContractor(chapter, null))
                            .collect(Collectors.toList()));
        } catch (ClassCastException e) {
            exceptionMessage = INVALID_VALUE;
            log.error(INVALID_VALUE + dto.getStatus().toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }

        DtoWithPageForUi<ChapterDto> dtoWithListForUi;
        if (exceptionMessage != null) {
            dtoWithListForUi = DtoWithPageForUi.<ChapterDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithListForUi = DtoWithPageForUi.<ChapterDto>builder()
                                   .list(list)
                                   .status(status)
                                   .page(page)
                                   .countOnPage(count)
                                   .lastPageNumber(lastPageNumber)
                                   .name(dto.getName())
                                   .url(CONTRACTOR_PAGES_LIST_WITH_FREE_CHAPTERS_JSP)
                                   .build();
        }
        return dtoWithListForUi;
    }

    @Override
    public DtoWithPageForUi<DeveloperDto> getMyDevelopers(PageRequestDto dto) {

        String exceptionMessage = null;
        List<DeveloperDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        ProjectStatus status = null;
        try {
            status = (ProjectStatus) dto.getStatus();
            Page<Developer> developerPage = service.getMyDevelopers(dto.getId(), status, page, count);
            page = developerPage.getPageNumber();
            lastPageNumber = developerPage.getLastPageNumber();
            list.addAll(developerPage.getList()
                            .stream()
                            .map(developer -> {
                                Integer developerDebt = null;
                                try {
                                    developerDebt = service.getTotalDeptByDeveloper(dto.getId(), developer.getId());
                                } catch (IOException e) {
                                    log.error(GETTING_OF_DEBT + dto.getId() + WITH_DEVELOPER_ID + developer.getId() + FAILED, e);
                                }
                                return DeveloperConverter.convertToDto(developer, developerDebt);
                            })
                            .collect(Collectors.toList()));
        } catch (ClassCastException e) {
            exceptionMessage = INVALID_VALUE;
            log.error(INVALID_VALUE + dto.getStatus().toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }

        DtoWithPageForUi<DeveloperDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<DeveloperDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<DeveloperDto>builder()
                                   .page(page)
                                   .countOnPage(count)
                                   .lastPageNumber(lastPageNumber)
                                   .list(list)
                                   .status(status)
                                   .url(CONTRACTOR_PAGES_LIST_WITH_DEVELOPERS_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProposalDto> getMyProposals(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ProposalDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        ProposalStatus status = null;
        try {
            status = (ProposalStatus) dto.getStatus();
            Page<Proposal> proposalPage = service.getMyProposals(dto.getId(), status, page, count);
            page = proposalPage.getPageNumber();
            lastPageNumber = proposalPage.getLastPageNumber();
            list.addAll(proposalPage.getList()
                            .stream()
                            .map(ProposalConverter::convertToDto)
                            .collect(Collectors.toList()));
        } catch (ClassCastException e) {
            exceptionMessage = INVALID_VALUE;
            log.error(INVALID_VALUE + dto.getStatus().toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }

        DtoWithPageForUi<ProposalDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<ProposalDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<ProposalDto>builder()
                                   .page(page)
                                   .countOnPage(count)
                                   .lastPageNumber(lastPageNumber)
                                   .list(list)
                                   .status(status)
                                   .url(CONTRACTOR_PAGES_LIST_WITH_PROPOSALS_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ChapterDto> getMyChaptersByProjectId(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ChapterDto> list = new ArrayList<>();

        try {
            list.addAll(service.getMyChaptersByProjectId(dto.getId(), dto.getSecondId()).stream()
                            .map(chapter -> {
                                Integer chapterDebt = Util.getDebtByChapter(chapter);
                                return ChapterConverter.getChapterDtoForContractor(chapter, chapterDebt);
                            })
                            .collect(Collectors.toList()));
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }

        DtoWithPageForUi<ChapterDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<ChapterDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<ChapterDto>builder()
                                   .list(list)
                                   .name(dto.getName())
                                   .id(dto.getId())
                                   .url(CONTRACTOR_PAGES_LIST_WITH_CHAPTERS_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<CalculationDto> getCalculationsByChapter(PageRequestDto dto) {

        String exceptionMessage = null;
        List<CalculationDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;

        try {
            Page<Calculation> calculationPage = service.getCalculationsByChapter(dto.getId(), page, count);
            page = calculationPage.getPageNumber();
            lastPageNumber = calculationPage.getLastPageNumber();
            list.addAll(calculationPage.getList().stream()
                            .map(calculation -> {
                                Integer[] sums = Util.getCalculationSums(calculation);
                                return CalculationConverter.convertToDto(calculation, sums[0], sums[1], sums[2]);
                            })
                            .collect(Collectors.toList()));
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }

        DtoWithPageForUi<CalculationDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<CalculationDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<CalculationDto>builder()
                                   .page(page)
                                   .countOnPage(count)
                                   .lastPageNumber(lastPageNumber)
                                   .id(dto.getId())
                                   .name(dto.getName())
                                   .list(list)
                                   .url(CONTRACTOR_PAGES_LIST_WITH_CALCULATIONS_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<CalculationDto> updateWorkPriceFact(PageRequestDto dto) {

        String exceptionMessage = null;
        try {
            service.updateWorkPriceFact(dto.getCount(), dto.getId());
        } catch (NotUpdateDataInDbException e) {
            exceptionMessage = CALCULATION_NOT_UPDATED;
            log.debug(CALCULATION_NOT_UPDATED + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }

        DtoWithPageForUi<CalculationDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<CalculationDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<CalculationDto>builder()
                                   .url(SLASH_STRING + GET_MY_CALCULATION_CONTRACTOR_SERVLET)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<CalculationDto> createCalculation(CreateRequestDto dto) {

        String exceptionMessage = null;
        try {
            service.createCalculation(dto.getId(), dto.getInt1(), dto.getInt2(), dto.getInt3());
        } catch (NotCreateDataInDbException e) {
            exceptionMessage = CALCULATION_NOT_CREATED;
            log.debug(CALCULATION_NOT_CREATED + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }

        DtoWithPageForUi<CalculationDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<CalculationDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<CalculationDto>builder()
                                   .url(SLASH_STRING + GET_MY_CALCULATION_CONTRACTOR_SERVLET)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProposalDto> setProposalStatus(PageRequestDto dto) {

        String exceptionMessage = null;

        try {
            service.setProposalStatus(dto.getId(), (ProposalStatus) dto.getStatus());
            log.trace(PROPOSAL_STATUS_CHANGED + dto.getId());
        } catch (ClassCastException e) {
            exceptionMessage = INVALID_VALUE;
            log.debug(INVALID_VALUE + dto.getStatus().toString(), e);
        } catch (NotUpdateDataInDbException e) {
            exceptionMessage = PROPOSAL_STATUS_NOT_UPDATE;
            log.debug(PROPOSAL_STATUS_NOT_UPDATE + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        DtoWithPageForUi<ProposalDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<ProposalDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<ProposalDto>builder()
                                   .url(SLASH_STRING + MAIN_CONTRACTOR_SERVLET)
                                   .status(dto.getStatus())
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProposalDto> createProposal(CreateRequestDto dto) {

        String exceptionMessage = null;
        try {
            service.createProposal(dto.getId(), dto.getSecondId());
        } catch (NotCreateDataInDbException e) {
            exceptionMessage = PROPOSAL_NOT_CREATE;
            log.debug(PROPOSAL_NOT_CREATE + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }

        DtoWithPageForUi<ProposalDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<ProposalDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<ProposalDto>builder()
                                   .url(SLASH_STRING + GET_FREE_CHAPTERS_CONTRACTOR_SERVLET)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    private ProjectDto getProjectDtoForContractor(long contractorId, Project project) {

        Integer projectPrice = project.getChapters().stream()
                                   .filter(chapter -> chapter.getContractor() != null)
                                   .filter(chapter -> chapter.getContractor().getId().equals(contractorId))
                                   .map(Chapter::getPrice)
                                   .reduce(ZERO_INT_VALUE, Integer::sum);
        Integer projectDebt = project.getChapters().stream()
                                  .filter(chapter -> chapter.getContractor() != null)
                                  .filter(chapter -> chapter.getContractor().getId().equals(contractorId))
                                  .map(Util::getDebtByChapter)
                                  .reduce(ZERO_INT_VALUE, Integer::sum);
        return ProjectConverter.convertToDto(project, projectPrice, projectDebt);
    }
}
