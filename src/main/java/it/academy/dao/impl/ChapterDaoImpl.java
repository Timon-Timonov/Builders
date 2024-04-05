package it.academy.dao.impl;

import it.academy.dao.ChapterDao;
import it.academy.pojo.Chapter;
import it.academy.pojo.enums.ChapterStatus;

import java.util.List;

public class ChapterDaoImpl extends DaoImpl<Chapter, Long> implements ChapterDao {

    public ChapterDaoImpl() {

        super(Chapter.class);
    }

    @Override
    public List<String> getAllChapterNames() {
        return null;
    }

    @Override
    public List<Chapter> getFreeChapters(String chapterName, int page, int count) {
        return null;
    }

    @Override
    public List<Chapter> getChaptersByProjectIdContractorId(Long projectId, Long contractorId) {
        return null;
    }

    @Override
    public List<Chapter> getChaptersByProjectId(Long projectId) {
        return null;
    }

    @Override
    public List<Chapter> getChaptersByContractorId(Long contractorId, ChapterStatus status, int page, int count) {
        return null;
    }
}