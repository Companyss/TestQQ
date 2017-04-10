package com.example.testqq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.testqq.R;
import com.example.testqq.vules.SPUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by 宋宝春 on 2017/3/27.
 */

public class PrivateMessageAdapter extends BaseAdapter{
    private Context context;
    private List<EMMessage> list;
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
    private  void setViewmessage(ViewHorder viewHolder,EMMessage emMessage){
        //设置时间布局可见
        viewHolder.timeLoy.setVisibility(View.VISIBLE);
        //设置时间与日期显示的并赋值给控件
        DateFormat dateFormat = new SimpleDateFormat("MM—dd HH:mm");
        viewHolder.time.setText(dateFormat.format(emMessage.getMsgTime()));
        //判断消息是否从这发出
        if (getname().equals(emMessage.getFrom())){
            //设置rightloy是否可见
            viewHolder.rightLoy.setVisibility(View.VISIBLE);
            viewHolder.liftLoy.setVisibility(View.GONE);
            //设置我发送的用户名
            viewHolder.rightName.setText(getname());
            //
            EMTextMessageBody text = (EMTextMessageBody) emMessage.getBody();
            //设置我发送的内容
            viewHolder.rightMessage.setText(text.getMessage());
        }else {
            viewHolder.rightLoy.setVisibility(View.GONE);
            viewHolder.liftLoy.setVisibility(View.VISIBLE);
            //设置谁给我发送的用户名
            viewHolder.leftName.setText(emMessage.getUserName());
            //
            EMTextMessageBody txt= (EMTextMessageBody) emMessage.getBody();
            //设置发送的内容
            viewHolder.leftMessage.setText(txt.getMessage());
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
        private ImageView leftImg,rightImg;
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
        }

    }
}
