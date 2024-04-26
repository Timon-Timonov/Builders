package it.academy.dao;

import it.academy.pojo.User;

import javax.persistence.NoResultException;
import java.util.List;

public interface UserDao extends Dao<User, Long> {

    User getUser(String email) throws NoResultException;

    List<User> getAdministrators();
}
