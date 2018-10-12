package com.example.project.phonevideo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.phonevideo.R;

public class TitleBar  extends LinearLayout implements View.OnClickListener {

    private View tv_search;
    private View rl_game;
    private View more;
    private Context context;


    //代码中实例化该类的时候
    public TitleBar(Context context) {
        this(context,null);
    }
//布局文件中使用该类的时候 通过构造方法
    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        this.context = context;
    }
//当需要设置样式的时候
    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //当布局文件加载完成的时候回调这个方法

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_search = getChildAt(1);
        rl_game = getChildAt(2);
        more = getChildAt(3);
        //设置点击事件
        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        more.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_search:
                    Toast.makeText(context,"搜索全网",Toast.LENGTH_LONG).show();
                    break;
                case R.id.rl_game:
                    Toast.makeText(context,"播放历史",Toast.LENGTH_LONG).show();
                    break;
                case R.id.more:
                    Toast.makeText(context,"更多内容，敬请期待！",Toast.LENGTH_LONG).show();
                    break;
            }
    }
}
