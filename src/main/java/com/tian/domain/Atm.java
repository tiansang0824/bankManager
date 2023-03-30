package com.tian.domain;

import org.springframework.stereotype.Component;

@Component("atm")
public class Atm {
    private int atmId;
    private int atmBalance;

    public Atm() {
    }

    public Atm(int atmId, int atmBalance) {
        this.atmId = atmId;
        this.atmBalance = atmBalance;
    }

    @Override
    public String toString() {
        return "Atm{" +
                "atmId=" + atmId +
                ", atmBalance=" + atmBalance +
                '}';
    }

    public int getAtmId() {
        return atmId;
    }

    public void setAtmId(int atmId) {
        this.atmId = atmId;
    }

    public int getAtmBalance() {
        return atmBalance;
    }

    public void setAtmBalance(int atmBalance) {
        this.atmBalance = atmBalance;
    }
}
