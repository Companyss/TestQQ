package com.example.testqq.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testqq.R;
import com.example.testqq.activity.BaseActivity;
import com.example.testqq.activity.PictureActivity;
import com.example.testqq.activity.VideoActivity;
import com.example.testqq.vules.Image;
import com.example.testqq.vules.SPUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;

import org.wlf.filedownloader.FileDownloader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 宋宝春 on 2017/3/27.
 */

public class PrivateMessageAdapter extends BaseAdapter {
    private Context context;
    private List<EMMessage> list;
    private EMImageMessageBody text;
    public PrivateMessageAdapter(Context context, List<EMMessage> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHorder viewHorder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.meeage_item, null);
            viewHorder = new ViewHorder();
            viewHorder.setViews(convertView);
            //存标签
            convertView.setTag(viewHorder);
        } else {
            //取标签
            viewHorder = (ViewHorder) convertView.getTag();
        }
        setViewmessage(viewHorder, (EMMessage) getItem(position), position);
        return convertView;
    }

    //给布局控件属性赋值的方法
    private void setViewmessage(final ViewHorder viewHolder, final EMMessage emMessage, final int position) {
        //设置时间布局可见
        viewHolder.timeLoy.setVisibility(View.VISIBLE);
        //设置时间与日期显示的并赋值给控件
        DateFormat dateFormat = new SimpleDateFormat("MM—dd HH:mm");
        viewHolder.time.setText(dateFormat.format(emMessage.getMsgTime()));
        //判断消息是否从这发出
        //获取消息类型
        EMMessage.Type typeMsg = emMessage.getType();
        switch (typeMsg) {
            case TXT:
                setText(viewHolder, emMessage);
                break;
            case IMAGE:
                setImage(viewHolder, emMessage, position);
                break;
            case VIDEO:
                setVoide(viewHolder, emMessage,position);
                break;
        }


    }

    //设置文本
    private void   setText(ViewHorder viewHolder, EMMessage emMessage) {
        if (getname().equals(emMessage.getFrom())) {
            //设置rightloy是否可见
            viewHolder.rightLoy.setVisibility(View.VISIBLE);
            viewHolder.liftLoy.setVisibility(View.GONE);
            viewHolder.rightMessage.setVisibility(View.VISIBLE);
            viewHolder.rightpictrue.setVisibility(View.GONE);
            viewHolder.rightVoid.setVisibility(View.GONE);
            //设置我发送的用户名
            viewHolder.rightName.setText(getname());
            //
            EMTextMessageBody text = (EMTextMessageBody) emMessage.getBody();
            String message = text.getMessage();
            SpannableString spannableString=new SpannableString(message);
            String str="\\[[^\\]]+\\]";
            Pattern compile = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
            Matcher matcher = compile.matcher(spannableString);
            while (matcher.find()){
                String group = matcher.group();
                int start = matcher.start();
                int i = start + group.length();
                try {
                    if (Image.getImae(group)==0){
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                spannableString.setSpan(new ImageSpan(context,Image.getImae(group)),
                        start,i, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //设置我发送的内容
            viewHolder.rightMessage.setText(spannableString);
            viewHolder.rightMessage.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            viewHolder.rightLoy.setVisibility(View.GONE);
            viewHolder.liftLoy.setVisibility(View.VISIBLE);
            viewHolder.leftMessage.setVisibility(View.VISIBLE);
            viewHolder.leftpictrue.setVisibility(View.GONE);
            viewHolder.leftVoid.setVisibility(View.GONE);
            //设置谁给我发送的用户名
            viewHolder.leftName.setText(emMessage.getUserName());
            //
            EMTextMessageBody txt = (EMTextMessageBody) emMessage.getBody();
            //设置发送的内容
            viewHolder.leftMessage.setText(txt.getMessage());
        }
    }

    //设置图片
    private void setImage(ViewHorder viewHolder, EMMessage emMessage, final int position) {
        text = (EMImageMessageBody) emMessage.getBody();
        if (getname().equals(emMessage.getFrom())) {
            //设置rightloy是否可见
            viewHolder.rightLoy.setVisibility(View.VISIBLE);
            viewHolder.liftLoy.setVisibility(View.GONE);
            viewHolder.rightMessage.setVisibility(View.GONE);
            viewHolder.rightpictrue.setVisibility(View.VISIBLE);
            viewHolder.rightVoid.setVisibility(View.GONE);
            //设置我发送的用户名
            viewHolder.rightName.setText(getname());
            //
            //设置我发送的图片
            Glide.with(context)
                    .load(text.getLocalUrl())
                    .override(300, 200)
                    .into(viewHolder.rightpictrue);
        } else {
            viewHolder.rightLoy.setVisibility(View.GONE);
            viewHolder.liftLoy.setVisibility(View.VISIBLE);
            viewHolder.leftMessage.setVisibility(View.GONE);
            viewHolder.leftpictrue.setVisibility(View.VISIBLE);
            viewHolder.leftVoid.setVisibility(View.GONE);
            //设置谁给我发送的用户名
            viewHolder.leftName.setText(emMessage.getUserName());
            //

            //设置我接收到的图片
            Glide.with(context)
                    .load(text.getThumbnailUrl())
                    .override(300, 200)
                    .into(viewHolder.leftpictrue);
        }
        viewHolder.leftpictrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PictureActivity.class);
                EMImageMessageBody text = (EMImageMessageBody) list.get(position).getBody();
                intent.putExtra("paht", text.getThumbnailUrl());
                context.startActivity(intent);
            }
        });
        viewHolder.rightpictrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PictureActivity.class);
                EMImageMessageBody text = (EMImageMessageBody) list.get(position).getBody();
                intent.putExtra("rpaht", text.getLocalUrl());
                context.startActivity(intent);
            }
        });
    }

    //设置视频
    private void setVoide(ViewHorder viewHolder, EMMessage emMessage, final int i) {
        //视频消息类型消息体
        final EMVideoMessageBody text1 = (EMVideoMessageBody) emMessage.getBody();
        //判断是否是自己发送的
        if (getname().equals(emMessage.getFrom())) {
            //设置rightloy是否可见leftVoid
            viewHolder.rightLoy.setVisibility(View.VISIBLE);
            viewHolder.liftLoy.setVisibility(View.GONE);
            viewHolder.rightMessage.setVisibility(View.GONE);
            viewHolder.rightpictrue.setVisibility(View.GONE);
            viewHolder.rightVoid.setVisibility(View.VISIBLE);
            //设置我发送的用户名
            viewHolder.rightName.setText(getname());

            //设置我发送的视频的略缩图
            //
            Glide.with(context)
                    .load(text1.getLocalUrl())
                    .override(300, 200)
                    .into(viewHolder.rightVoid);
        } else {



            viewHolder.rightLoy.setVisibility(View.GONE);
            viewHolder.liftLoy.setVisibility(View.VISIBLE);
            viewHolder.leftMessage.setVisibility(View.GONE);
            viewHolder.leftpictrue.setVisibility(View.GONE);
            viewHolder.leftVoid.setVisibility(View.VISIBLE);
            //设置谁给我发送的用户名
            viewHolder.leftName.setText(emMessage.getUserName());
            //    EMImageMessageBody text = (EMImageMessageBody) emMessage.getBody();
            //设置我接收到的图片
            Glide.with(context)
                    .load(text1.getThumbnailUrl())
                    .override(300, 200)
                    .into(viewHolder.leftVoid);

        }
        viewHolder.leftVoid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<String, String>();
                if (!TextUtils.isEmpty(text1.getSecret())) {
                    map.put("share_secret", text1.getSecret());

                }
                switch (text1.downloadStatus()) {
                    case DOWNLOADING:
                        Toast.makeText(context,"下载中...",Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESSED:
                        Toast.makeText(context,"下载成功",Toast.LENGTH_SHORT).show();
                        break;

                    case FAILED:
                    case PENDING:
                        XiaZai(map, i);
                        break;
                    default:
                        //   return EMFileMessageBody.EMDownloadStatus.SUCCESSED;
                        break;
                }
            }
        });
//        viewHolder.leftVoid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(context,VideoActivity.class);
//                String remoteUrl = text1.getRemoteUrl();
//                intent.putExtra("lvoide",remoteUrl);
//                context.startActivity(intent);
//            }
//        });
        viewHolder.rightVoid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoActivity.class);
                String remoteUrl = text1.getLocalUrl();
                intent.putExtra("rvoide", remoteUrl);
                context.startActivity(intent);
            }
        });
    }
   //下载接收到视频方法
    private void XiaZai(HashMap<String, String> map, int i) {
        final EMVideoMessageBody videoMessageBody= (EMVideoMessageBody) list.get(i).getBody();
        final String s = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4";
        EMClient.getInstance().chatManager().downloadFile(videoMessageBody.getRemoteUrl(),
                s,
                map, new EMCallBack() {
                    @Override
                    public void onSuccess() {

                        Log.e("onSuccess", "成功");
                        Intent intent = new Intent(context, VideoActivity.class);
                        intent.putExtra("lvoide", s);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e("onError", "失败" + i + "  " + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
    }

    //刷新方法
    public void upData(List<EMMessage> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    //获取本地的name
    public String getname() {
        return SPUtils.getlastLoginUserName(context);
    }


    //内部类
    class ViewHorder {
        private LinearLayout timeLoy;
        private RelativeLayout liftLoy, rightLoy;
        private TextView time, leftName, leftMessage, rightName, rightMessage;
        private ImageView leftImg, rightImg, leftpictrue, rightpictrue, leftVoid, rightVoid;

        //初始化数据
        void setViews(View view) {
            timeLoy = (LinearLayout) view.findViewById(R.id.item_msg_time_loy);
            time = (TextView) view.findViewById(R.id.item_msg_time_textview);
            liftLoy = (RelativeLayout) view.findViewById(R.id.item_msg_lift_loy1);
            leftName = (TextView) view.findViewById(R.id.item_msg_lift_name1);
            leftMessage = (TextView) view.findViewById(R.id.item_msg_lift_message1);
            leftImg = (ImageView) view.findViewById(R.id.item_msg_lift_img1);
            rightLoy = (RelativeLayout) view.findViewById(R.id.item_msg_right_loy1);
            rightName = (TextView) view.findViewById(R.id.item_msg_right_name1);
            rightMessage = (TextView) view.findViewById(R.id.item_msg_right_message1);
            rightImg = (ImageView) view.findViewById(R.id.item_msg_right_img1);
            leftpictrue = (ImageView) view.findViewById(R.id.item_msg_lift_image1);
            rightpictrue = (ImageView) view.findViewById(R.id.item_msg_right_image1);
            leftVoid = (ImageView) view.findViewById(R.id.item_msg_lift_void11);
            rightVoid = (ImageView) view.findViewById(R.id.item_msg_right_void11);
        }

    }
}
