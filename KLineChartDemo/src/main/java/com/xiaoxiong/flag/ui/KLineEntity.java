package com.xiaoxiong.flag.ui;


import com.xiaoxiong.flag.ui.entity.IKLine;

/**
 * K线实体
 * Created by tifezh on 2016/5/16.
 */
public class KLineEntity implements IKLine {

    public String code;

    public String getDate() {
        return date;
    }

    @Override
    public float getOpenPrice() {
        return open;
    }

    @Override
    public float getHighPrice() {
        return high;
    }

    @Override
    public float getLowPrice() {
        return low;
    }

    @Override
    public float getClosePrice() {
        return close;
    }

    @Override
    public float getMa5Price() {
        return ma5Price;
    }

    @Override
    public float getMa10Price() {
        return ma10Price;
    }

    @Override
    public float getMa20Price() {
        return ma20Price;
    }

    @Override
    public float getMa30Price() {
        return ma30Price;
    }

    @Override
    public float getMa60Price() {
        return ma60Price;
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
    public float getR() {
        return r;
    }

    @Override
    public float getRsi() {
        return rsi;
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
        return volume;
    }

    @Override
    public float getMa5Volume() {
        return ma5Volume;
    }

    @Override
    public float getMa10Volume() {
        return ma10Volume;
    }

    public String date = "";
    public float open;
    public float high;
    public float low;
    public float close;
    public float volume;

    public float ma5Price;

    public float ma10Price;

    public float ma20Price;

    public float ma30Price;

    public float ma60Price;

    public float dea;

    public float dif;

    public float macd;

    public float k;

    public float d;

    public float j;

    public float r;

    public float rsi;

    public float up;

    public float mb;

    public float dn;

    public float ma5Volume;

    public float ma10Volume;

    public int acrossType = 0;


}
