package com.example.testqq.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.testqq.R;
import com.example.testqq.activity.FoundGroupActivity;
import com.example.testqq.activity.GroupActivity;
import com.example.testqq.adapter.LinkmanAdapter;
import com.hyphenate.EMContactListener;
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

public class LinkmanFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener, AdapterView.OnItemLongClickListener {
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
        message.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        getFriend();
        //注册好友状态监听
        EMClient.getInstance().contactManager().setContactListener(getEMContactListener());
    }

    /**
     * 初始化数据
     */
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
        addbtn.setOnLongClickListener(this);
        addfroupbtn.setOnClickListener(this);
        grouplist.setOnClickListener(this);
        hander=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==1){
                    listView.setAdapter(new LinkmanAdapter(getActivity(),newlist));
                }
            }
        };
        listView.setOnItemLongClickListener(this);

    }

    /**
     * 添加好友
     */
    private void add1(){
        try {
            strmess  = message.getText().toString();
            strname = name.getText().toString();
            //同意好友请求
            EMClient.getInstance().contactManager().acceptInvitation(strname);
            //添加好友 参数为要添加的好友的username和添加理由
            EMClient.getInstance().contactManager().addContact(strname, strmess);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取好友列表
     */
    private void getFriend() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   //获取好友列表 获取好友的 username list，开发者需要根据 username 去自己服务器获取好友的详情。
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


    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linkman_add_button:
                try {
                  strname.equals("");
                }catch (Exception e){
                    Toast.makeText(getActivity(),"用户名为空",Toast.LENGTH_SHORT).show();
                }
                add1();
                UpDataFriend();
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

    /**
     * 添加好友并刷新
     */
    private void UpDataFriend() {
        newlist.add(strname);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                linkmanAdapter.upData(newlist);
            }
        });
    }

    /**
     * 好友状态监听
     * @return
     */
    private EMContactListener getEMContactListener(){
        EMContactListener emContactListener=new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message.setVisibility(View.GONE);
                        name.setVisibility(View.GONE);
                    }
                });

            }

            @Override
            public void onContactDeleted(String s) {

            }

            @Override
            public void onContactInvited(String s, String s1) {

            }

            @Override
            public void onContactAgreed(String s) {

            }

            @Override
            public void onContactRefused(String s) {

            }
        };
                return emContactListener;
    }

    /**
     * 长按监听
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        message.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        return true;
    }


    private void delete(int i){
        try {
            EMClient.getInstance().contactManager().deleteContact(newlist.get(i));
        } catch (HyphenateException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder a=new AlertDialog.Builder(getActivity());
        a.setTitle("是否删除好友");
        a.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(position);
                newlist.remove(position);
                        linkmanAdapter.upData(newlist);
        }
        });
        a.setNegativeButton("取消",null);
        a.show();
        return false;
    }
}
