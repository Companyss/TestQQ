package com.example.testqq.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.testqq.R;

/**
 * Created by 宋宝春 on 2017/4/17.
 */
public class PictureActivity extends BaseActivity{
    private ImageView s,ss;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity);
        s= (ImageView) findViewById(R.id.s);
        ss= (ImageView) findViewById(R.id.ss);
        String paht = getIntent().getStringExtra("paht");
        Glide.with(this)
                .load(paht)
                .into(s);
        String rpaht = getIntent().getStringExtra("rpaht");
        Glide.with(this)
                .load(rpaht)
                .into(ss);
    }
}
