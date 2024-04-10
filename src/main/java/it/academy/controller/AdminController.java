package it.academy.controller;

import it.academy.dto.ContractorDto;
import it.academy.dto.DeveloperDto;
import it.academy.dto.ProjectDto;
import it.academy.dto.UserDto;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;

import java.io.IOException;
import java.util.List;

public interface AdminController {

    UserDto createAdmin(String email, String password) throws IOException, EmailOccupaidException, NotCreateDataInDbException;

    UserDto getUser(String email) throws IOException;

    List<ContractorDto> getAllContractors(UserStatus status, int page, int count) throws IOException;

    List<DeveloperDto> getAllDevelopers(UserStatus status, int page, int count) throws IOException;

    List<ProjectDto> getAllProjects(ProjectStatus status, int page, int count) throws IOException;


    void cancelUser(Long userId) throws IOException, NotUpdateDataInDbException;

    void deleteUser(Long userId) throws IOException, NotUpdateDataInDbException;

    void deleteCalculation(Long calculationId) throws IOException, NotUpdateDataInDbException;

    void deleteChapter(Long chapterId) throws IOException, NotUpdateDataInDbException;

    void deleteMoneyTransfer(Long transferId) throws IOException, NotUpdateDataInDbException;

    void deleteProject(Long projectId) throws IOException, NotUpdateDataInDbException;

    void deleteProposal(Long proposalId) throws IOException, NotUpdateDataInDbException;
}
