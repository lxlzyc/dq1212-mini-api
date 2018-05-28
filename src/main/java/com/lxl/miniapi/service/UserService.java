package com.lxl.miniapi.service;

import com.lxl.miniapi.dao.UserDao;
import com.lxl.miniapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: TODO
 * @author: lxl
 */
@Repository
public class UserService {

    @Autowired
    UserDao userDao;

    public String getFirstUserNickName() {
        return userDao.getFirstUserNickName();
    }

    public User getUserById(long userId) {
        return userDao.getUserById(userId);
    }

    public int insertUser(User user) {
        return userDao.insertUser(user);

    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }
}
