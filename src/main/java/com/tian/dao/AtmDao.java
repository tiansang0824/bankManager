package com.tian.dao;

import com.tian.domain.Atm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface AtmDao {
    @Select("select * from atms where atmid = #{atmid}")
    Atm selectAtmById (@Param("atmid") int atmId);

    @Update("update atms set atmbalance = #{atmBalance} where atmid = #{atmid}")
    void editAtmBalance(@Param("atmid")int atmId, @Param("atmBalance")int atmBalance);
}
