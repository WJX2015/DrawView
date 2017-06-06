package com.example.lenovo_g50_70.drawview.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.lenovo_g50_70.drawview.R;
import com.example.lenovo_g50_70.drawview.bean.EventMsg;
import com.example.lenovo_g50_70.drawview.view.MyView;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class MainActivity extends AppCompatActivity {

    private MyView mMyView;             //自定义画板
    private FloatingActionButton fab;   //浮动按钮
    private Intent mIntent;             //功能模块Activity
    private LayoutInflater mInflater;   //动态加载布局

    private WindowManager mWindowManager;//屏幕管理器
    private int mScreenWidth;            //屏幕宽度
    private int mScreenHeight;           //屏幕高度

    private View mView;         //画笔大小视图
    private SeekBar mSeekBar;   //进度条
    private int size = 5;       //画笔初始化大小

    private RadioGroup mGroup;            //画笔样式选择
    private int mStyle=1;                 //画笔默认样式
    private int mId=R.id.radio_stroke;    //记录选中ID

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
        mInflater = LayoutInflater.from(this);
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
                mMyView.saveBitmap();
                break;
            //画笔大小
            case 2:
                dialogSize();
                break;
            //画笔颜色
            case 3:
                dialogColor();
                break;
            //画笔类型
            case 4:
                dialogStyle();
                break;
            //撤销路径
            case 5:
                mMyView.revoke();
                break;
            //恢复路径
            case 6:
                mMyView.recover();
                break;
            //清空画布
            case 7:
                mMyView.clear();
                break;
        }

        String msg = "onEventMainThread收到了消息：" + eventMsg.getType();

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 画笔颜色调节
     */
    private void dialogColor() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("画笔颜色调节器")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //Toast.makeText(MainActivity.this,"0x" + Integer.toHexString(selectedColor),Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("确定", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        mMyView.setPaintColor(selectedColor);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    /**
     * 画笔样式调节
     */
    private void dialogStyle() {
        View view = mInflater.inflate(R.layout.dialog_style, null);
        mGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        mGroup.check(mId);
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.radio_stroke:
                        mStyle =1;
                        mId=R.id.radio_stroke;
                        break;
                    case R.id.radio_fill:
                        mStyle =2;
                        mId=R.id.radio_fill;
                        break;
                    case R.id.radio_eraser:
                        mStyle =3;
                        mId=R.id.radio_eraser;
                        break;
                }
            }
        });

        //对话框的显示
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("画笔样式调节器");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMyView.setPaintStyle(mStyle);
                    }
                });

        builder.show().getWindow().setLayout((int) (mScreenWidth * 0.8), (int) (mScreenHeight * 0.5));
    }

    /**
     * 画笔粗细调节
     */
    private void dialogSize() {
        View view = mInflater.inflate(R.layout.dialog_size, null);

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
