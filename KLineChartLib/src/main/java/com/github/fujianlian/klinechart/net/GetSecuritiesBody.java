package com.github.fujianlian.klinechart.net;

public class GetSecuritiesBody {
    private String method = "get_ticks";
    private String token = "5b6a9ba2b8f678b126667f2c0ec900b21e0d3f5f";
    private String code = "000001.XSHE";
    private String end_date = "2018-07-03";


    public void setMethod(String method) {
        this.method = method;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
