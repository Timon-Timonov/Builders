package it.academy.dao;

import it.academy.pojo.User;

public interface UserDao extends Dao<User, Long> {

    User getUser(String email);
}
