package com.example.lenovo_g50_70.drawview.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.lenovo_g50_70.drawview.R;
import com.example.lenovo_g50_70.drawview.bean.EventMsg;
import com.example.lenovo_g50_70.drawview.view.MyView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class MainActivity extends AppCompatActivity {

    private MyView mMyView; //自定义画板
    private FloatingActionButton fab;   //浮动按钮
    private Intent mIntent; //功能模块Activity

    private WindowManager mWindowManager;//屏幕管理器
    private int mScreenWidth;            //屏幕宽度
    private int mScreenHeight;           //屏幕高度

    private View mView;         //画笔大小视图
    private SeekBar mSeekBar;   //进度条
    private int size = 5;         //画笔初始化大小

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initEventBus();
    }

    private void initEventBus() {
        /**
         * 哪个页面用到EventBus
         * 就在哪个页面注册
         */
        EventBus.getDefault().register(this);
    }

    private void initViews() {
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();
        mMyView = (MyView) findViewById(R.id.myView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(MainActivity.this, DialogActivity.class);
                startActivity(mIntent);
            }
        });
    }

    @Subscribe
    public void onEventMainThread(EventMsg eventMsg) {

        switch (eventMsg.getType()) {
            //保存路径
            case 1:
                mMyView.savePath();
                break;
            //画笔大小
            case 2:
                dialogShow();
                break;
            //画笔颜色
            case 3:

                break;
            //画笔类型
            case 4:

                break;
            //撤销路径
            case 5:

                break;
            //恢复路径
            case 6:

                break;
            //清空画布
            case 7:

                break;
        }

        String msg = "onEventMainThread收到了消息：" + eventMsg.getType();

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 画笔调节
     */
    private void dialogShow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_show, null);

        //初始化画笔大小的视图
        mView = view.findViewById(R.id.seekView);

        //用于更新界面中mView的视图
        final ViewGroup.LayoutParams params = mView.getLayoutParams();
        params.height = size*2;
        mView.setLayoutParams(params);

        //初始化进度条
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
        mSeekBar.setMax(100);
        mSeekBar.setProgress(size);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 5) {
                    //画笔大小
                    size = progress;
                    //画笔视图大小
                    params.height = progress*2;
                    //设置画笔视图的大小
                    mView.setLayoutParams(params);
                } else {
                    params.height = size*2;
                    mView.setLayoutParams(params);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //对话框的显示
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("画笔粗细调节器");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMyView.setPaintSize(size);
                    }
                });

        builder.show().getWindow().setLayout((int) (mScreenWidth * 0.8), (int) (mScreenHeight * 0.5));
    }

}
