package it.academy.dao.functionalInterfaces;

import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.io.IOException;

@FunctionalInterface
public interface TransactionVoidBody {

    void execute() throws IOException, EntityNotFoundException, NoResultException, ConstraintViolationException;
}
