package it.academy.controller.impl;

import it.academy.controller.AdminController;
import it.academy.dto.*;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.Project;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;
import it.academy.service.AdminService;
import it.academy.service.impl.AdminServiceImpl;
import it.academy.util.converters.ContractorConverter;
import it.academy.util.converters.DeveloperConverter;
import it.academy.util.converters.ProjectConverter;
import it.academy.util.converters.UserConverter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AdminControllerImpl implements AdminController {

    private final AdminService adminService = new AdminServiceImpl();

    @Override
    public UserDto createAdmin(String email, String password) throws IOException, EmailOccupaidException, NotCreateDataInDbException {

        return UserConverter.convertToDto(adminService.createAdmin(email, password));
    }

    @Override
    public UserDto getUser(String email) throws IOException {

        return UserConverter.convertToDto(adminService.getUser(email));
    }

    @Override
    public Page<ContractorDto> getAllContractors(UserStatus status, int page, int count) throws IOException {

        Page<Contractor> contractorPage = adminService.getAllContractors(status, page, count);
        int pageNumber = contractorPage.getPageNumber();
        List<ContractorDto> list = contractorPage.getList()
                                       .stream()
                                       .map(contractor -> ContractorConverter.convertToDto(contractor, null))
                                       .collect(Collectors.toList());

        return new Page<>(list, pageNumber);
    }

    @Override
    public Page<DeveloperDto> getAllDevelopers(UserStatus status, int page, int count) throws IOException {

        Page<Developer> developerPage = adminService.getAllDevelopers(status, page, count);
        int pageNumber = developerPage.getPageNumber();
        List<DeveloperDto> list = developerPage.getList()
                                      .stream()
                                      .map(developer -> DeveloperConverter.convertToDto(developer, null))
                                      .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public Page<ProjectDto> getAllProjects(ProjectStatus status, int page, int count) throws IOException {

        Page<Project> projectPage = adminService.getAllProjects(status, page, count);
        int pageNumber = projectPage.getPageNumber();
        List<ProjectDto> list = projectPage.getList()
                                    .stream()
                                    .map(project -> ProjectConverter.convertToDto(project, null, null))
                                    .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public void cancelUser(Long userId) throws IOException, NotUpdateDataInDbException {

        adminService.cancelUser(userId);
    }

    @Override
    public void deleteUser(Long userId) throws IOException, NotUpdateDataInDbException {

        adminService.deleteUser(userId);
    }

    @Override
    public void deleteCalculation(Long calculationId) throws IOException, NotUpdateDataInDbException {

        adminService.deleteCalculation(calculationId);
    }

    @Override
    public void deleteChapter(Long chapterId) throws IOException, NotUpdateDataInDbException {

        adminService.deleteChapter(chapterId);
    }

    @Override
    public void deleteMoneyTransfer(Long transferId) throws IOException, NotUpdateDataInDbException {

        adminService.deleteMoneyTransfer(transferId);
    }

    @Override
    public void deleteProject(Long projectId) throws IOException, NotUpdateDataInDbException {

        adminService.deleteProject(projectId);
    }

    @Override
    public void deleteProposal(Long proposalId) throws IOException, NotUpdateDataInDbException {

        adminService.deleteProposal(proposalId);
    }
}
