package com.xiaoxiong.flag.picture;

public class KlinePoint {
    private int x;
    private int y;
    private int color;
    private int type;
    private float  k;
    private float  d;
    private float  j;
    private int acrossType = 0;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getK() {
        return k;
    }

    public void setK(float k) {
        this.k = k;
    }

    public float getD() {
        return d;
    }

    public void setD(float d) {
        this.d = d;
    }

    public float getJ() {
        return j;
    }

    public void setJ(float j) {
        this.j = j;
    }

    public int getAcrossType() {
        return acrossType;
    }

    public void setAcrossType(int acrossType) {
        this.acrossType = acrossType;
    }
}
