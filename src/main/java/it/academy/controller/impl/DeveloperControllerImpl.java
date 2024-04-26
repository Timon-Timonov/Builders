package it.academy.controller.impl;

import it.academy.controller.DeveloperController;
import it.academy.dto.*;
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
        throws Exception {

        return DeveloperConverter.convertToDto(
            developerService.createDeveloper(email, password, name, city, street, building), null);
    }

    @Override
    public Page<ProjectDto> getMyProjects(
        long developerId, ProjectStatus status, int page, int count) throws Exception {

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
        long developerId, ProjectStatus status, int page, int count) throws Exception {

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
    public Page<ProposalDto> getAllMyProposals(long developerId, ProposalStatus status, int page, int count) throws Exception {

        Page<Proposal> proposalPage = developerService.getAllMyProposals(developerId, status, page, count);
        int pageNumber = proposalPage.getPageNumber();
        List<ProposalDto> list = proposalPage.getList().stream()
                                     .map(ProposalConverter::convertToDto)
                                     .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public ProjectDto createProject(
        long developerId, String name, String city, String street, String building)
        throws Exception {

        return ProjectConverter.convertToDto(developerService.createProject(developerId, name, city, street, building), null, null);
    }

    @Override
    public void createChapter(long projectId, String name, Integer price) throws Exception {

        developerService.createChapter(projectId, name, price);
    }

    @Override
    public void cancelChapter(long chapterId) throws Exception {

        developerService.cancelChapter(chapterId);
    }

    @Override
    public List<ChapterDto> getChaptersByProjectId(long projectId) throws Exception {

        return developerService.getChaptersByProjectId(projectId).stream()
                   .map(this::getChapterDtoForDeveloper)
                   .collect(Collectors.toList());
    }

    @Override
    public Page<ChapterDto> getChaptersByContractorIdAndDeveloperId(
        long developerId, long contractorId, ProjectStatus status, int page, int count) throws Exception {

        Page<Chapter> chapterPage = developerService.getChaptersByContractorIdAndDeveloperId(developerId, contractorId, status, page, count);
        int pageNumber = chapterPage.getPageNumber();
        List<ChapterDto> list = chapterPage.getList().stream()
                                    .map(this::getChapterDtoForDeveloper)
                                    .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public void changeStatusOfProposal(long proposalId, ProposalStatus newStaatus) throws Exception {

        developerService.changeStatusOfProposal(proposalId, newStaatus);
    }

    @Override
    public Page<ProposalDto> getProposalsByChapterId(
        long chapterId, ProposalStatus status, int page, int count) throws Exception {

        Page<Proposal> proposalPage = developerService.getProposalsByChapterId(chapterId, status, page, count);
        int pageNumber = proposalPage.getPageNumber();
        List<ProposalDto> list = proposalPage.getList().stream()
                                     .map(ProposalConverter::convertToDto)
                                     .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public void changeProjectStatus(long projectId, ProjectStatus newStatus) throws Exception {

        developerService.changeProjectStatus(projectId, newStatus);
    }

    @Override
    public Page<CalculationDto> getCalculationsByChapterId(long chapterId, int page, int count) throws Exception {

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
    public void payAdvance(int sum, long calculationId) throws Exception {

        developerService.payAdvance(sum, calculationId);
    }

    @Override
    public void payForWork(int sum, long calculationId) throws Exception {

        developerService.payForWork(sum, calculationId);
    }

    @Override
    public int getProjectDept(Project project) {

        return developerService.getProjectDept(project);
    }

    @Override
    public int getTotalDeptByContractor(long contractorId, long developerId) throws IOException {

        return developerService.getTotalDeptByContractor(contractorId, developerId);
    }

    private ChapterDto getChapterDtoForDeveloper(Chapter chapter) {

        int chapterDebt = Util.getDebtByChapter(chapter);
        return ChapterConverter.convertToDto(chapter, chapterDebt);
    }
}
