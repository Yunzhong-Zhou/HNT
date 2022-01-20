package com.app.hnt.view.chart;

import com.github.tifezh.kchartlib.chart.entity.IKLine;


/**
 * K线实体
 * Created by tifezh on 2016/5/16.
 */

public class KLineEntity implements IKLine {
    public String Date;
    public float Open;
    public float High;
    public float Low;
    public float Close;
    public float Volume;
    public float Amount;

    public float MA5Price;

    public float MA10Price;

    public float MA20Price;

    public float dea;

    public float dif;

    public float macd;

    public float k;

    public float d;

    public float j;

    public float rsi1;

    public float rsi2;

    public float rsi3;

    public float up;

    public float mb;

    public float dn;

    public float MA5Volume;

    public float MA10Volume;

    public String type;

    public KLineEntity(String date,
                       float open,
                       float high,
                       float low,
                       float close,
                       float volume,
                       float amount,
                       float MA5Price,
                       float MA10Price,
                       float MA20Price,
                       float dea,
                       float dif,
                       float macd,
                       float k,
                       float d,
                       float j,
                       float rsi1,
                       float rsi2,
                       float rsi3,
                       float up,
                       float mb,
                       float dn,
                       float MA5Volume,
                       float MA10Volume,
                       String type) {
        Date = date;
        Open = open;
        High = high;
        Low = low;
        Close = close;
        Volume = volume;
        Amount = amount;
        this.MA5Price = MA5Price;
        this.MA10Price = MA10Price;
        this.MA20Price = MA20Price;
        this.dea = dea;
        this.dif = dif;
        this.macd = macd;
        this.k = k;
        this.d = d;
        this.j = j;
        this.rsi1 = rsi1;
        this.rsi2 = rsi2;
        this.rsi3 = rsi3;
        this.up = up;
        this.mb = mb;
        this.dn = dn;
        this.MA5Volume = MA5Volume;
        this.MA10Volume = MA10Volume;
        this.type = type;
    }

    public String getDatetime() {
        return Date;
    }

    @Override
    public float getOpenPrice() {
        return Open;
    }

    @Override
    public float getHighPrice() {
        return High;
    }

    @Override
    public float getLowPrice() {
        return Low;
    }

    @Override
    public float getClosePrice() {
        return Close;
    }

    @Override
    public float getMA5Price() {
        return MA5Price;
    }

    @Override
    public float getMA10Price() {
        return MA10Price;
    }

    @Override
    public float getMA20Price() {
        return MA20Price;
    }

    @Override
    public float getAmount() {
        return Amount;
    }


    @Override
    public String getType() {
        return type;
    }

    @Override
    public float getDea() {
        return dea;
    }

    @Override
    public float getDif() {
        return dif;
    }

    @Override
    public float getMacd() {
        return macd;
    }

    @Override
    public float getK() {
        return k;
    }

    @Override
    public float getD() {
        return d;
    }

    @Override
    public float getJ() {
        return j;
    }

    @Override
    public float getRsi1() {
        return rsi1;
    }

    @Override
    public float getRsi2() {
        return rsi2;
    }

    @Override
    public float getRsi3() {
        return rsi3;
    }

    @Override
    public float getUp() {
        return up;
    }

    @Override
    public float getMb() {
        return mb;
    }

    @Override
    public float getDn() {
        return dn;
    }

    @Override
    public float getVolume() {
        return Volume;
    }

    @Override
    public float getMA5Volume() {
        return MA5Volume;
    }

    @Override
    public float getMA10Volume() {
        return MA10Volume;
    }

}
