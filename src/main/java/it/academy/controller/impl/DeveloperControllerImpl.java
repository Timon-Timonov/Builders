package it.academy.controller.impl;

import it.academy.controller.DeveloperController;
import it.academy.dto.*;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.exceptions.RoleException;
import it.academy.pojo.Chapter;
import it.academy.pojo.Project;
import it.academy.pojo.enums.ChapterStatus;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.service.DeveloperService;
import it.academy.service.impl.DeveloperServiceImpl;
import it.academy.util.Util;
import it.academy.util.converters.*;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class DeveloperControllerImpl implements DeveloperController {

    private final DeveloperService developerService = new DeveloperServiceImpl();

    @Override
    public DeveloperDto createDeveloper(String email, String password, String name, String city, String street, String building) throws IOException, NotCreateDataInDbException, EmailOccupaidException {

        return DeveloperConverter.convertToDto(developerService.createDeveloper(email, password, name, city, street, building), null);
    }

    @Override
    public DeveloperDto getDeveloper(Long userId) throws IOException, RoleException {

        return DeveloperConverter.convertToDto(developerService.getDeveloper(userId), null);
    }

    @Override
    public List<ProjectDto> getMyProjects(Long developerId, ProjectStatus status, int page, int count) throws IOException {

        return developerService.getMyProjects(developerId, status, page, count).stream()
                   .map(project -> {
                       Integer projectPrice = project.getChapters().stream()
                                                  .map(Chapter::getPrice)
                                                  .reduce(0, Integer::sum);
                       Integer projectDebt = developerService.getProjectDept(project);
                       return ProjectConverter.convertToDto(project, projectPrice, projectDebt);
                   })
                   .collect(Collectors.toList());
    }

    @Override
    public List<ContractorDto> getMyContractors(Long developerId, ProjectStatus status, int page, int count) throws IOException {

        return developerService.getMyContractors(developerId, status, page, count).stream()
                   .map(contractor -> {
                       Integer contractorDebt = null;
                       try {
                           contractorDebt = developerService.getTotalDeptByContractor(contractor.getId(), developerId);
                       } catch (IOException e) {
                           log.error("Geting of contractorDebt by contractorId " + contractor.getId() + " and developerId " + developerId + " failed.", e);
                       }
                       return ContractorConverter.convertToDto(contractor, contractorDebt);
                   })
                   .collect(Collectors.toList());
    }

    @Override
    public List<ProposalDto> getAllMyProposals(Long developerId, ProposalStatus status, int page, int count) throws IOException {

        return developerService.getAllMyProposals(developerId, status, page, count).stream()
                   .map(ProposalConverter::convertToDto)
                   .collect(Collectors.toList());
    }

    @Override
    public void createProject(Long developerId, String name, String city, String street, String building) throws IOException, NotCreateDataInDbException {

        developerService.createProject(developerId, name, city, street, building);
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
    public List<ChapterDto> getChaptersByContractorId(Long contractorId, ChapterStatus status, int page, int count) throws IOException {

        return developerService.getChaptersByContractorId(contractorId, status, page, count).stream()
                   .map(this::getChapterDtoForDeveloper)
                   .collect(Collectors.toList());
    }

    @Override
    public void rejectProposal(Long proposalId) throws IOException, NotUpdateDataInDbException {

        developerService.rejectProposal(proposalId);
    }

    @Override
    public void approveProposal(Long proposalId) throws IOException, NotUpdateDataInDbException {

        developerService.approveProposal(proposalId);
    }

    @Override
    public List<ProposalDto> getProposalsByChapterId(Long chapterId, ProposalStatus status, int page, int count) throws IOException {

        return developerService.getProposalsByChapterId(chapterId, status, page, count).stream()
                   .map(ProposalConverter::convertToDto)
                   .collect(Collectors.toList());
    }

    @Override
    public void startProject(Long projectId) throws IOException, NotUpdateDataInDbException {

        developerService.startProject(projectId);
    }

    @Override
    public void endProject(Long projectId) throws IOException, NotUpdateDataInDbException {

        developerService.endProject(projectId);
    }

    @Override
    public void cancelProject(Long projectId) throws IOException, NotUpdateDataInDbException {

        developerService.cancelProject(projectId);
    }

    @Override
    public List<CalculationDto> getCalculationsByChapterId(Long chapterId, int page, int count) throws IOException {

        return developerService.getCalculationsByChapterId(chapterId, page, count).stream()
                   .map(calculation -> {
                       Integer[] sums = Util.getDebtFromCalculation(calculation);
                       return CalculationConverter.convertToDto(calculation, sums[0], sums[1], sums[2]);
                   })
                   .collect(Collectors.toList());
    }

    @Override
    public void payAdvance(Integer sum, Long calculationId) throws IOException, NotCreateDataInDbException {

        developerService.payAdvance(sum, calculationId);
    }

    @Override
    public void payForWork(Integer sum, Long calculationId) throws IOException, NotCreateDataInDbException {

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
