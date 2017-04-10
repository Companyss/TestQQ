package com.example.testqq.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.testqq.R;
import com.example.testqq.adapter.InformationAdapter;
import com.example.testqq.adapter.PrivateMessageAdapter;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.EMServiceNotReadyException;

import java.util.ArrayList;
import java.util.List;

/**
 * 会话页
 * Created by 宋宝春 on 2017/3/27.
 */
public class PrivateMessageActivity extends  BaseActivity implements EMCallBack, EMMessageListener, View.OnClickListener, TextWatcher {
    private ListView listView;
    private Button sendbtn;
    private EditText editText;
    private List<EMMessage> list;
    private String urseName;
    private  String groupId;
    private PrivateMessageAdapter mesageAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private  EMConversation convertion;
    private  String text;
    private TextView name,yuyin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);
        //收到消息监听
        EMClient.getInstance().chatManager().addMessageListener(this);
        // EMClient.getInstance().chatManager().addMessageListener(msgListener);
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        registerReceiver(new CallReceiver(),callFilter);
        init();

        initView();
        load();
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
        name= (TextView) findViewById(R.id.private_message_name_text);
        yuyin= (TextView) findViewById(R.id.private_message_yuyin_text);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.private_message_swipe_refresh_layout);
       urseName=getIntent().getStringExtra("ursename");
        groupId=getIntent().getStringExtra("groupId");
        String text = getIntent().getStringExtra("text");
        sendbtn.setOnClickListener(this);
        //获取没有发送的文本
        editText.addTextChangedListener(this);
        yuyin.setOnClickListener(this);
        editText.setText(text);
  }
private void initView(){
    initDate();
    mesageAdapter=new PrivateMessageAdapter(this,list);
    listView.setAdapter(mesageAdapter);
    name.setText(urseName);
}
public void load(){
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    list = convertion.getAllMessages();
                    mesageAdapter=new PrivateMessageAdapter(PrivateMessageActivity.this,list);
                    listView.setAdapter(mesageAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                }
            },1000);
        }
    });
}

    /**
     * 发送消息
     * @param strmessage
     */
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
        text="";
  }
    //添加数据
    private void addEmmessage(EMMessage emmessage){
         list.add(emmessage);
         mesageAdapter.notifyDataSetChanged();
        //设置被选的item在最后一位
      //  listView.setSelection(listView.getBottom());
    }
    //返回数据
    //对话数据源
    public  void initDate(){
        if (TextUtils.isEmpty(groupId)) {
            //获取单个回话
             convertion = EMClient.getInstance()
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
        mesageAdapter.upData(list);
        this.list.addAll(list);
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

        switch (v.getId()){
            case R.id.private_message_sendbtn:
                try {
                    sendMessage(edtext);
                    mesageAdapter.upData(list);
                    sendBroadcast(new Intent("send"));
                } catch (Exception e) {
                    e.printStackTrace();
                    toastShow(this, "请输入消息内容");
                }
                editText.setText("");
                break;
            case R.id.private_message_yuyin_text:
                voicecall();
                break;
        }


    }

    /**
     * 返回键
     */
    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("urseName",urseName);
        intent.putExtra("text",text);
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * 等到Edittext的文本
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
              text=s.toString();
    }

    /**
     *
     */
   private void voicecall(){

       try {//单参数
           toastShow(PrivateMessageActivity.this,"已经执行了！");
           EMClient.getInstance().callManager().makeVoiceCall(urseName);
       } catch (EMServiceNotReadyException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
   }
    class CallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 拨打方username
            String from = intent.getStringExtra("from");
            // call type
            String type = intent.getStringExtra("type");
            //跳转到通话页面
            Splik(PrivateMessageActivity.this,new Intent(PrivateMessageActivity.this,VoiceCallActivity.class));

        }
    }

}
