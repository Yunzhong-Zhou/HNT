package com.app.hnt.model;

import java.io.Serializable;

/**
 * Created by zyz on 2020/6/28.
 */
public class WebSocketModel implements Serializable {

    /**
     * ch : market.btcusdt.kline.1min
     * ts : 1593313154425
     * tick : {"id":1593313140,"open":8980.52,"close":8980.49,"low":8980.48,"high":8980.52,"amount":1.982793,"vol":17806.45301476,"count":68}
     */

    private String ch;
    private long ts;
    private TickBean tick;

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public TickBean getTick() {
        return tick;
    }

    public void setTick(TickBean tick) {
        this.tick = tick;
    }

    public static class TickBean {
        /**
         * id : 1593313140
         * open : 8980.52
         * close : 8980.49
         * low : 8980.48
         * high : 8980.52
         * amount : 1.982793
         * vol : 17806.45301476
         * count : 68
         */

        private Long id;
        private double open;
        private double close;
        private double low;
        private double high;
        private double amount;
        private double vol;
        private int count;

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
