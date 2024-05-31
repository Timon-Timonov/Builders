package it.academy.dao;

import it.academy.dao.functionalInterfaces.*;
import it.academy.dto.Page;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;

public interface Dao<E, ID> {

    E get(ID id) throws EntityNotFoundException;

    void update(E e);

    void delete(ID id) throws EntityNotFoundException;

    void create(E e);

    void closeManager();

    void executeInOneVoidTransaction(TransactionVoidBody body)
        throws IOException, EntityNotFoundException, NoResultException, ConstraintViolationException;

    E executeInOneEntityTransaction(TransactionEntityBody<E> body)
        throws Exception;

    Page<E> executeInOnePageTransaction(TransactionPageBody<E> body)
        throws Exception;

    List<E> executeInOneListTransaction(TransactionListBody<E> body)
        throws Exception;

    boolean executeInOneBoolTransaction(TransactionBoolBody body)
        throws Exception;
}
