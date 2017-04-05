package com.example.testqq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.testqq.R;
import com.example.testqq.activity.GroupInfoActivity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.List;

/**
 * Created by 宋宝春 on 2017/4/5.
 */
public class GroupInfoAdapter extends BaseAdapter{
    private Context context;
    private List<String>  list;
    public GroupInfoAdapter(Context context, String groupId) {
        //根据群组ID从本地获取群组基本信息
        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
        this.list = group.getMembers();//获取内存中的群成员
        String owner = group.getOwner();//获取群主
        this.list.remove(owner);
        this.context=context;
        this.list.add(0,group.getOwner());


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
           MyHolder holder=null;

        if (convertView==null){
            holder=new MyHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_groupinfo,null);
              holder.name= (TextView) convertView.findViewById(R.id.item_groupinfo_name);
             convertView.setTag(holder);
        }else {
          holder= (MyHolder) convertView.getTag();
        }
        String item = (String) getItem(position);
        holder.name.setText(item);
        if (0==position){
            holder.name.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }else {
            holder.name.setTextColor(context.getResources().getColor(R.color.black));
        }
        return convertView;
    }
    class MyHolder{
         private TextView name;

    }
}
