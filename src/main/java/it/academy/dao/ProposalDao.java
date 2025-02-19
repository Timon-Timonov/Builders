package it.academy.dao;

import it.academy.pojo.Proposal;
import it.academy.pojo.enums.ProposalStatus;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;

public interface ProposalDao extends Dao<Proposal, Long> {

    List<Proposal> getProposalsByContractorId(Long contractorId, ProposalStatus status, int page, int count)
        throws NoResultException;

    Proposal getProposalByChapterIdContractorId(Long chapterId, Long contractorId) throws NoResultException;

    List<Proposal> getProposalsByChapterId(Long chapterId, ProposalStatus status, int page, int count)
        throws NoResultException;

    List<Proposal> getProposalsByDeveloperId(Long developerId, ProposalStatus status, int page, int count)
        throws NoResultException;


    Long getCountOfProposalsByContractorId(Long contractorId, ProposalStatus status)
        throws NoResultException;

    Long getCountOfProposalsByChapterId(Long chapterId, ProposalStatus status)
        throws NoResultException;

    Long getCountOfProposalsByDeveloperId(Long developerId, ProposalStatus status)
        throws NoResultException;

    boolean isAnyProposalOfChapterApproved(Long chapterId) throws IOException;

    void rejectAllConsiderateProposalsOfChapter(long chapterId) throws IOException;
}
