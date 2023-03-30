package com.tian.service;

import com.tian.domain.User;

public interface UserService {
    boolean ifLogIn(String username, String password);

    boolean addUser(User user);

    boolean delUser(int userId);

    void editUser(User user);

    User selectUserById(int userId);

    void addBalance(int atmId, int addBalance);

    void reduceBalance(int atmId, int reduceBalance);

    int getUserIdByNameAndPwd(String username, String password);
}
