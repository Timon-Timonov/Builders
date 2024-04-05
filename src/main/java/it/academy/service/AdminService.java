package it.academy.service;

import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.Project;
import it.academy.pojo.User;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;

import java.io.IOException;
import java.util.List;

public interface AdminService {

    User getUser(String email) throws IOException;

    List<Contractor> getAllContractors(UserStatus status, int page, int count) throws IOException;

    List<Developer> getAllDevelopers(UserStatus status, int page, int count) throws IOException;

    List<Project> getAllProjects(ProjectStatus status, int page, int count) throws IOException;





    void cancelUser(Long userId) throws IOException, NotUpdateDataInDbException;

    void deleteUser(Long userId) throws IOException, NotUpdateDataInDbException;

    void deleteCalculation(Long calculationId) throws IOException, NotUpdateDataInDbException;

    void deleteChapter(Long chapterId) throws IOException, NotUpdateDataInDbException;

    void deleteMoneyTransfer(Long transferId) throws IOException, NotUpdateDataInDbException;

    void deleteProject(Long projectId) throws IOException, NotUpdateDataInDbException;

    void deleteProposal(Long proposalId) throws IOException, NotUpdateDataInDbException;
}
