package it.academy.dao.impl;

import it.academy.dao.CalculationDao;
import it.academy.pojo.Calculation;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;

public class CalculationDaoImpl extends DaoImpl<Calculation, Long> implements CalculationDao {

    public CalculationDaoImpl() {

        super(Calculation.class);
    }

    @Override
    public List<Calculation> getCalculationsByChapterId(Long chapterId, int page, int count)
        throws IOException {

        TypedQuery<Calculation> query = getEm().createQuery(
            "SELECT ca FROM Calculation ca WHERE ca.chapter.id=:chapterId ORDER BY ca.month DESC",
            Calculation.class);
        return query.setParameter("chapterId", chapterId)
                   .setMaxResults(count)
                   .setFirstResult((page - 1) * count)
                   .getResultList();
    }

    @Override
    public Integer getCountOfCalculationsByChapterId(Long chapterId) throws NoResultException, IOException {

        TypedQuery<Integer> query = getEm().createQuery(
            "SELECT CONT(ca) FROM Calculation ca WHERE ca.chapter.id=:chapterId ",
            Integer.class);
        return query.setParameter("chapterId", chapterId)
                   .getSingleResult();
    }
}