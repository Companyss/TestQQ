package com.example.testqq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.testqq.R;
import com.example.testqq.activity.LoginActivity;
import com.hyphenate.chat.EMClient;

/**
 * 设置页
 * Created by 宋宝春 on 2017/3/22.
 */

public class SettingUpFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Button quitLogin;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        //加载设置的布局文件    并将其返回
        view=inflater.inflate(R.layout.fragment_settingup,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }
    private void  init(){
        quitLogin= (Button) view.findViewById(R.id.setting_quit_login_button);
        quitLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_quit_login_button:
                //退出环信服务器，再次启动程序需要重新登录
                EMClient.getInstance().logout(true);
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
