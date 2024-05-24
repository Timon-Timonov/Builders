package it.academy.dao;

import it.academy.dto.Page;
import it.academy.dao.functionalInterfaces.*;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;

public interface Dao<T, R> {

    T get(R id) throws EntityNotFoundException;

    void update(T t);

    void delete(R id) throws EntityNotFoundException;

    void create(T t);

    void closeManager();

    void executeInOneVoidTransaction(TransactionVoidBody body)
        throws IOException, EntityNotFoundException, NoResultException, ConstraintViolationException;

    T executeInOneEntityTransaction(TransactionEntityBody<T> body)
        throws Exception;

    Page<T> executeInOnePageTransaction(TransactionPageBody<T> body)
        throws Exception;

    List<T> executeInOneListTransaction(TransactionListBody<T> body)
        throws Exception;

    boolean executeInOneBoolTransaction(TransactionBoolBody body)
        throws Exception;
}
