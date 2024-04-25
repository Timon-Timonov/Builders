package it.academy.dao;

import it.academy.exceptions.EmailOccupaidException;
import it.academy.util.functionalInterfaces.TransactionObjectBody;
import it.academy.util.functionalInterfaces.TransactionVoidBody;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import java.io.IOException;
import java.util.List;

public interface Dao<T, R> {

    T get(R id) throws EntityNotFoundException;

    void update(T t);

    void delete(R id) throws EntityNotFoundException;

    void create(T t);

    void closeManager();

    void executeInOneTransaction(TransactionVoidBody body)
        throws IOException, EntityNotFoundException, NoResultException, ConstraintViolationException;

    Object executeInOneTransaction(TransactionObjectBody body)
        throws Exception;
}
