package com.app.hnt.model;

import java.util.List;

/**
 * Created by zyz on 2020/7/1.
 */
public class WebSocket_ListModel {

    /**
     * id : btcusdt
     * status : ok
     * ts : 1593575990173
     * rep : market.btcusdt.kline.1min
     * data : [{"id":1593575940,"open":9138.59,"close":9138.59,"low":9138.58,"high":9138.59,"amount":1.7547855312296536,"vol":16036.26155684,"count":81}]
     */

    private String id;
    private String status;
    private long ts;
    private String rep;
    private List<DataBean> data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1593575940
         * open : 9138.59
         * close : 9138.59
         * low : 9138.58
         * high : 9138.59
         * amount : 1.7547855312296536
         * vol : 16036.26155684
         * count : 81
         */

        private Long id;
        private double open;
        private double close;
        private double low;
        private double high;
        private double amount;
        private double vol;
        private int count;

        public DataBean(Long id, double open, double close, double low, double high, double amount, double vol, int count) {
            this.id = id;
            this.open = open;
            this.close = close;
            this.low = low;
            this.high = high;
            this.amount = amount;
            this.vol = vol;
            this.count = count;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public double getOpen() {
            return open;
        }

        public void setOpen(double open) {
            this.open = open;
        }

        public double getClose() {
            return close;
        }

        public void setClose(double close) {
            this.close = close;
        }

        public double getLow() {
            return low;
        }

        public void setLow(double low) {
            this.low = low;
        }

        public double getHigh() {
            return high;
        }

        public void setHigh(double high) {
            this.high = high;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getVol() {
            return vol;
        }

        public void setVol(double vol) {
            this.vol = vol;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
