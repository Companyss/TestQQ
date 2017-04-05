package com.example.testqq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.testqq.R;
import com.example.testqq.bean.CreateGroupBean;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;

/**
 * 创建群组页
 * Created by 宋宝春 on 2017/3/30.
 */
public class FoundGroupActivity extends  BaseActivity implements View.OnClickListener {
    private Button fountbtn;
    private EditText groupName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foundgroup);
        initView();
    }
//初始化控件
    private void initView(){
        groupName= (EditText) findViewById(R.id.found_group_name);
        fountbtn= (Button) findViewById(R.id.found_group_found_button);
        fountbtn.setOnClickListener(this);
    }
//创建群组方法
    private void fountGroup(){
        //实例化实体类
        CreateGroupBean data = getData();
        //获取群组名称
        String groupname = data.getGroupname();
        String desc = data.getDesc();
        String[] allMembres = data.getAllMembres();
        String reason = data.getReason();
        EMGroupManager.EMGroupOptions options = data.getOptions();
        try {
            //创建
            EMClient.getInstance().groupManager().createGroup(groupname, desc, allMembres, reason, options);
            //创建成功提示
            toastShow(FoundGroupActivity.this,"创建成功");
            //关闭页面
            finish();
            //跳转群组页
          Splik(FoundGroupActivity.this,new Intent(FoundGroupActivity.this,GroupActivity.class));
        } catch (HyphenateException e) {
            e.printStackTrace();
            toastShow(FoundGroupActivity.this,"创建失败");
        }
    }
//返回实体类装载数据的对象
    public CreateGroupBean getData() {
//实例化信息类
        CreateGroupBean  cgb=new CreateGroupBean();
        //调用信息类的set方法传值
        cgb.setGroupname(getEdtext(groupName));
        cgb.setAllMembres(new String[]{});
        cgb.setReason("");
        cgb.setDesc("");
        EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
        option.maxUsers = 200;
        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
        cgb.setOptions(option);
        return cgb;
    }

    @Override
    public void onClick(View v) {
        fountGroup();
    }

}
