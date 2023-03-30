package com.tian.service.serviceImpl;

import com.tian.dao.UserDao;
import com.tian.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userServiceImpl")
public class UserServiceImpl implements com.tian.service.UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public boolean ifLogIn(String username, String password) {
        // 通过username和password匹配检查是否存在对应用户。
        int i = userDao.selectByNameAndPwd(username, password);
        return i != 0; // 返回查询结果。
    }

    /**
     * 通过id查询单个user对象
     * @param userId userid
     * @return 返回user对象
     */
    @Override
    public User selectUserById(int userId) {
        return userDao.selectUserById(userId);
    }

    @Override
    public void addBalance(int userId, int addBalance) {
        // 获取atm对象以得到目前balance值。
        User user = userDao.selectUserById(userId);
        // 修改balance。
        userDao.editBalance(userId, user.getBalance() + addBalance);
    }

    @Override
    public void reduceBalance(int userId, int reduceBalance) {
        // 获取atm对象。
        User user = userDao.selectUserById(userId);
        // 修改值。
        userDao.editBalance(userId, user.getBalance() - reduceBalance); // 修改balance。
    }

    @Override
    public int getUserIdByNameAndPwd(String username, String password) {
        return userDao.getUserIdByNameAndPwd(username, password);
    }


    // 下面的函数都不需要了////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean addUser(User user) {
        return false;
    }

    @Override
    public boolean delUser(int userId) {

        return false;
    }

    @Override
    public void editUser(User user) {

    }


}
