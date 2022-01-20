package com.app.hnt.model;

import java.io.Serializable;

/**
 * Created by zyz on 2018/2/9.
 */

public class AvailableAmountModel implements Serializable {
    /**
     * ofc_wallet_addr : sfgddfghhhhgfddssfggh
     * ofc_min_withdrawal_money : 10
     * ofc_max_withdrawal_money : 100000
     * ofc_usable_money : 0.00
     * usdt_wallet_addr :
     * common_usable_money : 88895.01
     * withdrawal_service_charge : 1
     * min_withdrawal_money : 10
     * max_withdrawal_money : 100000
     * trade_password : e10adc3949ba59abbe56e057f20f883e
     * hk : 81ea5f6202aba9f86bb6b0711c49dbe8
     */

    private String ofc_wallet_addr;
    private String ofc_min_withdrawal_money;
    private String ofc_max_withdrawal_money;
    private String ofc_usable_money;
    private String usdt_wallet_addr;
    private String common_usable_money;
    private String withdrawal_service_charge;
    private String min_withdrawal_money;
    private String max_withdrawal_money;
    private String trade_password;
    private String hk;

    public String getOfc_wallet_addr() {
        return ofc_wallet_addr;
    }

    public void setOfc_wallet_addr(String ofc_wallet_addr) {
        this.ofc_wallet_addr = ofc_wallet_addr;
    }

    public String getOfc_min_withdrawal_money() {
        return ofc_min_withdrawal_money;
    }

    public void setOfc_min_withdrawal_money(String ofc_min_withdrawal_money) {
        this.ofc_min_withdrawal_money = ofc_min_withdrawal_money;
    }

    public String getOfc_max_withdrawal_money() {
        return ofc_max_withdrawal_money;
    }

    public void setOfc_max_withdrawal_money(String ofc_max_withdrawal_money) {
        this.ofc_max_withdrawal_money = ofc_max_withdrawal_money;
    }

    public String getOfc_usable_money() {
        return ofc_usable_money;
    }

    public void setOfc_usable_money(String ofc_usable_money) {
        this.ofc_usable_money = ofc_usable_money;
    }

    public String getUsdt_wallet_addr() {
        return usdt_wallet_addr;
    }

    public void setUsdt_wallet_addr(String usdt_wallet_addr) {
        this.usdt_wallet_addr = usdt_wallet_addr;
    }

    public String getCommon_usable_money() {
        return common_usable_money;
    }

    public void setCommon_usable_money(String common_usable_money) {
        this.common_usable_money = common_usable_money;
    }

    public String getWithdrawal_service_charge() {
        return withdrawal_service_charge;
    }

    public void setWithdrawal_service_charge(String withdrawal_service_charge) {
        this.withdrawal_service_charge = withdrawal_service_charge;
    }

    public String getMin_withdrawal_money() {
        return min_withdrawal_money;
    }

    public void setMin_withdrawal_money(String min_withdrawal_money) {
        this.min_withdrawal_money = min_withdrawal_money;
    }

    public String getMax_withdrawal_money() {
        return max_withdrawal_money;
    }

    public void setMax_withdrawal_money(String max_withdrawal_money) {
        this.max_withdrawal_money = max_withdrawal_money;
    }

    public String getTrade_password() {
        return trade_password;
    }

    public void setTrade_password(String trade_password) {
        this.trade_password = trade_password;
    }

    public String getHk() {
        return hk;
    }

    public void setHk(String hk) {
        this.hk = hk;
    }
}
