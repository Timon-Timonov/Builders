package it.academy.dao;

import it.academy.pojo.Calculation;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CalculationDao extends Dao<Calculation, Long> {

    Map<Calculation, Integer[]> getCalculationsByChapterId(Long chapterId, int page, int count) throws NoResultException, IOException;

    Long getCountOfCalculationsByChapterId(Long chapterId) throws NoResultException, IOException;

    int updateWorkPriceFact(Integer workPriceFact, Long calculationId);
}
