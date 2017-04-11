package com.example.testqq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.testqq.R;
import com.example.testqq.activity.BaseActivity;

import java.util.List;

/**
 * Created by 宋宝春 on 2017/4/11.
 */

public class PictureRecyclerViewAdapter extends RecyclerView.Adapter<PictureRecyclerViewAdapter.MyHolder>{
    private List<String> list;
    private Context context;
   private String path;

    public PictureRecyclerViewAdapter(List<String> list, Context context) {
        this.list =list;
        this.context = context;
        }


    @Override
    public PictureRecyclerViewAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.picture_item,parent,false));
    }

    @Override
    public void onBindViewHolder(PictureRecyclerViewAdapter.MyHolder holder, final int position) {
          path=list.get(position);
        BaseActivity.httpImg(context,holder.image, this.path);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        public MyHolder(View itemView) {
            super(itemView);
            image= (ImageView) itemView.findViewById(R.id.picture_image_item);
        }
    }

}
