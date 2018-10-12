package com.example.project.phonevideo.pager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.phonevideo.R;
import com.example.project.phonevideo.Utils.TimeUtils;
import com.example.project.phonevideo.activity.SystemVideoPlayer;
import com.example.project.phonevideo.adapter.VideoFragmentAdapter;
import com.example.project.phonevideo.domain.MediaItem;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class VideoPager extends BasePager {
    private ListView lv_list;
    private TextView nomedia;
    private ProgressBar pb_loading;
    private static final String TAG="VideoPager";
    public ArrayList<MediaItem> mediaItems;
    public TimeUtils timeUtils;
    public VideoFragmentAdapter videoFragmentAdapter;


    private TextView textView;
    public VideoPager(Context context) {

        super(context);
      //  timeUtils = new TimeUtils();
    }

    @Override
    public View initView() {
       View view = View.inflate(context, R.layout.layout_video,null);
       lv_list = view.findViewById(R.id.lv_list);
       nomedia = view.findViewById(R.id.nomedia);
       pb_loading = view.findViewById(R.id.pb_loading);

       //设置listview item点击事件
        lv_list.setOnItemClickListener(new MyOnClickListener());
       return view;
    }
    private class MyOnClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MediaItem mediaItem = mediaItems.get(position);
            Toast.makeText(context,"mediaitem == "+mediaItem.toString(),Toast.LENGTH_SHORT).show();
            //调起系统播放器 隐示意图
          /*  Intent intent = new Intent();
            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
            context.startActivity(intent);*/
            //调用自己写的播放器 显示意图
           /* Intent intent = new Intent(context,SystemVideoPlayer.class);
            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
            context.startActivity(intent);*/
            //3.传递列表数据 对象 序列化
            Intent intent = new Intent(context,SystemVideoPlayer.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist",mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position",position);
            context.startActivity(intent);

        }
    }


    @Override
    public void initData() {
        super.initData();
        Log.i(TAG,"Video视频页面数据被初始化了");
        getDataFromLocal();
    }
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
          //  Log.i(TAG, "handleMessage: " + msg.what);
            if (mediaItems !=  null && mediaItems.size() > 0){
              //  Log.i(TAG,"mediaItems"+ mediaItems);
                //有数据 设置适配器 把文本隐藏
                videoFragmentAdapter = new VideoFragmentAdapter(context,mediaItems);
                lv_list.setAdapter(videoFragmentAdapter);
                nomedia.setVisibility(View.GONE);
            }else {
                //没数据 把文本显示和加载条隐藏
                nomedia.setVisibility(View.VISIBLE);

            }
            pb_loading.setVisibility(View.GONE);


        }
    };

    private void getDataFromLocal() {
        mediaItems = new ArrayList<>();
        //1.从内容提供者拿
        //2.遍历sd卡拿取
        //要在子线程拿取数据
        new Thread(){
            @Override
            public void run() {
                super.run();
                //    SystemClock.sleep(2000);
                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                String[] objs = {
                        MediaStore.MediaColumns.DISPLAY_NAME,//视频文件在sd卡的名称
                        MediaStore.Video.Media.DURATION,//视频总时长
                        MediaStore.Video.Media.SIZE,//视频大小
                        MediaStore.Video.Media.DATA,//视频绝对地址
                        MediaStore.Video.Media.ARTIST,//视频演唱者
                };
                Cursor cursor  = resolver.query(uri,objs,null,null,null);
                if (cursor != null){
                    while (cursor.moveToNext()){

                        MediaItem mediaItem = new MediaItem();

                        mediaItems.add(mediaItem);
                        // mediaItems.addAll(mediaItems);
                        String DisplayName = cursor.getString(0);
                        mediaItem.setName(DisplayName);
                        long Duration = cursor.getLong(1);
                        mediaItem.setDruation(Duration);
                        long Size = cursor.getLong(2);
                        mediaItem.setSize(Size);
                        String Data = cursor.getString(3);
                        mediaItem.setData(Data);
                        String Artist = cursor.getString(4);
                        mediaItem.setArtist(Artist);

                    }
                    cursor.close();

                }

                handler.sendEmptyMessage(100);
            }
        }.start();

    }



}
