package com.tian.service;

import com.tian.domain.Atm;

public interface AtmService {
    Atm selectAtmInfo (int Atmid);

    void addBalance(int atmId, int addBalance);

    void reduceBalance(int atmId, int reduceBalance);
}
