package com.example.testqq.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.testqq.R;
import com.example.testqq.adapter.GroupInfoAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * 群员列表页面
 * Created by 宋宝春 on 2017/3/31.
 */
public class GroupInfoActivity extends BaseActivity implements View.OnClickListener {
    private Button addbtn;
    private EditText uresenameedt;
    private ListView listView;
    private String groupId;
    private GroupInfoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        initialize();
        setAdapter();
    }

    private void initialize() {
        addbtn = (Button) findViewById(R.id.group_info_add_btn);
        uresenameedt = (EditText) findViewById(R.id.group_info_ursename);
        listView = (ListView) findViewById(R.id.group_info_list_view);
        groupId = getIntent().getStringExtra("groupId");
        addbtn.setOnClickListener(this);

    }
//群主添加好友
    private void addFriend() {
        String newmembersStr = getEdtext(uresenameedt);
        final String[] newmembers = newmembersStr.split(",");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //群主加人调用此方法
                    EMClient.getInstance().groupManager().addUsersToGroup(groupId, newmembers);//需异步处理
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    //设置适配器
  private void setAdapter(){
      adapter=new GroupInfoAdapter(GroupInfoActivity.this,groupId);
      listView.setAdapter(adapter);

  }
    @Override
    public void onClick(View v) {
        addFriend();
    }
}
