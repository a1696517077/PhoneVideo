package com.example.project.phonevideo.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class NetAudioPager  extends BasePager{

    private static final String TAG="NetAudioPager";
    private TextView textView;

    public NetAudioPager(Context context) {
        super(context);
    }
    @Override
    public View initView() {
        Log.i(TAG,"NetAudio视频页面被初始化了");
        textView = new TextView(context);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);

        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        Log.i(TAG,"NetAudio视频页面数据被初始化了");
        textView.setText("网络音乐");
    }
}
