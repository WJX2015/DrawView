package com.example.lenovo_g50_70.drawview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lenovo-G50-70 on 2017/5/21.
 */

public class YourView extends View {
    private final int screenWidth;
    private final int screenHeight;

    private Paint mPaint;
    private Path mPath;
    private Canvas mBufferCanvas;
    private Bitmap mBufferBitmap;

    public YourView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ininP();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

    }

    private void ininP() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                float dx = event.getX();
                float dy = event.getY();
                mPath.moveTo(dx, dy);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                float mx = event.getX();
                float my = event.getY();
                mPath.lineTo(mx, my);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                float ux = event.getX();
                float uy = event.getY();
                mPath.lineTo(ux, uy);
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mPath, mPaint);
        //1. 创建缓冲画布
        mBufferCanvas = new Canvas();
        //2. 创建缓存Bitmap
        mBufferBitmap = Bitmap.createBitmap(screenWidth,screenHeight, Bitmap.Config.ARGB_8888);
        //3. 将缓冲位图设置给缓冲画布
        mBufferCanvas.setBitmap(mBufferBitmap);
        //5. 用屏幕的画布绘制缓冲位图
        canvas.drawBitmap(mBufferBitmap,0,0, null);
    }
}
