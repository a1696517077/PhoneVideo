package com.example.project.phonevideo.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.example.project.phonevideo.R;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //延迟两秒
                //执行在主线程中
                startMainActivity();
            }
        },2000);

    }
    private boolean isStartMain = false;

    private void startMainActivity() {

        if (!isStartMain){
            isStartMain = true;
            //跳转到主页面并且把当前页面关闭
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            //关闭当前页面
            finish();

        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startMainActivity();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }


}
