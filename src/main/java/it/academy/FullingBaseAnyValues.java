package it.academy;

import it.academy.dao.CalculationDao;
import it.academy.dao.impl.CalculationDaoImpl;
import it.academy.exceptions.NotCreateDataInDbException;
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
import it.academy.service.ContractorService;
import it.academy.service.DeveloperService;
import it.academy.service.impl.AdminServiceImpl;
import it.academy.service.impl.ContractorServiceImpl;
import it.academy.service.impl.DeveloperServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static it.academy.util.constants.Constants.FIRST_PAGE_NUMBER;

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
    public static final int INT11 = 11;
    public static final int INT12 = 12;

    public static final int INT115 = 115;
    public static final int INT100 = 100;
    public static final int BOUND2 = 50_000;

    public static final int MAX_COUNT_OF_CALCULATIONS = 15;
    public static final int ONE_PART_OF_PRICE_FOR_ADVANCE = 10;
    public static final int COUNT_OF_PROPOSALS_TO_APPROVE = 45;
    public static final int COUNT_OF_PROPOSALS_TO_REJECT = 5;
    public static final int BOUND3 = 2558;
    public static final int BOUND4 = 2023;
    public static int MAX_PROPOSAL_COUNT_PER_CONTRACTOR = 15;
    public static final int DEVELOPER_COUNT = 10;
    public static final int CONTRACTOR_COUNT = 10;
    public static final String[] CHAPTER_NAMES = {"GP", "KG", "NVK", "VK", "TS", "EL", "AR", "AOP", "TEL", "TL"};
    public static final String[] CITY_NAMES = {"New York", "Moscow", "Minsk", "Piter", "Vilnus", "Warshawa", "Berlin"};

    public static final Random RANDOM = new Random();
    private final AdminService ADMIN_SERVICE = new AdminServiceImpl();
    private final DeveloperService DEVELOPER_SERVICE = new DeveloperServiceImpl();
    private final ContractorService CONTRACTOR_SERVICE = new ContractorServiceImpl();

    private int proposalCount = 1;
    private int userCount = 1;
    private int projectCount = 1;
    private int chapterCount = 1;


    public static void main(String[] args) throws Exception {

        FullingBaseAnyValues fuller = new FullingBaseAnyValues();
        fuller.MakeBaseFull();
    }

    private void MakeBaseFull() throws Exception {

        CreationOfUsers();
        startInteraction();
    }

    private void startInteraction() throws Exception {

        List<Project> allProjectList = ADMIN_SERVICE.getAllProjects();

        changeProjectStatus(allProjectList);
        List<Proposal> proposalList = getAllConsiderationsProposals();
        rejectAnyProposals(proposalList);
        List<Proposal> approvedList = approveAnyProposals(proposalList);
        List<Chapter> chaptersInWork = startBusiness(approvedList);
        List<Calculation> calculationList = createCalculations(chaptersInWork);
        payMoney(calculationList);
    }

    private List<Proposal> getAllConsiderationsProposals() throws Exception {

        List<Proposal> proposalList = new ArrayList<>();
        List<Developer> allActiveDevelopers = new ArrayList<>();
        List<Chapter> activeChapterList = new ArrayList<>();
        try {
            allActiveDevelopers.addAll(
                ADMIN_SERVICE.getAllDevelopers(UserStatus.ACTIVE, 1, DEVELOPER_COUNT).getList());
        } catch (IOException ignored) {
        }
        allActiveDevelopers.forEach(developer -> {
            try {
                List<Project> allProjectList = new ArrayList<>();
                allProjectList.addAll(DEVELOPER_SERVICE.getMyProjects(developer.getId(), ProjectStatus.PREPARATION, 1, projectCount).getList());
                allProjectList.addAll(DEVELOPER_SERVICE.getMyProjects(developer.getId(), ProjectStatus.IN_PROCESS, 1, projectCount).getList());

                allProjectList.forEach(project -> {
                    try {
                        activeChapterList.addAll(DEVELOPER_SERVICE.getChaptersByProjectId(project.getId()));
                    } catch (Exception ignored) {
                    }
                });
            } catch (Exception ignored) {
            }
        });
        activeChapterList.forEach(chapter -> {
            try {
                proposalList.addAll(DEVELOPER_SERVICE.getProposalsByChapterId(chapter.getId(), ProposalStatus.CONSIDERATION, 1, proposalCount).getList());
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
                    int sum = calculation.getWorkPricePlan() / ONE_PART_OF_PRICE_FOR_ADVANCE;
                    DEVELOPER_SERVICE.payAdvance(sum, calculation.getId());
                    DEVELOPER_SERVICE.payForWork(sum * (calculationIndex.get() % INT9), calculation.getId());
                } else {
                    int k = (calculationIndex.get() % INT5 * INT2);
                    int sum = calculation.getWorkPricePlan() * k / INT10;
                    DEVELOPER_SERVICE.payForWork(sum, calculation.getId());
                }
                calculationIndex.getAndIncrement();
            } catch (Exception ignored) {
            }
        });
    }

    private List<Calculation> createCalculations(List<Chapter> chaptersInWork) {

        List<Calculation> calculationList = new ArrayList<>();
        CalculationDao calculationDao = new CalculationDaoImpl();
        chaptersInWork.forEach(chapter -> {
            int thisCalculationCount = RANDOM.nextInt(MAX_COUNT_OF_CALCULATIONS);
            for (int i = 0; i < thisCalculationCount; i++) {
                int mm = i % INT12 + INT1;
                try {
                    int workPricePlan = (BOUND3 * (i + INT8)) / (i + INT2);
                    CONTRACTOR_SERVICE.createCalculation(chapter.getId(), BOUND4, mm, workPricePlan);


                } catch (Exception ignored) {
                }
            }
            try {
                List<Calculation> list = calculationDao.getCalculationsByChapterId(chapter.getId(), FIRST_PAGE_NUMBER, Integer.MAX_VALUE);
                calculationList.addAll(list);
                list.forEach(calculation -> {
                    int workPriceFact = calculation.getWorkPricePlan() * INT115 / INT100;
                    try {
                        CONTRACTOR_SERVICE.updateWorkPriceFact(workPriceFact, calculation.getId());
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

    private List<Chapter> startBusiness(List<Proposal> approvedList) {

        return approvedList.stream()
                   .peek(proposal -> {
                       try {
                           CONTRACTOR_SERVICE.setProposalStatus(proposal.getId(), ProposalStatus.ACCEPTED_BY_CONTRACTOR);
                       } catch (Exception ignored) {
                       }
                   })
                   .map(Proposal::getChapter)
                   .collect(Collectors.toList());
    }

    private List<Proposal> approveAnyProposals(List<Proposal> proposalList) {

        List<Proposal> approvedList = new ArrayList<>();
        int stop = Math.min(COUNT_OF_PROPOSALS_TO_APPROVE, proposalList.size());
        for (int i = 0; i < stop; i++) {
            try {
                Proposal proposal = proposalList.remove(RANDOM.nextInt(proposalList.size()));
                DEVELOPER_SERVICE.changeStatusOfProposal(proposal.getId(), ProposalStatus.APPROVED);
                proposal.setStatus(ProposalStatus.APPROVED);
                approvedList.add(proposal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return approvedList;
    }

    private void rejectAnyProposals(List<Proposal> proposalList) {

        for (int i = 0; i < COUNT_OF_PROPOSALS_TO_REJECT; i++) {
            try {
                Proposal proposal = proposalList.remove(RANDOM.nextInt(proposalList.size()));
                DEVELOPER_SERVICE.changeStatusOfProposal(proposal.getId(), ProposalStatus.REJECTED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void changeProjectStatus(List<Project> allProjectList) {

        AtomicInteger projectIndex = new AtomicInteger(INT1);
        allProjectList.forEach(project -> {
            if (projectIndex.get() % INT4 != INT0) {
                try {
                    DEVELOPER_SERVICE.changeProjectStatus(project.getId(), ProjectStatus.IN_PROCESS);
                    project.setStatus(ProjectStatus.IN_PROCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (projectIndex.get() % INT7 == INT0) {
                    try {
                        DEVELOPER_SERVICE.changeProjectStatus(project.getId(), ProjectStatus.COMPLETED);
                        project.setStatus(ProjectStatus.COMPLETED);
                    } catch (IOException ignored) {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (projectIndex.get() % INT8 == INT0) {
                    try {
                        DEVELOPER_SERVICE.changeProjectStatus(project.getId(), ProjectStatus.CANCELED);
                        project.setStatus(ProjectStatus.CANCELED);
                    } catch (IOException ignored) {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            projectIndex.getAndIncrement();
        });
    }

    private void CreationOfUsers() throws Exception {

        ADMIN_SERVICE.createAdmin(getUniqEmail(), getPassword());
        for (int i = 0; i < DEVELOPER_COUNT; i++) {
            Developer dev = DEVELOPER_SERVICE.createDeveloper(getUniqEmail(), getPassword(), getUserName(), CITY_NAMES[i % CITY_NAMES.length], getStreet(), String.valueOf(userCount * INT4 / INT3));
            int thisProjectCount = RANDOM.nextInt(INT5);
            for (int j = 0; j < thisProjectCount; j++) {
                DEVELOPER_SERVICE.createProject(dev.getId(), getProjectName(), CITY_NAMES[RANDOM.nextInt(CITY_NAMES.length)], getStreet(), String.valueOf(projectCount * INT4 / INT3));

                int thisChapterCount = RANDOM.nextInt(INT11);
                for (int k = 0; k < thisChapterCount; k++) {
                    DEVELOPER_SERVICE.createChapter(projectCount, CHAPTER_NAMES[k], RANDOM.nextInt(BOUND2));
                    if (thisChapterCount % INT5 == 0) {
                        DEVELOPER_SERVICE.cancelChapter(chapterCount);
                    }
                    chapterCount++;
                }
                projectCount++;
            }
        }
        for (int i = 0; i < CONTRACTOR_COUNT; i++) {

            Contractor con = CONTRACTOR_SERVICE.createContractor(getUniqEmail(), getPassword(), getUserName(), CITY_NAMES[i % CITY_NAMES.length], getStreet(), String.valueOf(userCount * INT4 / INT3));
            int thisProposalCount = RANDOM.nextInt(MAX_PROPOSAL_COUNT_PER_CONTRACTOR);
            for (int j = 0; j < thisProposalCount; j++) {
                Proposal proposal;
                try {
                    CONTRACTOR_SERVICE.createProposal((RANDOM.nextInt(chapterCount) + INT1), con.getId());
                } catch (NotCreateDataInDbException e) {
                    continue;
                }
                proposalCount++;
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

        return UNIQUE_USER_NAME + userCount;
    }

    private String getStreet() {

        return WEST_AVENUE + userCount % 7;
    }

    private String getProjectName() {

        return (UNIQUE_PROJECT_NAME + projectCount);
    }
}
