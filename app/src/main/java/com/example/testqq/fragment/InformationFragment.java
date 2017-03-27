package com.example.testqq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testqq.R;
import com.example.testqq.activity.PrivateMessageActivity;
import com.example.testqq.adapter.InformationAdapter;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 消息列表页
 * Created by 宋宝春 on 2017/3/22.
 */

public class InformationFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, EMCallBack {
    private ListView listView;
    private View view;
    private List<EMConversation> list;
    private EditText message, account;
    private Button send, edxt;
    private InformationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //加载消息列表的布局文件     并将其返回
        view = inflater.inflate(R.layout.fragment_infromation, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        setListView();
        getMessage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        romeMessageListener();
    }

    //初始化
    private void initialize() {
        listView = (ListView) view.findViewById(R.id.information_list_view);
        message = (EditText) view.findViewById(R.id.main_message);
        account = (EditText) view.findViewById(R.id.main_account);
        edxt = (Button) view.findViewById(R.id.main_exit);
        send = (Button) view.findViewById(R.id.main_send);

        //设置监听
        send.setOnClickListener(this);
        edxt.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        list = new ArrayList<EMConversation>();
    }

    private void setListView() {
        List<EMConversation> data = getData();
            adapter = new InformationAdapter(getActivity(), data);
        listView.setAdapter(adapter);

    }

    //查询会话的数据
    private List<EMConversation> getData() {
        Map<String, EMConversation> conversationMap = EMClient.getInstance()
                .chatManager().getAllConversations();
        for (EMConversation emConversation : conversationMap.values()) {
            list.add(emConversation);
        }
        paixu();
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_exit://点击退出
                //退出环信服务器，再次启动程序需要重新登录
                EMClient.getInstance().logout(true);
                break;
            case R.id.main_send://点击发送
                //调用发送文本消息方法
                sendMessage();
                List<EMConversation> data = getData();
                adapter.upData(data);
                account.setText(" ");
                message.setText(" ");
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PrivateMessageActivity.class);
        EMConversation emc= (EMConversation) adapter.getItem(position);
        intent.putExtra("ursename",emc.getUserName());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


        return false;
    }
//发送消息
    private void sendMessage() {
        String messageStr = message.getText().toString();
        String accountStr = account.getText().toString();
//创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(messageStr, accountStr);
        message.setMessageStatusCallback(this);
//发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

    }

    @Override
    public void onSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "消息发送成功", Toast.LENGTH_SHORT).show();

            }
        });
    }
  //发送失败
    @Override
    public void onError(int i, String s) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "消息发送失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }
 //发送成功
    @Override
    public void onProgress(int i, String s) {

    }

    //注册消息监听来接收消息
    public void getMessage() {
        EMClient.getInstance().chatManager().addMessageListener(getMessagetListener());

    }

    //移除listener
    public void romeMessageListener() {
        EMClient.getInstance().chatManager().removeMessageListener(getMessagetListener());

    }
     //监听消息的listener
    private EMMessageListener getMessagetListener() {
        EMMessageListener messageListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(final List<EMMessage> messages) {
                //收到消息
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                      //adapter.upData(messages);
                    }
                });

            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> list) {
                //收到已读回执
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> list) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        return messageListener;
    }
    //排序
    private void paixu(){
        //集合排序依据接口   给list集合排序的方法
        Comparator com=new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                //转换类型， 直接指定泛型不需要强转
                EMConversation p1=(EMConversation)o1;
                EMConversation p2=(EMConversation)o2;
                if (p1.getLastMessage().getMsgTime()<p2.getLastMessage().getMsgTime())
                    return 1;
                else if (p1.getLastMessage().getMsgTime()==p2.getLastMessage().getMsgTime())
                    return 0;
                else if (p1.getLastMessage().getMsgTime()>p2.getLastMessage().getMsgTime())
                    return -1;
                return 0;
            }
        };
        Collections.sort(list,com);
    }
}