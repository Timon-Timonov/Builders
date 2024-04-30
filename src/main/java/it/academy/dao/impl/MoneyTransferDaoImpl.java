package it.academy.dao.impl;

import it.academy.dao.MoneyTransferDao;
import it.academy.pojo.MoneyTransfer;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;

public class MoneyTransferDaoImpl extends DaoImpl<MoneyTransfer, Long> implements MoneyTransferDao {

    public MoneyTransferDaoImpl() {

        super(MoneyTransfer.class);
    }

    @Override
    public List<MoneyTransfer> getMoneyTransfersByCalculationId(Long calculationId) throws NoResultException, IOException {

        TypedQuery<MoneyTransfer> query = getEm().createQuery("SELECT  mt from MoneyTransfer mt WHERE mt.calculation.id=:calculationId ",
            MoneyTransfer.class);
        return query.setParameter("calculationId", calculationId)
                   .getResultList();
    }
}
