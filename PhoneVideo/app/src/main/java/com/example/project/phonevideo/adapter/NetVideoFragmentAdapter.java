package com.example.project.phonevideo.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.phonevideo.R;
import com.example.project.phonevideo.Utils.TimeUtils;
import com.example.project.phonevideo.domain.MediaItem;

import org.xutils.x;

import java.util.ArrayList;

public class NetVideoFragmentAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<MediaItem> mediaItems;


    public NetVideoFragmentAdapter(Context context, ArrayList<MediaItem>mediaitems){
        this.context = context;
        this.mediaItems = mediaitems;



    }
    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder viewHolder;
        if (convertView == null){
            convertView = View.inflate(context, R.layout.item_netvideo_fragment,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
            viewHolder.tv_desc = convertView.findViewById(R.id.tv_desc);

            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        MediaItem mediaItem = mediaItems.get(position);
        viewHolder.tv_name.setText(mediaItem.getName());
        viewHolder.tv_desc.setText(mediaItem.getDesc());
        x.image().bind(viewHolder.iv_icon,mediaItem.getImageUrl());

        return convertView;
    }


static class ViewHolder{
    ImageView iv_icon;
    TextView tv_name;
    TextView tv_desc;

  }
}
