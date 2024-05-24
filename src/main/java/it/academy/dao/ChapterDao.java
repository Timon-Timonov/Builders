package it.academy.dao;

import it.academy.pojo.Chapter;
import it.academy.pojo.enums.ProjectStatus;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ChapterDao extends Dao<Chapter, Long> {

    Chapter getChapterForCalculation(long id) throws NoResultException, IOException;

    List<String> getAllChapterNames() throws NoResultException, IOException;

    List<Chapter> getFreeChapters(long contractorId, String chapterName, ProjectStatus projectStatus, int page, int count) throws NoResultException, IOException;

    Map<Chapter,Integer[]> getChaptersByProjectIdContractorId(long projectId, long contractorId) throws NoResultException, IOException;

    Map<Chapter,Integer[]> getChaptersByProjectId(long projectId) throws NoResultException, IOException;

    Map<Chapter,Integer[]> getChaptersByContractorIdAndDeveloperId(long developerId, long contractorId, ProjectStatus status, int page, int count)
        throws NoResultException, IOException;

    long getCountOfFreeChaptersByName(long contractorId, String chapterName, ProjectStatus projectStatus) throws NoResultException, IOException;

    long getCountOfChaptersByContractorIdAndDeveloperId(long developerId, long contractorId, ProjectStatus status)
        throws NoResultException, IOException;

    List<Chapter> getAllChaptersByDeveloperIdContractorId(long developerId, long contractorId) throws NoResultException, IOException;


    List<Chapter> getChaptersByContractorId(long contractorId, int page, int count) throws NoResultException, IOException;

    long getCountOfChaptersByContractorId(long contractorId) throws NoResultException, IOException;

    void cancelChaptersByProjectId(long projectId) throws IOException;
}
