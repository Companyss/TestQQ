package com.example.testqq.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testqq.R;

/**
 * 基础类 用于创建一些公用的方法
 * Created by 宋宝春 on 2017/3/22.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     *  Toast提示信息的方法
     */
    public static void toastShow(final Activity activity, final String message){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
               Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 跳转页面的方法
     * @activity 本类的Activity
     * @intent  实例化序跳转的Activity
     */
    public void Splik(Activity activity,Intent intent){
        activity.startActivity(intent);
    }

    /**
     * @param ed  EditText 对象
     * @return  字符串
     * Edtext返回字符串的方法
     */
    public String getEdtext(EditText ed) {
        return ed.getText().toString();
    }

    /**
     *
     * @param errCoed
     *
     * 登录提示信息的方法
     */
    public void errToast(int errCoed) {
        String str = "";
        switch (errCoed) {
            case 1:
                str="账号为空！";
                break;
            case 2:
                str="密码为空！";
                break;
            case 3:
                str="账号密码不匹配";
                break;
            case 4:
                str="2次密码不一致！";
                break;
            case 5:
                str="输入的内容违法了！";
                break;
        }
        //打印显示
        toastShow(this,str);
    }
    public static void httpImg(Context context, ImageView imageView, String s) {

        Glide.with(context).load(s)//加载图片地址
                .placeholder(R.mipmap.ic_launcher)//加载未完成是显示的图片
                .error(R.mipmap.ic_launcher)//加载失败时显示的图片
                //F.centerCrop()     //拉伸高度
                .fitCenter()
                //     .transform(new GlideRoundTransform(context))
                .override(450, 450)
                .into(imageView);//将加载的图片显示在控件上

    }
}
