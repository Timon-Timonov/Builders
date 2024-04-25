package it.academy.controller.impl;

import it.academy.controller.DeveloperController;
import it.academy.dto.*;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.exceptions.RoleException;
import it.academy.pojo.Calculation;
import it.academy.pojo.Chapter;
import it.academy.pojo.Project;
import it.academy.pojo.Proposal;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.service.DeveloperService;
import it.academy.service.impl.DeveloperServiceImpl;
import it.academy.util.Util;
import it.academy.util.converters.*;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static it.academy.util.constants.Messages.*;

@Log4j2
public class DeveloperControllerImpl implements DeveloperController {

    private final DeveloperService developerService = new DeveloperServiceImpl();

    @Override
    public DeveloperDto createDeveloper(
        String email, String password, String name, String city, String street, String building)
        throws IOException, NotCreateDataInDbException, EmailOccupaidException {

        return DeveloperConverter.convertToDto(
            developerService.createDeveloper(email, password, name, city, street, building), null);
    }

    @Override
    public DeveloperDto getDeveloper(Long userId) throws IOException, RoleException {

        return DeveloperConverter.convertToDto(developerService.getDeveloper(userId), null);
    }

    @Override
    public Page<ProjectDto> getMyProjects(
        Long developerId, ProjectStatus status, int page, int count) throws IOException {

        Page<Project> projectPage = developerService.getMyProjects(developerId, status, page, count);
        int pageNumber = projectPage.getPageNumber();
        List<ProjectDto> list = projectPage.getList().stream()
                                    .map(project -> {
                                        Integer projectPrice = project.getChapters().stream()
                                                                   .map(Chapter::getPrice)
                                                                   .reduce(0, Integer::sum);
                                        Integer projectDebt = developerService.getProjectDept(project);
                                        return ProjectConverter.convertToDto(project, projectPrice, projectDebt);
                                    })
                                    .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public Page<ContractorDto> getMyContractors(
        Long developerId, ProjectStatus status, int page, int count) throws IOException {

        Page<Contractor> contractorPage = developerService.getMyContractors(developerId, status, page, count);
        int pageNumber = contractorPage.getPageNumber();
        List<ContractorDto> list = contractorPage.getList().stream()
                                       .map(contractor -> {
                                           Integer contractorDebt = null;
                                           try {
                                               contractorDebt = developerService.getTotalDeptByContractor(contractor.getId(), developerId);
                                           } catch (IOException e) {
                                               log.error(GETING_OF_CONTRACTOR_DEBT_BY_CONTRACTOR_ID + contractor.getId() + AND_DEVELOPER_ID + developerId + FAILED, e);
                                           }
                                           return ContractorConverter.convertToDto(contractor, contractorDebt);
                                       })
                                       .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public Page<ProposalDto> getAllMyProposals(Long developerId, ProposalStatus status, int page, int count) throws IOException {

        Page<Proposal> proposalPage = developerService.getAllMyProposals(developerId, status, page, count);
        int pageNumber = proposalPage.getPageNumber();
        List<ProposalDto> list = proposalPage.getList().stream()
                                     .map(ProposalConverter::convertToDto)
                                     .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public ProjectDto createProject(
        Long developerId, String name, String city, String street, String building)
        throws IOException, NotCreateDataInDbException {

        return ProjectConverter.convertToDto(developerService.createProject(developerId, name, city, street, building), null, null);
    }

    @Override
    public void createChapter(Long projectId, String name, Integer price) throws IOException, NotCreateDataInDbException {

        developerService.createChapter(projectId, name, price);
    }

    @Override
    public void cancelChapter(Long chapterId) throws IOException, NotUpdateDataInDbException {

        developerService.cancelChapter(chapterId);
    }

    @Override
    public List<ChapterDto> getChaptersByProjectId(Long projectId) throws IOException {

        return developerService.getChaptersByProjectId(projectId).stream()
                   .map(this::getChapterDtoForDeveloper)
                   .collect(Collectors.toList());
    }

    @Override
    public Page<ChapterDto> getChaptersByContractorIdAndDeveloperId(
        Long developerId, Long contractorId, ProjectStatus status, int page, int count) throws IOException {

        Page<Chapter> chapterPage = developerService.getChaptersByContractorIdAndDeveloperId(developerId, contractorId, status, page, count);
        int pageNumber = chapterPage.getPageNumber();
        List<ChapterDto> list = chapterPage.getList().stream()
                                    .map(this::getChapterDtoForDeveloper)
                                    .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public void rejectProposal(Long proposalId) throws IOException, NotUpdateDataInDbException {

        developerService.rejectProposal(proposalId);
    }

    @Override
    public void considerateProposal(Long proposalId) throws IOException, NotUpdateDataInDbException {

        developerService.considerateProposal(proposalId);
    }

    @Override
    public void approveProposal(Long proposalId) throws IOException, NotUpdateDataInDbException {

        developerService.approveProposal(proposalId);
    }

    @Override
    public Page<ProposalDto> getProposalsByChapterId(
        Long chapterId, ProposalStatus status, int page, int count) throws Exception {

        Page<Proposal> proposalPage = developerService.getProposalsByChapterId(chapterId, status, page, count);
        int pageNumber = proposalPage.getPageNumber();
        List<ProposalDto> list = proposalPage.getList().stream()
                                     .map(ProposalConverter::convertToDto)
                                     .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public void startProject(Long projectId) throws Exception {

        developerService.startProject(projectId);
    }

    @Override
    public void endProject(Long projectId) throws Exception {

        developerService.endProject(projectId);
    }

    @Override
    public void cancelProject(Long projectId) throws Exception {

        developerService.cancelProject(projectId);
    }

    @Override
    public Page<CalculationDto> getCalculationsByChapterId(Long chapterId, int page, int count) throws Exception {

        Page<Calculation> calculationPage = developerService.getCalculationsByChapterId(chapterId, page, count);
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
    public void payAdvance(int sum, Long calculationId) throws Exception {

        developerService.payAdvance(sum, calculationId);
    }

    @Override
    public void payForWork(int sum, Long calculationId) throws Exception {

        developerService.payForWork(sum, calculationId);
    }

    @Override
    public Integer getProjectDept(Project project) {

        return developerService.getProjectDept(project);
    }

    @Override
    public Integer getTotalDeptByContractor(Long contractorId, Long developerId) throws IOException {

        return developerService.getTotalDeptByContractor(contractorId, developerId);
    }

    private ChapterDto getChapterDtoForDeveloper(Chapter chapter) {

        Integer chapterDebt = Util.getDebtByChapter(chapter);
        return ChapterConverter.convertToDto(chapter, chapterDebt);
    }
}
