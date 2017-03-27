package com.example.testqq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.testqq.R;
import com.example.testqq.adapter.PrivateMessageAdapter;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 会话页
 * Created by 宋宝春 on 2017/3/27.
 */
public class PrivateMessageActivity extends  BaseActivity implements EMCallBack, EMMessageListener, View.OnClickListener {
    private ListView listView;
    private Button sendbtn;
    private EditText editText;
    private List<EMMessage> list;
    private String urseName;
    private  String groupId;
    private PrivateMessageAdapter mesageAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);
        EMClient.getInstance().chatManager().addMessageListener(this);
        init();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    private void init(){

      listView= (ListView) findViewById(R.id.private_message_listview);
      sendbtn= (Button) findViewById(R.id.private_message_sendbtn);
      editText= (EditText) findViewById(R.id.private_message_edtext);

      urseName=getIntent().getStringExtra("ursename");
      groupId=getIntent().getStringExtra("groupId");
      sendbtn.setOnClickListener(this);
  }
private void initView(){
    initDate();
    mesageAdapter=new PrivateMessageAdapter(this,list);
    listView.setAdapter(mesageAdapter);
}


    private void sendMessage(String strmessage){
        EMMessage message;
//创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        if (TextUtils.isEmpty(urseName)) {
             message = EMMessage.createTxtSendMessage(strmessage,groupId);
        }else {
            //toChatusername为对方用户或聊天ID
            message = EMMessage.createTxtSendMessage(strmessage, urseName);
        }
//如果是群聊，设置chattype，默认是单聊
        if (TextUtils.isEmpty(urseName)){
            //创建一条文本消息，content为消息文字内容
            //toChatusername为对方用户或聊天ID
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        //注册消息接听
        message.setMessageStatusCallback(this);

//发送消息
      EMClient.getInstance().chatManager().sendMessage(message);
      addEmmessage(message);
  }
    //添加数据
    private void addEmmessage(EMMessage emmessage){
         list.add(emmessage);
         mesageAdapter.notifyDataSetChanged();
        //设置被选的item在最后一位
        listView.setSelection(listView.getBottom());
    }
    //返回数据
    //对话数据源
    public  void initDate(){
        if (TextUtils.isEmpty(groupId)) {
            //获取单个回话
            EMConversation convertion = EMClient.getInstance()
                    .chatManager().getConversation(urseName);
            //获取此回话的所有消息
            list = convertion.getAllMessages();
        }else {
            //获取单个回话
            EMConversation convertion = EMClient.getInstance()
                    .chatManager().getConversation(groupId);

            if (convertion!=null) {
                //获取此回话的所有消息
                list = convertion.getAllMessages();
            }else {
                list=new ArrayList<EMMessage>();
            }
        }

    }

//成功
    @Override
    public void onSuccess() {

    }
//失败
    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onProgress(int i, String s) {

    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    @Override
    public void onClick(View v) {
        String edtext = getEdtext(editText);
        try {
            sendMessage(edtext);
        } catch (Exception e) {
            e.printStackTrace();
            toastShow(this, "请输入消息内容");
        }
        editText.setText("");
    }
}
