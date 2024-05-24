package it.academy.dao.impl;

import it.academy.dao.ProposalDao;
import it.academy.pojo.Proposal;
import it.academy.pojo.enums.ChapterStatus;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;

public class ProposalDaoImpl extends DaoImpl<Proposal, Long> implements ProposalDao {

    public ProposalDaoImpl() {

        super(Proposal.class);
    }

    @Override
    public List<Proposal> getProposalsByContractorId(Long contractorId, ProposalStatus status, int page, int count)
        throws NoResultException {

        Query query = getEm().createQuery(
            "SELECT prop, proj, ch, dev, us, contr " +
                "FROM Proposal prop LEFT JOIN Chapter ch " +
                "ON prop.chapter.id=ch.id LEFT JOIN Project proj " +
                "ON ch.project.id=proj.id LEFT JOIN Developer dev " +
                "ON proj.developer.id=dev.id LEFT JOIN Contractor contr " +
                "ON prop.contractor.id=contr.id LEFT JOIN User us " +
                "ON us.id =dev.id " +

                "WHERE contr.id=:contractorId " +
                "AND prop.status=:proposalStatus " +

                "ORDER BY dev.name ASC, proj.name ASC, ch.name ASC");

        query.setParameter("contractorId", contractorId)
            .setParameter("proposalStatus", status)
            .setMaxResults(count)
            .setFirstResult((page - 1) * count);

        List<Object[]> list = (List<Object[]>) query.getResultList();
        return list.stream()
                   .map(arr -> (Proposal) arr[0])
                   .collect(Collectors.toList());
    }

    @Override
    public Proposal getProposalByChapterIdContractorId(Long chapterId, Long contractorId)
        throws NoResultException {

        TypedQuery<Proposal> query = getEm().createQuery(
            "SELECT p FROM Proposal p WHERE p.chapter.id=:chapterId AND p.contractor.id=:contractorId",
            Proposal.class);
        return query.setParameter("chapterId", chapterId)
                   .setParameter("contractorId", contractorId)
                   .getSingleResult();
    }

    @Override
    public List<Proposal> getProposalsByChapterId(Long chapterId, ProposalStatus status, int page, int count)
        throws NoResultException {

        TypedQuery<Proposal> query = getEm().createQuery(
            "SELECT p FROM Proposal p " +
                "WHERE p.chapter.id=:chapterId " +
                "AND p.status=:proposalStatus " +
                "ORDER BY p.contractor.name ASC",
            Proposal.class);
        return query.setParameter("chapterId", chapterId)
                   .setParameter("proposalStatus", status)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public List<Proposal> getProposalsByDeveloperId(Long developerId, ProposalStatus status, int page, int count)
        throws NoResultException {

        TypedQuery<Proposal> query = getEm().createQuery(
            "SELECT prop " +
                "FROM Proposal prop LEFT JOIN Chapter ch " +
                "ON prop.chapter.id=ch.id " +

                "WHERE prop.status=:proposalStatus " +
                "AND ch.status =:chapterStatus " +
                "AND ch.project.developer.id=:developerId " +
                "AND ch.project.status <> :projectStatus1 " +
                "AND ch.project.status<>:projectStatus2 " +
                "ORDER BY ch.project.name ASC, ch.name ASC",
            Proposal.class);
        return query.setParameter("proposalStatus", status)
                   .setParameter("chapterStatus", ChapterStatus.FREE)
                   .setParameter("developerId", developerId)
                   .setParameter("projectStatus1", ProjectStatus.CANCELED)
                   .setParameter("projectStatus2", ProjectStatus.COMPLETED)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }


    @Override
    public Long getCountOfProposalsByContractorId(Long contractorId, ProposalStatus status) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT( p) " +
                "FROM Proposal p INNER JOIN Contractor contr " +
                "ON p.contractor.id=contr.id " +

                "WHERE p.contractor.id=:contractorId " +
                "AND p.status=:proposalStatus ",
            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("proposalStatus", status)
                   .getSingleResult();
    }

    @Override
    public Long getCountOfProposalsByChapterId(Long chapterId, ProposalStatus status) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(p) FROM Proposal p " +
                "WHERE p.chapter.id=:chapterId " +
                "AND p.status=:proposalStatus",
            Long.class);
        return query.setParameter("chapterId", chapterId)
                   .setParameter("proposalStatus", status)
                   .getSingleResult();
    }

    @Override
    public Long getCountOfProposalsByDeveloperId(Long developerId, ProposalStatus status) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(prop) " +
                "FROM Proposal prop LEFT JOIN Chapter ch " +
                "ON prop.chapter.id=ch.id " +
                "WHERE prop.status=:proposalStatus " +
                "AND ch.status =:chapterStatus " +
                "AND ch.project.developer.id=:developerId " +
                "AND ch.project.status <>:projectStatus1 " +
                "AND ch.project.status <>:projectStatus2",
            Long.class);
        return query.setParameter("proposalStatus", status)
                   .setParameter("chapterStatus", ChapterStatus.FREE)
                   .setParameter("developerId", developerId)
                   .setParameter("projectStatus1", ProjectStatus.CANCELED)
                   .setParameter("projectStatus2", ProjectStatus.COMPLETED)
                   .getSingleResult();
    }

    @Override
    public boolean isAnyProposalOfChapterApproved(Long chapterId) throws IOException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(p) FROM Proposal p WHERE p.chapter.id=:chapterId AND p.status=:proposalStatus",
            Long.class);
        Long countOfApprovedProposals = query.setParameter("chapterId", chapterId)
                                            .setParameter("proposalStatus", ProposalStatus.APPROVED)
                                            .getSingleResult();
        return countOfApprovedProposals != ZERO_INT_VALUE;
    }

    @Override
    public void rejectAllConsiderateProposalsOfChapter(long chapterId) throws IOException {

        Query query = getEm().createQuery(
            "UPDATE Proposal p SET p.status=:newStatus WHERE p.chapter.id=:chapterId AND p.status=:oldStatus ");
        query.setParameter("newStatus", ProposalStatus.REJECTED)
            .setParameter("oldStatus", ProposalStatus.CONSIDERATION)
            .setParameter("chapterId", chapterId)
            .executeUpdate();
    }
}