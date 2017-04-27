package com.example.testqq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.testqq.R;
import com.example.testqq.vules.ClistenMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by 宋宝春 on 2017/4/26.
 */

public class ExpressionAdapter extends RecyclerView.Adapter<ExpressionAdapter.ExpressionHolder>{
    private Context context;
    private int[] image;

    public ExpressionAdapter(Context context,  int[] image) {
        this.context = context;
        this.image = image;
    }

    @Override
    public ExpressionAdapter.ExpressionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_expression,parent,false);
        ExpressionHolder my=new ExpressionHolder(view);
        return my;
    }

    @Override
    public void onBindViewHolder(ExpressionAdapter.ExpressionHolder holder, final int position) {
            holder.imageView.setImageResource(image[position]);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置RecycleView的点击事件
                ClistenMessage.getInstance().getSendListener().setItemOnclistener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return image.length;
    }

    public class ExpressionHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public ExpressionHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.item_image);
        }
    }
}
