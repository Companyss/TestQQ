package com.example.testqq.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceGroup;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testqq.R;
import com.example.testqq.vules.MyDialog;
import com.example.testqq.vules.SPUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * 登录页
 * Created by 宋宝春 on 2017/3/23.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText account, password;
    private Button loginbtn;
    private TextView registerbtn;
    private MyDialog g;
    private CheckBox remember_box, protocol_box;

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
        registerbtn = (TextView) findViewById(R.id.login_register_button);
        remember_box = (CheckBox) findViewById(R.id.login_remember_box);
        protocol_box = (CheckBox) findViewById(R.id.login_protocol_box);
        loginbtn.setOnClickListener(this);
        registerbtn.setOnClickListener(this);
        //进度提示框
        g = new MyDialog(this, R.style.CustomDialog);

        account.setText(SPUtils.getlastLoginUserName(this));
        password.setText(SPUtils.getlastLoginPassword(this));
        //设置光标的位置在字符串的最后一位
        account.setSelection(account.getText().toString().length());
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
                g.show();
                break;
            default:
                errToast(reCde);
                break;
        }
        SPUtils.setLastLoginUsername(this, stracc);
        SPUtils.setLastLoginPassword(this, strpass);
    }

    //必须点击同意才可以登录
    private void protocol() {
                if (protocol_box.isChecked() == true) {
                    startLogin();
        } else {
            toastShow(this, "请阅读用户协议后点击同意");
        }
    }

    /**
     * 登录方法
     *
     * @param stracc  账户
     * @param strpass 密码
     */
    private void login(String stracc, String strpass) {
        EMClient.getInstance().login(stracc, strpass, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                //跳转到主页
                Splik(LoginActivity.this, new Intent(LoginActivity.this, HomepageActivity.class));
                toastShow(LoginActivity.this, "登录聊天服务器成功！");
                g.cancel();

            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                toastShow(LoginActivity.this, "登录聊天服务器失败！");
                g.cancel();
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
        switch (v.getId()) {
            case R.id.login_login_button:
                protocol();
                break;
            case R.id.login_register_button:
                Splik(LoginActivity.this, new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }
}
