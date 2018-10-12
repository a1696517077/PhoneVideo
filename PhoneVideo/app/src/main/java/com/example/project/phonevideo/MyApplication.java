package com.example.project.phonevideo;

import android.app.Application;
import org.xutils.x;
/**
 * @author on liuzhiwei
 * @version 1.0
 * describe TODO
 * @package com.example.project.phonevideo
 * @filename MyApplication
 * @date on 2018/10/4 12:48
 * @email 2284130231@qq.com
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
