package it.academy.dao;

import it.academy.pojo.MoneyTransfer;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;

public interface MoneyTransferDao extends Dao<MoneyTransfer, Long> {

    List<MoneyTransfer> getMoneyTransfersByCalculationId(Long calculationId) throws NoResultException, IOException;
}
