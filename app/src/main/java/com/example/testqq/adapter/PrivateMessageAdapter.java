package com.example.testqq.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testqq.R;
import com.example.testqq.activity.PictureActivity;
import com.example.testqq.activity.VideoActivity;
import com.example.testqq.vules.SPUtils;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by 宋宝春 on 2017/3/27.
 */

public class PrivateMessageAdapter extends BaseAdapter{
    private Context context;
    private List<EMMessage> list;
    private  EMImageMessageBody text;
    public PrivateMessageAdapter(Context context, List<EMMessage> list) {
        this.context = context;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHorder viewHorder;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.meeage_item,null);
            viewHorder=new ViewHorder();
            viewHorder.setViews(convertView);
            //存标签
            convertView.setTag(viewHorder);
        }else {
            //取标签
            viewHorder= (ViewHorder) convertView.getTag();
        }
        setViewmessage(viewHorder, (EMMessage) getItem(position));
        return convertView;
    }
    //给布局控件属性赋值的方法
    private  void setViewmessage(final ViewHorder viewHolder, EMMessage emMessage){
        //设置时间布局可见
        viewHolder.timeLoy.setVisibility(View.VISIBLE);
        //设置时间与日期显示的并赋值给控件
        DateFormat dateFormat = new SimpleDateFormat("MM—dd HH:mm");
        viewHolder.time.setText(dateFormat.format(emMessage.getMsgTime()));
        //判断消息是否从这发出
        //获取消息类型
        EMMessage.Type typeMsg = emMessage.getType();
        switch (typeMsg){
            case TXT:
                if (getname().equals(emMessage.getFrom())){
                    //设置rightloy是否可见
                    viewHolder.rightLoy.setVisibility(View.VISIBLE);
                    viewHolder.liftLoy.setVisibility(View.GONE);
                    viewHolder.rightMessage.setVisibility(View.VISIBLE);
                    viewHolder.rightpictrue.setVisibility(View.GONE);
                    viewHolder.rightVoid.setVisibility(View.GONE);
                    //设置我发送的用户名
                    viewHolder.rightName.setText(getname());
                    //
                    EMTextMessageBody text = (EMTextMessageBody) emMessage.getBody();
                    //设置我发送的内容
                    viewHolder.rightMessage.setText(text.getMessage());
                }else {
                    viewHolder.rightLoy.setVisibility(View.GONE);
                    viewHolder.liftLoy.setVisibility(View.VISIBLE);
                    viewHolder.leftMessage.setVisibility(View.VISIBLE);
                    viewHolder.leftpictrue.setVisibility(View.GONE);
                    viewHolder.leftVoid.setVisibility(View.GONE);
                    //设置谁给我发送的用户名
                    viewHolder.leftName.setText(emMessage.getUserName());
                    //
                    EMTextMessageBody txt= (EMTextMessageBody) emMessage.getBody();
                    //设置发送的内容
                    viewHolder.leftMessage.setText(txt.getMessage());
                }
                 break;
            case IMAGE:
               text = (EMImageMessageBody) emMessage.getBody();
                if (getname().equals(emMessage.getFrom())){
                    //设置rightloy是否可见
                    viewHolder.rightLoy.setVisibility(View.VISIBLE);
                    viewHolder.liftLoy.setVisibility(View.GONE);
                    viewHolder.rightMessage.setVisibility(View.GONE);
                    viewHolder.rightpictrue.setVisibility(View.VISIBLE);
                    viewHolder.rightVoid.setVisibility(View.GONE);
                    //设置我发送的用户名
                    viewHolder.rightName.setText(getname());
                    //
             //        text = (EMImageMessageBody) emMessage.getBody();
                    //设置我发送的图片
                    Glide.with(context)
                            .load(text.getLocalUrl())
                            .override(300, 200)
                            .into(viewHolder.rightpictrue);
                }else {
                    viewHolder.rightLoy.setVisibility(View.GONE);
                    viewHolder.liftLoy.setVisibility(View.VISIBLE);
                    viewHolder.leftMessage.setVisibility(View.GONE);
                    viewHolder.leftpictrue.setVisibility(View.VISIBLE);
                    viewHolder.leftVoid.setVisibility(View.GONE);
                    //设置谁给我发送的用户名
                    viewHolder.leftName.setText(emMessage.getUserName());
                    //
                //    EMImageMessageBody text = (EMImageMessageBody) emMessage.getBody();
                    //设置我接收到的图片
                    Glide.with(context)
                            .load(text.getThumbnailUrl())
                            .override(300, 200)
                            .into(viewHolder.leftpictrue);
                }
                viewHolder.leftpictrue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context,PictureActivity.class);
                        intent.putExtra("paht",text.getThumbnailUrl());
                        context.startActivity(intent);
                    }
                });
                viewHolder.rightpictrue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context,PictureActivity.class);
                        intent.putExtra("rpaht",text.getLocalUrl());
                        context.startActivity(intent);
                    }
                });
                break;
            case VIDEO:
                final EMVideoMessageBody  text1 = (EMVideoMessageBody) emMessage.getBody();
                if (getname().equals(emMessage.getFrom())){
                    //设置rightloy是否可见
                    viewHolder.rightLoy.setVisibility(View.VISIBLE);
                    viewHolder.liftLoy.setVisibility(View.GONE);
                    viewHolder.rightMessage.setVisibility(View.GONE);
                    viewHolder.rightpictrue.setVisibility(View.GONE);
                    viewHolder.rightVoid.setVisibility(View.VISIBLE);
                    //设置我发送的用户名
                    viewHolder.rightName.setText(getname());

                    //设置我发送的视频的略缩图
                  //
                    Glide.with(context)
                            .load(text1.getLocalUrl())
                            .override(300, 200)
                            .into(viewHolder.rightVoid);
                }else {
                    viewHolder.rightLoy.setVisibility(View.GONE);
                    viewHolder.liftLoy.setVisibility(View.VISIBLE);
                    viewHolder.leftMessage.setVisibility(View.GONE);
                    viewHolder.leftpictrue.setVisibility(View.GONE);
                    viewHolder.leftVoid.setVisibility(View.VISIBLE);
                    //设置谁给我发送的用户名
                    viewHolder.leftName.setText(emMessage.getUserName());
                    //
                    //    EMImageMessageBody text = (EMImageMessageBody) emMessage.getBody();
                    //设置我接收到的图片
                    Glide.with(context)
                            .load(text1.getThumbnailUrl())
                            .override(300, 200)
                            .into(viewHolder.leftVoid);
                }
                viewHolder.leftVoid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context,VideoActivity.class);
                        intent.putExtra("void",text1.getRemoteUrl());
                        context.startActivity(intent);
                    }
                });
                viewHolder.rightVoid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context,VideoActivity.class);
                        String remoteUrl = text1.getLocalUrl();
                        intent.putExtra("rvoide",remoteUrl);
                        context.startActivity(intent);
                    }
                });
                break;
        }


    }
    //刷新方法
    public void upData(List<EMMessage> list){
        this.list=list;
        this.notifyDataSetChanged();
    }
    //获取本地的name
    public String getname(){
       return SPUtils.getlastLoginUserName(context);
    }


    //内部类
    class ViewHorder{
        private LinearLayout timeLoy;
        private RelativeLayout liftLoy,rightLoy;
        private TextView time,leftName,leftMessage,rightName,rightMessage;
        private ImageView leftImg,rightImg, leftpictrue,rightpictrue,leftVoid,rightVoid;
        //初始化数据
        void setViews(View view){
            timeLoy= (LinearLayout) view.findViewById(R.id.item_msg_time_loy);
            time = (TextView) view.findViewById(R.id.item_msg_time_textview);
            liftLoy= (RelativeLayout) view.findViewById(R.id.item_msg_lift_loy1);
            leftName= (TextView) view.findViewById(R.id.item_msg_lift_name1);
            leftMessage= (TextView) view.findViewById(R.id.item_msg_lift_message1);
            leftImg= (ImageView) view.findViewById(R.id.item_msg_lift_img1);
            rightLoy= (RelativeLayout) view.findViewById(R.id.item_msg_right_loy1);
            rightName= (TextView) view.findViewById(R.id.item_msg_right_name1);
            rightMessage= (TextView) view.findViewById(R.id.item_msg_right_message1);
            rightImg= (ImageView) view.findViewById(R.id.item_msg_right_img1);
            leftpictrue= (ImageView) view.findViewById(R.id.item_msg_lift_image1);
            rightpictrue= (ImageView) view.findViewById(R.id.item_msg_right_image1);
           leftVoid= (ImageView) view.findViewById(R.id.item_msg_lift_void11);
            rightVoid= (ImageView) view.findViewById(R.id.item_msg_right_void11);
        }

    }
}
