package it.academy.controller.impl;

import it.academy.controller.AdminController;
import it.academy.controller.dto.CreateRequestDto;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.LoginDto;
import it.academy.controller.dto.PageRequestDto;
import it.academy.dto.*;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.*;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.Roles;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;
import it.academy.service.AdminService;
import it.academy.service.dto.Page;
import it.academy.service.impl.AdminServiceImpl;
import it.academy.util.Util;
import it.academy.util.converters.*;
import lombok.extern.log4j.Log4j2;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.academy.util.constants.JspURLs.*;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
public class AdminControllerImpl implements AdminController {

    private final AdminService service = new AdminServiceImpl();

    @Override
    public LoginDto logIn(UserDto userDtoFromUi) {

        String exceptionMessage = null;
        UserDto userFromDb = null;
        String url = null;

        String email = userDtoFromUi.getEmail();
        if (email != null && !email.isBlank()) {
            User user = null;
            try {
                user = service.getUser(email);
            } catch (Exception e) {
                exceptionMessage = SOMETHING_WENT_WRONG;
            }

            if (user != null) {
                String password = userDtoFromUi.getPassword();
                if (password != null && !password.isEmpty()) {
                    if (password.equals(user.getPassword())) {
                        if (UserStatus.ACTIVE.equals(user.getStatus())) {
                            Roles role = user.getRole();
                            userFromDb = UserConverter.convertToDto(user);
                            switch (role) {
                                case CONTRACTOR:
                                    url = CONTRACTOR_PAGES_MAIN_JSP;
                                    break;
                                case DEVELOPER:
                                    url = DEVELOPER_PAGES_MAIN_JSP;
                                    break;
                                case ADMIN:
                                    url = ADMIN_PAGES_MAIN_JSP;
                                    break;
                                default:
                                    exceptionMessage = ROLE_IS_INVALID;
                            }
                        } else {
                            exceptionMessage = USER_HAS_NOT_ACTIVE_STATUS_IT_IS_IMPOSSIBLE_TO_USE_THIS_ACCOUNT;
                        }
                    } else {
                        exceptionMessage = PASSWORD_IS_INVALID;
                    }
                } else {
                    exceptionMessage = PASSWORD_IS_EMPTY;
                }
            } else {
                exceptionMessage = EMAIL_IS_INVALID;
            }
        } else {
            exceptionMessage = EMAIL_IS_EMPTY;
        }

        LoginDto loginDto = LoginDto.builder().build();
        if (exceptionMessage != null) {
            loginDto.setExceptionMessage(exceptionMessage);
        } else {
            loginDto.setUrl(url);
            loginDto.setUserFromDb(userFromDb);
        }
        return loginDto;
    }

    @Override
    public LoginDto createAdmin(CreateRequestDto dto) {

        String exceptionMessage = null;

        try {
            User newUser = service.createAdmin(dto.getEmail(), dto.getPassword());
            if (newUser == null) {
                throw new NotCreateDataInDbException();
            }
            log.trace(ACCOUNT + CREATED_SUCCESSFUL + newUser.getId());
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
                           .url(SLASH_STRING + MAIN_ADMINISTRATOR_SERVLET)
                           .build();
        }
        return loginDto;
    }

    @Override
    public UserDto getUser(String email) throws Exception {

        return UserConverter.convertToDto(service.getUser(email));
    }

    @Override
    public DtoWithPageForUi<ContractorDto> getAllContractors(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ContractorDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();
        UserStatus status = null;
        try {
            status = (UserStatus) dto.getStatus();
            Page<Contractor> contractorPage = service.getAllContractors(status, page, count);
            page = contractorPage.getPageNumber();
            list.addAll(contractorPage.getList()
                            .stream()
                            .map(contractor -> ContractorConverter.convertToDto(contractor, null))
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
                                   .url(ADMIN_PAGES_LIST_WITH_CONTRACTORS_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<UserDto> getAllAdministrators() {

        String exceptionMessage = null;
        List<UserDto> list = new ArrayList<>();
        try {
            list.addAll(service.getAllAdministrators()
                            .stream()
                            .map(UserConverter::convertToDto)
                            .collect(Collectors.toList()));
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }

        DtoWithPageForUi<UserDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<UserDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<UserDto>builder()
                                   .list(list)
                                   .url(ADMIN_PAGES_LIST_WITH_ADMINS_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<DeveloperDto> getAllDevelopers(PageRequestDto dto) {

        String exceptionMessage = null;
        List<DeveloperDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();
        UserStatus status = null;
        try {
            status = (UserStatus) dto.getStatus();
            Page<Developer> developerPage = service.getAllDevelopers(status, page, count);
            page = developerPage.getPageNumber();
            list.addAll(developerPage.getList()
                            .stream()
                            .map(developer -> DeveloperConverter.convertToDto(developer, null))
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
                                   .list(list)
                                   .status(status)
                                   .url(ADMIN_PAGES_LIST_WITH_DEVELOPERS_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProjectDto> getProjectsByDeveloper(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ProjectDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();
        ProjectStatus status = null;

        try {
            status = (ProjectStatus) dto.getStatus();
            Page<Project> projectPage = service.getProjectsByDeveloper(dto.getId(), status, page, count);
            page = projectPage.getPageNumber();
            list.addAll(projectPage.getList()
                            .stream()
                            .map(project -> ProjectConverter.convertToDto(project, null, null))
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
                                   .url(ADMIN_PAGES_LIST_WITH_PROJECTS_JSP)
                                   .id(dto.getId())
                                   .name(dto.getName())
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ChapterDto> getChaptersByProjectId(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ChapterDto> list = new ArrayList<>();

        try {
            list.addAll(service.getChaptersByProjectId(dto.getId()).stream()
                            .map(chapter -> ChapterConverter.getChapterDtoForContractor(chapter, null))
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
                                   .url(ADMIN_PAGES_LIST_WITH_CHAPTERS_FROM_PROJECT_JSP)
                                   .build();
        }
        return dtoWithListForUi;
    }

    @Override
    public DtoWithPageForUi<ChapterDto> getChaptersByContractorId(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ChapterDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();

        try {
            Page<Chapter> chapterPage = service.getChaptersByContractorId(dto.getId(), page, count);
            page = chapterPage.getPageNumber();
            list.addAll(chapterPage.getList()
                            .stream()
                            .map(chapter -> ChapterConverter.getChapterDtoForContractor(chapter, null))
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
                                   .page(page)
                                   .countOnPage(count)
                                   .list(list)
                                   .name(dto.getName())
                                   .id(dto.getId())
                                   .url(ADMIN_PAGES_LIST_WITH_CHAPTERS_FROM_CONTRACTOR_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<MoneyTransferDto> getMoneyTransfers(PageRequestDto dto) {

        String exceptionMessage = null;
        List<MoneyTransferDto> list = new ArrayList<>();

        try {
            list.addAll(service.getMoneyTransfers(dto.getId()).stream()
                            .map(MoneyTransferConverter::convertToDto)
                            .collect(Collectors.toList()));
        } catch (NoResultException e) {
            exceptionMessage = THERE_IS_NO_SUCH_DATA_IN_DB;
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + dto.getId(), e);
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
                                   .id(dto.getId())
                                   .list(list)
                                   .url(ADMIN_PAGES_LIST_WITH_MONEY_TRANSFERS_JSP)
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
            Page<Calculation> calculationPage = service.getCalculationsByChapterId(dto.getId(), page, count);
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
                                   .page(page)
                                   .countOnPage(count)
                                   .id(dto.getId())
                                   .name(dto.getName())
                                   .list(list)
                                   .url(ADMIN_PAGES_LIST_WITH_CALCULATIONS_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProposalDto> getProposalsByChapterId(PageRequestDto dto) {

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
                                   .url(ADMIN_PAGES_LIST_WITH_PROPOSALS_FROM_CHAPTER_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProposalDto> getProposalsByContractorId(PageRequestDto dto) {

        String exceptionMessage = null;
        List<ProposalDto> list = new ArrayList<>();
        Integer page = dto.getPage();
        Integer count = dto.getCount();
        ProposalStatus status = null;

        try {
            status = (ProposalStatus) dto.getStatus();
            Page<Proposal> proposalPage = service.getProposalsByContractorId(dto.getId(), status, page, count);
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
                                   .url(ADMIN_PAGES_LIST_WITH_PROPOSALS_FROM_CONTRACTOR_JSP)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<UserDto> changeUserStatus(PageRequestDto dto) {

        String exceptionMessage = null;

        try {
            service.changeUserStatus(dto.getId(), (UserStatus) dto.getStatus());
            log.trace(USER_STATUS_CHANED_USER_ID + dto.getId());
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
        DtoWithPageForUi<UserDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<UserDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<UserDto>builder()
                                   .url(SLASH_STRING + MAIN_ADMINISTRATOR_SERVLET)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<UserDto> deleteUser(PageRequestDto dto) {

        String exceptionMessage = null;

        try {
            service.deleteUser(dto.getId());
            log.trace(USER_DELETE_ID + dto.getId());
        } catch (NotUpdateDataInDbException e) {
            exceptionMessage = USER_NOT_DELETE;
            log.debug(DELETE_FAIL_USER_ID + dto.getId(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }

        DtoWithPageForUi<UserDto> dtoWithPageForUi;
        if (exceptionMessage != null) {
            dtoWithPageForUi = DtoWithPageForUi.<UserDto>builder()
                                   .exceptionMessage(exceptionMessage)
                                   .build();
        } else {
            dtoWithPageForUi = DtoWithPageForUi.<UserDto>builder()
                                   .url(SLASH_STRING + MAIN_ADMINISTRATOR_SERVLET)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<CalculationDto> deleteCalculation(PageRequestDto dto) {

        String exceptionMessage = null;

        try {
            service.deleteCalculation(dto.getId());
            log.trace(CHAPTER_DELETE_ID + dto.getId());
        } catch (NotUpdateDataInDbException e) {
            exceptionMessage = DELETE_FAIL_CALCULATION_ID + dto.getId();
            log.error(DELETE_FAIL_CALCULATION_ID + dto.getId(), e);
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
                                   .url(SLASH_STRING + GET_CALCULATION_ADMINISTRATOR_SERVLET)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ChapterDto> deleteChapter(PageRequestDto dto) {

        String exceptionMessage = null;

        try {
            service.deleteChapter(dto.getId());
            log.trace(CHAPTER_DELETE_ID + dto.getId());
        } catch (NotUpdateDataInDbException e) {
            exceptionMessage = DELETE_FAIL_CHAPTER_ID + dto.getId();
            log.error(DELETE_FAIL_CHAPTER_ID + dto.getId(), e);
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
                                   .url(SLASH_STRING + GET_CHAPTERS_FROM_PROJECT_ADMINISTRATOR_SERVLET)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProjectDto> deleteMoneyTransfer(PageRequestDto dto) {

        String exceptionMessage = null;

        try {
            service.deleteMoneyTransfer(dto.getId());
            log.trace(MONEY_TRANSFER_DELETE_ID + dto.getId());
        } catch (NotUpdateDataInDbException e) {
            exceptionMessage = DELETE_FAIL_MONEY_TRANSFER_ID + dto.getId();
            log.error(DELETE_FAIL_MONEY_TRANSFER_ID + dto.getId(), e);
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
                                   .url(SLASH_STRING + GET_MONEY_TRANSFER_ADMINISTRATOR_SERVLET)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProjectDto> deleteProject(PageRequestDto dto) {

        String exceptionMessage = null;

        try {
            service.deleteProject(dto.getId());
            log.trace(PROJECT_DELETE_ID + dto.getId());
        } catch (NotUpdateDataInDbException e) {
            exceptionMessage = PROJECT_NOT_DELETE;
            log.debug(DELETE_FAIL_PROJECT_ID + dto.getId(), e);
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
                                   .url(SLASH_STRING + GET_PROJECTS_ADMINISTRATOR_SERVLET)
                                   .build();
        }
        return dtoWithPageForUi;
    }

    @Override
    public DtoWithPageForUi<ProposalDto> deleteProposal(PageRequestDto dto) {

        String exceptionMessage = null;
        boolean showByContractor = Boolean.parseBoolean(dto.getName());
        String url = showByContractor ?
                         SLASH_STRING + GET_PROPOSALS_FROM_CONTRACTOR_ADMINISTRATOR_SERVLET
                         : SLASH_STRING + GET_PROPOSALS_FROM_CHAPTER_ADMINISTRATOR_SERVLET;

        try {
            service.deleteProposal(dto.getId());
            log.trace(PROPOSAL_DELETE_ID + dto.getId());
        } catch (NotUpdateDataInDbException e) {
            exceptionMessage = PROPOSAL_DELETE_FAILED;
            log.debug(PROPOSAL_DELETE_FAILED + dto.getId(), e);
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
                                   .build();
        }
        return dtoWithPageForUi;
    }
}
