package it.academy.util.functionalInterfaces;

import it.academy.exceptions.EmailOccupaidException;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.io.IOException;

@FunctionalInterface
public interface TransactionBody {

    void execute() throws IOException, EntityNotFoundException, NoResultException, ConstraintViolationException;
}
