package com.example.testqq.bean;

import com.hyphenate.chat.EMGroupManager;

/**
 * Created by 宋宝春 on 2017/3/30.
 */
public class CreateGroupBean {
    private  String groupname;//群名称
    private String desc;//群简介
    private String[] allMembres;//初始群成员，只有自己可以空数组；
    private String  reason;//邀请成员加入的reason
    private EMGroupManager.EMGroupOptions options;//群组类型选项，可以设置群组最大用户数默认200及群组类型

    public EMGroupManager.EMGroupOptions getOptions() {
        return options;
    }

    public void setOptions(EMGroupManager.EMGroupOptions options) {
        this.options = options;
    }

    public String getReason() {

        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String[] getAllMembres() {
        return allMembres;
    }

    public void setAllMembres(String[] allMembres) {
        this.allMembres = allMembres;
    }
}
