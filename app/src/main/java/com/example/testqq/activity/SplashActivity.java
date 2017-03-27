package com.example.testqq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.testqq.R;
import com.hyphenate.chat.EMClient;

import java.util.Date;

/**
 * Created by 李晓 on 2017/3/22.
 * 开屏页
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //判断之前是否登录过

              if (EMClient.getInstance().isLoggedInBefore()) {
                    //拿到开始执行的时间
                    long startTime = new Date().getTime();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    //执行到当前行的时间-开始时间得到加载时间=消耗时间
                    long time = new Date().getTime() - startTime;
                    //如果之前登录过就停留 (2000 - time) 秒跳转到登录页面
                    try {
                        Thread.sleep(2000 - time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Splik(SplashActivity.this, new Intent(SplashActivity.this, HomepageActivity.class));

                } else {
                    //如果之前没有登录过就停留2秒跳转到注册页面
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Splik(SplashActivity.this, new Intent(SplashActivity.this, LoginActivity.class));
                }
            }
        }).start();

    }

}
