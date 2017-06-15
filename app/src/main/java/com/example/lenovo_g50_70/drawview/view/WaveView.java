package com.example.lenovo_g50_70.drawview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by lenovo-G50-70 on 2017/6/12.
 */

public class WaveView extends View implements View.OnClickListener{

    //波浪画笔
    private Paint mWavePaint;
    //测试红点画笔
    private Paint mRedPaint;

    //波浪路径
    private Path mWavePath;
    //一个波浪长度
    private int mWaveLength=1000;
    //波浪个数
    private int mWaveCount;
    //平移偏移量
    private int mOffset;
    //波纹的中间轴
    private int mCenterY;

    //屏幕宽度
    private int mScreeenWidth;
    //屏幕高度
    private int mScreenHeight;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mWavePath=new Path();
        //黑体
        mWavePaint =new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置浅灰色
        mWavePaint.setColor(Color.LTGRAY);
        //填充和画边
        mWavePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        setOnClickListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreeenWidth=w;
        mScreenHeight=h;
        //加1.5，至少保证波纹有2个，至少2个才能实现平移效果
        mWaveCount = (int) Math.round(mScreeenWidth/mWaveLength+1.5);
        mCenterY =mScreenHeight/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWavePath.reset();
        //移动到屏幕外最左边
        mWavePath.moveTo(-mWaveLength+mOffset,mCenterY);
        for(int i=0;i<mWaveCount;i++){
            //正弦曲线
            mWavePath.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY + 60, (-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY);
            mWavePath.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY - 60, i * mWaveLength + mOffset, mCenterY);

            //测试红点
            canvas.drawCircle((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY + 60, 5, mRedPaint);
            canvas.drawCircle((-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY, 5, mRedPaint);
            canvas.drawCircle((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY - 60, 5, mRedPaint);
            canvas.drawCircle(i * mWaveLength + mOffset, mCenterY, 5, mRedPaint);
        }

        //填充矩形
        mWavePath.lineTo(mScreeenWidth,mScreenHeight);
        mWavePath.lineTo(0,mScreenHeight);
        mWavePath.close();
        canvas.drawPath(mWavePath,mWavePaint);
    }

    @Override
    public void onClick(View v) {
        ValueAnimator animator = ValueAnimator.ofInt(0, mWaveLength);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }
}
