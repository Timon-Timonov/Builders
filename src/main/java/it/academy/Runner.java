package it.academy;

import it.academy.pojo.Address;
import it.academy.pojo.User;
import it.academy.pojo.enums.Roles;
import it.academy.pojo.legalEntities.Developer;
import it.academy.pojo.legalEntities.LegalEntity;
import it.academy.util.HibernateUtil;

import javax.persistence.EntityManager;

public class Runner {
    public static void main(String[] args) {

        EntityManager em = HibernateUtil.getEntityManager();

        User user = User.builder()
                        .email("asrfderfg1@mail12>ru")
                        .password("qwerty")
                        .role(Roles.DEVELOPER)
                        .build();

        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        User usreFromDB=em.find(User.class, 1L);


        LegalEntity dev = Developer.builder()
                              .name("OAO")
                              .user(usreFromDB)
                              .address(Address.builder()
                                           .city("grodno")
                                           .street("hhdgd")
                                           .building(35)
                                           .build())

                              .build();

        em.getTransaction().begin();
        em.persist(dev);
        em.getTransaction().commit();
        em.close();

        em = HibernateUtil.getEntityManager();

        System.out.println(em.find(User.class, 1L));
        em.close();
        HibernateUtil.closeFactory();
    }
}
