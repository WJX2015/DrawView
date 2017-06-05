package com.example.lenovo_g50_70.drawview.bean;

/**
 * 气温曲线图的气温点
 * Created by lenovo-G50-70 on 2017/6/3.
 */

public class TempPoint {
    private int x;      //同一时刻，两曲线图的X坐标相同
    private int minY;   //低温曲线图的Y坐标
    private int maxY;   //高温曲线图的Y坐标

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public TempPoint(int x,int maxY,int minY){
        this.x=x;
        this.maxY=maxY;
        this.minY=minY;
    }
}
