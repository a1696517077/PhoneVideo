package com.example.project.phonevideo.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.phonevideo.R;
import com.example.project.phonevideo.Utils.TimeUtils;
import com.example.project.phonevideo.domain.MediaItem;
import com.example.project.phonevideo.view.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//系统播放器
public class SystemVideoPlayer extends Activity implements View.OnClickListener {

    private static final int PROGRESS = 1;
    private static final int HIDE_MEDIACONTROLLER = 2;//隐藏控制面板
    private static final int FULL_SCREEN = 1;
    private static final int DEFAULT_SCREEN =2 ;
    private static final int SHOW_SPEED = 3;
    private VideoView videoView;
    private Uri uri;
    private LinearLayout top;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitchPlayer;
    private LinearLayout bottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button exit;
    private Button videoPre;
    private Button btnStartPause;
    private Button next;
    private Button videoSwitchScreen;
    private MyReceiver myReceiver;

    private TimeUtils timeUtils;
    //调节声音
    private AudioManager am;

    private int currentVoice;//当前音量

    private int maxVoice;//最大音量
    //0-15



    ArrayList<MediaItem> mediaItems;
    private int position;

    //1、定义手势识别器
    private GestureDetector gestureDetector;

    private RelativeLayout media_controller;

    private boolean isShowMediaController   = false;

    private boolean isFullScreen = false;

    private int screenWidth = 0;

    private int screentHeight = 0;

    private int VideoWidth ;
    private int VideoHeight;


    private boolean isMute = false;
    private boolean isNetUri;//是否是网络的uri



    private TextView tv_netspeed;

    private LinearLayout ll_buffer;


    private TextView tv_loading_netspeed;
    private LinearLayout ll_loading;

    private boolean isUseSystem = false;
    private   int precurrentPosition;

    public void findViews() {

        setContentView(R.layout.activity_system_video);
        videoView = findViewById(R.id.videoview);

        top = findViewById(R.id.top);

        tvName = findViewById(R.id.tv_name);

        ivBattery = findViewById(R.id.iv_battery);

        tvSystemTime = findViewById(R.id.tv_system_time);

        btnVoice = findViewById(R.id.btn_voice);

        seekbarVoice = findViewById(R.id.seekbar_voice);

        btnSwitchPlayer = findViewById(R.id.btn_switch_player);

        bottom = findViewById(R.id.bottom);

        tvCurrentTime = findViewById(R.id.tv_current_time);

        seekbarVideo = findViewById(R.id.seekbar_video);

        tvDuration = findViewById(R.id.tv_duration);

        exit = findViewById(R.id.exit);

        videoPre = findViewById(R.id.video_pre);

        btnStartPause = findViewById(R.id.btn_start_pause);

        next = findViewById(R.id.next);

        videoSwitchScreen = findViewById(R.id.video_switch_screen);

        media_controller = findViewById(R.id.media_controller);

       // tv_netspeed = findViewById(R.id.tv_netspeed);
       // ll_buffer = findViewById(R.id.ll_buffer);
        tv_netspeed = findViewById(R.id.tv_netspeed);
        ll_buffer = findViewById(R.id.ll_buffer);

        //tv_loading_netspeed = findViewById(R.id.tv_loading_netspeed);
        //ll_loading = findViewById(R.id.ll_loading);
        tv_loading_netspeed = findViewById(R.id.tv_loading_netspeed);
        ll_loading = findViewById(R.id.ll_loading);

        btnVoice.setOnClickListener(this);

        btnSwitchPlayer.setOnClickListener(this);

        exit.setOnClickListener(this);

        videoPre.setOnClickListener(this);

        btnStartPause.setOnClickListener(this);

        next.setOnClickListener(this);

        videoSwitchScreen.setOnClickListener(this);
        //最大音量跟seekbar关联
        seekbarVoice.setMax(maxVoice);
        //设置当前进度，当前音量
        seekbarVoice.setProgress(currentVoice);

        //更新网速

        handler.sendEmptyMessage(SHOW_SPEED);

    }

    private void setListener() {
        //准备好监听

        videoView.setOnPreparedListener(new MyOnPrepareListener());

        //播放出错监听
        videoView.setOnErrorListener(new MyOnErrorListener());

        //播放完成监听
        videoView.setOnCompletionListener(new MyOnCompletionListener());

        //设置seekbar 状态的监听
        seekbarVideo.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());

        seekbarVoice.setOnSeekBarChangeListener(new VoiceOnSeekBarChangeListener());
            if (isUseSystem){
                //监听视频播放卡顿 用系统api
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    videoView.setOnInfoListener(new MyOnInfoListener());
            }else {

                }

        }

    }
    class  MyOnInfoListener implements MediaPlayer.OnInfoListener{

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {

            switch (what){
                //拖动卡了
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    Toast.makeText(SystemVideoPlayer.this,"卡了",Toast.LENGTH_SHORT).show();
                    ll_buffer.setVisibility(View.VISIBLE);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END://视频卡结束了拖动卡结束了
                    Toast.makeText(SystemVideoPlayer.this,"卡完了",Toast.LENGTH_SHORT).show();
                    ll_buffer.setVisibility(View.GONE);
                    break;
            }
            return true;
        }
    }



    class  VoiceOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    updataVoice(progress,false);
                }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(HIDE_MEDIACONTROLLER);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
        }

    }


//设置音量的大小
    private void updataVoice(int progress,boolean isMute) {
        if (isMute){
            am.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
            seekbarVoice.setProgress(0);
        }else {
            am.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            seekbarVoice.setProgress(progress);
            currentVoice = progress;
        }

    }



    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        //手指滑动调用

        //fromuser 用户引起为true
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                if (progress >0){
                    isMute = false;
                }else {
                    isMute = true;
                }
                videoView.seekTo(progress);
            }
        }

        //手指触碰回调
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(HIDE_MEDIACONTROLLER);
        }

        //手指离开调用
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
        }
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        findViews();

        setListener();

        getData();


        //设置控制面板
        // videoView.setMediaController(new MediaController(this));

        setData();
    }



    private void setData() {
        if (mediaItems != null && mediaItems.size()>0){
            MediaItem mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getName());//设置视频名称
            isNetUri = timeUtils.isNetUri(mediaItem.getData());
            videoView.setVideoPath(mediaItem.getData());
        }else if (uri != null){
            tvName.setText(uri.toString());//设置视频名称
            videoView.setVideoURI(uri);
            isNetUri = timeUtils.isNetUri(uri.toString());
            videoView.setVideoURI(uri);
        }else {
            Toast.makeText(SystemVideoPlayer.this,"大哥你没有传递数据",Toast.LENGTH_SHORT).show();
        }
        setButtonState();

    }


    private void getData() {
        //得到播放地址
        uri = getIntent().getData();

      mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
       position = getIntent().getIntExtra("position",0);

    }



    private void initData() {
        timeUtils = new TimeUtils();
        myReceiver = new MyReceiver();
        //注册电量广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(myReceiver, intentFilter);
        //2.实例化手势识别器 并且重写双击 点击 长按
        gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                //Toast.makeText(SystemVideoPlayer.this,"被长按",Toast.LENGTH_SHORT).show();
                startAndPause();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //Toast.makeText(SystemVideoPlayer.this,"被双击",Toast.LENGTH_SHORT).show();
                setFullScreenAndDefault();
                return super.onDoubleTap(e);

            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
               // Toast.makeText(SystemVideoPlayer.this,"被单击",Toast.LENGTH_SHORT).show();
                if (isShowMediaController){
                    //隐藏
                    hideMediaController();
                    //把隐藏消息移除
                    handler.removeMessages(HIDE_MEDIACONTROLLER);
                }else {
                    //显示
                    showMediaController();
                    //发消息隐藏
                    handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);

                }
                return super.onSingleTapConfirmed(e);
            }
        });
        //得到屏幕的宽和高
        //方法过时
        //screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        //screentHeight = getWindowManager().getDefaultDisplay().getHeight();
        //得到屏幕宽高的新方式
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screentHeight = displayMetrics.heightPixels;

        //得到音量
         am = (AudioManager) getSystemService(AUDIO_SERVICE);
         currentVoice = am.getStreamVolume(AudioManager.STREAM_MUSIC);
         maxVoice = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);


    }



    private void setFullScreenAndDefault() {
        if (isFullScreen){
            //默认
            setVideoType(DEFAULT_SCREEN);
        }else {
            //全屏
            setVideoType(FULL_SCREEN);
        }
    }



    private void setVideoType(int defaultScreen) {
        switch (defaultScreen){
            case FULL_SCREEN:
                videoView.setVideoSize(screenWidth,screentHeight);
                //全屏
                //1.设置视频画面的大小
                //2.设置按钮和状态
                btnSwitchPlayer.setBackgroundResource(R.drawable.btn_video_switch_screen_default_selector);
            isFullScreen = true;
                break;
            case DEFAULT_SCREEN:
                //默认
                //视频真实宽和高

                int width =screenWidth;
                int height = screentHeight;
                int mVideoWidth = VideoWidth;
                int mVideoHeight = VideoHeight;
                //1.设置视频画面的大小
                if ( mVideoWidth * height  < width * mVideoHeight ) {

                    width = height * mVideoWidth / mVideoHeight;
                } else if ( mVideoWidth * height  > width * mVideoHeight ) {

                    height = width * mVideoHeight / mVideoWidth;
                }
                videoView.setVideoSize(width,height);
                //2.设置按钮的状态
                btnSwitchPlayer.setBackgroundResource(R.drawable.btn_video_switch_screen_selector);
                isFullScreen = false;
                break;
        }
    }



    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);//0-100
            setBattery(level);


        }
    }



    private void setBattery(int level) {
        if (level <= 0){
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        }else if (level <= 10){
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        }else if (level <= 20){
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        }else if (level <= 40){
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        }else if (level <= 60){
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        }else if (level <= 80){
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        }else if (level <= 100){
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }else
            ivBattery.setImageResource(R.drawable.ic_battery_100);
    }




    @Override
    public void onClick(View v) {

        if (v == btnVoice) {
            if (v == btnVoice){
                isMute = !isMute;
            }
            // Handle clicks for btnVoice
            updataVoice(currentVoice,isMute);
        } else if (v == btnSwitchPlayer) {
            // Handle clicks for btnSwitchPlayer
            showSwitchDialog();
        } else if (v == exit) {
            finish();
            // Handle clicks for exit
        } else if (v == videoPre) {
            // Handle clicks for videoPre
            //播放上一个
            PlayPreVideo();
        } else if (v == btnStartPause) {
            // Handle clicks for btnStartPause
            startAndPause();
        } else if (v == next) {
            // Handle clicks for next
            playNextVideo();
        } else if (v == videoSwitchScreen) {
            // Handle clicks for videoSwitchScreen
            setFullScreenAndDefault();

        }
        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);


    }
    private void showSwitchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当你播放视频有声音无画面请切换万能播放器");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               startVitamioPlayer();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }




    private void startAndPause() {
        if (videoView.isPlaying()) {
            //视频在播放 设置为暂停
            videoView.pause();
            //按钮状态设置为播放状态
            btnStartPause.setBackgroundResource(R.drawable.btn_start_start_selector);

        } else {
            //视频播放
            videoView.start();
            //按钮状态设置为暂停
            btnStartPause.setBackgroundResource(R.drawable.btn_start_pause_selector);
        }
    }



    //播放上一个视频
    private void PlayPreVideo() {
        if (mediaItems !=null && mediaItems.size() >0){
            //播放上一个
            position--;
            if (position >= 0){
                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri = timeUtils.isNetUri(mediaItem.getData());
                videoView.setVideoPath(mediaItem.getData());

                //设置按钮状态
                setButtonState();
            }
        }else if (uri != null){
            //上一个和下一个按钮设置灰色并且不可以点击
            setButtonState();

        }

    }



//播放下一个视频
    private void playNextVideo() {
        if (mediaItems !=null && mediaItems.size() >0){
            //播放下一个
            position++;
            if (position < mediaItems.size()){

                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri = timeUtils.isNetUri(mediaItem.getData());
                videoView.setVideoPath(mediaItem.getData());


                //设置按钮状态
                setButtonState();
            }
        }else if (uri != null){
            //上一个和下一个按钮设置灰色并且不可以点击
            setButtonState();

        }
    }



    private void setButtonState() {
        if (mediaItems != null && mediaItems.size() >0){
            //两个按钮设置灰色
           if (mediaItems.size() ==1){
               setEnable(false);
           }else if (mediaItems.size()==2){
               if (position ==0){
                   videoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                   videoPre.setEnabled(false);

                   next.setBackgroundResource(R.drawable.btn_next_selector);
                   next.setEnabled(true);
               }else if (position==mediaItems.size()-1){
                   next.setBackgroundResource(R.drawable.btn_pre_gray);
                   next.setEnabled(false);


                   videoPre.setBackgroundResource(R.drawable.video_pre_selector);
                   videoPre.setEnabled(true);
               }
           }else {
               if (position ==0){
                   videoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                   videoPre.setEnabled(false);
               }else if (position==mediaItems.size()-1){
                   next.setBackgroundResource(R.drawable.btn_next_gray);
                   next.setEnabled(false);
               }else {
                   setEnable(true);
               }
           }
        }else if (uri != null){
            setEnable(false);
        }
    }



    private void setEnable(boolean isEnable) {
        if (isEnable){
            videoPre.setBackgroundResource(R.drawable.video_pre_selector);
            videoPre.setEnabled(true);
            next.setBackgroundResource(R.drawable.btn_next_selector);
            next.setEnabled(true);
        }else {
            videoPre.setBackgroundResource(R.drawable.btn_pre_gray);
            videoPre.setEnabled(false);
            next.setBackgroundResource(R.drawable.btn_next_gray);
            next.setEnabled(false);
        }

    }



    class MyOnPrepareListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
          VideoWidth =   mp.getVideoWidth();
          VideoHeight =   mp.getVideoHeight();
            videoView.start();//开始播放
            //视频总时长
            int duration = videoView.getDuration();
            seekbarVideo.setMax(duration);
            tvDuration.setText(timeUtils.stringforTime(duration));
            hideMediaController();//默认隐藏控制面板
            //2.发消息



            //默认屏幕播放
            handler.sendEmptyMessage(PROGRESS);

           // videoView.setVideoSize(200,200);
           // videoView.setVideoSize(mp.getVideoWidth(),mp.getVideoHeight());

            setVideoType(DEFAULT_SCREEN);

            //把加载效果消掉

            ll_loading.setVisibility(View.GONE);


        }
    }



    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
          //  Toast.makeText(SystemVideoPlayer.this, "播放出错", Toast.LENGTH_SHORT).show();
            //1.播放格式不支持 跳转万能播放器
            startVitamioPlayer();
            //2.播放网络视频的时候，网络中断。1.如果确实断了，可以提示，2.网络断断续续的 ，重新播放
            //3.播放本地文件的时候本地文件有空白-下载做完成
            return false;
        }
    }
//1.把数据传入vitamio播放器
    //2.关闭系统播放器
    private void startVitamioPlayer() {

        if (videoView!=null){
            videoView.stopPlayback();
        }
        Intent intent = new Intent(this,VitamioVideoPlayer.class);

        if (mediaItems != null && mediaItems.size()>0){
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist",mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position",position);

        }else if (uri!=null){
            intent.setData(uri);
        }
        startActivity(intent);
        finish();
    }


    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
          //  Toast.makeText(SystemVideoPlayer.this, "播放完成", Toast.LENGTH_SHORT).show();
            playNextVideo();
        }
    }



    Handler handler =   new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_SPEED:
                    String netspeed = timeUtils.getNetSpeed(SystemVideoPlayer.this);


                   // tv_loading_netspeed.setText("视频加载中"+ netspeed);
                    //tv_netspeed.setText("缓冲中。。。" + netspeed);
                    tv_loading_netspeed.setText("视频加载中"+ netspeed);
                    tv_netspeed.setText("缓冲中..." + netspeed);
                    handler.removeMessages(SHOW_SPEED);
                    handler.sendEmptyMessageDelayed(SHOW_SPEED, 2000);

                    break;
                case HIDE_MEDIACONTROLLER:
                    //隐藏控制面板
                    hideMediaController();
                    break;

                case PROGRESS:
                    //1.得到当前视频的播放进度
                    int currenPoisiton = videoView.getCurrentPosition();
                    //2.SeekBar setProgress(当前进度）
                    seekbarVideo.setProgress(currenPoisiton);

                    //更新文本播放进度

                    tvCurrentTime.setText(timeUtils.stringforTime(currenPoisiton));

                    //设置系统时间
                    tvSystemTime.setText(getSystem());
                    //缓冲进度的更新
                    //只有网络的视频才有缓冲 本地的没有
                    if (isNetUri){


                        int buffer = videoView.getBufferPercentage();//0-100

                        int totalBuffer = buffer * seekbarVideo.getMax();

                        int secondaryProgress = totalBuffer/100;

                        seekbarVideo.setSecondaryProgress(secondaryProgress);
                    }else {

                        seekbarVideo.setSecondaryProgress(0);
                    }

                    //监听卡
                    if(!isUseSystem && videoView.isPlaying()){

                        int buffer = currenPoisiton - precurrentPosition;
                        if (buffer < 500) {
                        //卡了
                            ll_buffer.setVisibility(View.VISIBLE);
                        }else {
                            //不卡
                            ll_buffer.setVisibility(View.GONE);
                        }

                    }else {
                        ll_buffer.setVisibility(View.GONE);
                    }

                precurrentPosition = currenPoisiton;

                    //3.每秒更新一次
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
            }
        }
    };



    private String getSystem() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String systemTime = simpleDateFormat.format(new Date());

        return systemTime;
    }

    @Override
    protected void onDestroy() {
    //释放资源的时候，先释放子类，再释放父类
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
        super.onDestroy();
    }

    private float startY;
    //屏幕的高
    private float touchRang;

    private int mVol;//一按下的音量

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //第三步，把手势传递给手势识别器
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //1.按下记录值
                startY = event.getY();
                mVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                touchRang = Math.min(screentHeight,screenWidth);
                handler.removeMessages(HIDE_MEDIACONTROLLER);

                break;
            case MotionEvent.ACTION_MOVE:
                //移动的时候记录相关值
                float endY = event.getY();
                float distanceY = startY - endY;

                float delta = (distanceY/touchRang)*maxVoice;
                int voice = (int) Math.min(Math.max(mVol+delta,0),maxVoice);
                if (delta != 0){
                    isMute = false;
                    updataVoice(voice,isMute);
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
                break;
        }
        return super.onTouchEvent(event);
    }




    //显示控制面板
    private void showMediaController(){
        media_controller.setVisibility(View.VISIBLE);
        isShowMediaController = true;


    }
    //隐藏控制面板
    private void hideMediaController(){
        media_controller.setVisibility(View.GONE);
        isShowMediaController = false;

    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            currentVoice --;
            updataVoice(currentVoice,false);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
            return  true;
        }else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            currentVoice ++;
            updataVoice(currentVoice,false);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
