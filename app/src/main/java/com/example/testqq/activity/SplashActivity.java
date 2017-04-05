package com.example.testqq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testqq.R;
import com.hyphenate.chat.EMClient;

import java.util.Date;

/**
 * Created by 李晓 on 2017/3/22.
 * 开屏页
 */

public class SplashActivity extends BaseActivity {
    private ImageView imageView;
    private final static long TIME = 3000;
    // 声明TextView
    private TextView textView;
    // ImageView的alpha值
    private float[] image_alpha = {0.1f, 0.3f, 0.5f, 0.7f, 0.9f};
    private int alpha;
    private int touming[] = {0, 50, 100, 200};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        init();

    }

    /**
     * 初始化控件
     */
    private void init() {
        imageView = (ImageView) findViewById(R.id.spacer_image_view);
        gradient();
        intentToNext();
    }

    /**
     * 渐变加载图片
     */
    private void gradient() {
        imageView.setAlpha(0.0f);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 4; i++) {
                    alpha = i;
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setAlpha(image_alpha[alpha]);
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 开屏页跳转
     */
    private void intentToNext() {
        //耗时操作需要启动分线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                //判断是否登陆过
                if (EMClient.getInstance().isLoggedInBefore()) {
                    //拿到开始执行的时间
                    long timeMillis = System.currentTimeMillis();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    //执行到当前行的时间-开始时间得到加载时间=消耗时间
                    long newtime = System.currentTimeMillis() - timeMillis;
                    try {
                        //如果之前登录过就停留 (2000 - time) 秒跳转到主页面
                        Thread.sleep(TIME - newtime);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //跳转到主页
                    Splik(SplashActivity.this, new Intent(SplashActivity.this, HomepageActivity.class));
                    //跳转完成后关闭页面
                    finish();
                    //没有登陆过2秒之后跳转到登录页
                } else {
                    try {
                        Thread.sleep(TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //跳转到登录页
                    Splik(SplashActivity.this, new Intent(SplashActivity.this, LoginActivity.class));
                    //跳转完成后关闭页面
                    finish();
                }
            }
        }).start();
    }
}
