package com.example.testqq.activity;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
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

import com.example.testqq.fragment.ExpressionFragment;
import com.example.testqq.fragment.InformationFragment;
import com.example.testqq.fragment.PictureFragment;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.exceptions.EMServiceNotReadyException;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;


/**
 * 会话页
 * Created by 宋宝春 on 2017/3/27.
 */
public class PrivateMessageActivity extends BaseActivity implements EMCallBack, EMMessageListener, View.OnClickListener, TextWatcher, OnFileDownloadStatusListener {
    String newFileDir = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath()
            + File.separator
            + "FileDownloader";
    private ListView listView;
    private Button sendbtn, imagebtn, voidbtn,expressionbtn;
    private EditText editText;
    private List<EMMessage> list;
    private String urseName;
    private String groupId, msgId;
    private PrivateMessageAdapter mesageAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String text;
    private EMMessage imageSendMessage;
    private PictureFragment pictureFragment;
    private ExpressionFragment expressionFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FrameLayout a,aa;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);
        //收到消息监听
        EMClient.getInstance().chatManager().addMessageListener(this);
        // EMClient.getInstance().chatManager().addMessageListener(msgListener);
        //注册广播接收器
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
                registerReceiver(new CallReceiver(), callFilter);
        initView();
        //下载监听
        FileDownloader.registerDownloadStatusListener(this);
        setListViewAdapter();
        setActionBar();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(this);
        // 取消下载监听
        FileDownloader.unregisterDownloadStatusListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        listView = (ListView) findViewById(R.id.private_message_listview);
        sendbtn = (Button) findViewById(R.id.private_message_sendbtn);
        imagebtn = (Button) findViewById(R.id.private_message_image_btn);
        voidbtn = (Button) findViewById(R.id.private_message_void_btn);
        expressionbtn = (Button) findViewById(R.id.private_message_expression_btn);
        editText = (EditText) findViewById(R.id.private_message_edtext);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.private_message_swipe_refresh_layout);
       a= (FrameLayout) findViewById(R.id.a);
        aa= (FrameLayout) findViewById(R.id.aq);
       pictureFragment = new PictureFragment();
        expressionFragment=new ExpressionFragment();
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
        voidbtn.setOnClickListener(this);
        expressionbtn.setOnClickListener(this);
        editText.setText(text);
        data();
    }

    /**
     * 获取聊天记录
     *
     * @return
     */
    private List<EMMessage> liaotianjilu() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(urseName);
//获取此会话的所有消息
        List<EMMessage> messages1 = conversation.getAllMessages();
//SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多

        for (EMMessage s : list) {
            msgId = s.getMsgId();
        }
//获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
        List<EMMessage> messages = conversation.loadMoreMsgFromDB(msgId, 20);
        return messages;
    }

    /**
     * 下拉加载数据
     */
    private void data() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<EMMessage> data = liaotianjilu();
                        mesageAdapter.upData(data);
                        swipeRefreshLayout.setRefreshing(false);
                        //    Toast.makeText(getActivity(), "数据已更新", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        });
    }

    /**
     * 设置ActionBar的标题
     */
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
        //设置适配器
        listView.setAdapter(mesageAdapter);

    }


    /**
     * 发送文本消息消息
     *
     * @param strmessage 文本内容字符串
     */
    private void sendMessageText(String strmessage) {
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

    /**
     * 发送图片消息
     *
     * @param path 图片路径
     */
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

    /**
     * 发送视频消息
     */
    private void sendVoid() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_SHOW_ACTION_ICONS, 30);
        startActivityForResult(intent, 106);
    }


    /**
     * 发送一条消息
     *
     * @param message 消息内容类型
     */
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

    /**
     * 关闭Fragment
     */
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
    private void closeexpressionFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(expressionFragment);
        fragmentTransaction.commit();
        //从fragment的返回棧中移除fragment
        fragmentManager
                .popBackStackImmediate(
                        "message_expression_fragment"
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

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        Log.e("onMessageReceived", "收到消息" + list);
        this.list.addAll(list);
        mesageAdapter.upData(this.list);
        // 修改过 本地缩略图路径的 视频消息集合
        ArrayList<EMMessage> list1 = new ArrayList<>();
        for (EMMessage msg:list){
            switch (msg.getType()){
                case VIDEO:
                  EMVideoMessageBody text1= (EMVideoMessageBody) msg.getBody();
                    String name = System.currentTimeMillis() + "/" + ".jpg";
                    FileDownloader.createAndStart(text1.getThumbnailUrl(),newFileDir,name);

                    // 把本地路径设置给消息体
                    text1.setLocalThumb(newFileDir + "/" + name);
                    // 把消息体添加到 消息对象中
                    msg.addBody(text1);
                    // 把修改过的消息对象添加到集合中
                    list1.add(msg);
                    // 处理过的消息添加到数据源
                    this.list.add(msg);
                    break;
                default:
                    // 添加到数据源
                    this.list.add(msg);
                    break;
            }
            // 把消息导入到数据库
            EMClient.getInstance().chatManager().importMessages(list1);
            // 刷新listview
            mesageAdapter.notifyDataSetChanged();
            Log.e("onMessageReceived", "onMessageReceived" + list.size());
        }
    }
//

    @Override
    public void onClick(View v) {
        String edtext = getEdtext(editText);

        switch (v.getId()) {
            case R.id.private_message_sendbtn:
                try {
                    sendMessageText(edtext);
                    mesageAdapter.upData(list);
                    sendBroadcast(new Intent("send"));
                } catch (Exception e) {
                    e.printStackTrace();
                    toastShow(this, "请输入消息内容");
                }
                editText.setText("");
                break;
            case R.id.private_message_image_btn:
                  a.setVisibility(View.VISIBLE);
                  aa.setVisibility(View.GONE);
                if (pictureFragment.isAdded()) {
                    closeImgFragment();
                } else {
                    openImgFragment();
                }
                break;

            case R.id.private_message_void_btn:
                sendVoid();
                break;
            case R.id.private_message_expression_btn:
                a.setVisibility(View.GONE);
                aa.setVisibility(View.VISIBLE);
                if (expressionFragment.isAdded()){
                    closeexpressionFragment();
                }else {

                    openexpressionFragment();
                }
             //   toastShow(this,"点了");
                break;

        }
    }



    /**
     * 加载fragment
     */
    private void openImgFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.a, pictureFragment);
        fragmentTransaction.addToBackStack("message_bottom_fragment");
        fragmentTransaction.commit();
    }
    private void openexpressionFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.aq, expressionFragment);
        fragmentTransaction.addToBackStack("message_expression_fragment");
        fragmentTransaction.commit();
        toastShow(this,"点了");
    }
    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 106:
                if (resultCode == RESULT_OK) {
                    setVoide(data);
                }
                break;
        }
    }

    private void setVoide(Intent data) {
        //获取文件路径
        String path = getPath(data.getData());
        //实例化媒体播放类
        MediaPlayer me=new MediaPlayer();
        try {

            me.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取视频的时长
        int duration = me.getDuration();
        Log.e("视频时长","duration="+duration);
        //实例化获取帧数据
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        //设置数据源
        mediaMetadataRetriever.setDataSource(path);
        //获取某一针图像
        Bitmap frameAtTime = mediaMetadataRetriever.getFrameAtTime(1000);
        File f = pictureFragment.creatBitMap(frameAtTime);
        //释放资源
        mediaMetadataRetriever.release();
        me.release();
        EMMessage videoSendMessage = EMMessage.createVideoSendMessage(
                path ,//视频路径
                f.getAbsolutePath()   //视频预览路径
                , duration                //视频时长
                , urseName);//用户名
        sendMessage(videoSendMessage);
    }

    /**
     * 根据视频文件的URI获取文件路径
     *
     * @param uri 视频文件URi
     * @return 文件路径
     */
    private String getPath(Uri uri) {
        //定义需要查询的字段  路径
        String[] projection = {MediaStore.Video.Media.DATA};
        //查询Uri
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        //获取 所需要的字段 对应的列下标
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        //将游标指针移动到第一个
        cursor.moveToFirst();
        //返回根据字段下标获取数据
        return cursor.getString(column_index);
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
    public void getEdit(String s){
        String edtext = getEdtext(editText);
        editText.setText(edtext+s);

    }
    //成功
    @Override
    public void onSuccess() {
        Log.e("onSuccess", "成功xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    }

    //失败
    @Override
    public void onError(int i, String s) {
        Log.e("OnError", "失败xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = " + i + "　　" + s);
    }

    @Override
    public void onProgress(int i, String s) {

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



    @Override
    public void onFileDownloadStatusWaiting(DownloadFileInfo downloadFileInfo) {
        
    }

    @Override
    public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) {

    }

    @Override
    public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) {

    }

    @Override
    public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float downloadSpeed, long remainingTime) {

    }

    @Override
    public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) {

    }

    @Override
    public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo) {
        list.clear();
//        //获取此会话的所有消息
//        list = (ArrayList<EMMessage>) conversation.getAllMessages();

        mesageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason failReason) {

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

