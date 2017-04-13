package com.example.testqq.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.testqq.R;
import com.example.testqq.adapter.PrivateMessageAdapter;

import com.example.testqq.fragment.PictureFragment;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.EMServiceNotReadyException;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;


/**
 * 会话页
 * Created by 宋宝春 on 2017/3/27.
 */
public class PrivateMessageActivity extends BaseActivity implements EMCallBack, EMMessageListener, View.OnClickListener, TextWatcher {
    public static String[] picture = new String[]{".jpg", ".png", ".gif", ".bmp"};
    private ListView listView;
    private Button sendbtn, imagebtn, voidbtn, yuyinbtn;
    private EditText editText;
    private List<EMMessage> list;
    private String urseName;
    private String groupId;
    private PrivateMessageAdapter mesageAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    private String text;
    private EMMessage imageSendMessage;
    private PictureFragment pictureFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);
        //收到消息监听
        EMClient.getInstance().chatManager().addMessageListener(this);
        // EMClient.getInstance().chatManager().addMessageListener(msgListener);
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        registerReceiver(new CallReceiver(), callFilter);
        initView();

        setListViewAdapter();
        setActionBar();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }


    /**
     * 初始化控件
     */
    private void initView() {
        listView = (ListView) findViewById(R.id.private_message_listview);
        sendbtn = (Button) findViewById(R.id.private_message_sendbtn);
        imagebtn = (Button) findViewById(R.id.private_message_image_btn);
        voidbtn = (Button) findViewById(R.id.private_message_void_btn);
        yuyinbtn = (Button) findViewById(R.id.private_message_yuyin_btn);
        editText = (EditText) findViewById(R.id.private_message_edtext);
        pictureFragment = new PictureFragment();
        fragmentManager = getSupportFragmentManager();
        //获取用户名
        urseName = getIntent().getStringExtra("ursename");
        //获取群组ID
        groupId = getIntent().getStringExtra("groupId");
        //获取草稿文本
        String text = getIntent().getStringExtra("text");


        sendbtn.setOnClickListener(this);
        //获取没有发送的文本
        editText.addTextChangedListener(this);

        imagebtn.setOnClickListener(this);
        sendbtn.setOnClickListener(this);
        editText.setText(text);


    }

    //设置标题栏的title
    private void setActionBar() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(urseName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //加载标题栏的按钮布局
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_message_bar, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * ActionBar所有的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("urseName", urseName);
                intent.putExtra("text", text);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.menu_message_bar_text:
                voicecall();
                Splik(PrivateMessageActivity.this, new Intent(PrivateMessageActivity.this, VoiceCallActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 给listView设置消息适配器
     */
    private void setListViewAdapter() {
        //获取数据内容
        initDate();
        //实例化适配器
        mesageAdapter = new PrivateMessageAdapter(this, list);
        //设置是陪你
        listView.setAdapter(mesageAdapter);

    }


    /**
     * 发送消息
     *
     * @param strmessage
     */
    private void sendMessage(String strmessage) {
        EMMessage message;
//创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        if (TextUtils.isEmpty(urseName)) {
            message = EMMessage.createTxtSendMessage(strmessage, groupId);
        } else {
            //toChatusername为对方用户或聊天ID
            message = EMMessage.createTxtSendMessage(strmessage, urseName);
        }
//如果是群聊，设置chattype，默认是单聊
        if (TextUtils.isEmpty(urseName)) {
            //创建一条文本消息，content为消息文字内容
            //toChatusername为对方用户或聊天ID
            message.setChatType(EMMessage.ChatType.GroupChat);
        }


//发送消息
        sendMessage(message);
        text = "";
    }

    public void sendImage(String path) {

        //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        imageSendMessage = EMMessage.createImageSendMessage(path, false, urseName);

        if (TextUtils.isEmpty(urseName)) {
            //创建一条文本消息，content为消息文字内容
            //toChatusername为对方用户或聊天ID
            imageSendMessage.setChatType(EMMessage.ChatType.GroupChat);
        }
        sendMessage(imageSendMessage);

    }

    private void sendMessage(EMMessage message) {
        //如果是群聊，设置chattype，默认是单聊
//                if (chatType == CHATTYPE_GROUP)
        message.setChatType(EMMessage.ChatType.Chat);
        //注册消息接听
        message.setMessageStatusCallback(this);

        //发送消息
        EMClient.getInstance()
                .chatManager()
                .sendMessage(message);
        //图片发送之后 关闭图片选择fragment
        if (pictureFragment.isAdded()) {
            closeImgFragment();
        }
        list.add(message);

        //调用刷新消息列表的方法
        mesageAdapter.upData(list);
    }

    //关闭Fragment
    private void closeImgFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(pictureFragment);
        fragmentTransaction.commit();
        //从fragment的返回棧中移除fragment
        fragmentManager
                .popBackStackImmediate(
                        "message_bottom_fragment"
                        , FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * 返回数据
     * 对话数据源
     */
    public void initDate() {
        if (TextUtils.isEmpty(groupId)) {
            //获取单个回话
            EMConversation convertion = EMClient.getInstance()
                    .chatManager().getConversation(urseName);
            //获取此回话的所有消息
            if (convertion != null) {
                list = convertion.getAllMessages();
            } else {
                list = new ArrayList<EMMessage>();
            }
        } else {
            //获取单个回话
            EMConversation convertion = EMClient.getInstance()
                    .chatManager().getConversation(groupId);

            if (convertion != null) {
                //获取此回话的所有消息
                list = convertion.getAllMessages();
            } else {
                list = new ArrayList<EMMessage>();
            }
        }

    }

    //成功
    @Override
    public void onSuccess() {
        toastShow(this, "成功");
    }

    //失败
    @Override
    public void onError(int i, String s) {
        toastShow(this, "失败");
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

        switch (v.getId()) {
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
            case R.id.private_message_image_btn:
                if (pictureFragment.isAdded()) {
                    closeImgFragment();
                } else {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.a, pictureFragment);
                    fragmentTransaction.addToBackStack("message_bottom_fragment");
                    fragmentTransaction.commit();
                }
                break;
        }
    }

    /**
     * 返回键
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("urseName", urseName);
        intent.putExtra("text", text);
        setResult(RESULT_OK, intent);
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
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
        text = s.toString();
    }

    /**
     * 语音通话
     */
    private void voicecall() {

        try {//单参数
            EMClient.getInstance().callManager().makeVoiceCall(urseName);
        } catch (EMServiceNotReadyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 广播接收器 用来同步数据
     */
    class CallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 拨打方username
            String from = intent.getStringExtra("from");
            // call type
            String type = intent.getStringExtra("type");
            //跳转到通话页面
            Splik(PrivateMessageActivity.this, new Intent(PrivateMessageActivity.this, VoiceCallActivity.class));

        }
    }


}

