package com.example.lenovo_g50_70.drawview.bean;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * 保存手指所画的线
 * Created by lenovo-G50-70 on 2017/6/6.
 */

public class Drawpath {
    public Path mPath;

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint paint) {
        mPaint = paint;
    }

    public Path getPath() {
        return mPath;
    }

    public void setPath(Path path) {
        mPath = path;
    }

    public Paint mPaint;


}
