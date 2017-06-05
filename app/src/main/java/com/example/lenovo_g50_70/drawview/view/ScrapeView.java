package com.example.lenovo_g50_70.drawview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.lenovo_g50_70.drawview.R;

/**
 * Created by lenovo-G50-70 on 2017/6/5.
 */

public class ScrapeView extends View {
    //背景图层
    private Bitmap mBackground;
    //前景图层
    private Bitmap mForeground;
    //前景图层的画布
    private Canvas mForeCanvas;
    //模拟手指滑动刮开路径的画笔
    private Paint mPaint;
    //手指滑动的路径
    private Path mPath;
    //前景图的文字画笔
    private Paint mForePaint;
    //前景图的文字内容
    private String mForeContent="mForePaint";
    private WindowManager mWindowManager;//屏幕管理器
    private int mScreenWidth;            //屏幕宽度
    private int mScreenHeight;           //屏幕高度

    public ScrapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews() {
        //初始化屏幕管理器
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        //获取屏幕宽度
        mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();
        //获取屏幕高度
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();
        //初始化模拟手指滑动的画笔
        mPaint = new Paint();
        //设置画笔的透明度，当前完全透明,  [0,255]
        mPaint.setAlpha(0);
        //设置画笔的样式，当前为空心
        mPaint.setStyle(Paint.Style.STROKE);
        //设置画笔的大小
        mPaint.setStrokeWidth(50);
        //设置图形重叠时的处理方式  经常用来制作橡皮的擦除效果
        // 当前为交集模式，显示下层
        mPaint.setXfermode(new PorterDuffXfermode((PorterDuff.Mode.DST_IN)));
        //画笔接洽点类型 如影响矩形但角的外轮廓
        // Miter:结合处为锐角,Round:结合处为圆弧：BEVEL:结合处为直线。
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //画笔笔触风格 如影响画笔但始末端
        // 例子:用圆珠笔在纸上戳一点，这个点一定是个圆，它代表了笔的笔触形状
        // 如果我们把一支铅笔笔尖削成方形的，那么画出来的线条会是一条弯曲的“矩形”，这就是笔触的意思
        // 笔触类型：ROUND圆形 SQUARE正方形 BUTT
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //初始化手指滑动的路径
        mPath =new Path();
        //初始化背景图
        mBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        //初始化前景图
        mForeground=Bitmap.createBitmap(mScreenWidth,mScreenHeight, Bitmap.Config.ARGB_8888);
        //mForeground =BitmapFactory.decodeResource(getResources(),R.drawable.foreGround);
        //初始化前景画布,并画入前景图
        mForeCanvas =new Canvas(mForeground);
        //初始化内容画笔
        mForePaint =new Paint();
        //设置画笔颜色
        mForePaint.setColor(Color.WHITE);
        //设置文字大小
        mForePaint.setTextSize(100);
        //设置画笔大小
        mForePaint.setStrokeWidth(20);
        //设置前景图的背景颜色
        mForeCanvas.drawColor(Color.GRAY);
        //在前景图绘制文字
        mForeCanvas.drawText(mForeContent,mForeCanvas.getWidth()/4,mForeCanvas.getHeight()/2,mForePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //每次按下都重置路径
                mPath.reset();
                mPath.moveTo(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
                mPath.lineTo(event.getX(),event.getY());
                break;
        }
        //模拟手指刮除效果
        mForeCanvas.drawPath(mPath,mPaint);
        //刷新视图，重新绘制界面
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景图层
        canvas.drawBitmap(mBackground,0,0,null);
        //绘制前景图层
        canvas.drawBitmap(mForeground,0,0,null);
    }
}
