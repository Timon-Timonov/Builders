package it.academy.dao.impl;

import it.academy.dao.ProposalDao;
import it.academy.pojo.Proposal;
import it.academy.pojo.enums.ProposalStatus;

import javax.persistence.NoResultException;
import java.util.List;

public class ProposalDaoImpl extends DaoImpl<Proposal, Long> implements ProposalDao {

    public ProposalDaoImpl() {

        super(Proposal.class);
    }

    @Override
    public List<Proposal> getProposalsByContractorId(Long contractorId, ProposalStatus status, int page, int count) throws NoResultException {
        return null;
    }

    @Override
    public Proposal getProposalByChapterIdContractorId(Long chapterId, Long contractorId) throws NoResultException {
        return null;
    }

    @Override
    public List<Proposal> getProposalsByChapterId(Long chapterId, ProposalStatus status, int page, int count) throws NoResultException {
        return null;
    }

    @Override
    public List<Proposal> getProposalsByDeveloperId(Long developerId, ProposalStatus status, int page, int count) {
        return null;
    }
}