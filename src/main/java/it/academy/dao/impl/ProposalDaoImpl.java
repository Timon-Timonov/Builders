package it.academy.dao.impl;

import it.academy.dao.ProposalDao;
import it.academy.pojo.Proposal;
import it.academy.pojo.enums.ChapterStatus;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class ProposalDaoImpl extends DaoImpl<Proposal, Long> implements ProposalDao {

    public ProposalDaoImpl() {

        super(Proposal.class);
    }

    @Override
    public List<Proposal> getProposalsByContractorId(Long contractorId, ProposalStatus status, int page, int count)
        throws NoResultException {

        TypedQuery<Proposal> query = getEm().createQuery(
            "SELECT p FROM Proposal p WHERE p.contractor.id=:contractorId AND p.status=:proposalStatus" +
                " ORDER BY p.chapter.project.developer.name ASC, p.chapter.project.name ASC, chapter.name ASC",
            Proposal.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("proposalStatus", status)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
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
            "SELECT p FROM Proposal p WHERE p.chapter.id=:chapterId AND p.status=:proposalStatus ORDER BY p.contractor.name ASC",
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
            "SELECT prop FROM Proposal prop LEFT JOIN Chapter ch ON prop.chapter.id=ch.id WHERE prop.status=:proposalStatus AND ch.status =:chapterStatus AND ch.project.developer.id=:developerId AND ch.project.status IN :projectStatuses ORDER BY ch.project.name ASC, ch.name ASC",
            Proposal.class);
        return query.setParameter("proposalStatus", status)
                   .setParameter("chapterStatus", ChapterStatus.FREE)
                   .setParameter("developerId", developerId)
                   .setParameter("projectStatuses", new ProjectStatus[]{ProjectStatus.PREPARATION, ProjectStatus.IN_PROCESS})
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }


    @Override
    public Long getCountOfProposalsByContractorId(Long contractorId, ProposalStatus status) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(p) FROM Proposal p WHERE p.contractor.id=:contractorId AND p.status=:proposalStatus" +
                " ORDER BY p.chapter.project.developer.name ASC, p.chapter.project.name ASC, chapter.name ASC",
            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("proposalStatus", status)
                   .getSingleResult();
    }

    @Override
    public Long getCountOfProposalsByChapterId(Long chapterId, ProposalStatus status) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(p) FROM Proposal p WHERE p.chapter.id=:chapterId AND p.status=:proposalStatus",
            Long.class);
        return query.setParameter("chapterId", chapterId)
                   .setParameter("proposalStatus", status)
                   .getSingleResult();
    }

    @Override
    public Long getCountOfProposalsByDeveloperId(Long developerId, ProposalStatus status) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(prop) FROM Proposal prop LEFT JOIN Chapter ch ON prop.chapter.id=ch.id WHERE prop.status=:proposalStatus AND ch.status =:chapterStatus AND ch.project.developer.id=:developerId AND ch.project.status IN :projectStatuses  ",
            Long.class);
        return query.setParameter("proposalStatus", status)
                   .setParameter("chapterStatus", ChapterStatus.FREE)
                   .setParameter("developerId", developerId)
                   .setParameter("projectStatuses", new ProjectStatus[]{ProjectStatus.PREPARATION, ProjectStatus.IN_PROCESS})
                   .getSingleResult();
    }
}