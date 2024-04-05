package it.academy.dao.impl;

import it.academy.dao.UserDao;
import it.academy.pojo.User;

public class UserDaoImpl extends DaoImpl<User, Long> implements UserDao {

    public UserDaoImpl() {

        super(User.class);
    }

    @Override
    public User getUser(String email) {
        return null;
    }
}
