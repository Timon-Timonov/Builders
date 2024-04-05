package it.academy.dao.impl;

import it.academy.dao.MoneyTransferDao;
import it.academy.pojo.MoneyTransfer;

public class MoneyTransferDaoImpl extends DaoImpl<MoneyTransfer, Long> implements MoneyTransferDao {

    public MoneyTransferDaoImpl() {

        super(MoneyTransfer.class);
    }
}
