package com.tian.dao;

import com.tian.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface UserDao {
    void adduser(User user);

    void delUser(int userId);

    @Select("select * from users where userid = #{userid}")
    User selectUserById(@Param("userid") int userId);

    void editUserInfoById(User user);

    @Update("update users set balance = #{balance} where userid = #{userid}")
    void editBalance(@Param("userid") int userId, @Param("balance") int balance);


    @Select("select count(*) from users where username=#{username} and password=#{password}")
    int selectByNameAndPwd (@Param("username")String username, @Param("password")String password);

    @Select("select userid from users where username=#{username} and password=#{password}")
    int getUserIdByNameAndPwd(@Param("username")String username, @Param("password")String password);
}
