package it.academy.dao.impl;

import it.academy.dao.CalculationDao;
import it.academy.pojo.Calculation;

import java.util.List;

public class CalculationDaoImpl extends DaoImpl<Calculation, Long> implements CalculationDao {

    public CalculationDaoImpl() {

        super(Calculation.class);
    }

    @Override
    public List<Calculation> getCalculationsByChapterId(Long chapterId, int page, int count) {
        return null;
    }
}