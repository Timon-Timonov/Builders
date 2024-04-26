package it.academy.controller.impl;

import it.academy.controller.AdminController;
import it.academy.dto.*;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.Calculation;
import it.academy.pojo.Chapter;
import it.academy.pojo.Project;
import it.academy.pojo.Proposal;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
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

public class AdminControllerImpl implements AdminController {

    private final AdminService adminService = new AdminServiceImpl();

    @Override
    public void createAdmin(String email, String password) throws Exception {

        UserConverter.convertToDto(adminService.createAdmin(email, password));
    }

    @Override
    public UserDto getUser(String email) throws Exception {

        return UserConverter.convertToDto(adminService.getUser(email));
    }

    @Override
    public Page<ContractorDto> getAllContractors(UserStatus status, int page, int count) throws Exception {

        Page<Contractor> contractorPage = adminService.getAllContractors(status, page, count);
        int pageNumber = contractorPage.getPageNumber();
        List<ContractorDto> list = contractorPage.getList()
                                       .stream()
                                       .map(contractor -> ContractorConverter.convertToDto(contractor, null))
                                       .collect(Collectors.toList());

        return new Page<>(list, pageNumber);
    }

    @Override
    public List<UserDto> getAllAdministrators() throws Exception {

        return adminService.getAllAdministrators()
                   .stream()
                   .map(UserConverter::convertToDto)
                   .collect(Collectors.toList());
    }

    @Override
    public Page<DeveloperDto> getAllDevelopers(UserStatus status, int page, int count) throws Exception {

        Page<Developer> developerPage = adminService.getAllDevelopers(status, page, count);
        int pageNumber = developerPage.getPageNumber();
        List<DeveloperDto> list = developerPage.getList()
                                      .stream()
                                      .map(developer -> DeveloperConverter.convertToDto(developer, null))
                                      .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public Page<ProjectDto> getProjectsByDeveloper(long developerId, ProjectStatus status, int page, int count) throws Exception {

        Page<Project> projectPage = adminService.getProjectsByDeveloper(developerId, status, page, count);
        int pageNumber = projectPage.getPageNumber();
        List<ProjectDto> list = projectPage.getList()
                                    .stream()
                                    .map(project -> ProjectConverter.convertToDto(project, null, null))
                                    .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public List<ChapterDto> getChaptersByProjectId(long projectId) throws Exception {

        return adminService.getChaptersByProjectId(projectId).stream()
                   .map(chapter -> ChapterConverter.convertToDto(chapter, null))
                   .collect(Collectors.toList());
    }

    @Override
    public Page<ChapterDto> getChaptersByContractorId(long contractorId, int page, int count) throws Exception {

        Page<Chapter> chapterPage = adminService.getChaptersByContractorId(contractorId, page, count);
        int pageNumber = chapterPage.getPageNumber();
        List<ChapterDto> list = chapterPage.getList()
                                    .stream()
                                    .map(chapter -> ChapterConverter.convertToDto(chapter, null))
                                    .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public List<MoneyTransferDto> getMoneyTransfers(long calculationId) throws Exception {

        return adminService.getMoneyTransfers(calculationId).stream()
                   .map(MoneyTransferConverter::convertToDto)
                   .collect(Collectors.toList());
    }

    @Override
    public Page<CalculationDto> getCalculationsByChapterId(long chapterId, int page, int count) throws Exception {

        Page<Calculation> calculationPage = adminService.getCalculationsByChapterId(chapterId, page, count);
        int pageNumber = calculationPage.getPageNumber();
        List<CalculationDto> list = calculationPage.getList().stream()
                                        .map(calculation -> {
                                            Integer[] sums = Util.getDebtFromCalculation(calculation);
                                            return CalculationConverter.convertToDto(calculation, sums[0], sums[1], sums[2]);
                                        })
                                        .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public Page<ProposalDto> getProposalsByChapterId(long chapterId, ProposalStatus status, int page, int count) throws Exception {

        Page<Proposal> proposalPage = adminService.getProposalsByChapterId(chapterId, status, page, count);
        int pageNumber = proposalPage.getPageNumber();
        List<ProposalDto> list = proposalPage.getList().stream()
                                     .map(ProposalConverter::convertToDto)
                                     .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public Page<ProposalDto> getProposalsByContractorId(long contractorId, ProposalStatus status, int page, int count) throws Exception {

        Page<Proposal> proposalPage = adminService.getProposalsByContractorId(contractorId, status, page, count);
        int pageNumber = proposalPage.getPageNumber();
        List<ProposalDto> list = proposalPage.getList().stream()
                                     .map(ProposalConverter::convertToDto)
                                     .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public void changeUserStatus(long userId, UserStatus newStatus) throws Exception {

        adminService.changeUserStatus(userId, newStatus);
    }

    @Override
    public void deleteUser(long userId) throws IOException, NotUpdateDataInDbException {

        adminService.deleteUser(userId);
    }

    @Override
    public void deleteCalculation(long calculationId) throws IOException, NotUpdateDataInDbException {

        adminService.deleteCalculation(calculationId);
    }

    @Override
    public void deleteChapter(long chapterId) throws IOException, NotUpdateDataInDbException {

        adminService.deleteChapter(chapterId);
    }

    @Override
    public void deleteMoneyTransfer(long transferId) throws IOException, NotUpdateDataInDbException {

        adminService.deleteMoneyTransfer(transferId);
    }

    @Override
    public void deleteProject(long projectId) throws IOException, NotUpdateDataInDbException {

        adminService.deleteProject(projectId);
    }

    @Override
    public void deleteProposal(long proposalId) throws IOException, NotUpdateDataInDbException {

        adminService.deleteProposal(proposalId);
    }
}
