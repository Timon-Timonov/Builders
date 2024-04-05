package it.academy.dao;

import it.academy.pojo.Calculation;

import java.util.List;

public interface CalculationDao extends Dao<Calculation, Long> {

    List<Calculation> getCalculationsByChapterId(Long chapterId, int page, int count);
}
