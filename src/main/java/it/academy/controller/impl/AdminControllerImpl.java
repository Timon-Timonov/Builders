package it.academy.controller.impl;

import it.academy.controller.AdminController;
import it.academy.controller.dto.LoginDto;
import it.academy.service.dto.Page;
import it.academy.dto.*;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.*;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.Roles;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;
import it.academy.service.AdminService;
import it.academy.service.impl.AdminServiceImpl;
import it.academy.util.Util;
import it.academy.util.converters.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static it.academy.util.constants.JspURLs.*;
import static it.academy.util.constants.Messages.*;

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
    public void createAdmin(String email, String password) throws Exception {

        UserConverter.convertToDto(service.createAdmin(email, password));
    }

    @Override
    public UserDto getUser(String email) throws Exception {

        return UserConverter.convertToDto(service.getUser(email));
    }

    @Override
    public Page<ContractorDto> getAllContractors(UserStatus status, int page, int count) throws Exception {

        Page<Contractor> contractorPage = service.getAllContractors(status, page, count);
        int pageNumber = contractorPage.getPageNumber();
        List<ContractorDto> list = contractorPage.getList()
                                       .stream()
                                       .map(contractor -> ContractorConverter.convertToDto(contractor, null))
                                       .collect(Collectors.toList());

        return new Page<>(list, pageNumber);
    }

    @Override
    public List<UserDto> getAllAdministrators() throws Exception {

        return service.getAllAdministrators()
                   .stream()
                   .map(UserConverter::convertToDto)
                   .collect(Collectors.toList());
    }

    @Override
    public Page<DeveloperDto> getAllDevelopers(UserStatus status, int page, int count) throws Exception {

        Page<Developer> developerPage = service.getAllDevelopers(status, page, count);
        int pageNumber = developerPage.getPageNumber();
        List<DeveloperDto> list = developerPage.getList()
                                      .stream()
                                      .map(developer -> DeveloperConverter.convertToDto(developer, null))
                                      .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public Page<ProjectDto> getProjectsByDeveloper(long developerId, ProjectStatus status, int page, int count) throws Exception {

        Page<Project> projectPage = service.getProjectsByDeveloper(developerId, status, page, count);
        int pageNumber = projectPage.getPageNumber();
        List<ProjectDto> list = projectPage.getList()
                                    .stream()
                                    .map(project -> ProjectConverter.convertToDto(project, null, null))
                                    .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public List<ChapterDto> getChaptersByProjectId(long projectId) throws Exception {

        return service.getChaptersByProjectId(projectId).stream()
                   .map(chapter -> ChapterConverter.getChapterDtoForContractor(chapter, null))
                   .collect(Collectors.toList());
    }

    @Override
    public Page<ChapterDto> getChaptersByContractorId(long contractorId, int page, int count) throws Exception {

        Page<Chapter> chapterPage = service.getChaptersByContractorId(contractorId, page, count);
        int pageNumber = chapterPage.getPageNumber();
        List<ChapterDto> list = chapterPage.getList()
                                    .stream()
                                    .map(chapter -> ChapterConverter.getChapterDtoForContractor(chapter, null))
                                    .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public List<MoneyTransferDto> getMoneyTransfers(long calculationId) throws Exception {

        return service.getMoneyTransfers(calculationId).stream()
                   .map(MoneyTransferConverter::convertToDto)
                   .collect(Collectors.toList());
    }

    @Override
    public Page<CalculationDto> getCalculationsByChapterId(long chapterId, int page, int count) throws Exception {

        Page<Calculation> calculationPage = service.getCalculationsByChapterId(chapterId, page, count);
        int pageNumber = calculationPage.getPageNumber();
        List<CalculationDto> list = calculationPage.getList().stream()
                                        .map(calculation -> {
                                            Integer[] sums = Util.getCalculationSums(calculation);
                                            return CalculationConverter.convertToDto(calculation, sums[0], sums[1], sums[2]);
                                        })
                                        .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public Page<ProposalDto> getProposalsByChapterId(long chapterId, ProposalStatus status, int page, int count) throws Exception {

        Page<Proposal> proposalPage = service.getProposalsByChapterId(chapterId, status, page, count);
        int pageNumber = proposalPage.getPageNumber();
        List<ProposalDto> list = proposalPage.getList().stream()
                                     .map(ProposalConverter::convertToDto)
                                     .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public Page<ProposalDto> getProposalsByContractorId(long contractorId, ProposalStatus status, int page, int count) throws Exception {

        Page<Proposal> proposalPage = service.getProposalsByContractorId(contractorId, status, page, count);
        int pageNumber = proposalPage.getPageNumber();
        List<ProposalDto> list = proposalPage.getList().stream()
                                     .map(ProposalConverter::convertToDto)
                                     .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public void changeUserStatus(long userId, UserStatus newStatus) throws Exception {

        service.changeUserStatus(userId, newStatus);
    }

    @Override
    public void deleteUser(long userId) throws IOException, NotUpdateDataInDbException {

        service.deleteUser(userId);
    }

    @Override
    public void deleteCalculation(long calculationId) throws IOException, NotUpdateDataInDbException {

        service.deleteCalculation(calculationId);
    }

    @Override
    public void deleteChapter(long chapterId) throws IOException, NotUpdateDataInDbException {

        service.deleteChapter(chapterId);
    }

    @Override
    public void deleteMoneyTransfer(long transferId) throws IOException, NotUpdateDataInDbException {

        service.deleteMoneyTransfer(transferId);
    }

    @Override
    public void deleteProject(long projectId) throws IOException, NotUpdateDataInDbException {

        service.deleteProject(projectId);
    }

    @Override
    public void deleteProposal(long proposalId) throws IOException, NotUpdateDataInDbException {

        service.deleteProposal(proposalId);
    }
}
