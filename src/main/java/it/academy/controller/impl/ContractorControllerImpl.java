package it.academy.controller.impl;

import it.academy.controller.ContractorController;
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
import it.academy.pojo.legalEntities.Developer;
import it.academy.service.ContractorService;
import it.academy.service.impl.ContractorServiceImpl;
import it.academy.util.Util;
import it.academy.util.converters.*;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static it.academy.util.constants.Messages.*;

@Log4j2
public class ContractorControllerImpl implements ContractorController {


    private final ContractorService contractorService = new ContractorServiceImpl();

    @Override
    public ContractorDto createContractor(String email, String password, String name, String city, String street, String building) throws IOException, NotCreateDataInDbException, EmailOccupaidException {

        return ContractorConverter.convertToDto(contractorService.createContractor(email, password, name, city, street, building), null);
    }

    @Override
    public ContractorDto getContractor(Long userId) throws IOException, RoleException {

        return ContractorConverter.convertToDto(contractorService.getContractor(userId), null);
    }

    @Override
    public Page<ProjectDto> getMyProjects(Long contractorId, ProjectStatus status, int page, int count) throws IOException {

        Page<Project> projectPage = contractorService.getMyProjects(contractorId, status, page, count);
        int pageNumber = projectPage.getPageNumber();
        List<ProjectDto> list = projectPage.getList()
                                    .stream()
                                    .map(project -> getProjectDtoForContractor(contractorId, project))
                                    .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public Page<ProjectDto> getMyProjectsByDeveloper(Long developerId, Long contractorId, ProjectStatus status, int page, int count) throws IOException {

        Page<Project> projectPage = contractorService.getMyProjectsByDeveloper(developerId, contractorId, status, page, count);
        int pageNumber = projectPage.getPageNumber();
        List<ProjectDto> list = projectPage.getList().stream()
                                    .map(project -> getProjectDtoForContractor(contractorId, project))
                                    .collect(Collectors.toList());

        return new Page<>(list, pageNumber);
    }

    @Override
    public List<String> getAllChapterNames() throws IOException {

        return contractorService.getAllChapterNames();
    }

    @Override
    public Page<ChapterDto> getFreeChapters(Long contractorId, String chapterName, ProjectStatus projectStatus, int page, int count) throws IOException {

        Page<Chapter> chapterPage = contractorService.getFreeChapters(contractorId, chapterName, projectStatus, page, count);
        int pageNumber = chapterPage.getPageNumber();
        List<ChapterDto> list = chapterPage.getList()
                                    .stream()
                                    .map(chapter -> ChapterConverter.convertToDto(chapter, null))
                                    .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public Page<DeveloperDto> getMyDevelopers(Long contractorId, ProjectStatus status, int page, int count) throws IOException {

        Page<Developer> developerPage = contractorService.getMyDevelopers(contractorId, status, page, count);
        int pageNumber = developerPage.getPageNumber();
        List<DeveloperDto> list = developerPage.getList()
                                      .stream()
                                      .map(developer -> {
                                          Integer developerDebt = null;
                                          try {
                                              developerDebt = contractorService.getTotalDeptByDeveloper(contractorId, developer.getId());
                                          } catch (IOException e) {
                                              log.error(GETTING_OF_DEBT + contractorId + WITH_DEVELOPER_ID + developer.getId() + FAILED, e);
                                          }
                                          return DeveloperConverter.convertToDto(developer, developerDebt);
                                      })
                                      .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public Page<ProposalDto> getMyProposals(Long contractorId, ProposalStatus status, int page, int count) throws IOException {

        Page<Proposal> proposalPage = contractorService.getMyProposals(contractorId, status, page, count);
        int pageNumber = proposalPage.getPageNumber();
        List<ProposalDto> list = proposalPage.getList()
                                     .stream()
                                     .map(ProposalConverter::convertToDto)
                                     .collect(Collectors.toList());
        return new Page<>(list, pageNumber);
    }

    @Override
    public List<ChapterDto> getMyChaptersByProjectId(Long ProjectId, Long ContractorId) throws IOException {


        return contractorService.getMyChaptersByProjectId(ProjectId, ContractorId).stream()
                   .map(chapter -> {
                       Integer chapterDebt = Util.getDebtByChapter(chapter);
                       return ChapterConverter.convertToDto(chapter, chapterDebt);
                   })
                   .collect(Collectors.toList());
    }

    @Override
    public Page<CalculationDto> getCalculationsByChapter(Long chapterId, int page, int count) throws IOException {

        Page<Calculation> calculationPage = contractorService.getCalculationsByChapter(chapterId, page, count);
        List<CalculationDto> list = calculationPage.getList().stream()
                                        .map(calculation -> {
                                            Integer[] sums = Util.getDebtFromCalculation(calculation);
                                            return CalculationConverter.convertToDto(calculation, sums[0], sums[1], sums[2]);
                                        })
                                        .collect(Collectors.toList());
        return new Page<>(list, calculationPage.getPageNumber());
    }

    @Override
    public void updateWorkPriceFact(Integer workPrice, Long calculationId) throws IOException, NotUpdateDataInDbException {

        contractorService.updateWorkPriceFact(workPrice, calculationId);
    }

    @Override
    public CalculationDto createCalculation(Long chapterId, Integer YYYY, Integer MM, Integer workPricePlan) throws IOException, NotCreateDataInDbException {

        Calculation calculation = contractorService.createCalculation(chapterId, YYYY, MM, workPricePlan);
        return CalculationConverter.convertToDto(calculation, null, null, null);
    }

    @Override
    public void setProposalStatus(Long proposalId, ProposalStatus newStatus) throws IOException, NotUpdateDataInDbException {

        contractorService.setProposalStatus(proposalId, newStatus);
    }

    @Override
    public ProposalDto createProposal(Long chapterId, Long contractorId) throws IOException, NotCreateDataInDbException {

        return ProposalConverter.convertToDto(contractorService.createProposal(chapterId, contractorId));
    }

    private ProjectDto getProjectDtoForContractor(Long contractorId, Project project) {

        AtomicReference<Integer> projectDebt = new AtomicReference<>(0);
        Integer projectPrice = project.getChapters().stream()
                                   .filter(chapter -> chapter.getContractor() != null)
                                   .filter(chapter -> chapter.getContractor().getId().equals(contractorId))
                                   .peek(chapter ->
                                             projectDebt.updateAndGet(v -> v + Util.getDebtByChapter(chapter))
                                   )
                                   .map(Chapter::getPrice)
                                   .reduce(0, Integer::sum);
        return ProjectConverter.convertToDto(project, projectPrice, projectDebt.get());
    }
}
