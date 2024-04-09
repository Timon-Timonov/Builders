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
            "SELECT ch.name FROM Chapter ch ORDER BY ch.name ASC ",
            String.class);
        return query.getResultList();
    }

    @Override
    public List<Chapter> getFreeChapters(String chapterName, int page, int count)
        throws IOException {

        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT ch FROM Chapter ch WHERE ch.name=:chapterName AND ch.status=:status AND ch.project.status<>:cancelStatus AND ch.project.status<>:completeStatus ORDER BY ch.name ASC",
            Chapter.class);
        return query.setParameter("chapterName", chapterName)
                   .setParameter("status", ChapterStatus.FREE)
                   .setParameter("cancelStatus", ProjectStatus.CANCELED)
                   .setParameter("completeStatus", ProjectStatus.COMPLETED)
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
    public Integer getCountOfFreeChaptersByName(String chapterName) throws NoResultException, IOException {

        TypedQuery<Integer> query = getEm().createQuery(
            "SELECT Count(ch) FROM Chapter ch WHERE ch.name=:chapterName AND ch.status=:status AND ch.project.status<>:cancelStatus AND ch.project.status<>:completeStatus",
            Integer.class);
        return query.setParameter("chapterName", chapterName)
                   .setParameter("status", ChapterStatus.FREE)
                   .setParameter("cancelStatus", ProjectStatus.CANCELED)
                   .setParameter("completeStatus", ProjectStatus.COMPLETED)
                   .getSingleResult();
    }

    @Override
    public Integer getCountOfChaptersByContractorId(Long contractorId, ChapterStatus status) throws NoResultException, IOException {

        TypedQuery<Integer> query = getEm().createQuery(
            "SELECT COUNT(ch) FROM Chapter ch WHERE ch.contractor.id=:contractorId AND ch.status=:status ORDER BY ch.name ASC",
            Integer.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("status", status)
                   .getSingleResult();
    }
}