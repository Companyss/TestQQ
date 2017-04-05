package com.example.testqq.vules;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 宋宝春 on 2017/3/28.
 */

public class SPUtils  {
    private static final String SP_NAME="urse";
    private static final String LAST_LOGIN_USERNAME="ls";
    private static final String SP_PASSWORD="password";
    private static final String LAST_LOGIN_PASSWORD="wo";
    public static void  setLastLoginUsername(Context context,String ursename){
        context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE)
                .edit()
                .putString(LAST_LOGIN_USERNAME,ursename)
                .apply();
    }
    public static String getlastLoginUserName(Context context){
        return context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE).getString(LAST_LOGIN_USERNAME,"");
    }

    public static void setLastLoginPassword(Context context,String ursename){
        context.getSharedPreferences(SP_PASSWORD,Context.MODE_PRIVATE)
                .edit()
                .putString(LAST_LOGIN_PASSWORD,ursename)
                .apply();
    }
    public static String getlastLoginPassword(Context context){
        return context.getSharedPreferences(SP_PASSWORD,Context.MODE_PRIVATE).getString(LAST_LOGIN_PASSWORD,"");
    }

}
