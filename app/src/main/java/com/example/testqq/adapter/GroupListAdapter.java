package com.example.testqq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testqq.R;
import com.hyphenate.chat.EMGroup;

import java.util.List;

/**
 * Created by 宋宝春 on 2017/3/30.
 */
public class GroupListAdapter extends BaseAdapter{
    private Context context;
  private List<EMGroup> list;

    public GroupListAdapter(Context context, List<EMGroup> list) {
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
       MyHolder holder;
        if (convertView==null){
            holder=new MyHolder();
            convertView = View.inflate(context, R.layout.item_grouplist, null);
            holder.getViews(convertView);
            convertView.setTag(holder);
        }else {
             holder = (MyHolder) convertView.getTag();

        }
        holder.groupName.setText(list.get(position).getGroupName());
        return convertView;
    }
    public void upData(List<EMGroup> list){
        this.list=list;
        this.notifyDataSetChanged();
    }
    class MyHolder{
        TextView groupName;
        //初始化控件
        void getViews(View view){

            groupName= (TextView) view.findViewById(R.id.item_group_name);

        }

    }
}
