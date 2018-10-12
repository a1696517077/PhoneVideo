package com.example.project.phonevideo.fragment;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.project.phonevideo.R;
import com.example.project.phonevideo.Utils.TimeUtils;
import com.example.project.phonevideo.adapter.VideoFragmentAdapter;
import com.example.project.phonevideo.domain.MediaItem;
import java.util.ArrayList;


public class VideoFragment extends Fragment {

    private static final String TAG = "VideoFragment";
    public Context context;
    public ListView lv_list;
    public TextView nomedia;
    public ProgressBar pb_loading;
    public ArrayList<MediaItem> mediaItems;
    public TimeUtils timeUtils;
    public View view;
    public VideoFragmentAdapter videoFragmentAdapter;



    public VideoFragment(){
       // super();
        timeUtils = new TimeUtils();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initDate();



    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_video,container,false);
    }



    public  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "handleMessage: " + msg.what);
            if (mediaItems !=  null && mediaItems.size() > 0){
                Log.i(TAG,"mediaItems"+ mediaItems);
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


    public void initDate() {

        FromLocalGetData();
        }
    public View initView() {
        view = View.inflate(context,R.layout.layout_video,null);
        lv_list = view.findViewById(R.id.lv_list);
        nomedia = view.findViewById(R.id.nomedia);
        pb_loading = view.findViewById(R.id.pb_loading);
        return view;

    }

    public  void FromLocalGetData() {
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
