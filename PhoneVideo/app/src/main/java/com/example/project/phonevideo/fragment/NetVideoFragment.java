package com.example.project.phonevideo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.phonevideo.R;

public class NetVideoFragment extends android.support.v4.app.Fragment {
    public NetVideoFragment(){

    }
    public static NetVideoFragment newInstance(){
        NetVideoFragment netVideoFragment = new NetVideoFragment();
        return netVideoFragment;
    }
    public void initDate() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_netvideo,container,false);
    }
}
