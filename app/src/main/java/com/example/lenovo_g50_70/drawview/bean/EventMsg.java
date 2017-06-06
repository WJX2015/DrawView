package com.example.lenovo_g50_70.drawview.bean;

/**
 * Created by lenovo-G50-70 on 2017/6/1.
 */

public class EventMsg {


    public void setType(int type) {
        mType = type;
    }

    private int mType;

    public EventMsg(){}

    public EventMsg(int type){
        this.mType =type;
    }

    public int getType(){
        return mType;
    }
}
