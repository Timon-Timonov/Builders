package it.academy.dao.impl;

import it.academy.dao.Dao;
import it.academy.service.dto.Page;
import it.academy.util.HibernateUtil;
import it.academy.util.functionalInterfaces.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import java.io.IOException;
import java.util.List;

import static it.academy.util.constants.Messages.CREATED_SUCCESSFUL;

@Log4j2
public class DaoImpl<T, R> implements Dao<T, R> {

    private final Class<T> clazz;
    private EntityManager em;

    protected DaoImpl(Class<T> clazz) {

        this.clazz = clazz;
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
        log.info(CREATED_SUCCESSFUL + object);
    }

    @Override
    public void executeInOneVoidTransaction(TransactionVoidBody body)
        throws RollbackException, IOException, EntityNotFoundException, NoResultException, ConstraintViolationException {

        try {
            getEm().getTransaction().begin();
            body.execute();
            getEm().getTransaction().commit();
        } catch (Exception e) {
            getEm().getTransaction().rollback();
            throw e;
        } finally {
            closeManager();
        }
    }

    @Override
    public T executeInOneEntityTransaction(TransactionEntityBody<T> body)
        throws Exception {

        T o;
        try {
            getEm().getTransaction().begin();
            o = body.execute();
            getEm().getTransaction().commit();
        } catch (Exception e) {
            getEm().getTransaction().rollback();
            throw e;
        } finally {
            closeManager();
        }
        return o;
    }

    @Override
    public Page<T> executeInOnePageTransaction(TransactionPageBody<T> body) throws Exception {

        Page<T> o;
        try {
            getEm().getTransaction().begin();
            o = body.execute();
            getEm().getTransaction().commit();
        } catch (Exception e) {
            getEm().getTransaction().rollback();
            throw e;
        } finally {
            closeManager();
        }
        return o;
    }

    @Override
    public List<T> executeInOneListTransaction(TransactionListBody<T> body) throws Exception {

        List<T> o;
        try {
            getEm().getTransaction().begin();
            o = body.execute();
            getEm().getTransaction().commit();
        } catch (Exception e) {
            getEm().getTransaction().rollback();
            throw e;
        } finally {
            closeManager();
        }
        return o;
    }

    @Override
    public boolean executeInOneBoolTransaction(TransactionBoolBody body) throws Exception {

        boolean o;
        try {
            getEm().getTransaction().begin();
            o = body.execute();
            getEm().getTransaction().commit();
        } catch (Exception e) {
            getEm().getTransaction().rollback();
            throw e;
        } finally {
            closeManager();
        }
        return o;
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
}
