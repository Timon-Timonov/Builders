package it.academy.dao.impl;

import it.academy.dao.ChapterDao;
import it.academy.pojo.Chapter;
import it.academy.pojo.enums.ChapterStatus;
import it.academy.pojo.enums.ProjectStatus;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;

public class ChapterDaoImpl extends DaoImpl<Chapter, Long> implements ChapterDao {

    public ChapterDaoImpl() {

        super(Chapter.class);
    }

    @Override
    public long getCountOfFreeChaptersByName(long contractorId, String chapterName, ProjectStatus projectStatus) throws NoResultException, IOException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT (DISTINCT ch) FROM Chapter ch LEFT JOIN Proposal  prop ON ch.id=prop.chapter.id WHERE ch.name=:chapterName AND ch.status=:chapterStatus AND ch.project.status=:projectStatus AND ch.id NOT IN (SELECT prop.chapter.id FROM Proposal prop WHERE prop.contractor.id=:contractorId)",
            Long.class);
        return query.setParameter("chapterName", chapterName)
                   .setParameter("chapterStatus", ChapterStatus.FREE)
                   .setParameter("projectStatus", projectStatus)
                   .setParameter("contractorId", contractorId)
                   .getSingleResult();
    }

    @Override
    public List<Chapter> getFreeChapters(long contractorId, String chapterName, ProjectStatus projectStatus, int page, int count)
        throws IOException {

        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT DISTINCT ch FROM Chapter ch LEFT JOIN Proposal  prop ON ch.id=prop.chapter.id WHERE ch.name=:chapterName AND ch.status=:chapterStatus AND ch.project.status=:projectStatus AND ch.id NOT IN (SELECT prop.chapter.id FROM Proposal prop WHERE prop.contractor.id=:contractorId) ",
            Chapter.class);
        return query.setParameter("chapterName", chapterName)
                   .setParameter("chapterStatus", ChapterStatus.FREE)
                   .setParameter("projectStatus", projectStatus)
                   .setParameter("contractorId", contractorId)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
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
    public List<Chapter> getChaptersByProjectIdContractorId(long projectId, long contractorId)
        throws IOException {

        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT ch FROM Chapter ch WHERE ch.project.id=:projectId AND ch.contractor.id=:contractorId ORDER BY ch.name ASC",
            Chapter.class);
        return query.setParameter("projectId", projectId)
                   .setParameter("contractorId", contractorId)
                   .getResultList();
    }

    @Override
    public List<Chapter> getChaptersByProjectId(long projectId)
        throws IOException {

        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT ch FROM Chapter ch WHERE ch.project.id=:projectId ORDER BY ch.name ASC",
            Chapter.class);
        return query.setParameter("projectId", projectId)
                   .getResultList();
    }

    @Override
    public List<Chapter> getChaptersByContractorIdAndDeveloperId(long developerId, long contractorId, ProjectStatus status, int page, int count)
        throws IOException {

        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT ch  FROM Chapter ch LEFT JOIN Project proj ON ch.project.id=proj.id WHERE  proj.status=:status AND ch.contractor.id=:contractorId AND proj.developer.id=:developerId ORDER BY proj.name, ch.name",
            Chapter.class);
        return query.setParameter("developerId", developerId)
                   .setParameter("contractorId", contractorId)
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
    public long getCountOfChaptersByContractorIdAndDeveloperId(long developerId, long contractorId, ProjectStatus status) throws NoResultException, IOException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(ch) FROM Chapter ch LEFT JOIN Project proj ON ch.project.id=proj.id WHERE  proj.status=:status AND ch.contractor.id=:contractorId AND proj.developer.id=:developerId",
            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("developerId", developerId)
                   .setParameter("status", status)
                   .getSingleResult();
    }

    @Override
    public List<Chapter> getAllChaptersByDeveloperIdContractorId(long developerId, long contractorId) throws NoResultException, IOException {


        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT ch FROM Chapter ch WHERE ch.project.developer.id=:developerId AND ch.contractor.id=:contractorId",
            Chapter.class);
        return query.setParameter("developerId", developerId)
                   .setParameter("contractorId", contractorId)
                   .getResultList();
    }

    @Override
    public List<Chapter> getChaptersByContractorId(long contractorId, int page, int count) throws NoResultException, IOException {

        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT ch FROM Chapter ch WHERE ch.contractor.id=:contractorId ",
            Chapter.class);
        return query.setParameter("contractorId", contractorId)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public long getCountOfChaptersByContractorId(long contractorId) throws NoResultException, IOException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT (ch) FROM Chapter ch WHERE ch.contractor.id=:contractorId ",
            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .getSingleResult();
    }

    @Override
    public void cancelChaptersByProjectId(long projectId) throws IOException {

        Query query = getEm().createQuery(
            "UPDATE Chapter ch SET ch.status=:status WHERE ch.project.id=:projectId ");
        query.setParameter("projectId", projectId)
            .setParameter("status", ChapterStatus.CANCELED)
            .executeUpdate();

    }
}