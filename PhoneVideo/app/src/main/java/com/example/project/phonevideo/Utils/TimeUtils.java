package com.example.project.phonevideo.Utils;

import android.content.Context;
import android.net.TrafficStats;

import java.util.Formatter;
import java.util.Locale;

import static android.net.TrafficStats.getUidRxBytes;

public class TimeUtils {

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    private long lastTotalRxBytes = 0;
    private long lastTimeStamp = 0;

    public TimeUtils() {
        //转换成字符串时间
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    public String stringforTime(int timeMS) {
        int totalSeconds = (timeMS / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
//判断是否是网络的资源
    public boolean isNetUri(String uri) {
        boolean reault = false;
        if (uri != null){
            if (uri.toLowerCase().startsWith("http")||uri.toLowerCase().startsWith("mms")||uri.toLowerCase().startsWith("rtsp")){

                reault = true;

            }
        }
            return reault;
    }

    //得到网速 每隔两秒调用一次
    public  String getNetSpeed(Context context) {

        String netSpeed = "0 kb/s";

        long nowTotalRxBytes = getUidRxBytes(context.getApplicationInfo().uid)== TrafficStats.UNSUPPORTED ? 0 :(TrafficStats.getTotalRxBytes()/1024);//转为KB
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换

        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;


        netSpeed= String.valueOf(speed) + " kb/s";

       return netSpeed;
    }



}
