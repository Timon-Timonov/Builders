package it.academy.dao.impl;

import it.academy.dao.Dao;
import it.academy.util.HibernateUtil;
import it.academy.util.functionalInterfaces.TransactionBody;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import java.io.IOException;
import java.util.List;

import static it.academy.util.Constants.SELECT_ALL_FROM_TABLE;
import static it.academy.util.Constants.SELECT_COUNT_FROM_TABLE;

@Log4j2
public class DaoImpl<T, R> implements Dao<T, R> {

    private final Class<T> clazz;
    private EntityManager em;

    protected DaoImpl(Class<T> clazz) {

        this.clazz = clazz;
    }

    @Override
    public List<T> getAll() {

        return getEm().createQuery(getAllQuery(), clazz).getResultList();
    }

    @Override
    public T get(R r) throws EntityNotFoundException {

        return getEm().find(clazz, r);
    }

    @Override
    public void update(T object) {

        getEm().merge(object);
    }

    @Override
    public void delete(R key) throws EntityNotFoundException {

        Object rootEntity = getEm().getReference(clazz, key);
        getEm().remove(rootEntity);
    }

    @Override
    public void create(T object) {

        getEm().persist(object);
        log.info("Created successful" + object);
    }


    @Override
    public long countOfEntitiesInBase() {

        String countQuery = String.format(SELECT_COUNT_FROM_TABLE, clazz.getSimpleName());
        return getEm().createQuery(countQuery, Long.class).getSingleResult();
    }

    @Override
    public void executeInOneTransaction(TransactionBody body)
        throws RollbackException, IOException, EntityNotFoundException, NoResultException, ConstraintViolationException {

        try {
            getEm().getTransaction().begin();
            body.execute();
            getEm().getTransaction().commit();
        } finally {
            closeManager();
        }
    }


    protected EntityManager getEm() {

        if (em == null || !em.isOpen()) {
            em = HibernateUtil.getEntityManager();
        }
        return em;
    }

    @Override
    public void closeManager() {

        if (em == null || !em.isOpen()) {
            return;
        }
        em.close();
    }

    protected String getAllQuery() {

        return String.format(SELECT_ALL_FROM_TABLE, clazz.getSimpleName());
    }
}
