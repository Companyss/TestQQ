package com.example.testqq.vules;

import com.example.testqq.interfach.SendListener;

/**
 * Created by 宋宝春 on 2017/4/26.
 */
public class ClistenMessage {
    private SendListener sendListener;

    private static ClistenMessage ourInstance = null;

    public SendListener getSendListener() {
        return this.sendListener;
    }

    public void setSendListener(SendListener sendListener) {
        this.sendListener = sendListener;
    }

    public synchronized static ClistenMessage getInstance() {
        if (ourInstance==null){
            ourInstance=new ClistenMessage();
        }
        return ourInstance;
    }

}
