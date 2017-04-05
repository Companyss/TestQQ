package com.example.testqq.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.testqq.R;
import com.example.testqq.adapter.PrivateMessageAdapter;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

/**
 * 群消息页面
 * Created by 宋宝春 on 2017/3/30.
 */
public class GroupMessageActivity extends BaseActivity implements EMCallBack, View.OnClickListener, EMMessageListener {
    private List<EMMessage>   list;
    private EditText message;
    private Button send;
    private TextView groupMessage,titlename;
    private ListView listView;
    private String ursename,groupId;
    private PrivateMessageAdapter adapter;
    private SharedPreferences sp;
    private static  String s1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
        initView();
        init();
        //注册监听
        EMClient.getInstance().chatManager().addMessageListener(this);
        setTitleName();
    }
    private void  initView(){
        message= (EditText) findViewById(R.id.group_message_edtext);
        send= (Button) findViewById(R.id.group_message_sendbtn);
        groupMessage= (TextView) findViewById(R.id.group_message_news);
        titlename= (TextView) findViewById(R.id.group_message_title_name);
        listView= (ListView) findViewById(R.id.group_message_listview);
        adapter=new PrivateMessageAdapter(this,list);
        sp=getSharedPreferences("as", this.MODE_PRIVATE);
        ursename=getIntent().getStringExtra("ursename");
        groupId = getIntent().getStringExtra("groupId");
        send.setOnClickListener(this);
        groupMessage.setOnClickListener(this);
        titlename.setOnClickListener(this);
    }
    private void init(){
        initDate();
        adapter=new PrivateMessageAdapter(this,list);
        listView.setAdapter(adapter);
    }

    private void initDate() {
        if (TextUtils.isEmpty(groupId)) {
            //获取单个回话
            EMConversation convertion = EMClient.getInstance()
                    .chatManager().getConversation(ursename);
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

    private void sendMessage(String msgStr){
        EMMessage message;
        //创建一条文本消息，content为消息文字内容
        if (TextUtils.isEmpty(ursename)){
            //创建一条文本消息，content为消息文字内容
            message = EMMessage.createTxtSendMessage(msgStr, groupId);
        }else {
            //创建一条文本消息，content为消息文字内容
            //toChatusername为对方用户或聊天ID
            message = EMMessage.createTxtSendMessage(msgStr, ursename);

        }
        //如果是群聊，设置chattype，默认是单聊
        if (TextUtils.isEmpty(ursename)){
            //创建一条文本消息，content为消息文字内容
            //toChatusername为对方用户或聊天ID
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        //TODO 设置聊天类型
        //设置状态消息回调
        message.setMessageStatusCallback(this);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        addmsg2list(message);
    }

    private void addmsg2list(EMMessage message) {
             list.add(message);
             adapter.notifyDataSetChanged();
         //设置被选的item在最后一位
        listView.setSelection(listView.getBottom());
    }
 private void xiugai(final String name){
     new Thread(new Runnable() {
         @Override
         public void run() {
             //修改群名称
             try {
                 EMClient.getInstance().groupManager().changeGroupName(groupId,name);//需异步处理
             } catch (HyphenateException e) {
                 e.printStackTrace();
             }
         }
     }).start();

 }
    @Override
    public void onSuccess() {

    }
    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onProgress(int i, String s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.group_message_sendbtn:
                try {
                    sendMessage(getEdtext(message));
                }catch (Exception e){
                    toastShow(GroupMessageActivity.this,"消息内容为空了！");
                }
                message.setText("");
                break;
            case R.id.group_message_news:
                if (!TextUtils.isEmpty(groupId)) {
                    Intent intent = new Intent(GroupMessageActivity.this, GroupInfoActivity.class);
                    intent.putExtra("groupId", groupId);
                    startActivity(intent);
                }
                break;
            case R.id.group_message_title_name:
                titlename();
                break;

        }
    }

    private void setTitleName(){
        if (TextUtils.isEmpty(groupId)) {
            titlename.setText(ursename);
            groupMessage.setVisibility(View.GONE);
        } else {
            titlename.setText(groupId);
            groupMessage.setVisibility(View.VISIBLE);
        }
    }
    private void titlename(){
        String s = titlename.getText().toString();
        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        final EditText ed=new EditText(this);
        ad.setTitle("修改群名称");
        ed.setHint(s);
        ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {

           @Override
           public void onClick(DialogInterface dialog, int which) {
               s1 = ed.getText().toString();
               sp.edit().putString("name",s1).commit();
               xiugai(sp.getString("name","没有"));
               dialog.dismiss();
           }
       });
       ad.setNegativeButton("取消",null);
        ad.setView(ed);
       ad.show();

   }
    @Override
    public void onMessageReceived(final List<EMMessage> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toastShow(GroupMessageActivity.this,"sad");
                for(EMMessage message:list){
                    addmsg2list(message);
                }
            }
        });
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
}
