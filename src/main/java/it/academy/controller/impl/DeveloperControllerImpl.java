package it.academy.controller.impl;

import it.academy.controller.DeveloperController;
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
import it.academy.service.DeveloperService;
import it.academy.service.dto.Page;
import it.academy.service.impl.DeveloperServiceImpl;
import it.academy.servlet.utils.WhatToDo;
import it.academy.util.Util;
import it.academy.util.converters.*;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.JspURLs.*;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
public class DeveloperControllerImpl implements DeveloperController {

    private final DeveloperService service = new DeveloperServiceImpl();

    @Override
    public LoginDto createDeveloper(CreateRequestDto dto) {

        String exceptionMessage = null;
        UserDto userFromDb = null;

        try {
            Developer newDeveloper = service.createDeveloper(dto.getEmail(), dto.getPassword(), dto.getName(),
                dto.getCity(), dto.getStreet(), dto.getBuilding());
            if (newDeveloper == null) {
                throw new NotCreateDataInDbException();
            }
            userFromDb = UserConverter.convertToDto(newDeveloper.getUser());
            log.trace(ACCOUNT + CREATED_SUCCESSFUL + newDeveloper.getId());
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
                           .url(DEVELOPER_PAGES_MAIN_JSP)
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
        ProjectStatus status = null;

        try {
            status = (ProjectStatus) dto.getStatus();
            Page<Project> projectPage = service.getMyProjects(dto.getId(), status, page, count);
            page = projectPage.getPageNumber();
            list.addAll(projectPage.getList().stream()
                            .map(project -> {
                                Integer projectPrice = project.getChapters().stream()
                                                           .map(Chapter::getPrice)
                                                           .reduce(ZERO_INT_VALUE, Integer::sum);
                                Integer projectDebt = Util.getProjectDept(project);
                                return ProjectConverter.convertToDto(project, projectPrice, projectDebt);
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
                                   .status(status)
                                   .url(DEVELOPER_PAGES_LIST_WITH_PROJECTS_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ContractorDto> getMyContractors(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ContractorDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();
        ProjectStatus status = null;
        try {
            status = (ProjectStatus) dto.getStatus();
            Page<Contractor> contractorPage = service.getMyContractors(dto.getId(), status, page, count);
            page = contractorPage.getPageNumber();
            list.addAll(contractorPage.getList().stream()
                            .map(contractor -> {
                                Integer contractorDebt = null;
                                try {
                                    contractorDebt = service.getTotalDeptByContractor(contractor.getId(), dto.getId());
                                } catch (IOException e) {
                                    log.error(GETING_OF_CONTRACTOR_DEBT_BY_CONTRACTOR_ID + contractor.getId() + AND_DEVELOPER_ID + dto.getId() + FAILED, e);
                                }
                                return ContractorConverter.convertToDto(contractor, contractorDebt);
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

        DtoWithPageForUi<ContractorDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<ContractorDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<ContractorDto>builder()
                                   .page(page)
                                   .countOnPage(count)
                                   .list(list)
                                   .status(status)
                                   .url(DEVELOPER_PAGES_LIST_WITH_CONTRACTORS_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProposalDto> getAllMyProposals(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ProposalDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();
        ProposalStatus status = null;
        try {
            status = (ProposalStatus) dto.getStatus();
            Page<Proposal> proposalPage = service.getAllMyProposals(dto.getId(), status, page, count);
            page = proposalPage.getPageNumber();
            list.addAll(proposalPage.getList().stream()
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
                                   .list(list)
                                   .status(status)
                                   .url(DEVELOPER_PAGES_LIST_WITH_ALL_PROPOSALS_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProjectDto> createProject(CreateRequestDto dto) {

        String exceptionMessage = null;
        List<ProjectDto> list = new ArrayList<>();

        Project project = null;
        try {
            project = service.createProject(dto.getId(), dto.getName(), dto.getCity(), dto.getStreet(), dto.getBuilding());
            if (project == null) {
                throw new NotCreateDataInDbException();
            }
            list.add(ProjectConverter.convertToDto(project, null, null));
            log.trace(PROJECT + CREATED_SUCCESSFUL + project.getId());
        } catch (NotCreateDataInDbException e) {
            exceptionMessage = PROJECT_NOT_CREATE;
            log.debug(PROJECT_NOT_CREATE + dto.toString(), e);
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
                                   .id(project.getId())
                                   .name(project.getName())
                                   .status(project.getStatus())
                                   .list(list)
                                   .url(DEVELOPER_PAGES_CREATE_CHAPTER_PAGE_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ChapterDto> createChapter(CreateRequestDto dto) {

        String exceptionMessage = null;

        try {
            service.createChapter(dto.getId(), dto.getName(), dto.getInt1());
        } catch (NotCreateDataInDbException e) {
            exceptionMessage = CHAPTER_NOT_CREATE;
            log.debug(PROJECT_NOT_CREATE + dto.toString(), e);
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
                                   .url(SLASH_STRING + GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ChapterDto> cancelChapter(PageRequestDto dto) {

        String exceptionMessage = null;

        try {
            service.cancelChapter(dto.getId());
            log.trace(CHAPTER_STATUS_CHANGED + dto.getId());
        } catch (NotUpdateDataInDbException e) {
            exceptionMessage = CHANGING_OF_CHAPTER_STATUS_FAILED;
            log.error(CHANGING_OF_CHAPTER_STATUS_FAILED + dto.getId(), e);
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
                                   .url(SLASH_STRING + GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET)
                                   .status(dto.getStatus())
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ChapterDto> getChaptersByProject(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ChapterDto> list = new ArrayList<>();

        try {
            list.addAll(service.getChaptersByProjectId(dto.getId()).stream()
                            .map(ChapterConverter::getChapterDtoForDeveloper)
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
                                   .id(dto.getId())
                                   .list(list)
                                   .name(dto.getName())
                                   .url(DEVELOPER_PAGES_LIST_WITH_CHAPTERS_JSP)
                                   .build();
        }
        return dtoWithListForUi;
    }

    @Override
    public DtoWithPageForUi<ChapterDto> getChaptersByContractorIdAndDeveloperId(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ChapterDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();
        ProjectStatus status = null;

        try {
            status = (ProjectStatus) dto.getStatus();
            Page<Chapter> chapterPage = service.getChaptersByContractorIdAndDeveloperId(
                dto.getSecondId(), dto.getId(), status, page, count);
            page = chapterPage.getPageNumber();
            list.addAll(chapterPage.getList().stream()
                            .map(ChapterConverter::getChapterDtoForDeveloper)
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

        DtoWithPageForUi<ChapterDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<ChapterDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<ChapterDto>builder()
                                   .page(page)
                                   .countOnPage(count)
                                   .list(list)
                                   .name(dto.getName())
                                   .id(dto.getId())
                                   .status(status)
                                   .url(DEVELOPER_PAGES_LIST_WITH_CHAPTERS_BY_ONE_CONTRACTOR_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProposalDto> changeStatusOfProposal(PageRequestDto dto) {

        String exceptionMessage = null;
        String url = WhatToDo.SHOW_PROPOSALS.toString().equals(dto.getName()) ?
                         SLASH_STRING + MAIN_DEVELOPER_SERVLET
                         :SLASH_STRING + GET_MY_PROPOSALS_FROM_CHAPTER_DEVELOPER_SERVLET;

        try {
            service.changeStatusOfProposal(dto.getId(), (ProposalStatus) dto.getStatus());
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
                                   .url(url)
                                   .status(dto.getStatus())
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProposalDto> getProposalsByChapter(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ProposalDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();
        ProposalStatus status = null;

        try {
            status = (ProposalStatus) dto.getStatus();
            Page<Proposal> proposalPage = service.getProposalsByChapterId(dto.getId(), status, page, count);
            page = proposalPage.getPageNumber();
            list.addAll(proposalPage.getList().stream()
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
                                   .list(list)
                                   .status(status)
                                   .name(dto.getName())
                                   .id(dto.getId())
                                   .url(DEVELOPER_PAGES_LIST_WITH_PROPOSALS_OF_CHAPTER_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProjectDto> changeProjectStatus(PageRequestDto dto) {

        String exceptionMessage = null;

        try {
            service.changeProjectStatus(dto.getId(), (ProjectStatus) dto.getStatus());
            log.trace(PROJECT_STATUS_CHANGED + dto.getId());
        } catch (ClassCastException e) {
            exceptionMessage = INVALID_VALUE;
            log.debug(INVALID_VALUE + dto.getStatus().toString(), e);
        } catch (NotUpdateDataInDbException e) {
            exceptionMessage = PROJECT_STATUS_NOT_UPDATE;
            log.debug(PROJECT_STATUS_NOT_UPDATE + dto.toString(), e);
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
                                   .url(SLASH_STRING + GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET)
                                   .status(dto.getStatus())
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<CalculationDto> getCalculationsByChapterId(PageRequestDto dto) {

        String exceptionMessage = null;
        List<CalculationDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();

        try {
            Page<Calculation> calculationPage = service.getCalculationsByChapterId(
                dto.getId(), page, count);
            page = calculationPage.getPageNumber();
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
                                   .id(dto.getId())
                                   .page(page)
                                   .countOnPage(count)
                                   .name(dto.getName())
                                   .list(list)
                                   .url(DEVELOPER_PAGES_LIST_WITH_CALCULATIONS_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<MoneyTransferDto> payMoney(CreateRequestDto dto) {

        long calculationId = dto.getId();
        int sumAdvance = dto.getInt1();
        int sumForWork = dto.getInt2();
        String exceptionMessage = null;

        try {
            if (sumAdvance != ZERO_INT_VALUE) {
                service.payAdvance(sumAdvance, calculationId);
            } else if (sumForWork != ZERO_INT_VALUE) {
                service.payForWork(sumForWork, calculationId);
            } else {
                throw new NotCreateDataInDbException();
            }
        } catch (NotCreateDataInDbException e) {
            exceptionMessage = NOT_SUCCESS_OPERATION;
            log.debug(NOT_SUCCESS_OPERATION + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        DtoWithPageForUi<MoneyTransferDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<MoneyTransferDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<MoneyTransferDto>builder()
                                   .url(SLASH_STRING + GET_MY_CALCULATION_DEVELOPER_SERVLET)
                                   .build();
        }
        return dtoWithPageForUi;
    }
}
