package com.example.testqq.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.testqq.R;
import com.example.testqq.activity.HomepageActivity;
import com.example.testqq.activity.PrivateMessageActivity;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 宋宝春 on 2017/3/23.
 */

public class InformationAdapter extends BaseAdapter {
    private Context context;
    private List<EMConversation> list;
    private Long timeMessage;
    private  String userName,ss;
    private HomepageActivity activity;
    private Map<String,String> map=new HashMap<>();
    public InformationAdapter(Context context, List<EMConversation> list, HomepageActivity a) {
        this.context = context;
        this.list = list;
        this.activity=a;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;

        //获取回话的Item
        EMConversation item = (EMConversation) getItem(position);
        if (convertView==null){

            convertView=View.inflate(context, R.layout.item_information,null);
            viewHolder= new ViewHolder();
            //初始化控件
            viewHolder.rmove= (Button) convertView.findViewById(R.id.item_information_remove_button);
            viewHolder.name= (TextView) convertView.findViewById(R.id.item_information_name);
            viewHolder.message= (TextView) convertView.findViewById(R.id.item_information_message);
            viewHolder.time= (TextView) convertView.findViewById(R.id.item_information_time);
            viewHolder.relativeLayout= (RelativeLayout) convertView.findViewById(R.id.item_information_relative_layout);
            viewHolder.unread= (TextView) convertView.findViewById(R.id.item_information_unread);
            //存标签
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        //获取对应item的用户名
         userName = item.getUserName();
        if (!TextUtils.isEmpty(map.get(userName))){
            viewHolder.message.setText("[草稿]"+map.get(userName));
        }else {

            try {
                EMMessage lastMessage = item.getLastMessage();

                switch (lastMessage.getType()){
                    case TXT:
                        ss=item.getLastMessage().getBody().toString();
                        viewHolder.message.setText(ss);
                        break;
                    case IMAGE:
                        viewHolder.message.setText("[图片]");
                        break;
                    case VIDEO:
                        viewHolder.message.setText("[视频]");
                        break;
                }

            } catch (Exception e) {
                ss = "";
                e.printStackTrace();
            }
            //将消息赋值给Textview



        }
        DateFormat dateFormat = new SimpleDateFormat("MM—dd HH:mm");
    if (item.getLastMessage()!=null){


        viewHolder.time.setText(dateFormat.format(item.getLastMessage().getMsgTime()));
        //将消息最后的显示时间赋值
     //
    //   viewHolder.time.setText(getTimeMessage((EMConversation) getItem(position)));
        //赋值给textview，，
        viewHolder.name.setText(userName);
        //定义一个接收消息的字符串

        viewHolder.unread.setText(getweidu()+"");
}
        //删除
        viewHolder.rmove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                romev(position);
            }
        });

   viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Intent intent = new Intent(context, PrivateMessageActivity.class);
           EMConversation emc = (EMConversation) getItem(position);
           intent.putExtra("ursename", emc.getUserName());
           activity.tiaozhuan(intent,101);
           notifyDataSetChanged();
           o();
       }
   });
        return convertView;
    }
    public void upData(List<EMConversation> list){
        this.list=list;
        this.notifyDataSetChanged();
    }
    public void romev(int i){
        EMConversation emCon = list.get(i);
        EMClient.getInstance()
                .chatManager()
                .deleteConversation(emCon.getUserName(), true);
        list.remove(i);
        this.notifyDataSetChanged();

    }
    private int  getweidu(){
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
       return conversation.getUnreadMsgCount();
    }
    public void o(){
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
//指定会话消息未读数清零
        conversation.markAllMessagesAsRead();
//把一条消息置为已读
        conversation.markMessageAsRead(ss);
//所有未读消息数清零
        EMClient.getInstance().chatManager().markAllConversationsAsRead();
    }

    public void setMap(Map<String, String> map) {
        this.map=map;
         notifyDataSetChanged();
    }

    class ViewHolder{
        TextView name,message,time,unread;
        RelativeLayout relativeLayout;
        Button rmove;
    }

    private String getTimeMessage(EMConversation item) {
        //消息最后一次显示的时间
        timeMessage=item.getLastMessage().getMsgTime();
        //收到消息距离当前的最后时间
        long time = new Date().getTime() - timeMessage;
        //调用毫秒转分钟的方法
        int Min=MSSwitchMin(time);
        //判断是否大于60分钟，如果大于就转换成小时
        if (Min>60){
            //判断是否大于24小时，如果大于就显示几天前
            if (MinSwitchHour(Min)>24){
                return HourSwitchDay(MinSwitchHour(Min))+"天前";
            }
            return MinSwitchHour(Min)+"小时前";
        }else {
            //判断是否大于1分钟，如果大于则显示几分钟前，否则就显示刚刚。
            if (Min>1)
                return Min+"分钟前";
            else
                return "刚刚";
        }
    }

    /**
     *  定义时间的换算方法
     */
    //毫秒秒转分
    public int MSSwitchMin(long time){
        return (int) (time/1000/60);
    }
    //分钟转小时
    public  int MinSwitchHour(long time){
        return (int) (time/60);
    }
    //小时转天
    public int HourSwitchDay(long time){
        return (int) (time/24);
    }


}
