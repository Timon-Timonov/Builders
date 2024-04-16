package it.academy.dao.impl;

import it.academy.dao.ChapterDao;
import it.academy.pojo.Chapter;
import it.academy.pojo.enums.ChapterStatus;
import it.academy.pojo.enums.ProjectStatus;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;

public class ChapterDaoImpl extends DaoImpl<Chapter, Long> implements ChapterDao {

    public ChapterDaoImpl() {

        super(Chapter.class);
    }

    @Override
    public List<String> getAllChapterNames()
        throws IOException {

        TypedQuery<String> query = getEm().createQuery(
            "SELECT DISTINCT (ch.name) FROM Chapter ch ORDER BY ch.name ASC ",
            String.class);
        return query.getResultList();
    }

    @Override
    public List<Chapter> getFreeChapters(Long contractorId,String chapterName, ProjectStatus projectStatus, int page, int count)
        throws IOException {

        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT ch FROM Chapter ch, Proposal  prop WHERE prop.contractor.id=:contractorId AND prop.chapter<>ch AND ch.name=:chapterName AND ch.status=:status AND ch.project.status=:projectStatus ORDER BY ch.name ASC",
            Chapter.class);
        return query.setParameter("chapterName", chapterName)
                   .setParameter("status", ChapterStatus.FREE)
                   .setParameter("projectStatus", projectStatus)
                   .setParameter("contractorId", contractorId)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public List<Chapter> getChaptersByProjectIdContractorId(Long projectId, Long contractorId)
        throws IOException {

        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT ch FROM Chapter ch WHERE ch.project.id=:projectId AND ch.contractor.id=:contractorId ORDER BY ch.name ASC",
            Chapter.class);
        return query.setParameter("projectId", projectId)
                   .setParameter("contractorId", contractorId)
                   .getResultList();
    }

    @Override
    public List<Chapter> getChaptersByProjectId(Long projectId)
        throws IOException {

        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT ch FROM Chapter ch WHERE ch.project.id=:projectId ORDER BY ch.name ASC",
            Chapter.class);
        return query.setParameter("projectId", projectId)
                   .getResultList();
    }

    @Override
    public List<Chapter> getChaptersByContractorId(Long contractorId, ChapterStatus status, int page, int count)
        throws IOException {

        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT ch FROM Chapter ch WHERE ch.contractor.id=:contractorId AND ch.status=:status ORDER BY ch.name ASC",
            Chapter.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("status", status)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public Chapter get(Long chapterId) throws EntityNotFoundException {

        Chapter chapter = super.get(chapterId);
        if (chapter != null) {
            getEm().refresh(chapter);
        } else {
            throw new EntityNotFoundException();
        }
        return chapter;
    }

    @Override
    public Long getCountOfFreeChaptersByName(Long contractorId,String chapterName, ProjectStatus projectStatus) throws NoResultException, IOException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT Count(ch) FROM Chapter ch,Proposal prop WHERE prop.chapter<>ch AND prop.contractor.id=:contractorId AND ch.name=:chapterName AND ch.status=:status AND ch.project.status=:projectStatus",
            Long.class);
        return query.setParameter("chapterName", chapterName)
                   .setParameter("status", ChapterStatus.FREE)
                   .setParameter("projectStatus", projectStatus)
                   .setParameter("contractorId", contractorId)
                   .getSingleResult();
    }

    @Override
    public Long getCountOfChaptersByContractorId(Long contractorId, ChapterStatus status) throws NoResultException, IOException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(ch) FROM Chapter ch WHERE ch.contractor.id=:contractorId AND ch.status=:status ORDER BY ch.name ASC",
            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("status", status)
                   .getSingleResult();
    }

    @Override
    public List<Chapter> getAllChaptersByDeveloperIdContractorId(Long developerId, Long contractorId) throws NoResultException, IOException {


        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT ch FROM Chapter ch WHERE ch.project.developer.id=:developerId AND ch.contractor.id=:contractorId",
            Chapter.class);
        return query.setParameter("developerId", developerId)
                   .setParameter("contractorId", contractorId)
                   .getResultList();
    }
}