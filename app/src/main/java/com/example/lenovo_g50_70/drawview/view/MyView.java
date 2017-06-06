package com.example.lenovo_g50_70.drawview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.lenovo_g50_70.drawview.bean.Drawpath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

    private List<Drawpath> mSavepaths;  //保存的路径
    private List<Drawpath> mDelpaths;   //删除的路径
    private Drawpath mDrawpath;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initCanvas();
    }

    /**
     * 初始化画板相关
     */
    private void initCanvas() {
        mPaint = new Paint();
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(10);
        mPath = new Path();//初始化路径
        //创建缓存Bitmap
        mBufferBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        //创建缓冲画布
        mBufferCanvas = new Canvas();
        //将缓冲位图设置给缓冲画布
        mBufferCanvas.setBitmap(mBufferBitmap);
        //铺垫橡皮擦的效果
        mBufferCanvas.drawColor(Color.WHITE);
    }

    /**
     * 初始化内容
     */
    private void initView() {
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();

        mSavepaths = new ArrayList<>();
        mDelpaths = new ArrayList<>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //每次都创建新的保存  mPath.reset无法满足撤销
                mPath = new Path();//填坑
                mDrawpath = new Drawpath();
                mDrawpath.setPaint(mPaint);
                mDrawpath.setPath(mPath);
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
                mBufferCanvas.drawPath(mPath, mPaint);
                mSavepaths.add(mDrawpath);
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
        canvas.drawBitmap(mBufferBitmap, 0, 0, null);
        if (mPath != null) {
            // 实时更新显示路径
            canvas.drawPath(mPath, mPaint);
        }
    }

    /**
     * 保存画出的路径
     *
     * @return 画线的保存位置
     */
    public void savePath() {
        //格式化时间的对象
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        //获取当前时间
        Date curDate = new Date(System.currentTimeMillis());
        //格式化当前时间,并作为文件名保存到手机
        String curTimeFile = formatter.format(curDate) + ".png";
        //创建文件路径
        File extBaseDir = Environment.getExternalStorageDirectory();
        //创建文件，保存画线
        File file = new File(extBaseDir.getAbsolutePath(), curTimeFile);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            //压缩图片的方法，100表示不压缩，0表示压缩100%
            mBufferBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            MediaStore.Images.Media.insertImage(getContext().getContentResolver(), mBufferBitmap, "title", "description");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 画板保存到图库
     */
    public void saveBitmap() {
        MediaStore.Images.Media.insertImage(getContext().getContentResolver(), getChartBitmap(), "title", "description");
        Toast.makeText(getContext(), "图片保存", Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置画笔粗细
     *
     * @param size
     */
    public void setPaintSize(int size) {
        mPaint.setStrokeWidth(size);
        Toast.makeText(getContext(), "画笔大小调节", Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置画笔颜色
     *
     * @param color
     */
    public void setPaintColor(int color) {
        mPaint.setColor(color);
        Toast.makeText(getContext(), "画笔颜色调节", Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置画笔样式
     *
     * @param style
     */
    public void setPaintStyle(int style) {
        switch (style) {
            case 1:
                //描边
                mPaint.setStyle(Paint.Style.STROKE);
                break;
            case 2:
                //填充并描边
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                break;
            case 3:
                //橡皮擦
                mPaint.setColor(Color.WHITE);
                break;
        }
        Toast.makeText(getContext(), "画笔样式设置", Toast.LENGTH_SHORT).show();
    }

    /**
     * 撤销的核心思想就是将画布清空
     * 将保存下来的Path路径最后一个移除掉
     * 重新将路径画在画布上面。
     */
    public void revoke() {
        //撤销的前提是画板上最少有一条线
        if (mSavepaths != null && mSavepaths.size() > 0) {
            //重新初始化以清空画布
            initCanvas();
            //获取mSavepaths的最后一个元素
            Drawpath path = mSavepaths.get(mSavepaths.size() - 1);
            //添加到删除列
            mDelpaths.add(path);
            //从保存列删除
            mSavepaths.remove(mSavepaths.size() - 1);
            //重复保存
            Iterator<Drawpath> iter = mSavepaths.iterator();
            //讲保存列中的路径重绘到画布
            while (iter.hasNext()) {
                Drawpath dp = iter.next();
                mBufferCanvas.drawPath(dp.getPath(), dp.getPaint());
            }
            //调用onDraw方法
            invalidate();
            Toast.makeText(getContext(), "撤销路径", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 恢复路径,撤销的相反
     */
    public void recover() {
        //判断是否有撤销的路径
        if (mDelpaths.size() > 0) {
            //获取最后撤销的路径
            Drawpath dp = mDelpaths.get(mDelpaths.size() - 1);
            //重新保存
            mSavepaths.add(dp);
            mBufferCanvas.drawPath(dp.getPath(), dp.getPaint());
            //从删除列移除
            mDelpaths.remove(mDelpaths.size() - 1);
            //重绘视图
            invalidate();
            Toast.makeText(getContext(), "恢复路径", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 清空画布
     */
    public void clear() {
        initCanvas();
        mSavepaths.clear();
        mDelpaths.clear();
        //视图重绘
        invalidate();
        Toast.makeText(getContext(), "清空画布", Toast.LENGTH_SHORT).show();
    }

    /**
     * 自定义View转换Bitmap
     * 原理:画出画布的背景
     *
     * @return
     */
    public Bitmap getChartBitmap() {
        // 创建一个bitmap 根据我们自定义view的大小
        Bitmap returnedBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
        // 绑定canvas
        Canvas canvas = new Canvas(returnedBitmap);
        // 获取视图的背景
        Drawable bgDrawable = getBackground();
        if (bgDrawable != null)
            // 如果有就绘制
            bgDrawable.draw(canvas);
        else
            // 没有就绘制白色
            canvas.drawColor(Color.WHITE);
        // 绘制
        draw(canvas);
        return returnedBitmap;
    }
}
