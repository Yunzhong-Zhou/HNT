package com.app.hnt.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zyz on 2019/6/4.
 */
public class Fragment2Model implements Serializable {
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
        private String title;
        private String earning_money;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getEarning_money() {
            return earning_money;
        }

        public void setEarning_money(String earning_money) {
            this.earning_money = earning_money;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}
