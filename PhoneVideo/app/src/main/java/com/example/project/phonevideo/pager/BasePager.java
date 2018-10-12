package com.example.project.phonevideo.pager;


import android.content.Context;
import android.view.View;

//基类
public  abstract class BasePager {
    public Context context;
    public View rootview;
    public boolean isInitData;

    //构造方法的时候 视图创建 initview 强制子类去实现
    //在初始化数据的时候 初始化子页面的数据
    public BasePager(Context context){
        this.context = context;//实例化上下文 子类创建视图需要
        rootview = initView();

    }
    //强制子类实现特定的效果
    public  abstract View initView();
//当子页面需要初始化数据，联网请求数据或者绑定数据的时候
    public void initData() {

    }

}



