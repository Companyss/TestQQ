package com.example.testqq.vules;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.testqq.R;

/**
 * Created by 宋宝春 on 2017/3/27.
 */

public class MyDialog extends ProgressDialog{
    public MyDialog(Context context) {
        super(context);
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIndeterminate(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.login_dialog);
        WindowManager.LayoutParams arr=getWindow().getAttributes();
        arr.height=WindowManager.LayoutParams.WRAP_CONTENT;
        arr.alpha=0.8f;
        arr.width=WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(arr);
    }
}
