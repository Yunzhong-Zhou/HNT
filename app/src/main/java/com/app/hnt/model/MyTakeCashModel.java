package com.app.hnt.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zyz on 2018/2/10.
 */

public class MyTakeCashModel implements Serializable {
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String id;
        private String sn;
        private String input_money;
        private String service_charge_money;
        private String money;
        private String status_rejected_cause;
        private String verify_at;
        private String status;
        private String status_title;
        private String createdAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getInput_money() {
            return input_money;
        }

        public void setInput_money(String input_money) {
            this.input_money = input_money;
        }

        public String getService_charge_money() {
            return service_charge_money;
        }

        public void setService_charge_money(String service_charge_money) {
            this.service_charge_money = service_charge_money;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getStatus_rejected_cause() {
            return status_rejected_cause;
        }

        public void setStatus_rejected_cause(String status_rejected_cause) {
            this.status_rejected_cause = status_rejected_cause;
        }

        public String getVerify_at() {
            return verify_at;
        }

        public void setVerify_at(String verify_at) {
            this.verify_at = verify_at;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus_title() {
            return status_title;
        }

        public void setStatus_title(String status_title) {
            this.status_title = status_title;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}
