package it.academy.dao.impl;

import it.academy.dao.Dao;
import it.academy.dao.functionalInterfaces.*;
import it.academy.dto.Page;
import it.academy.util.HibernateUtil;
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
public class DaoImpl<E, ID> implements Dao<E, ID> {

    private final Class<E> clazz;
    private EntityManager em;

    protected DaoImpl(Class<E> clazz) {

        this.clazz = clazz;
    }

    @Override
    public E get(ID id) throws EntityNotFoundException {

        return getEm().find(clazz, id);
    }

    @Override
    public void update(E e) {

        getEm().merge(e);
    }

    @Override
    public void delete(ID id) throws EntityNotFoundException {

        Object rootEntity = getEm().getReference(clazz, id);
        getEm().remove(rootEntity);
    }

    @Override
    public void create(E e) {

        getEm().persist(e);
        log.info(CREATED_SUCCESSFUL + e);
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
    public E executeInOneEntityTransaction(TransactionEntityBody<E> body)
        throws Exception {

        E o;
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
    public Page<E> executeInOnePageTransaction(TransactionPageBody<E> body) throws Exception {

        Page<E> o;
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
    public List<E> executeInOneListTransaction(TransactionListBody<E> body) throws Exception {

        List<E> o;
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
