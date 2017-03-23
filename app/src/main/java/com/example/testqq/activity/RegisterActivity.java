package com.example.testqq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.testqq.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by 李晓 on 2017/3/23.
 * 注册页
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText account, password, password2;
    private Button zhuce, quxiaozhuce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);
        initView();
    }

    //初始化所有的控件
    private void initView() {
        account = (EditText) findViewById(R.id.zhanghao);
        password = (EditText) findViewById(R.id.mima);
        password2 = (EditText) findViewById(R.id.mima2);
        zhuce = (Button) findViewById(R.id.zhuce);
        quxiaozhuce = (Button) findViewById(R.id.quxiaozuce);
        zhuce.setOnClickListener(this);
        quxiaozhuce.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zhuce:
                getRegist();
                break;
            case R.id.quxiaozuce:
                Splik(RegisterActivity.this, new Intent(RegisterActivity.this, LoginActivity.class));
                break;
        }
    }

    private void getRegist() {
        //先获取账号和密码
        String straccount = getEdtext(account);
        String strpassword1 = getEdtext(password);
        String strpassword2 = getEdtext(password2);
        int i = nameAndPass(straccount, strpassword1, strpassword2);
        switch (i) {
            case 0:
                try {
                    //输入的账号和密码符合，从服务端创建一个新的账号密码
                    EMClient.getInstance().createAccount(straccount, strpassword1);
                    toastShow(RegisterActivity.this, "注册成功。");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    toastShow(RegisterActivity.this, "注册失败！");
                }
                break;
            default:
                //不符合就提示 errToast(i) 这个方法里以下内容
                errToast(i);
                break;
        }
    }

    /**
     * @param name      账号
     * @param password  密码
     * @param password2 确认密码
     * @return
     */
    private int nameAndPass(String name, String password, String password2) {
        //如果账号为空返回1
        if (TextUtils.isEmpty(name)) {
            return 1;
        }
        //如果密码为空返回2
        if (TextUtils.isEmpty(password)) {
            return 2;
        }
        //如果第二次输入的密码为空返回2
        if (TextUtils.isEmpty(password2)) {
            return 2;
        }
        //如果账号与密码不匹配返回4
        if (!password.equals(password2)) {
            return 4;
        }
        //如果都正确就返回0
        return 0;
    }
}



