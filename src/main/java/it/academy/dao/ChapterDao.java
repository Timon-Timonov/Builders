package it.academy.dao;

import it.academy.pojo.Chapter;
import it.academy.pojo.enums.ProjectStatus;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;

public interface ChapterDao extends Dao<Chapter, Long> {

    List<String> getAllChapterNames() throws NoResultException, IOException;

    List<Chapter> getFreeChapters(Long contractorId, String chapterName, ProjectStatus projectStatus, int page, int count) throws NoResultException, IOException;

    List<Chapter> getChaptersByProjectIdContractorId(Long projectId, Long contractorId) throws NoResultException, IOException;

    List<Chapter> getChaptersByProjectId(Long projectId) throws NoResultException, IOException;

    List<Chapter> getChaptersByContractorIdAndDeveloperId(Long developerId, Long contractorId, ProjectStatus status, int page, int count)
        throws NoResultException, IOException;

    Long getCountOfFreeChaptersByName(Long contractorId, String chapterName, ProjectStatus projectStatus) throws NoResultException, IOException;

    Long getCountOfChaptersByContractorIdAndDeveloperId(Long developerId, Long contractorId, ProjectStatus status)
        throws NoResultException, IOException;

    List<Chapter> getAllChaptersByDeveloperIdContractorId(Long developerId, Long contractorId) throws NoResultException, IOException;

    List<Chapter> getChaptersByContractorId(Long contractorId, int page, int count) throws NoResultException, IOException;

    Long getCountOfChaptersByContractorId(Long contractorId) throws NoResultException, IOException;
}
