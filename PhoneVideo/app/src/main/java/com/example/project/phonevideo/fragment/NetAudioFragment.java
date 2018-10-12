package com.example.project.phonevideo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.phonevideo.R;

public class NetAudioFragment extends Fragment {
    public NetAudioFragment(){

    }
    public static NetAudioFragment newInstance(){
        NetAudioFragment netAudioFragment = new NetAudioFragment();
        return netAudioFragment;
    }
    public void initDate() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_netaudio,container,false);
    }
}
