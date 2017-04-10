package com.example.testqq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.testqq.R;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.EMNoActiveCallException;

/**
 * Created by 宋宝春 on 2017/4/10.
 */

public class VoiceCallActivity extends BaseActivity implements View.OnClickListener, EMCallStateChangeListener {
    private Button answer,hangup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);
        EMClient.getInstance().callManager().addCallStateChangeListener(this);
        init();
    }
   private void init(){
       answer= (Button) findViewById(R.id.voice_call_answer_btn);
       hangup= (Button) findViewById(R.id.voice_call_hangup_btn);
       answer.setOnClickListener(this);
       hangup.setOnClickListener(this);
   }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.voice_call_answer_btn:
                answer();
                break;
            case R.id.voice_call_hangup_btn:
                hangup();
                finish();
                break;
        }
    }
    /**
     * 接听通话
     * @throws EMNoActiveCallException
     *
     */

    private void answer(){
        try {
            EMClient.getInstance().callManager().answerCall();
        } catch (EMNoActiveCallException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    /**
     * 拒绝接听
     * @throws EMNoActiveCallException
     */
    private void hangup(){

        try {
            EMClient.getInstance().callManager().rejectCall();
        } catch (EMNoActiveCallException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

        @Override
        public void onCallStateChanged(CallState callState, CallError error) {
            switch (callState) {
                case CONNECTING: // 正在连接对方

                    break;
                case CONNECTED: // 双方已经建立连接

                    break;

                case ACCEPTED: // 电话接通成功

                    break;

                case NETWORK_UNSTABLE: //网络不稳定
                    if(error == CallError.ERROR_NO_DATA){
                        //无通话数据
                    }else{
                    }
                    break;
                case NETWORK_NORMAL: //网络恢复正常

                    break;
                default:
                    break;
            }

        }


}
