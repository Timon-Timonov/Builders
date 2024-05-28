package it.academy.dao.impl;

import it.academy.dao.ChapterDao;
import it.academy.pojo.Chapter;
import it.academy.pojo.enums.ChapterStatus;
import it.academy.pojo.enums.ProjectStatus;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;

public class ChapterDaoImpl extends DaoImpl<Chapter, Long> implements ChapterDao {

    public ChapterDaoImpl() {

        super(Chapter.class);
    }

    @Override
    public Chapter getChapterForCalculation(long id) throws NoResultException {

        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT ch FROM Chapter ch INNER JOIN Project proj " +
                "ON proj.id=ch.project.id " +
                "WHERE ch.id=:chapterId " +
                "AND proj.status=:projectStatus " +
                "AND ch.status=:chapterStatus",
            Chapter.class);
        return query.setParameter("chapterId", id)
                   .setParameter("chapterStatus", ChapterStatus.OCCUPIED)
                   .setParameter("projectStatus", ProjectStatus.IN_PROCESS)
                   .getSingleResult();
    }

    @Override
    public long getCountOfFreeChaptersByName(long contractorId, String chapterName, ProjectStatus projectStatus) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT (DISTINCT ch) " +
                "FROM Chapter ch LEFT JOIN Proposal  prop " +
                "ON ch.id=prop.chapter.id LEFT JOIN Project proj " +
                "ON proj.id=ch.project.id " +
                "WHERE ch.name=:chapterName " +
                "AND ch.status=:chapterStatus " +
                "AND proj.status=:projectStatus " +
                "AND ch.id NOT IN (SELECT prop.chapter.id FROM Proposal prop WHERE prop.contractor.id=:contractorId)",
            Long.class);
        return query.setParameter("chapterName", chapterName)
                   .setParameter("chapterStatus", ChapterStatus.FREE)
                   .setParameter("projectStatus", projectStatus)
                   .setParameter("contractorId", contractorId)
                   .getSingleResult();
    }

    @Override
    public List<Chapter> getFreeChapters(long contractorId, String chapterName, ProjectStatus projectStatus, int page, int count) {

        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT DISTINCT ch " +
                "FROM Chapter ch LEFT JOIN Proposal  prop " +
                "ON ch.id=prop.chapter.id " +
                "WHERE ch.name=:chapterName " +
                "AND ch.status=:chapterStatus " +
                "AND ch.project.status=:projectStatus " +
                "AND ch.id NOT IN (SELECT prop.chapter.id FROM Proposal prop WHERE prop.contractor.id=:contractorId) ",
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
    public List<String> getAllChapterNames() {

        TypedQuery<String> query = getEm().createQuery(
            "SELECT DISTINCT (ch.name) FROM Chapter ch ORDER BY ch.name ASC ",
            String.class);
        return query.getResultList();
    }

    @Override
    public Map<Chapter, Integer[]> getChaptersByProjectIdContractorId(long projectId, long contractorId) {

        Query queryWorkPrice = getEm().createQuery(
            "SELECT ch, SUM(calc.workPriceFact) " +
                "FROM Project proj INNER JOIN Chapter ch " +
                "ON proj.id=:projectId " +
                "AND ch.contractor.id=:contractorId " +
                "AND ch.project.id=proj.id LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id " +

                "GROUP BY ch " +
                "ORDER BY ch.name ASC");

        Query queryTransferSum = getEm().createQuery(
            "SELECT ch, SUM(tr.sum) " +
                "FROM Project proj INNER JOIN Chapter ch " +
                "ON proj.id=:projectId " +
                "AND ch.contractor.id=:contractorId " +
                "AND ch.project.id=proj.id LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id LEFT JOIN MoneyTransfer tr " +
                "ON tr.calculation.id=calc.id " +

                "GROUP BY ch " +
                "ORDER BY ch.name ASC");

        List<Query> queries = new ArrayList<>();
        queries.add(queryWorkPrice);
        queries.add(queryTransferSum);

        queries.forEach(query ->
                            query.setParameter("projectId", projectId)
                                .setParameter("contractorId", contractorId));

        Map<Chapter, Integer[]> map = new TreeMap<>(Comparator.comparing(Chapter::getName));

        List<Object[]> listWorkPrice = (List<Object[]>) queryWorkPrice.getResultList();
        List<Object[]> listTransferSum = (List<Object[]>) queryTransferSum.getResultList();

        listWorkPrice.forEach(res -> {
            Integer[] arr = new Integer[2];
            Arrays.fill(arr, ZERO_INT_VALUE);

            Chapter chapter = (Chapter) res[0];
            long workPrice = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;
            arr[0] = (int) workPrice;

            map.put(chapter, arr);
        });

        listTransferSum.forEach(res -> {
            Chapter chapter = (Chapter) res[0];
            long transferSum = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;
            map.get(chapter)[1] = (int) transferSum;
        });
        return map;
    }

    @Override
    public Map<Chapter, Integer[]> getChaptersByProjectId(long projectId) {

        Query queryWorkPrice = getEm().createQuery(
            "SELECT ch, SUM(calc.workPriceFact) " +
                "FROM Project proj INNER JOIN Chapter ch " +
                "ON ch.project.id=proj.id AND proj.id=:projectId LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id " +

                "GROUP BY ch " +
                "ORDER BY ch.name ASC");

        Query queryTransferSum = getEm().createQuery(
            "SELECT ch, SUM(tr.sum) " +
                "FROM Project proj INNER JOIN Chapter ch " +
                "ON ch.project.id=proj.id AND proj.id=:projectId LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id LEFT JOIN MoneyTransfer tr " +
                "ON tr.calculation.id=calc.id " +

                "GROUP BY ch " +
                "ORDER BY ch.name ASC");

        List<Query> queries = new ArrayList<>();
        queries.add(queryWorkPrice);
        queries.add(queryTransferSum);

        queries.forEach(query -> query.setParameter("projectId", projectId));

        Map<Chapter, Integer[]> map = new TreeMap<>(Comparator.comparing(Chapter::getName));

        List<Object[]> listWorkPrice = (List<Object[]>) queryWorkPrice.getResultList();
        List<Object[]> listTransferSum = (List<Object[]>) queryTransferSum.getResultList();

        listWorkPrice.forEach(res -> {
            Integer[] arr = new Integer[2];
            Arrays.fill(arr, ZERO_INT_VALUE);

            Chapter chapter = (Chapter) res[0];
            long workPrice = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;
            arr[0] = (int) workPrice;

            map.put(chapter, arr);
        });

        listTransferSum.forEach(res -> {
            Chapter chapter = (Chapter) res[0];
            long transferSum = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;
            map.get(chapter)[1] = (int) transferSum;
        });

        return map;
    }

    @Override
    public Map<Chapter, Integer[]> getChaptersByContractorIdAndDeveloperId(long developerId, long contractorId, ProjectStatus status, int page, int count) {

        Query queryWorkPrice = getEm().createQuery(
            "SELECT ch, SUM(calc.workPriceFact) " +
                "FROM Project proj INNER JOIN Chapter ch " +
                "ON ch.project.id=proj.id LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id " +

                "WHERE proj.developer.id=:developerId " +
                "AND ch.contractor.id=:contractorId " +
                "AND proj.status=:status " +

                "GROUP BY ch " +
                "ORDER BY proj.name, ch.name");

        Query queryTransferSum = getEm().createQuery(
            "SELECT ch, SUM(tr.sum) " +
                "FROM Project proj INNER JOIN Chapter ch " +
                "ON ch.project.id=proj.id LEFT JOIN Calculation calc " +
                "ON calc.chapter.id=ch.id LEFT JOIN MoneyTransfer tr " +
                "ON tr.calculation.id=calc.id " +

                "WHERE proj.developer.id=:developerId " +
                "AND ch.contractor.id=:contractorId " +
                "AND proj.status=:status " +

                "GROUP BY ch " +
                "ORDER BY proj.name, ch.name");

        List<Query> queries = new ArrayList<>();
        queries.add(queryWorkPrice);
        queries.add(queryTransferSum);

        queries.forEach(query -> query.setParameter("developerId", developerId)
                                     .setParameter("contractorId", contractorId)
                                     .setParameter("status", status)
                                     .setMaxResults(count)
                                     .setFirstResult((page - 1) * count));

        Map<Chapter, Integer[]> map = new TreeMap<>(Comparator.comparing(Chapter::getName));

        List<Object[]> listWorkPrice = (List<Object[]>) queryWorkPrice.getResultList();
        List<Object[]> listTransferSum = (List<Object[]>) queryTransferSum.getResultList();

        listWorkPrice.forEach(res -> {
            Integer[] arr = new Integer[2];
            Arrays.fill(arr, ZERO_INT_VALUE);

            Chapter chapter = (Chapter) res[0];
            long workPrice = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;
            arr[0] = (int) workPrice;

            map.put(chapter, arr);
        });

        listTransferSum.forEach(res -> {
            Chapter chapter = (Chapter) res[0];
            long transferSum = res[1] != null ? (long) res[1] : ZERO_LONG_VALUE;
            map.get(chapter)[1] = (int) transferSum;
        });

        return map;
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
    public long getCountOfChaptersByContractorIdAndDeveloperId(long developerId, long contractorId, ProjectStatus status) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT(ch) FROM Chapter ch LEFT JOIN Project proj " +
                "ON ch.project.id=proj.id " +
                "WHERE  proj.status=:status " +
                "AND ch.contractor.id=:contractorId " +
                "AND proj.developer.id=:developerId",
            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .setParameter("developerId", developerId)
                   .setParameter("status", status)
                   .getSingleResult();
    }

    @Override
    public List<Chapter> getAllChaptersByDeveloperIdContractorId(long developerId, long contractorId) throws NoResultException {

        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT ch FROM Chapter ch" +
                " WHERE ch.project.developer.id=:developerId " +
                "AND ch.contractor.id=:contractorId",
            Chapter.class);
        return query.setParameter("developerId", developerId)
                   .setParameter("contractorId", contractorId)
                   .getResultList();
    }

    @Override
    public List<Chapter> getChaptersByContractorId(long contractorId, int page, int count) throws NoResultException {

        TypedQuery<Chapter> query = getEm().createQuery(
            "SELECT ch FROM Chapter ch WHERE ch.contractor.id=:contractorId ",
            Chapter.class);
        return query.setParameter("contractorId", contractorId)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public long getCountOfChaptersByContractorId(long contractorId) throws NoResultException {

        TypedQuery<Long> query = getEm().createQuery(
            "SELECT COUNT (ch) FROM Chapter ch WHERE ch.contractor.id=:contractorId ",
            Long.class);
        return query.setParameter("contractorId", contractorId)
                   .getSingleResult();
    }

    @Override
    public void cancelChaptersByProjectId(long projectId) {

        Query query = getEm().createQuery(
            "UPDATE Chapter ch SET ch.status=:status WHERE ch.project.id=:projectId ");
        query.setParameter("projectId", projectId)
            .setParameter("status", ChapterStatus.CANCELED)
            .executeUpdate();
    }
}