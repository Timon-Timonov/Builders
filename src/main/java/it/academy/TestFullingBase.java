/*
package it.academy;

import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
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

public class TestFullingBase {

    public static final int MAX_COUNT_OF_CALCULATIONS = 15;
    public static final int ONE_PART_OF_PRICE_FOR_ADVANCE = 10;
    public static final int COUNT_OF_PROPOSALS_TO_APPROVE = 45;
    public static final int COUNT_OF_PROPOSALS_TO_REJECT = 5;
    public static int MAX_PROPOSAL_COUNT_PER_CONTRACTOR = 15;
    public static final int DEVELOPER_COUNT = 10;
    public static final int CONTRACTOR_COUNT = 10;
    public static final Random RANDOM = new Random();
    public static final String[] CHAPTER_NAMES = {"GP", "KG", "NVK", "VK", "TS", "EL", "AR", "AOP", "TEL", "TL"};
    public static final String[] CITY_NAMES = {"New York", "Moscow", "Minsk", "Piter", "Vilnus", "Warshawa", "Berlin"};
    private final AdminService ADMIN_SERVICE = new AdminServiceImpl();
    private final DeveloperService DEVELOPER_SERVICE = new DeveloperServiceImpl();
    private final ContractorService CONTRACTOR_SERVICE = new ContractorServiceImpl();

    private int proposalCount = 1;
    private int userCount = 1;
    private int projectCount = 1;
    private int chapterCount = 1;


    public static void main(String[] args) throws Exception {

        TestFullingBase fuller = new TestFullingBase();
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
                    } catch (IOException ignored) {
                    }
                });
            } catch (IOException ignored) {
            }
        });
        activeChapterList.forEach(chapter -> {
            try {
                proposalList.addAll(DEVELOPER_SERVICE.getProposalsByChapterId(chapter.getId(), ProposalStatus.CONSIDERATION, 1, proposalCount).getList());
            } catch (IOException ignored) {
            }
        });
        return proposalList;
    }

    private void payMoney(List<Calculation> calculationList) {

        AtomicInteger calculationIndex = new AtomicInteger(1);
        calculationList.forEach(calculation -> {
            try {
                if (calculationIndex.get() % 2 == 0) {
                    int sum = calculation.getWorkPricePlan() / ONE_PART_OF_PRICE_FOR_ADVANCE;
                    DEVELOPER_SERVICE.payAdvance(sum, calculation.getId());
                    DEVELOPER_SERVICE.payForWork(sum * (calculationIndex.get() % 9), calculation.getId());
                } else {
                    int k = (calculationIndex.get() % 5 * 2);
                    int sum = calculation.getWorkPricePlan() * k / 10;
                    DEVELOPER_SERVICE.payForWork(sum, calculation.getId());
                }
                calculationIndex.getAndIncrement();
            } catch (IOException | NotCreateDataInDbException ignored) {
            }
        });
    }

    private List<Calculation> createCalculations(List<Chapter> chaptersInWork) {

        List<Calculation> calculationList = new ArrayList<>();
        chaptersInWork.forEach(chapter -> {
            int thisCalculationCount = RANDOM.nextInt(MAX_COUNT_OF_CALCULATIONS);
            for (int i = 0; i < thisCalculationCount; i++) {
                int mm = i % 12 + 1;
                try {
                    int workPricePlan = (2558 * (i + 8)) / (i + 2);
                    Calculation calculation = CONTRACTOR_SERVICE
                                                  .createCalculation(chapter.getId(), 2023, mm, workPricePlan);
                    int workPriceFact = workPricePlan * 115 / 100;
                    CONTRACTOR_SERVICE.updateWorkPriceFact(workPriceFact, calculation.getId());
                    calculationList.add(calculation);
                } catch (IOException | NotCreateDataInDbException | NotUpdateDataInDbException ignored) {
                }
            }
        });
        return calculationList;
    }

    private List<Chapter> startBusiness(List<Proposal> approvedList) {

        return approvedList.stream()
                   .peek(proposal -> {
                       try {
                           CONTRACTOR_SERVICE.setProposalStatus(proposal.getId(), ProposalStatus.ACCEPTED_BY_CONTRACTOR);
                       } catch (IOException | NotUpdateDataInDbException ignored) {
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
                DEVELOPER_SERVICE.approveProposal((proposal.getId()));
                proposal.setStatus(ProposalStatus.APPROVED);
                approvedList.add(proposal);
            } catch (IOException | NotUpdateDataInDbException ignored) {
            }
        }
        return approvedList;
    }

    private void rejectAnyProposals(List<Proposal> proposalList) {

        for (int i = 0; i < COUNT_OF_PROPOSALS_TO_REJECT; i++) {
            try {
                Proposal proposal = proposalList.remove(RANDOM.nextInt(proposalList.size()));
                DEVELOPER_SERVICE.rejectProposal(proposal.getId());
            } catch (IOException | NotUpdateDataInDbException ignored) {
            }
        }
    }


    private void changeProjectStatus(List<Project> allProjectList) {

        AtomicInteger projectIndex = new AtomicInteger(1);
        allProjectList.forEach(project -> {
            if (projectIndex.get() % 4 != 0) {
                try {
                    DEVELOPER_SERVICE.startProject(project.getId());
                    project.setStatus(ProjectStatus.IN_PROCESS);
                } catch (IOException ignored) {
                } catch (NotUpdateDataInDbException e) {
                    e.printStackTrace();
                }
                if (projectIndex.get() % 7 == 0) {
                    try {
                        DEVELOPER_SERVICE.endProject(project.getId());
                        project.setStatus(ProjectStatus.COMPLETED);
                    } catch (IOException ignored) {
                    } catch (NotUpdateDataInDbException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (projectIndex.get() % 8 == 0) {
                    try {
                        DEVELOPER_SERVICE.cancelProject(project.getId());
                        project.setStatus(ProjectStatus.CANCELED);
                    } catch (IOException ignored) {
                    } catch (NotUpdateDataInDbException e) {
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
            Developer dev = DEVELOPER_SERVICE.createDeveloper(getUniqEmail(), getPassword(), getUserName(), CITY_NAMES[i % CITY_NAMES.length], getStreet(), String.valueOf(userCount * 4 / 3));
            int thisProjectCount = RANDOM.nextInt(5);
            for (int j = 0; j < thisProjectCount; j++) {
                DEVELOPER_SERVICE.createProject(dev.getId(), getProjectName(), CITY_NAMES[RANDOM.nextInt(CITY_NAMES.length)], getStreet(), String.valueOf(projectCount * 4 / 3));

                int thisChapterCount = RANDOM.nextInt(11);
                for (int k = 0; k < thisChapterCount; k++) {
                    DEVELOPER_SERVICE.createChapter((long) projectCount, CHAPTER_NAMES[k], RANDOM.nextInt(50_000));
                    if (thisChapterCount % 5 == 0) {
                        DEVELOPER_SERVICE.cancelChapter((long) chapterCount);
                    }
                    chapterCount++;
                }
                projectCount++;
                if (thisProjectCount % 5 == 0) {
                    DEVELOPER_SERVICE.cancelProject((long) projectCount);
                }
            }
        }
        for (int i = 0; i < CONTRACTOR_COUNT; i++) {

            Contractor con = CONTRACTOR_SERVICE.createContractor(getUniqEmail(), getPassword(), getUserName(), CITY_NAMES[i % CITY_NAMES.length], getStreet(), String.valueOf(userCount * 4 / 3));
            int thisProposalCount = RANDOM.nextInt(MAX_PROPOSAL_COUNT_PER_CONTRACTOR);
            for (int j = 0; j < thisProposalCount; j++) {
                Proposal proposal;
                try {
                    proposal = CONTRACTOR_SERVICE.createProposal((long) (RANDOM.nextInt(chapterCount) + 1), con.getId());
                } catch (NotCreateDataInDbException e) {
                    continue;
                }
                if (proposal.getId() == proposalCount) {
                    proposalCount++;
                }
            }
        }
    }

    private String getUniqEmail() {

        return "myEmail." + (userCount++) + "@mail.ru";
    }

    private String getPassword() {

        return userCount % 2 == 0 ? "1111" : "2222";
    }

    private String getUserName() {

        return "UniqueUserName" + userCount;
    }

    private String getStreet() {

        return "WestAvenue" + userCount % 7;
    }

    private String getProjectName() {

        return ("UniqueProjectName" + projectCount);
    }
}
*/
