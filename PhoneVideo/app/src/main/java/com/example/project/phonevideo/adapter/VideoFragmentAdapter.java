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

import java.util.ArrayList;

public class VideoFragmentAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<MediaItem> mediaItems;
    public TimeUtils timeUtils;

    public VideoFragmentAdapter(Context context,ArrayList<MediaItem>mediaitems){
        this.context = context;
        this.mediaItems = mediaitems;
        timeUtils = new TimeUtils();


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
            convertView = View.inflate(context, R.layout.item_video_fragment,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
            viewHolder.tv_time = convertView.findViewById(R.id.tv_time);
            viewHolder.tv_size = convertView.findViewById(R.id.tv_size);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        MediaItem mediaItem = mediaItems.get(position);
        viewHolder.tv_name.setText(mediaItem.getName());
        viewHolder.tv_size.setText(Formatter.formatFileSize(context,mediaItem.getSize()));
        viewHolder.tv_time.setText(timeUtils.stringforTime((int) mediaItem.getDruation()));

        return convertView;
    }


static class ViewHolder{
    ImageView iv_icon;
    TextView tv_name;
    TextView tv_time;
    TextView tv_size;
  }
}
