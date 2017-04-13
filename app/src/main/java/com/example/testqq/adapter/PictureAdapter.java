package com.example.testqq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.example.testqq.R;
import com.example.testqq.activity.BaseActivity;

import java.util.HashSet;
import java.util.List;

/**
 * Created by 宋宝春 on 2017/4/11.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.MyHolder>{
    private List<String> list;
    private Context context;
    private HashSet<String> setPath=new HashSet<String>();

    public PictureAdapter(List<String> list, Context context) {
        this.list =list;
        this.context = context;
        }


    @Override
    public PictureAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.picture_item,parent,false));
    }

    @Override
    public void onBindViewHolder(PictureAdapter.MyHolder holder, final int position) {
        final String path = list.get(position);
        BaseActivity.httpImg(context,holder.image, path);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    setPath.add(path);
                }else {
                    setPath.remove(path);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private CheckBox checkBox;
        public MyHolder(View itemView) {
            super(itemView);
            image= (ImageView) itemView.findViewById(R.id.picture_image_item);
            checkBox= (CheckBox) itemView.findViewById(R.id.picture_image_item_checbox);
        }
    }
   public HashSet<String> getPath(){
       return setPath;
   }
}
