package com.example.testqq.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testqq.R;
import com.example.testqq.activity.HomepageActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息列表页
 * Created by 宋宝春 on 2017/3/22.
 */

public class InformationFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemLongClickListener, EMCallBack {
    private ListView listView;
    private View view;
    private List<EMConversation> list;
    private EditText message, account;
    private Button send;
    private InformationAdapter adapter;
   private TextView tv;
    private SwipeRefreshLayout swipeRefreshLayout;
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
        getActivity().registerReceiver(new MyB(),new IntentFilter("send"));
        initialize();
        setListView();
        getMessage();
        ad();
        upDate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        romeMessageListener();
    }

    //初始化
    private void initialize() {
        listView = (ListView) view.findViewById(R.id.information_list_view);
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.information_swipe_refresh_layout);
        message = (EditText) view.findViewById(R.id.main_message);
        account = (EditText) view.findViewById(R.id.main_account);
        tv= (TextView) view.findViewById(R.id.information_text_view);
        send = (Button) view.findViewById(R.id.main_send);

        //设置监听
        send.setOnClickListener(this);
        listView.setOnItemLongClickListener(this);
        list = new ArrayList<EMConversation>();
    }
private void ad(){
    if (list.size()==0){
        listView.setEmptyView(tv);
    }else {
        tv.setVisibility(View.GONE);
    }

}
    //设置适配器
    private void setListView() {
        List<EMConversation> data = getData();
        HomepageActivity activity = (HomepageActivity) getActivity();
        adapter = new InformationAdapter(getActivity(), data,activity);
        listView.setAdapter(adapter);

    }

    //查询会话的数据
    private List<EMConversation> getData() {
        list.clear();
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
            case R.id.main_send://点击发送
                //调用发送文本消息方法
                sendMessage();
                List<EMConversation> data = getData();
                adapter.upData(data);
                account.setText("");
                message.setText("");

                break;
        }
    }
    //下拉刷新
private void upDate(){
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
     @Override
     public void onRefresh() {
        new  Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<EMConversation> data = getData();
                adapter.upData(data);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "数据已更新", Toast.LENGTH_SHORT).show();
            }
        },1000);
     }
 });

}


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
        ad.setTitle("是否删除会话");
        ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Toast.makeText(getActivity(), "已删除", Toast.LENGTH_SHORT).show();
                    adapter.romev(position);
                } catch (Exception e) {
                         e.printStackTrace();
                }
            }
        });
        ad.setNegativeButton("取消", null);
        ad.show();
        return true;
    }

    //发送消息
    private void sendMessage() {
        String messageStr = message.getText().toString();
        String accountStr = account.getText().toString();

//创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        if (TextUtils.isEmpty(messageStr)) {
            Toast.makeText(getActivity(), "消息内容为空了！", Toast.LENGTH_SHORT).show();
        }else {
            EMMessage message = EMMessage.createTxtSendMessage(messageStr, accountStr);
            message.setMessageStatusCallback(this);
            //发送消息
            EMClient.getInstance().chatManager().sendMessage(message);
        }
    }
    //发送成功
    @Override
    public void onSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "消息发送成功", Toast.LENGTH_SHORT).show();
                adapter.upData(list);
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
            public void onMessageReceived(final List<EMMessage> messages1) {
                //收到消息
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

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
    private void paixu() {
        //集合排序依据接口   给list集合排序的方法
        Comparator com = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                //转换类型， 直接指定泛型不需要强转
                EMConversation p1 = (EMConversation) o1;
                EMConversation p2 = (EMConversation) o2;
                if (p1.getLastMessage().getMsgTime() < p2.getLastMessage().getMsgTime())
                    return 1;
                else if (p1.getLastMessage().getMsgTime() == p2.getLastMessage().getMsgTime())
                    return 0;
                else if (p1.getLastMessage().getMsgTime() > p2.getLastMessage().getMsgTime())
                    return -1;
                return 0;
            }
        };
        try {
            Collections.sort(list, com);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMap(Map<String, String> map) {
        adapter.setMap(map);
    }

    class  MyB extends BroadcastReceiver{

      @Override
      public void onReceive(Context context, Intent intent) {
          List<EMConversation> data = getData();
          adapter.upData(data);
      }
  }
}