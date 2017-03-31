package com.example.testqq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.testqq.R;
import com.example.testqq.activity.FoundGroupActivity;
import com.example.testqq.activity.GroupActivity;
import com.example.testqq.adapter.LinkmanAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.exceptions.HyphenateException;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

/**
 * 联系人列表页
 * Created by 宋宝春 on 2017/3/22.
 */

public class LinkmanFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ListView listView;
    private List<String>  list;
    private EditText name,message;
    private Button addbtn,addfroupbtn,grouplist;
    private String  strmess,strname;
    private List<String>  newlist=new ArrayList<>();
    private Handler hander;
    private LinkmanAdapter linkmanAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //加载联系列表的布局文件    并将其返回
        view=inflater.inflate(R.layout.fragment_linkman,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        add();
        getFriend();
        listView.setAdapter(new LinkmanAdapter(getActivity(),list));
    }
    private void add(){
        try {
            EMClient.getInstance().contactManager().addContact(strname, strmess);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        message.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
    }
    private void init(){
        listView= (ListView) view.findViewById(R.id.linkman_list_view);
        name= (EditText) view.findViewById(R.id.linkman_name);
        message= (EditText) view.findViewById(R.id.linkman_message);
        addbtn= (Button) view.findViewById(R.id.linkman_add_button);
        addfroupbtn= (Button) view.findViewById(R.id.linkman_found_group_button);
        grouplist= (Button) view.findViewById(R.id.linkman_group_list);
         linkmanAdapter = new LinkmanAdapter(getActivity(), newlist);
        list=new ArrayList<String>();
        addbtn.setOnClickListener(this);
        addfroupbtn.setOnClickListener(this);
        grouplist.setOnClickListener(this);
        hander=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==1){
                    listView.setAdapter(linkmanAdapter);
                }
            }
        };
        listView.setAdapter(linkmanAdapter);

    }

    private void getFriend() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    list = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    if (list!=null) {
                        newlist.addAll(list);
                    }

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                hander.sendEmptyMessage(1);
            }
        }).start();

        }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linkman_add_button:
                addFriend();
                break;
            case R.id.linkman_found_group_button:
                Intent intent=new Intent(getActivity(),FoundGroupActivity.class);
                startActivity(intent);
                break;
            case R.id.linkman_group_list:
                Intent intent1=new Intent(getActivity(),GroupActivity.class);
                startActivity(intent1);
                break;
        }

    }

    private void addFriend() {
        message.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        strmess  = message.getText().toString();
        strname = name.getText().toString();
        newlist.add(strname);
        linkmanAdapter.upData(newlist);
    }
}
