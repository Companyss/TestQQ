package com.example.testqq.activity;

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
    private EditText zhanghao, mima;
    private EditText mima2;
    private Button zhuce, quxiaozhuce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);
        initView();
    }

    //初始化所有的控件
    private void initView() {
        zhanghao = (EditText) findViewById(R.id.zhanghao);
        mima = (EditText) findViewById(R.id.mima);
        mima2 = (EditText) findViewById(R.id.mima2);
        zhuce = (Button) findViewById(R.id.zhuce);
        quxiaozhuce = (Button) findViewById(R.id.quxiaozuce);
        zhuce.setOnClickListener(this);
        quxiaozhuce.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        //先获取账号和密码
        String name = zhanghao.getText().toString();
        String password = mima.getText().toString();
        int i = nameAndPass(name, password, mima2.getText().toString());
        switch (i) {
            case 0:
                //返回0
                try {
                    //输入的账号和密码符合，从服务端创建一个新的账号密码
                    EMClient.getInstance().createAccount(name, password);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                break;
            default:
                //不符合就提示 erro(i) 这个方法里以下内容
                erro(i);
                break;
        }
    }

    private void erro(int i) {
        String str = "";
        switch (i) {
            case 1:
                str = "账号不能为空";
                break;
            case 2:
                str = "密码不能为空";
                break;
            case 3:
                str = "密码不一致";
                break;
            default:
                str = "账号活密码不符合规定";
                break;
        }
    }

    /**
     *
     * @param name  账号
     * @param password  密码
     * @param password2   确认密码
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
        //如果第二次输入的密码为空返回3
        if (TextUtils.isEmpty(password2)) {
            return 3;
        }
        //如果账号与密码不匹配返回4
        if (!password.equals(name)) {
            return 4;
        }
        //如果都正确就返回0
        return 0;
    }
}



