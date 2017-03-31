package com.example.testqq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.testqq.R;
import com.example.testqq.activity.BaseActivity;
import com.example.testqq.adapter.GroupListAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 宋宝春 on 2017/3/30.
 */

public class GroupActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private List<EMGroup> groupList;
    private GroupListAdapter groupListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridegroup);
        initView();
    }
    private void initView(){
        listView = (ListView) findViewById(R.id.group_listview);
        groupList=new ArrayList<>();
        getData();
        groupListAdapter=new GroupListAdapter(this,groupList);
        listView.setAdapter(groupListAdapter);
        listView.setOnItemClickListener(this);
    }



  private void getData(){
      //从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
      try {
          groupList = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理
      } catch (HyphenateException e) {
          e.printStackTrace();
      }

//从本地加载群组列表
      groupList = EMClient.getInstance().groupManager().getAllGroups();
  }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EMGroup item = (EMGroup) groupListAdapter.getItem(position);
        intentTo(GroupMessageActivity.class,item.getGroupId());
    }
    //携带String类型的groupId的跳转
    public   void intentTo(Class<?> calss,String groupId){
        Intent intent=new Intent(this,calss);
        intent.putExtra("groupId",groupId);
       this.startActivity(intent);

    }

}
