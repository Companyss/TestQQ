package com.example.testqq.activity;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.testqq.R;
import com.example.testqq.adapter.PictureRecyclerViewAdapter;
import com.example.testqq.adapter.PrivateMessageAdapter;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.EMServiceNotReadyException;

import java.io.File;

import java.util.ArrayList;

import java.util.List;


/**
 * 会话页
 * Created by 宋宝春 on 2017/3/27.
 */
public class PrivateMessageActivity extends BaseActivity implements EMCallBack, EMMessageListener, View.OnClickListener, TextWatcher {
    public static String[] picture = new String[]{".jpg", ".png", ".gif", ".bmp"};
    private ListView listView;
    private Button sendbtn, imagebtn, voidbtn, yuyinbtn, sendimage;
    private EditText editText;
    private List<EMMessage> list;
    private String urseName;
    private String groupId;
    private PrivateMessageAdapter mesageAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String text;
    private TextView name, yuyin;
    private List<File> pictruelist;
    private PictureRecyclerViewAdapter pictureRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<String> imagelist = new ArrayList<>();
    private LinearLayout layout;
    private EMMessage imageSendMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);
        //收到消息监听
        EMClient.getInstance().chatManager().addMessageListener(this);
        // EMClient.getInstance().chatManager().addMessageListener(msgListener);
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        registerReceiver(new CallReceiver(), callFilter);
        init1();
        initView();
        getPicture();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    /**
     * 初始化控件
     */
    private void init1() {
        listView = (ListView) findViewById(R.id.private_message_listview);
        sendbtn = (Button) findViewById(R.id.private_message_sendbtn);
        imagebtn = (Button) findViewById(R.id.private_message_image_btn);
        voidbtn = (Button) findViewById(R.id.private_message_void_btn);
        yuyinbtn = (Button) findViewById(R.id.private_message_yuyin_btn);
        editText = (EditText) findViewById(R.id.private_message_edtext);
        name = (TextView) findViewById(R.id.private_message_name_text);
        yuyin = (TextView) findViewById(R.id.private_message_yuyin_text);
        recyclerView = (RecyclerView) findViewById(R.id.private_message_image_recyclerView);
        layout = (LinearLayout) findViewById(R.id.private_message_layout);
        sendimage = (Button) findViewById(R.id.send_image);
        //获取用户名
        urseName = getIntent().getStringExtra("ursename");
        //获取群组ID
        groupId = getIntent().getStringExtra("groupId");
        //获取草稿文本
        String text = getIntent().getStringExtra("text");
        pictruelist = new ArrayList<>();

        sendbtn.setOnClickListener(this);
        //获取没有发送的文本
        editText.addTextChangedListener(this);
        yuyin.setOnClickListener(this);
        imagebtn.setOnClickListener(this);
        sendbtn.setOnClickListener(this);
        editText.setText(text);


    }

    /**
     * 给 recyclerView设置适配器
     */
    private void init() {
        //实例化适配器
        pictureRecyclerViewAdapter = new PictureRecyclerViewAdapter(imagelist, PrivateMessageActivity.this);
         //获取recyclerview的线性布局管理器
        LinearLayoutManager lm = new LinearLayoutManager(this);
        //设置线性布局为水平布局
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置布局管理器
        recyclerView.setLayoutManager(lm);
        //给recyclerView设置适配器
        recyclerView.setAdapter(pictureRecyclerViewAdapter);
    }

    /**
     * 给listView设置消息适配器
     */
    private void initView() {
        //获取数据内容
        initDate();
        //实例化适配器
        mesageAdapter = new PrivateMessageAdapter(this, list);
        //设置是陪你
        listView.setAdapter(mesageAdapter);
        name.setText(urseName);
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
        //注册消息接听
        message.setMessageStatusCallback(this);

//发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        addEmmessage(message);
        text = "";
    }

    private void sendImage(String path) {
        //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        imageSendMessage = EMMessage.createImageSendMessage(path, false, urseName);

        EMClient.getInstance().chatManager().sendMessage(imageSendMessage);
        addEmmessage(imageSendMessage);
    }

    //添加数据
    private void addEmmessage(EMMessage emmessage) {
        list.add(emmessage);
        mesageAdapter.notifyDataSetChanged();
        //设置被选的item在最后一位
        //  listView.setSelection(listView.getBottom());
    }

    /**
     * 返回数据
     * 对话数据源
     */
    public void initDate() {
        if (TextUtils.isEmpty(groupId)) {
            //获取单个回话
             EMConversation  convertion = EMClient.getInstance()
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
            case R.id.private_message_yuyin_text:
                voicecall();
                Splik(PrivateMessageActivity.this, new Intent(PrivateMessageActivity.this, VoiceCallActivity.class));
                break;
            case R.id.private_message_image_btn:

                if (layout.getVisibility() == View.GONE) {

                    layout.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.GONE);
                }
                break;
            case R.id.send_image:
                imageSendMessage.setChatType(EMMessage.ChatType.GroupChat);

                //     sendImage();

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
     *语音通话
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

    //获取图片的路径
    public void getPicture() {

        Uri externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor query = contentResolver.query(externalContentUri, null, null, null, null);
        while (query.moveToNext()) {
            String string = query.getString(query.getColumnIndex(MediaStore.Images.Media.DATA));
            imagelist.add(string);
        }
    }
}

