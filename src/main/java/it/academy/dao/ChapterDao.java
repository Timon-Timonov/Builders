package it.academy.dao;

import it.academy.pojo.Chapter;
import it.academy.pojo.enums.ChapterStatus;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;

public interface ChapterDao extends Dao<Chapter, Long> {

    List<String> getAllChapterNames() throws NoResultException, IOException;

    List<Chapter> getFreeChapters(String chapterName, int page, int count) throws NoResultException, IOException;

    List<Chapter> getChaptersByProjectIdContractorId(Long projectId, Long contractorId) throws NoResultException, IOException;

    List<Chapter> getChaptersByProjectId(Long projectId) throws NoResultException, IOException;

    List<Chapter> getChaptersByContractorId(Long contractorId, ChapterStatus status, int page, int count)
        throws NoResultException, IOException;

    Long getCountOfFreeChaptersByName(String chapterName) throws NoResultException, IOException;

   Long getCountOfChaptersByContractorId(Long contractorId, ChapterStatus status)
        throws NoResultException, IOException;

    List<Chapter> getAllChaptersByDeveloperIdContractorId(Long developerId, Long contractorId) throws NoResultException, IOException;


}
