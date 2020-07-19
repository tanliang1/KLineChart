package com.xiaoxiong.flag.entity;

import com.xiaoxiong.flag.ui.KLineEntity;

public class PricePeriodResponseEntity  {
    private String date;//日期
    private String open; //开盘价
    private String close;//收盘价
    private String high ;//最高价
    private String low; //最低价;
    private String volume;//成交量;
    private String money;//日期
    //当unit为1d时，包含以下返回值:
    private String paused; //开盘价
    private String high_limit;//收盘价
    private String low_limit ;//最高价

    private String avg ;//

    private String pre_close ;//

    private String code = "" ;//

    //  当code为期货和期权时，包含以下返回值:

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPaused() {
        return paused;
    }

    public void setPaused(String paused) {
        this.paused = paused;
    }

    public String getHigh_limit() {
        return high_limit;
    }

    public void setHigh_limit(String high_limit) {
        this.high_limit = high_limit;
    }

    public String getLow_limit() {
        return low_limit;
    }

    public void setLow_limit(String low_limit) {
        this.low_limit = low_limit;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getPre_close() {
        return pre_close;
    }

    public void setPre_close(String pre_close) {
        this.pre_close = pre_close;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "PricePeriodResponseEntity{" +
                "date='" + date + '\'' +
                ", open='" + open + '\'' +
                ", close='" + close + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", volume='" + volume + '\'' +
                ", money='" + money + '\'' +
                ", paused='" + paused + '\'' +
                ", high_limit='" + high_limit + '\'' +
                ", low_limit='" + low_limit + '\'' +
                ", avg='" + avg + '\'' +
                ", pre_close='" + pre_close + '\'' +
                '}';
    }
}
