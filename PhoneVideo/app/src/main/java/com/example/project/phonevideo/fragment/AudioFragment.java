package com.example.project.phonevideo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.phonevideo.R;

public class AudioFragment extends Fragment {
    public AudioFragment(){

    }


    public static AudioFragment newInstance(){
        AudioFragment audioFragment = new AudioFragment();
        return audioFragment;
    }

    public void initDate() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_audio,container,false);
    }


}
