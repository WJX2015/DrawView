package com.example.lenovo_g50_70.drawview.bean;

/**
 * Created by lenovo-G50-70 on 2017/6/1.
 */

public class EventMsg {
    private String Msg;

    public void setType(int type) {
        mType = type;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    private int mType;


    public EventMsg(){}

    public EventMsg(String Msg){
        this.Msg=Msg;
    }

    public String getMsg(){
        return Msg;
    }

    public int getType(){
        return mType;
    }
}
