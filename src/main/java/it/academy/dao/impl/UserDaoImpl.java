package it.academy.dao.impl;

import it.academy.dao.UserDao;
import it.academy.pojo.User;
import it.academy.pojo.enums.Roles;

import javax.persistence.TypedQuery;
import java.util.List;

public class UserDaoImpl extends DaoImpl<User, Long> implements UserDao {

    public UserDaoImpl() {

        super(User.class);
    }

    @Override
    public User getUser(String email) {

        TypedQuery<User> query = getEm().createQuery(
            "SELECT u FROM User u WHERE u.email =:email",
            User.class);
        return query.setParameter("email", email)
                   .getSingleResult();
    }

    @Override
    public List<User> getAdministrators() {

        TypedQuery<User> query = getEm().createQuery(
            "SELECT u FROM User u WHERE u.role=:role",
            User.class);
        return query.setParameter("role", Roles.ADMIN)
                   .getResultList();
    }
}
