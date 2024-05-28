package it.academy;

import it.academy.dao.CalculationDao;
import it.academy.dao.ChapterDao;
import it.academy.dao.ProjectDao;
import it.academy.dao.impl.CalculationDaoImpl;
import it.academy.dao.impl.ChapterDaoImpl;
import it.academy.dao.impl.ProjectDaoImpl;
import it.academy.dto.*;
import it.academy.pojo.Calculation;
import it.academy.pojo.Chapter;
import it.academy.pojo.Project;
import it.academy.pojo.enums.ChapterStatus;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.service.AdminService;
import it.academy.service.ContractorService;
import it.academy.service.DeveloperService;
import it.academy.service.impl.AdminServiceImpl;
import it.academy.service.impl.ContractorServiceImpl;
import it.academy.service.impl.DeveloperServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static it.academy.util.constants.Constants.*;

public class FullingBaseAnyValues {

    public static final String MY_EMAIL = "myEmail.";
    public static final String MAIL_RU = "@mail.ru";
    public static final String STRING1111 = "1111";
    public static final String STRING2222 = "2222";
    public static final String UNIQUE_USER_NAME = "UniqueUserName";
    public static final String WEST_AVENUE = "WestAvenue";
    public static final String UNIQUE_PROJECT_NAME = "UniqueProjectName";
    public static final int INT0 = 0;
    public static final int INT1 = 1;
    public static final int INT2 = 2;
    public static final int INT3 = 3;
    public static final int INT4 = 4;
    public static final int INT5 = 5;
    public static final int INT7 = 7;
    public static final int INT8 = 8;
    public static final int INT9 = 9;
    public static final int INT10 = 10;
    public static final int INT12 = 12;

    public static final int INT115 = 115;
    public static final int INT100 = 100;
    public static final int BOUND2 = 50_000;
    public static final int BOUND3 = 2558;
    public static final int BOUND4 = 2023;

    public static final int MAX_COUNT_OF_CALCULATIONS = 15;
    public static final int ONE_PART_OF_PRICE_FOR_ADVANCE = 10;
    public static final int COUNT_OF_PROPOSALS_TO_APPROVE = 90;
    public static final int COUNT_OF_PROPOSALS_TO_REJECT = 15;
    public static final int MAX_PROJECT_COUNT_PER_DEVELOPER = 20;
    public static int MAX_PROPOSAL_COUNT_PER_CONTRACTOR = 35;
    public static final int DEVELOPER_COUNT = 10;
    public static final int CONTRACTOR_COUNT = 10;
    public static final String[] CHAPTER_NAMES = {"GP", "KG", "NVK", "VK", "TS", "EL", "AR", "AOP", "TEL", "TL", "GG"};
    public static final String[] CITY_NAMES = {"New York", "Moscow", "Minsk", "Piter", "Vilnus", "Warshawa", "Berlin"};

    public static final Random RANDOM = new Random();
    private final AdminService ADMIN_SERVICE = AdminServiceImpl.getInstance();
    private final DeveloperService DEVELOPER_SERVICE = DeveloperServiceImpl.getInstance();
    private final ContractorService CONTRACTOR_SERVICE = ContractorServiceImpl.getInstance();

    private final ProjectDao projectDao = new ProjectDaoImpl();
    private final ChapterDao chapterDao = new ChapterDaoImpl();
    private final CalculationDao calculationDao = new CalculationDaoImpl();

    private int proposalCount = 1;
    private int userCount = 1;
    private int projectCount = 1;
    private int chapterCount = 1;


    public static void main(String[] args) {

        FullingBaseAnyValues fuller = new FullingBaseAnyValues();
        fuller.MakeBaseFull();
    }

    private void MakeBaseFull() {

        CreationOfBaseData();
        startInteraction();
    }

    private void startInteraction() {

        List<Project> allProjectList = projectDao.getAll();

        changeProjectStatus(allProjectList);
        List<ProposalDto> proposalList = getAllConsiderationsProposals();
        rejectAnyProposals(proposalList);
        List<ProposalDto> approvedList = approveAnyProposals(proposalList);
        List<Long> chaptersInWork = startBusiness(approvedList);
        List<Calculation> calculationList = createCalculations(chaptersInWork);
        payMoney(calculationList);
    }

    private List<ProposalDto> getAllConsiderationsProposals() {

        List<ProposalDto> proposalList = new ArrayList<>();
        List<ChapterDto> activeChapterList = new ArrayList<>();
        List<DeveloperDto> allActiveDevelopers = new ArrayList<>(ADMIN_SERVICE.getAllDevelopers(FilterPageDto.builder()
                                                                                                    .status(UserStatus.ACTIVE)
                                                                                                    .count(DEVELOPER_COUNT)
                                                                                                    .page(FIRST_PAGE_NUMBER)
                                                                                                    .search(BLANK_STRING)
                                                                                                    .build()).getList());
        allActiveDevelopers.forEach(developer -> {
            try {
                List<Project> allProjectList = new ArrayList<>();
                allProjectList.addAll(projectDao.getProjectsByDeveloperId(developer.getId(), ProjectStatus.PREPARATION, FIRST_PAGE_NUMBER, projectCount).keySet());
                allProjectList.addAll(projectDao.getProjectsByDeveloperId(developer.getId(), ProjectStatus.IN_PROCESS, FIRST_PAGE_NUMBER, projectCount).keySet());

                allProjectList.forEach(project -> {
                    try {
                        activeChapterList.addAll(
                            DEVELOPER_SERVICE.getChaptersByProject(FilterPageDto.builder()
                                                                       .id(project.getId())
                                                                       .build()).getList());
                    } catch (Exception ignored) {
                    }
                });
            } catch (Exception ignored) {
            }
        });
        activeChapterList.forEach(chapter -> {
            try {
                proposalList.addAll(
                    DEVELOPER_SERVICE.getProposalsByChapter(FilterPageDto.builder()
                                                                .id(chapter.getId())
                                                                .status(ProposalStatus.CONSIDERATION)
                                                                .page(FIRST_PAGE_NUMBER)
                                                                .count(proposalCount)
                                                                .build()).getList());
            } catch (Exception ignored) {
            }
        });
        return proposalList;
    }

    private void payMoney(List<Calculation> calculationList) {

        AtomicInteger calculationIndex = new AtomicInteger(1);
        calculationList.forEach(calculation -> {
            try {
                if (calculationIndex.get() % INT2 == INT0) {
                    int sum = (calculation.getWorkPricePlan() / ONE_PART_OF_PRICE_FOR_ADVANCE) + 1;

                    DEVELOPER_SERVICE.payMoney(CreateRequestDto.builder()
                                                   .id(calculation.getId())
                                                   .int1(sum)
                                                   .int2(ZERO_INT_VALUE)
                                                   .int3(Integer.MAX_VALUE)
                                                   .build());
                    DEVELOPER_SERVICE.payMoney(CreateRequestDto.builder()
                                                   .id(calculation.getId())
                                                   .int1(ZERO_INT_VALUE)
                                                   .int2(sum * (calculationIndex.get() % INT9) + 1)
                                                   .int3(Integer.MAX_VALUE)
                                                   .build());
                } else {
                    double k = (calculationIndex.get() % INT5 * INT2) * 1.0;
                    int sum = (int) (calculation.getWorkPricePlan() * k / INT10) + 1;
                    DEVELOPER_SERVICE.payMoney(CreateRequestDto.builder()
                                                   .id(calculation.getId())
                                                   .int1(ZERO_INT_VALUE)
                                                   .int2(sum)
                                                   .int3(Integer.MAX_VALUE)
                                                   .build());
                }
                calculationIndex.getAndIncrement();
            } catch (Exception ignored) {
            }
        });
    }

    private List<Calculation> createCalculations(List<Long> chaptersInWork) {

        List<Calculation> calculationList = new ArrayList<>();
        chaptersInWork.stream()
            .map(chapterDao::get)
            .filter(chapter -> ChapterStatus.OCCUPIED.equals(chapter.getStatus()))
            .filter(chapter -> ProjectStatus.IN_PROCESS.equals(chapter.getProject().getStatus()))
            .map(Chapter::getId)
            .forEach(chapterId -> {
                int thisCalculationCount = RANDOM.nextInt(MAX_COUNT_OF_CALCULATIONS);
                for (int i = 0; i < thisCalculationCount; i++) {
                    int mm = i % INT12 + INT1;
                    try {
                        int workPricePlan = (BOUND3 * (i + INT8)) / (i + INT2);

                        CONTRACTOR_SERVICE.createCalculation(CreateRequestDto.builder()
                                                                 .id(chapterId)
                                                                 .int1(BOUND4)
                                                                 .int2(mm)
                                                                 .int3(workPricePlan)
                                                                 .build());
                    } catch (Exception ignored) {
                    }
                }
                try {
                    List<Calculation> list = new ArrayList<>(calculationDao.getCalculationsByChapterId(chapterId, FIRST_PAGE_NUMBER, Integer.MAX_VALUE).keySet());
                    calculationList.addAll(list);
                    list.forEach(calculation -> {
                        int workPriceFact = calculation.getWorkPricePlan() * INT115 / INT100;
                        try {
                            CONTRACTOR_SERVICE.updateWorkPriceFact(ChangeRequestDto.builder()
                                                                       .id(calculation.getId())
                                                                       .count(workPriceFact)
                                                                       .build());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        return calculationList;
    }

    private List<Long> startBusiness(List<ProposalDto> approvedList) {

        return approvedList.stream()
                   .peek(proposal -> {
                       try {
                           CONTRACTOR_SERVICE.setProposalStatus(ChangeRequestDto.builder()
                                                                    .id(proposal.getId())
                                                                    .status(ProposalStatus.ACCEPTED_BY_CONTRACTOR)
                                                                    .build());
                       } catch (Exception ignored) {
                       }
                   })
                   .map(ProposalDto::getChapterId)
                   .collect(Collectors.toList());
    }

    private List<ProposalDto> approveAnyProposals(List<ProposalDto> proposalList) {

        List<ProposalDto> approvedList = new ArrayList<>();
        int stop = Math.min(COUNT_OF_PROPOSALS_TO_APPROVE, proposalList.size());
        for (int i = 0; i < stop; i++) {
            try {
                ProposalDto proposal = proposalList.remove(RANDOM.nextInt(proposalList.size()));
                DtoWithPageForUi<ProposalDto> proposalDtoDtoWithPageForUi =
                    DEVELOPER_SERVICE.changeStatusOfProposal(ChangeRequestDto.builder()
                                                                 .id(proposal.getId())
                                                                 .status(ProposalStatus.APPROVED)
                                                                 .build());
                if (proposalDtoDtoWithPageForUi.getExceptionMessage() == null) {
                    proposal.setStatus(ProposalStatus.APPROVED);
                    approvedList.add(proposal);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return approvedList;
    }

    private void rejectAnyProposals(List<ProposalDto> proposalList) {

        for (int i = 0; i < COUNT_OF_PROPOSALS_TO_REJECT; i++) {
            try {
                ProposalDto proposal = proposalList.remove(RANDOM.nextInt(proposalList.size()));
                DEVELOPER_SERVICE.changeStatusOfProposal(ChangeRequestDto.builder()
                                                             .id(proposal.getId())
                                                             .status(ProposalStatus.REJECTED)
                                                             .build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void changeProjectStatus(List<Project> allProjectList) {

        AtomicInteger projectIndex = new AtomicInteger(INT1);
        allProjectList.forEach(project -> {
            if (projectIndex.get() % INT2 != INT0) {
                try {
                    DEVELOPER_SERVICE.changeProjectStatus(ChangeRequestDto.builder()
                                                              .id(project.getId())
                                                              .status(ProjectStatus.IN_PROCESS)
                                                              .build());
                    project.setStatus(ProjectStatus.IN_PROCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (projectIndex.get() % INT10 == INT0) {
                    try {
                        DEVELOPER_SERVICE.changeProjectStatus(ChangeRequestDto.builder()
                                                                  .id(project.getId())
                                                                  .status(ProjectStatus.COMPLETED)
                                                                  .build());
                        project.setStatus(ProjectStatus.COMPLETED);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (projectIndex.get() % INT7 == INT0) {
                    try {
                        DEVELOPER_SERVICE.changeProjectStatus(ChangeRequestDto.builder()
                                                                  .id(project.getId())
                                                                  .status(ProjectStatus.CANCELED)
                                                                  .build());
                        project.setStatus(ProjectStatus.CANCELED);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            projectIndex.getAndIncrement();
        });
    }

    private void CreationOfBaseData() {

        ADMIN_SERVICE.createAdmin(CreateRequestDto.builder()
                                      .email(getUniqEmail())
                                      .password(getPassword())
                                      .build());
        for (int i = 0; i < DEVELOPER_COUNT; i++) {
            long devId = DEVELOPER_SERVICE.createDeveloper(CreateRequestDto.builder()
                                                               .email(getUniqEmail())
                                                               .password(getPassword())
                                                               .name(getUserName())
                                                               .city(CITY_NAMES[i % CITY_NAMES.length])
                                                               .street(getStreet())
                                                               .building(String.valueOf(userCount * INT4 / INT3))
                                                               .build()).getUserFromDb().getId();
            int thisProjectCount = RANDOM.nextInt(MAX_PROJECT_COUNT_PER_DEVELOPER);
            for (int j = 0; j < thisProjectCount; j++) {
                DEVELOPER_SERVICE.createProject(CreateRequestDto.builder()
                                                    .id(devId)
                                                    .name(getProjectName())
                                                    .city(CITY_NAMES[RANDOM.nextInt(CITY_NAMES.length)])
                                                    .street(getStreet())
                                                    .building(String.valueOf(projectCount * INT4 / INT3))
                                                    .build());
                int thisChapterCount = RANDOM.nextInt(CHAPTER_NAMES.length);
                for (int k = 0; k < thisChapterCount; k++) {
                    DEVELOPER_SERVICE.createChapter(CreateRequestDto.builder()
                                                        .id((long) projectCount)
                                                        .name(CHAPTER_NAMES[k])
                                                        .int1(RANDOM.nextInt(BOUND2))
                                                        .build());
                    if (thisChapterCount % INT5 == 0) {
                        DEVELOPER_SERVICE.cancelChapter((long) chapterCount);
                    }
                    chapterCount++;
                }
                projectCount++;
            }
        }
        for (int i = 0; i < CONTRACTOR_COUNT; i++) {

            long conId = CONTRACTOR_SERVICE.createContractor(CreateRequestDto.builder()
                                                                 .email(getUniqEmail())
                                                                 .password(getPassword())
                                                                 .name(getUserName())
                                                                 .city(CITY_NAMES[i % CITY_NAMES.length])
                                                                 .street(getStreet())
                                                                 .building(String.valueOf(userCount * INT4 / INT3))
                                                                 .build()).getUserFromDb().getId();
            int thisProposalCount = RANDOM.nextInt(MAX_PROPOSAL_COUNT_PER_CONTRACTOR);
            for (int j = 0; j < thisProposalCount; j++) {
                DtoWithPageForUi<ProposalDto> proposal = CONTRACTOR_SERVICE.createProposal(CreateRequestDto.builder()
                                                                                               .id((long) (RANDOM.nextInt(chapterCount) + INT1))
                                                                                               .secondId(conId)
                                                                                               .build());
                if (proposal.getExceptionMessage() != null) {
                    proposalCount++;
                }
            }
        }
    }

    private String getUniqEmail() {

        return MY_EMAIL + (userCount++) + MAIL_RU;
    }

    private String getPassword() {

        return userCount % INT2 == INT0 ? STRING1111 : STRING2222;
    }

    private String getUserName() {

        return UNIQUE_USER_NAME + (userCount - 1);
    }

    private String getStreet() {

        return WEST_AVENUE + userCount % 7;
    }

    private String getProjectName() {

        return (UNIQUE_PROJECT_NAME + projectCount);
    }
}
