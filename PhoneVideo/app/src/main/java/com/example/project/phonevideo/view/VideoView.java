package com.example.project.phonevideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * @author on liuzhiwei
 * @version 1.0
 * describe TODO
 * @package com.example.project.phonevideo.view
 * @filename VideoView
 * @date on 2018/10/3 23:58
 * @email 2284130231@qq.com
 */
public class VideoView extends android.widget.VideoView {

    //在代码中一般创建的时候用这个
    public VideoView(Context context) {
        this(context,null);
    }
    //当这个类在布局文件中调用的时候 系统通过该构造方法实例化该类
    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    //当需要设置样式的时候去调用该方法
    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);

    }
//设置视频的宽和高
    public void setVideoSize(int videowidth,int videoheight){
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = videowidth;
        params.height = videoheight;
        setLayoutParams(params);
    }
}
