package it.academy.dao;

import it.academy.util.functionalInterfaces.TransactionBody;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface Dao<T, R> {

    List<T> getAll();

    T get(R id) throws EntityNotFoundException;

    void update(T t);

    void delete(R id) throws EntityNotFoundException;

    void create(T t);

    void closeManager();

    long countOfEntitiesInBase();

    void executeInOneTransaction(TransactionBody body) throws Exception;
}
