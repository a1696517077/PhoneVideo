package com.example.project.phonevideo.pager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.phonevideo.R;
import com.example.project.phonevideo.Utils.Constants;
import com.example.project.phonevideo.activity.SystemVideoPlayer;
import com.example.project.phonevideo.activity.VitamioVideoPlayer;
import com.example.project.phonevideo.adapter.NetVideoFragmentAdapter;
import com.example.project.phonevideo.adapter.VideoFragmentAdapter;
import com.example.project.phonevideo.domain.MediaItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class NetVideoPager extends BasePager {

    private static final String TAG="NetVideoPager";
    @ViewInject(R.id.lv_list)
    private ListView mlv_list;
    @ViewInject(R.id.tv_nonet)
    private TextView mtv_nonet;
    @ViewInject(R.id.pb_loading)
    private ProgressBar mpb_loading;

    private ArrayList<MediaItem> mediaItems;

    private NetVideoFragmentAdapter netVideoFragmentAdapter;

    public NetVideoPager(Context context) {

        super(context);
    }

    @Override
    public View initView() {
       View view = View.inflate(context, R.layout.layout_netvideo,null);
       //第一个参数指的是NetVideoPager 第二个参数是布局
          x.view().inject(NetVideoPager.this,view);

        mlv_list.setOnItemClickListener(new MyOnClickListener());
        return view;
    }
    private class MyOnClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MediaItem mediaItem = mediaItems.get(position);

            Intent intent = new Intent(context, VitamioVideoPlayer.class);
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
        RequestParams params = new RequestParams(Constants.NET_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                //主线程
                Log.e("NetVideoPager","联网成功=="+result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("NetVideoPager","联网失败=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("NetVideoPager","onCancelled=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("NetVideoPager","onFinished");
            }
        });

    }

    private void processData(String json) {
        //解析json数据
        mediaItems = parseJson(json);
        //设置适配器
        if (mediaItems !=  null && mediaItems.size() > 0){
            //  Log.i(TAG,"mediaItems"+ mediaItems);
            //有数据 设置适配器 把文本隐藏
            netVideoFragmentAdapter = new NetVideoFragmentAdapter(context,mediaItems);
           mlv_list.setAdapter(netVideoFragmentAdapter);
            mtv_nonet.setVisibility(View.GONE);
        }else {
            //没数据 把文本显示和加载条隐藏
            mtv_nonet.setVisibility(View.VISIBLE);

        }
        mpb_loading.setVisibility(View.GONE);


    }

    private ArrayList<MediaItem> parseJson(String json) {
        //解析json数据有两种方式
        //1.用系统的接口解析json
        //2.使用第三方解析工具 Gson fastjson
        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = (JSONArray) jsonObject.opt("trailers");
            if (jsonArray != null && jsonArray.length()>0){
                for (int i =0;i<jsonArray.length();i++){
                    JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);
                    if (jsonObjectItem != null){

                        MediaItem mediaItem = new MediaItem();
                        String movieName = jsonObjectItem.optString("movieName");

                        mediaItem.setName(movieName);
                        String videoTitle =jsonObjectItem.optString("videoTitle");

                        mediaItem.setDesc(videoTitle);
                        String imageUrl = jsonObjectItem.optString("coverImg");

                        mediaItem.setImageUrl(imageUrl);
                        String highUrl = jsonObjectItem.optString("highUrl");

                        mediaItem.setData(highUrl);
                        //添加到集合中
                        mediaItems.add(mediaItem);

                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mediaItems;
    }
}
