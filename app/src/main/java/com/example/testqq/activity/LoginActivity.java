package com.example.testqq.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.testqq.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * 登录页
 * Created by 宋宝春 on 2017/3/23.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText account, password;
    private Button loginbtn, registerbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    /**
     * 初始化控件
     */
    private void initialize() {
        account = (EditText) findViewById(R.id.login_account_edit_text);
        password = (EditText) findViewById(R.id.login_password_edit_text);
        loginbtn = (Button) findViewById(R.id.login_login_button);
        registerbtn = (Button) findViewById(R.id.login_register_button);
        loginbtn.setOnClickListener(this);
        registerbtn.setOnClickListener(this);

    }

    /**
     * 开始登录方法
     */
    private void startLogin() {
        String stracc = getEdtext(account);
        String strpass = getEdtext(password);
        int reCde = getReCde(stracc, strpass);
        switch (reCde) {
            case 0:
                login(stracc, strpass);
                break;
            default:
                errToast(reCde);
                break;
        }
    }

    /**
     * 登录方法
     * @param stracc    账户
     * @param strpass   密码
     */
    private void login(String stracc, String strpass) {
        EMClient.getInstance().login(stracc,strpass,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                //跳转到主页
                Splik(LoginActivity.this,new Intent(LoginActivity.this,HomepageActivity.class));
                toastShow(LoginActivity.this,"登录聊天服务器成功！");
            }
            @Override
            public void onProgress(int progress, String status) {

            }
            @Override
            public void onError(int code, String message) {
               toastShow(LoginActivity.this,"登录聊天服务器失败！");
            }
        });

    }


    /**
     * @param acc 账号
     * @param psw 密码
     * @return int类型的数 1表示账号为空  2密码为空  0成功
     */
    private int getReCde(String acc, String psw) {

        if (TextUtils.isEmpty(acc)) {
            return 1;
        }
        if (TextUtils.isEmpty(psw)) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_login_button:
                startLogin();
                break;
            case R.id.login_register_button:
                // 点击跳转注册页
               // Splik(LoginActivity.this,new Intent(LoginActivity.this,));
                break;
        }
    }
}
