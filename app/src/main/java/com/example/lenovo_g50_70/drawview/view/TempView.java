package com.example.lenovo_g50_70.drawview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.example.lenovo_g50_70.drawview.bean.TempPoint;
import com.example.lenovo_g50_70.drawview.bean.Temperature;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 气温曲线图
 * Created by lenovo-G50-70 on 2017/6/3.
 */

public class TempView extends View{
    private List<Temperature> mTemperatures=new ArrayList<>();
    private List<TempPoint> mPoints =new ArrayList<>();
    private Paint mPaint;
    private int mRadius=10; //气温图的点的半径
    private Path mMaxPath;  //高温曲线路径
    private Path mMinPath;  //低温曲线路径
    
    public TempView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initTempList();//初始化当天温度
        calPoint(); //初始化温度曲线的点
    }

    private void calPoint() {//初始化并计算每个点的位置
        for(int i=0;i<mTemperatures.size();i++){//一个点代表一天
            int x=50 +(i*150); //第一个点的X坐标为50，相邻两点距离150
            int minY =(40-(mTemperatures.get(i).getMinTemp()))*10;//初始化低温Y的位置
            int maxY =(40-(mTemperatures.get(i).getMaxTemp()))*10;//初始化高温Y的位置
            TempPoint point =new TempPoint(x,maxY,minY);
            mPoints.add(point);
        }

    }

    private void initTempList() {//初始化5天的天气，包含最高温和最低温
        Temperature t1 =new Temperature(12,26,new Date(2017,5,1));
        Temperature t2 =new Temperature(14,24,new Date(2017,5,2));
        Temperature t3 =new Temperature(15,29,new Date(2017,5,3));
        Temperature t4 =new Temperature(13,32,new Date(2017,5,4));
        Temperature t5 =new Temperature(16,28,new Date(2017,5,5));
        mTemperatures.add(t1);
        mTemperatures.add(t2);
        mTemperatures.add(t3);
        mTemperatures.add(t4);
        mTemperatures.add(t5);
        mTemperatures.add(t1);
        mTemperatures.add(t2);
        mTemperatures.add(t3);
        mTemperatures.add(t4);
        mTemperatures.add(t5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint =new Paint();    //新建画笔
        canvas.drawColor(Color.CYAN);   //画布北京颜色
        drawPoints(canvas);     //画点
        drawLines(canvas);      //画线
        super.onDraw(canvas);
    }

    private void drawLines(Canvas canvas) {
        mMaxPath =new Path();
        mMinPath =new Path();

        for(int i=0;i<mPoints.size();i++){
            TempPoint tp =mPoints.get(i);   //获取一个温度点
            if(i==0){//线是从左上角开始出发的，所以第一个点是移动位置
                mMaxPath.moveTo(tp.getX(),tp.getMaxY());
                mMinPath.moveTo(tp.getX(),tp.getMinY());
            }else {//其余的点都是连线
                mMaxPath.lineTo(tp.getX(), tp.getMaxY());
                mMinPath.lineTo(tp.getX(), tp.getMinY());
            }
        }
        mPaint.setStyle(Paint.Style.STROKE); //画笔设置不填充样式
        mPaint.setStrokeWidth(5); //画线的大小
        mPaint.setColor(Color.GREEN); //用绿色画低温曲线
        canvas.drawPath(mMinPath,mPaint);//画线
        mPaint.setColor(Color.RED); //用红色画高温曲线
        canvas.drawPath(mMaxPath,mPaint);
    }

    private void drawPoints(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);//画笔设置填充样式
        for(int i=0;i<mPoints.size();i++){
            TempPoint tp =mPoints.get(i);
            mPaint.setColor(Color.GREEN);//用绿色画低温曲线图
            //圆心X坐标，圆心Y坐标，圆半径，画笔，4参数
            canvas.drawCircle(tp.getX(),tp.getMinY(),mRadius,mPaint);
            mPaint.setColor(Color.RED);//用红色画高温曲线图
            canvas.drawCircle(tp.getX(),tp.getMaxY(),mRadius,mPaint);
        }
    }
}
