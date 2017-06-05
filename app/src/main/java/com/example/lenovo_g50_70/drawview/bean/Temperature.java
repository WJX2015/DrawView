package com.example.lenovo_g50_70.drawview.bean;

import java.util.Date;

import static android.R.attr.startX;

/**
 * Created by lenovo-G50-70 on 2017/6/3.
 */

public class Temperature {
    private int maxTemp;    //当天最高气温
    private int minTemp;    //当天最低气温
    private Date mDate;     //当天日期

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(Date startX) {
        this.mDate = startX;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Temperature(int maxTemp,int minTemp,Date startX){
        this.maxTemp=maxTemp;
        this.minTemp=minTemp;
        this.mDate=startX;
    }


}
