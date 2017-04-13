package com.example.testqq.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.testqq.R;
import com.example.testqq.activity.PrivateMessageActivity;
import com.example.testqq.adapter.PictureAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by 宋宝春 on 2017/4/13.
 */

public class PictureFragment extends Fragment implements View.OnClickListener {
    private View view;
    private List<String> imagelist = new ArrayList<>();
    private PictureAdapter pictureRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private Button senimage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //加载联系列表的布局文件    并将其返回
        view = inflater.inflate(R.layout.fragment_picture, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initview();
        getPicture();
        setRecyclerViewAdapter();
    }
    private void initview(){
        recyclerView = (RecyclerView) view.findViewById(R.id.picture_image_recyclerView);
        senimage= (Button) view.findViewById(R.id.picture_send_image);
        senimage.setOnClickListener(this);

    }
    //获取图片的路径
    public void getPicture() {

        Uri externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor query = contentResolver.query(externalContentUri, null, null, null, null);
        while (query.moveToNext()) {
            String string = query.getString(query.getColumnIndex(MediaStore.Images.Media.DATA));
            imagelist.add(string);
        }
    }
    private void setRecyclerViewAdapter() {
        //实例化适配器
        pictureRecyclerViewAdapter = new PictureAdapter(imagelist,getActivity());
        //获取recyclerview的线性布局管理器
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        //设置线性布局为水平布局
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置布局管理器
        recyclerView.setLayoutManager(lm);
        //给recyclerView设置适配器
        recyclerView.setAdapter(pictureRecyclerViewAdapter);
    }
    public HashSet<String> getPath(){

        HashSet<String> path=pictureRecyclerViewAdapter.getPath();
        return path;
    }

    @Override
    public void onClick(View v) {
        HashSet<String> path=pictureRecyclerViewAdapter.getPath();
        PrivateMessageActivity activity = (PrivateMessageActivity) getActivity();
        for (String strPath:path){
            Log.e("strpatn",strPath);
            activity.sendImage(strPath);
        }
    }

}
