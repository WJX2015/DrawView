package com.example.lenovo_g50_70.drawview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by lenovo-G50-70 on 2017/5/4.
 */

public class MyView extends View {

    private Bitmap mBufferBitmap; //缓存Bitmap
    private Canvas mBufferCanvas; //缓冲画布

    private Paint mPaint;   //画笔
    private Path mPath;     //路径
    private WindowManager mWindowManager;//屏幕管理器
    private int mScreenWidth;            //屏幕宽度
    private int mScreenHeight;           //屏幕高度

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();
        mPaint =new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(10);
        mPath =new Path();//初始化路径
        //创建缓存Bitmap
        mBufferBitmap =Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        //创建缓冲画布
        mBufferCanvas =new Canvas();
        //将缓冲位图设置给缓冲画布
        mBufferCanvas.setBitmap(mBufferBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float dx =event.getX();
                float dy =event.getY();
                mPath.moveTo(dx,dy);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float mx =event.getX();
                float my =event.getY();
                mPath.lineTo(mx,my);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                float ux =event.getX();
                float uy=event.getY();
                mPath.lineTo(ux,uy);
                mBufferCanvas.drawPath(mPath,mPaint);
                invalidate();
                break;
        }
        //返回true表示处理了触摸事件
        //曾掉坑，这里返回super的event，代码没错，画不出图
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //实现双缓存,即在缓存画布画多次
        canvas.drawBitmap(mBufferBitmap,0,0,null);
        if (mPath != null) {
            // 实时更新显示路径
            canvas.drawPath(mPath, mPaint);
        }
    }

}
