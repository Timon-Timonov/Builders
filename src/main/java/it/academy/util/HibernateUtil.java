package it.academy.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class HibernateUtil {

    private static EntityManagerFactory entityManagerFactory;

    private HibernateUtil() {
    }

    public static EntityManager getEntityManager() {

        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("builders");
        }
        return entityManagerFactory.createEntityManager();
    }
}
