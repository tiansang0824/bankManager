package com.tian.service.serviceImpl;

import com.tian.dao.AtmDao;
import com.tian.domain.Atm;
import com.tian.service.AtmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("atmServiceImpl")
public class AtmServiceImpl implements AtmService {

    // 自动装配一个dao。
    @Autowired
    private AtmDao atmDao;

    @Override
    public Atm selectAtmInfo(int atmId) {
        return atmDao.selectAtmById(atmId);
    }

    @Override
    public void addBalance(int atmId, int addBalance) {
        // 获取atm对象以得到目前balance值。
        Atm atm = atmDao.selectAtmById(atmId);
        atmDao.editAtmBalance(atmId, atm.getAtmBalance() + addBalance); // 修改balance。
    }

    @Override
    public void reduceBalance(int atmId, int reduceBalance) {
        // 获取atm对象。
        Atm atm = atmDao.selectAtmById(atmId);
        // 修改值。
        atmDao.editAtmBalance(atmId, atm.getAtmBalance() - reduceBalance); // 修改balance。
    }
}
