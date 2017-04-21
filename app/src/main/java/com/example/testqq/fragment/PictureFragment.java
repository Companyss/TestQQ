package com.example.testqq.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import com.example.testqq.activity.HomepageActivity;
import com.example.testqq.activity.PrivateMessageActivity;
import com.example.testqq.adapter.PictureAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 图片列表
 * Created by 宋宝春 on 2017/4/13.
 */

public class PictureFragment extends Fragment implements View.OnClickListener {
    private View view;
    private List<String> imagelist = new ArrayList<>();
    private PictureAdapter pictureRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private Button senimage,shotImage;
    private File file;
    private    PrivateMessageActivity activity;
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
        shotImage= (Button) view.findViewById(R.id.picture_shot_image);
        activity = (PrivateMessageActivity) getActivity();
        shotImage.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.picture_send_image:
                HashSet<String> path=pictureRecyclerViewAdapter.getPath();

                for (String strPath:path){
                    Log.e("strpatn",strPath);
                    activity.sendImage(strPath);
                }
                break;
            case R.id.picture_shot_image:
                Intent intent=new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                file = new File(Environment
                        .getExternalStorageDirectory()
                        .getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
                // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                try {
                    file.createNewFile();
                } catch (IOException e) {

                }
                startActivityForResult(intent,102);
                break;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 102:
                if (resultCode==getActivity().RESULT_OK) {
                    //获取图片的对象
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    File f = creatBitMap(bitmap);
                    activity.sendImage(f.getAbsolutePath());
                }
                break;


        }
    }

    @NonNull
    public File creatBitMap(Bitmap bitmap) {
        File f = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        try {
            //开启这个文件的输出流
            FileOutputStream out = new FileOutputStream(f);
            //吧bitmap内容写入输出流
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            try {
                //刷新输出流
                out.flush();
                //关闭输出流
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return f;
    }

}
