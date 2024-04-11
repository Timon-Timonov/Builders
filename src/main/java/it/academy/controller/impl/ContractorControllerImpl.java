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
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.service.ContractorService;
import it.academy.service.impl.ContractorServiceImpl;
import it.academy.util.Util;
import it.academy.util.converters.*;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
    public List<ProjectDto> getMyProjects(Long contractorId, ProjectStatus status, Integer page, int count) throws IOException {

        return contractorService.getMyProjects(contractorId, status, page, count)
                   .stream()
                   .map(project -> getProjectDtoForContractor(contractorId, project))
                   .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDto> getMyProjectsByDeveloper(Long developerId, Long contractorId, ProjectStatus status, int page, int count) throws IOException {

        return contractorService.getMyProjectsByDeveloper(developerId, contractorId, status, page, count)
                   .stream()
                   .map(project -> getProjectDtoForContractor(contractorId, project))
                   .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllChapterNames() throws IOException {

        return contractorService.getAllChapterNames();
    }

    @Override
    public List<ChapterDto> getFreeChapters(String chapterName, int page, int count) throws IOException {

        return contractorService.getFreeChapters(chapterName, page, count).stream()
                   .map(chapter -> ChapterConverter.convertToDto(chapter, null))
                   .collect(Collectors.toList());
    }

    @Override
    public List<DeveloperDto> getMyDevelopers(Long contractorId, ProjectStatus status, int page, int count) throws IOException {

        return contractorService.getMyDevelopers(contractorId, status, page, count)
                   .stream()
                   .map(developer -> {

                       Integer developerDebt = null;
                       try {
                           developerDebt = contractorService.getTotalDeptByDeveloper(contractorId, developer.getId());
                       } catch (IOException e) {
                           log.error("Getting of debt " + contractorId + " with developerId " + developer.getId() + " failed.", e);
                       }
                       return DeveloperConverter.convertToDto(developer, developerDebt);
                   })
                   .collect(Collectors.toList());
    }

    @Override
    public List<ProposalDto> getMyProposals(Long contractorId, ProposalStatus status, int page, int count) throws IOException {

        return contractorService.getMyProposals(contractorId, status, page, count)
                   .stream()
                   .map(ProposalConverter::convertToDto)
                   .collect(Collectors.toList());
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
    public List<CalculationDto> getCalculationsByChapter(Long chapterId, int page, int count) throws IOException {

        return contractorService.getCalculationsByChapter(chapterId, page, count).stream()
                   .map(calculation -> {
                       Integer[] sums = Util.getDebtFromCalculation(calculation);
                       return CalculationConverter.convertToDto(calculation, sums[0], sums[1], sums[2]);
                   })
                   .collect(Collectors.toList());
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
    public void startWork(Long proposalId) throws IOException, NotUpdateDataInDbException {

        contractorService.startWork(proposalId);
    }

    @Override
    public void cancelProposal(Long proposalId) throws IOException, NotUpdateDataInDbException {

        contractorService.cancelProposal(proposalId);
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
