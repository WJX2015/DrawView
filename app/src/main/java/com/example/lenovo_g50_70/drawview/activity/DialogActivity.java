package com.example.lenovo_g50_70.drawview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.lenovo_g50_70.drawview.R;
import com.example.lenovo_g50_70.drawview.bean.EventMsg;

import de.greenrobot.event.EventBus;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mSaveText;     //保存路径
    private TextView mSizeText;     //画笔大小
    private TextView mColorText;    //画笔颜色
    private TextView mClearText;    //清空画布
    private TextView mTypeText;     //画笔类型
    private TextView mCancelText;   //撤销路径
    private TextView mRecoveryText; //恢复路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        initViews();
    }

    private void initViews() {
        //初始化绑定
        mSaveText = (TextView) findViewById(R.id.tv_save);
        mSizeText = (TextView) findViewById(R.id.tv_size);
        mColorText = (TextView) findViewById(R.id.tv_color);
        mClearText = (TextView) findViewById(R.id.tv_clear);
        mTypeText = (TextView) findViewById(R.id.tv_type);
        mCancelText = (TextView) findViewById(R.id.tv_cancel);
        mRecoveryText = (TextView) findViewById(R.id.tv_recovery);
        //设置点击事件
        mSaveText.setOnClickListener(this);
        mSizeText.setOnClickListener(this);
        mColorText.setOnClickListener(this);
        mClearText.setOnClickListener(this);
        mTypeText.setOnClickListener(this);
        mCancelText.setOnClickListener(this);
        mRecoveryText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EventMsg eventMsg = new EventMsg();
        switch (v.getId()){
            case R.id.tv_save:
                eventMsg.setType(1);
                EventBus.getDefault().post(eventMsg);
                break;
            case R.id.tv_size:
                eventMsg.setType(2);
                EventBus.getDefault().post(eventMsg);
                break;
            case R.id.tv_color:
                eventMsg.setType(3);
                EventBus.getDefault().post(eventMsg);
                break;
            case R.id.tv_clear:
                eventMsg.setType(4);
                EventBus.getDefault().post(eventMsg);
                break;
            case R.id.tv_cancel:
                eventMsg.setType(5);
                EventBus.getDefault().post(eventMsg);
                break;
            case R.id.tv_recovery:
                eventMsg.setType(6);
                EventBus.getDefault().post(eventMsg);
                break;
            case R.id.tv_type:
                eventMsg.setType(7);
                EventBus.getDefault().post(eventMsg);
                break;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
