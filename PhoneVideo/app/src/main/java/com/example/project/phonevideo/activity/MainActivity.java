package com.example.project.phonevideo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.project.phonevideo.R;
import com.example.project.phonevideo.fragment.AudioFragment;
import com.example.project.phonevideo.fragment.NetAudioFragment;
import com.example.project.phonevideo.fragment.NetVideoFragment;
import com.example.project.phonevideo.fragment.VideoFragment;
import com.example.project.phonevideo.pager.AudioPager;
import com.example.project.phonevideo.pager.BasePager;
import com.example.project.phonevideo.pager.NetAudioPager;
import com.example.project.phonevideo.pager.NetVideoPager;
import com.example.project.phonevideo.pager.VideoPager;

import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.List;

public  class MainActivity extends FragmentActivity {

    private static int position;
    //private static int permission;
    private FrameLayout main_content;
    private RadioGroup rg_bottom_tag;
    private static ArrayList<BasePager> basePagers;

    private static final int REQUEST_PERMISSION = 0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_content = findViewById(R.id.main_content);
        rg_bottom_tag = findViewById(R.id.rg_bottom_tag);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            List<String> permission = new ArrayList<String>();

            if (hasWritePermission != PackageManager.PERMISSION_GRANTED){
                permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }else {

            }if (hasReadPermission != PackageManager.PERMISSION_GRANTED){
                permission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }else {

            }if (!permission.isEmpty()){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                        REQUEST_PERMISSION);

            }
        }




        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager(this));
        basePagers.add(new AudioPager(this));
        basePagers.add(new NetVideoPager(this));
        basePagers.add(new NetAudioPager(this));

        //设置radiogroup的监听
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //默认选中RadioButton第一个按钮
        rg_bottom_tag.check(R.id.rb_video);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION:{
                for (int i  = 0;i < permissions.length;i++){
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED){

                    }else if (grantResults[i] == PackageManager.PERMISSION_DENIED){

                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                default:
                    position = 0;
                    break;
                case R.id.rb_audio:
                    position =1;
                    break;
                case R.id.rb_netvideo:
                    position =2;
                    break;
                case R.id.rb_netaudio:
                    position = 3;
                    break;
            }
            setFragment();

        }
    }
    //把页面添加到fragmnet中
    private void setFragment() {
        //1.得到fragmentmanager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //3.替换
        ft.replace(R.id.main_content,new MyFragment());
        //4.提交
        ft.commit();

    }

    public static  class MyFragment extends Fragment{
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            BasePager basePager = getBasePager();
            if (basePager != null){
                //各个页面的视图
                return basePager.rootview;
            }
            return null;
        }



    }
    //根据位置得到对应的页面
    private static BasePager getBasePager() {
        BasePager basePager = basePagers.get(position);
        if (basePager != null){
            basePager.initData();
            //basePager.isInitData = true;
        }
        return basePager;
    }


    }






