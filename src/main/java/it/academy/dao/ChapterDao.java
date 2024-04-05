package it.academy.dao;

import it.academy.pojo.Chapter;
import it.academy.pojo.enums.ChapterStatus;

import java.util.List;

public interface ChapterDao extends Dao<Chapter, Long> {

    List<String> getAllChapterNames();

    List<Chapter> getFreeChapters(String chapterName, int page, int count);

    List<Chapter> getChaptersByProjectIdContractorId(Long projectId, Long contractorId);

    List<Chapter> getChaptersByProjectId(Long projectId);

    List<Chapter> getChaptersByContractorId(Long contractorId, ChapterStatus status, int page, int count);
}
