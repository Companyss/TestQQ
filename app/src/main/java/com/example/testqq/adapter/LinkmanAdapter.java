package com.example.testqq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.testqq.R;
import com.example.testqq.activity.BaseActivity;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by 宋宝春 on 2017/3/23.
 */

public class LinkmanAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public LinkmanAdapter(Context context, List<String> list) {
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
        MyHolder myHolder;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_linkman,parent,false);
           myHolder=new MyHolder();
            myHolder.name= (TextView) convertView.findViewById(R.id.item_linkman_name);
            convertView.setTag(myHolder);
        }else {
            myHolder= (MyHolder) convertView.getTag();
        }

        myHolder.name.setText(list.get(position));

        return convertView;
    }
    public void upData(List<String> list){
        this.list=list;
        this.notifyDataSetChanged();
    }
    class MyHolder{
        TextView name;

    }
}
