package com.example.testqq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.example.testqq.R;
import com.example.testqq.fragment.InformationFragment;
import com.example.testqq.fragment.LinkmanFragment;
import com.example.testqq.fragment.SettingUpFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页  主要有消息列表页  联系人 设置 三个页面
 * Created by 宋宝春 on 2017/3/22.
 */

public class HomepageActivity extends BaseActivity implements View.OnClickListener {
    private final static int ONE = 1;
    private final static int TWO = 2;
    private final static int ZERO = 0;
    private ViewPager viewPager;  //viewPager控件
    private InformationFragment informationFragment;  //消息列表Fragment对象
    private LinkmanFragment  linkmanFragment;    //联系人列表Fragment对象
    private SettingUpFragment settingUpFragment; //设置页的Fragment对象
    private FragmentManager fragmentManager;   //Fragment的管理器对象
    private List<Fragment> list = new ArrayList<Fragment>();//数据源泛型为Fragment
    private FragmentPagerAdapter fragmentPagerAdapter;//Fragment的适配器对象
    private Button informationButton, linkmanButton, settingUpButton;//点击按钮

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        //调用加载数据方法
        addData();
        //调用初始化方法
        initialize();
    }

    /**
     * 初始化方法
     */
    private void initialize() {
        //初始化viewPager
        viewPager = (ViewPager) findViewById(R.id.homepage_viewPager);
        //初始化Button
        informationButton = (Button) findViewById(R.id.homepage_information_button);
        linkmanButton = (Button) findViewById(R.id.homepage_linkman_button);
        settingUpButton = (Button) findViewById(R.id.homepage_setting_up_button);
        //获取管理器
        fragmentManager = getSupportFragmentManager();
        //获取Fragment的适配器
        fragmentPagerAdapter = new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };
        //给Viewpager设置适配器
        viewPager.setAdapter(fragmentPagerAdapter);
        //设置被选中的页面    参数初始页面的下标
        viewPager.setCurrentItem(0);
        //添加点击事件
        informationButton.setOnClickListener(this);
        linkmanButton.setOnClickListener(this);
        settingUpButton.setOnClickListener(this);
    }

    /**
     * 添加数据源方法
     */
    private void addData() {
        informationFragment = new InformationFragment();
        linkmanFragment = new LinkmanFragment();
        settingUpFragment = new SettingUpFragment();
        list.add(informationFragment);
        list.add(linkmanFragment);
        list.add(settingUpFragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击跳转消息列表页
            case R.id.homepage_information_button:
               viewPager.setCurrentItem(ZERO);
                break;
            //点击跳转联系人列表页
            case R.id.homepage_linkman_button:
                viewPager.setCurrentItem(ONE);
                break;
            //点击跳转设置页
            case R.id.homepage_setting_up_button:
                viewPager.setCurrentItem(TWO);
                break;
        }
    }


}
