package com.example.project.startallplayer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startAllPlayer(View view){
        Intent intent= new Intent();
        intent.setDataAndType(Uri.parse("http://vfx.mtime.cn/Video/2018/10/03/mp4/181003095735959762.mp4"),"video/*");
        startActivity(intent);
    }
}
