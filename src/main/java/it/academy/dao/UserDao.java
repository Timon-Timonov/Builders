package it.academy.dao;

import it.academy.pojo.User;

import javax.persistence.NoResultException;

public interface UserDao extends Dao<User, Long> {

    User getUser(String email) throws NoResultException;
}
