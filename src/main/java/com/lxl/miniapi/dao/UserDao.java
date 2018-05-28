package com.lxl.miniapi.dao;


import com.lxl.miniapi.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: TODO
 * @author: lxl
 */
public interface UserDao {

    String getFirstUserNickName();

    int insertUser(User user);

    User getUserById(@Param("userId") long userId);

    List<User> getUsers();
}
