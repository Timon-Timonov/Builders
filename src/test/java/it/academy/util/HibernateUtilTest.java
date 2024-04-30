package it.academy.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class HibernateUtilTest {

    @Test
    void getEntityManager() {
        assertNotNull(HibernateUtil.getEntityManager());
    }
}