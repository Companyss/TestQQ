package com.example.testqq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.testqq.R;

/**
 * Created by 宋宝春 on 2017/4/18.
 */
public class VideoActivity  extends BaseActivity{
    private VideoView videoView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        init();
    }
    private void  init(){
        videoView= (VideoView) findViewById(R.id.video_open_video_view);
        videoView.setMediaController(new MediaController(VideoActivity.this));
        String path = getIntent().getStringExtra("rvoide");
        videoView.setVideoPath(path);
        videoView.start();
        videoView.requestFocus();
    }
}
