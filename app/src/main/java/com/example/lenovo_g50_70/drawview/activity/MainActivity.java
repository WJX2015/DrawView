package com.example.lenovo_g50_70.drawview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.lenovo_g50_70.drawview.R;
import com.example.lenovo_g50_70.drawview.bean.EventMsg;
import com.example.lenovo_g50_70.drawview.view.MyView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class MainActivity extends AppCompatActivity {

    private MyView mMyView;
    private FloatingActionButton fab;
    private Intent mIntent;

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

        switch (eventMsg.getType()){
            //保存路径
            case 1:
                mMyView.savePath();
                break;
            //画笔大小
            case 2:

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

}
