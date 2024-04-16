package it.academy.util.functionalInterfaces;

import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.io.IOException;

@FunctionalInterface
public interface TransactionObjectBody {

    Object execute() throws IOException, EntityNotFoundException, NoResultException, ConstraintViolationException;
}
