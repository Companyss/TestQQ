package com.example.testqq.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.testqq.R;
import com.example.testqq.adapter.LinkmanAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.exceptions.HyphenateException;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

/**
 * 联系人列表页
 * Created by 宋宝春 on 2017/3/22.
 */

public class LinkmanFragment extends Fragment {
    private View view;
    private ListView listView;
    private List<String>  list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //加载联系列表的布局文件    并将其返回
        view=inflater.inflate(R.layout.fragment_linkman,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {

            EMClient.getInstance().contactManager().addContact("888", "888");
            EMClient.getInstance().contactManager().addContact("999", "tomyi");
        } catch (HyphenateException e) {
            e.printStackTrace();
            Log.e("asd","没有添加");
        }
        init();
    }
    private void init(){
        listView= (ListView) view.findViewById(R.id.linkman_list_view);
        list=new ArrayList<String>();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    list = EMClient.getInstance().contactManager().getAllContactsFromServer();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });


        listView.setAdapter(new LinkmanAdapter(getActivity(),list));
    }

}
