package it.academy.dao.impl;

import it.academy.dao.UserDao;
import it.academy.pojo.User;

import javax.persistence.TypedQuery;

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
}
