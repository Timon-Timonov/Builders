package it.academy.util.functionalInterfaces;

import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.RoleException;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.io.IOException;

@FunctionalInterface
public interface TransactionEntityBody<T> {

    T execute() throws Exception;
}
