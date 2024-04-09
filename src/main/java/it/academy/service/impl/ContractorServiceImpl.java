package it.academy.service.impl;

import it.academy.dao.*;
import it.academy.dao.impl.*;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.exceptions.RoleException;
import it.academy.pojo.*;
import it.academy.pojo.enums.*;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;
import it.academy.service.ContractorService;
import it.academy.util.Util;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class ContractorServiceImpl implements ContractorService {

    private final ContractorDao contractorDao = new ContractorDaoImpl();
    private final DeveloperDao developerDao = new DeveloperDaoImpl();
    private final CalculationDao calculationDao = new CalculationDaoImpl();
    private final ChapterDao chapterDao = new ChapterDaoImpl();
    private final ProjectDao projectDao = new ProjectDaoImpl();
    private final ProposalDao proposalDao = new ProposalDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public Contractor createContractor(
        String email, String password, String name, String city, String street, Integer building)
        throws IOException, NotCreateDataInDbException, EmailOccupaidException {

        AtomicReference<Contractor> contractor = new AtomicReference<>();
        AtomicBoolean isEmailOccupaid = new AtomicBoolean(false);
        contractorDao.executeInOneTransaction(() -> {
            AtomicReference<User> userFromDB = new AtomicReference<>();
            userDao.executeInOneTransaction(() -> {
                User user = null;
                try {
                    user = userDao.getUser(email);
                } catch (NoResultException e) {
                    log.trace("Email " + email + " not occupied", e);
                }
                if (user != null) {
                    log.trace("Email " + email + " occupied");
                    isEmailOccupaid.set(true);
                } else {
                    User newUser = User.builder()
                                       .email(email)
                                       .password(password)
                                       .role(Roles.CONTRACTOR)
                                       .status(UserStatus.AKTIVE)
                                       .build();
                    userDao.create(newUser);
                    userFromDB.set(newUser);
                }
            });
            if (userFromDB.get() != null) {
                Contractor newContractor = Contractor.builder()
                                               .name(name)
                                               .address(Address.builder()
                                                            .city(city)
                                                            .street(street)
                                                            .building(building)
                                                            .build())
                                               .user(userFromDB.get())
                                               .build();
                contractorDao.create(newContractor);
                log.trace("Contractor created: id=" + newContractor.getId());
                contractor.set(newContractor);
            }
        });
        if (isEmailOccupaid.get()) {
            throw new EmailOccupaidException("Email " + email + " occupied");
        } else if (contractor.get() == null) {
            throw new NotCreateDataInDbException();
        }
        return contractor.get();
    }

    @Override
    public Contractor getContractor(Long userId) throws IOException, RoleException, EntityNotFoundException {

        AtomicReference<Contractor> contractor = new AtomicReference<>();
        AtomicBoolean roleChecked = new AtomicBoolean(false);
        contractorDao.executeInOneTransaction(() -> {
            User user;
            try {
                user = userDao.get(userId);
            } catch (EntityNotFoundException e) {
                log.error("Contractor not found with id=" + userId);
                throw e;
            }
            if (user != null) {
                if (Roles.CONTRACTOR.equals(user.getRole())) {
                    contractor.set((Contractor) user.getLegalEntity());
                    roleChecked.set(true);
                } else {
                    log.error("User not Contractor. id=" + userId);
                }
            }
        });
        if (!roleChecked.get()) {
            throw new RoleException("User not Contractor");
        }
        return contractor.get();
    }

    @Override
    public List<Project> getMyProjects(Long contractorId, ProjectStatus status, int page, int count)
        throws IOException {

        int totalCount = projectDao.getCountOfProjectsByContractorId(contractorId, status);
        page = Util.getCorrectPageNumber(page, count, totalCount);

        List<Project> list = new ArrayList<>();
        try {
            list.addAll(projectDao.getProjectsByContractorId(contractorId, status, page, count));
        } catch (NoResultException e) {
            log.error("There is no such data in DB with contractorId=" + contractorId);
        }
        return list;
    }

    @Override
    public List<Project> getMyProjectsByDeveloper(
        Long developerId, Long contractorId, ProjectStatus status, int page, int count)
        throws IOException {

        int totalCount = projectDao.getCountOfProjectsByDeveloperIdContractorId(developerId, contractorId, status);
        page = Util.getCorrectPageNumber(page, count, totalCount);

        List<Project> list = new ArrayList<>();
        try {
            list.addAll(projectDao.getProjectsByDeveloperIdContractorId(developerId, contractorId, status, page, count));
        } catch (NoResultException e) {
            log.error("There is no such data in DB with contractorId=" + contractorId +
                          " and deleloperId=" + developerId);
        }
        return list;
    }

    @Override
    public List<String> getAllChapterNames() throws IOException {

        List<String> list = new ArrayList<>();
        try {
            list.addAll(chapterDao.getAllChapterNames());
        } catch (NoResultException e) {
            log.error("There is no such data in DB");
        }
        return list;
    }

    @Override
    public List<Chapter> getFreeChapters(String chapterName, int page, int count) throws IOException {

        int totalCount = chapterDao.getCountOfFreeChaptersByName(chapterName);
        page = Util.getCorrectPageNumber(page, count, totalCount);

        List<Chapter> list = new ArrayList<>();
        try {
            list.addAll(chapterDao.getFreeChapters(chapterName, page, count));
        } catch (NoResultException e) {
            log.error("There is no such data in DB with chpterName=" + chapterName);
        }
        return list;
    }

    @Override
    public List<Developer> getMyDevelopers(Long contractorId, ProjectStatus status, int page, int count)
        throws IOException {

        int totalCount = developerDao.getCountOfDevelopers(contractorId, status);
        page = Util.getCorrectPageNumber(page, count, totalCount);

        List<Developer> list = new ArrayList<>();
        try {
            list.addAll(developerDao.getDevelopersByContractorId(contractorId, status, page, count));
        } catch (NoResultException e) {
            log.error("There is no such data in DB with contractorId=" + contractorId);
        }
        return list;
    }

    @Override
    public List<Proposal> getMyProposals(Long contractorId, ProposalStatus status, int page, int count)
        throws IOException {

        int totalCount = proposalDao.getCountOfProposalsByContractorId(contractorId, status);
        page = Util.getCorrectPageNumber(page, count, totalCount);

        List<Proposal> list = new ArrayList<>();
        try {
            list.addAll(proposalDao.getProposalsByContractorId(contractorId, status, page, count));
        } catch (NoResultException e) {
            log.error("There is no such data in DB with contractorId=" + contractorId);
        }
        return list;
    }

    @Override
    public List<Chapter> getMyChaptersByProjectId(Long projectId, Long contractorId)
        throws IOException {

        List<Chapter> list = new ArrayList<>();
        try {
            list.addAll(chapterDao.getChaptersByProjectIdContractorId(projectId, contractorId));
        } catch (NoResultException e) {
            log.error("There is no such data in DB with contractorId=" + contractorId +
                          " and projectId=" + projectId);
        }
        return list;
    }

    @Override
    public List<Calculation> getCalculationsByChapter(Long chapterId, int page, int count)
        throws IOException {

        int totalCount = calculationDao.getCountOfCalculationsByChapterId(chapterId);
        page = Util.getCorrectPageNumber(page, count, totalCount);

        List<Calculation> list = new ArrayList<>();
        try {
            list.addAll(calculationDao.getCalculationsByChapterId(chapterId, page, count));
        } catch (NoResultException e) {
            log.error("There is no such data in DB with chapterId=" + chapterId);
        }
        return list;
    }

    @Override
    public void updateWorkPriceFact(Integer workPriceFact, Long calculationId)
        throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isUpdated = new AtomicBoolean(false);
        calculationDao.executeInOneTransaction(() -> {
            Calculation calculationFromDb = null;
            try {
                calculationFromDb = calculationDao.get(calculationId);
            } catch (EntityNotFoundException e) {
                log.error("Calculation not found. id=" + calculationId);
            }
            if (calculationFromDb != null) {
                calculationFromDb.setWorkPriceFact(workPriceFact);
                calculationDao.update(calculationFromDb);
                log.trace("Workprice updated. id=" + calculationId + ", value=" + workPriceFact);
                isUpdated.set(true);
            } else {
                log.debug("Workprice not updated. id=" + calculationId);
            }
        });
        if (!isUpdated.get()) {
            throw new NotUpdateDataInDbException();
        }
    }

    @Override
    public Calculation createCalculation(Long chapterId, Integer YYYY, Integer MM, Integer workPricePlan)
        throws IOException, NotCreateDataInDbException {

        AtomicReference<Calculation> calculation = new AtomicReference<>();
        calculationDao.executeInOneTransaction(() -> {
            Chapter chapter = null;
            try {
                chapter = chapterDao.get(chapterId);
            } catch (EntityNotFoundException e) {
                log.error("Chapter not found. id=" + chapterId);
            }
            if (chapter != null && ChapterStatus.OCCUPIED.equals(chapter.getStatus())) {
                Calculation newCalculation = Calculation.builder()
                                                 .chapter(chapter)
                                                 .month(Date.valueOf("" + YYYY + "-" + MM + "-" + "01"))
                                                 .workPricePlan(workPricePlan)
                                                 .build();
                calculationDao.create(newCalculation);
                log.trace("Calculation created. id=" + newCalculation.getId());
                calculation.set(newCalculation);
            } else {
                log.debug("Calculation not created. chapterId=" + chapterId);
            }
        });
        if (calculation.get() == null) {
            throw new NotCreateDataInDbException();
        }
        return calculation.get();
    }

    @Override
    public void startWork(Long proposalId) throws IOException, NotUpdateDataInDbException {

        setProposalStatus(proposalId, ProposalStatus.ACCEPTED_BY_CONTRACTOR);
    }

    @Override
    public void cancelProposal(Long proposalId) throws IOException, NotUpdateDataInDbException {

        setProposalStatus(proposalId, ProposalStatus.CANCELED);
    }

    private void setProposalStatus(Long proposalId, ProposalStatus canceled)
        throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isUpdated = new AtomicBoolean(false);
        proposalDao.executeInOneTransaction(() -> {
            Proposal proposal = null;
            try {
                proposal = proposalDao.get(proposalId);
            } catch (EntityNotFoundException e) {
                log.error("Proposal not found. id=" + proposalId);
            }
            if (proposal != null) {
                proposal.setStatus(ProposalStatus.ACCEPTED_BY_CONTRACTOR);
                proposalDao.update(proposal);
                log.trace("Proposal status update to " + canceled);
                isUpdated.set(true);
            } else {
                log.debug("Proposal status not update. id=" + proposalId);
            }
        });
        if (!isUpdated.get()) {
            throw new NotUpdateDataInDbException();
        }
    }

    @Override
    public Proposal createProposal(Long chapterId, Long contractorId)
        throws IOException, NotCreateDataInDbException {

        AtomicReference<Proposal> proposal = new AtomicReference<>();
        proposalDao.executeInOneTransaction(() -> {
            Chapter chapter;
            try {
                chapter = chapterDao.get(chapterId);
            } catch (EntityNotFoundException e) {
                log.error("There is no such data in DB with chapterId " + chapterId);
                return;
            }
            Contractor contractor;
            try {
                contractor = contractorDao.get(contractorId);
            } catch (EntityNotFoundException e) {
                log.error("There is no such data in DB with contractorId " + contractorId);
                return;
            }
            try {
                Proposal proposalFromDB = proposalDao
                                              .getProposalByChapterIdContractorId(chapterId, contractorId);
                proposal.set(proposalFromDB);
                log.debug("Data alredy exist in DB. id=" + proposalFromDB.getId());
                return;
            } catch (NoResultException e) {
                log.trace("There is no such proposal in DB with chapterId " + chapterId +
                              " and contractorId " + contractorId);
            }
            if (chapter != null && contractor != null) {
                Proposal newProposal = Proposal.builder()
                                           .chapter(chapter)
                                           .contractor(contractor)
                                           .build();
                proposalDao.create(newProposal);
                log.trace("Created proposal with id=" + newProposal.getId());
                proposal.set(newProposal);
            }
        });
        if (proposal.get() == null) {
            throw new NotCreateDataInDbException();
        }
        return proposal.get();
    }
}
