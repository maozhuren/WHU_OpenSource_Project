package com.example.nim.main;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nim.MyCache;
import com.example.nim.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.Collections;
import java.util.List;

public class MessageActivity extends ActionBarActivity implements View.OnClickListener{
    // view
    private Button send_b;
    private EditText sendText;
    private LinearLayout mes;
    private TextView txt_topbar;

    // data
    private String receiverid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
        setTitle(MyCache.getAccount());
        findViews();
        initData();
        setListener();
        pullMessageHistory();
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
        NIMClient.getService(MsgServiceObserve.class)
                .observeMsgStatus(sendingmessage,true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, false);
        NIMClient.getService(MsgServiceObserve.class)
                .observeMsgStatus(sendingmessage,false);
    }

    Observer<List<IMMessage>> incomingMessageObserver =
            new Observer<List<IMMessage>>() {
                @Override
                public void onEvent(List<IMMessage> messages) {
                    // 处理新收到的消息，为了上传处理方便，SDK保证参数messages全部来自同一个聊天对象。
                    for (IMMessage message : messages) {
                        if (message.getSessionType()==SessionTypeEnum.P2P&&receiverid.equals(message.getFromAccount())) {
                            TextView messagetemp = new TextView(MessageActivity.this);
                            messagetemp.setText(message.getFromAccount() + ": " + message.getContent());
                            messagetemp.setTextSize(20);
                            mes.addView(messagetemp);
                        }
                    }
                }
            };

    Observer<IMMessage> sendingmessage = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {

            TextView messagetemp = new TextView(MessageActivity.this);
            messagetemp.setText(message.getFromAccount() + ": " + message.getContent());
         //   Resources resources = getResources();
         //   Drawable drawable = resources.getDrawable(R.drawable.bubble1);
         //   messagetemp.setBackgroundDrawable(drawable);
        //    messagetemp.setGravity(Gravity.RIGHT);
            messagetemp.setTextSize(20);
            messagetemp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            mes.addView(messagetemp);
        }
    };



    private void findViews() {
        send_b = (Button)findViewById(R.id.send_b);
        sendText = (EditText)findViewById(R.id.sendText);
        mes = (LinearLayout)findViewById(R.id.mes);
        txt_topbar = (TextView)findViewById(R.id.txt_topbar);
    }

    private void initData() {
        Bundle bundle =getIntent().getExtras();
        receiverid = bundle.getString("friendAccount");
        txt_topbar.setText(receiverid);
    }

    private void setListener() {
        send_b.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_b:
                sendText();
                break;
            default:
                break;
        }
    }

    /**
     * 发送文本按钮响应事件
     */
    private void sendText() {
        IMMessage message = MessageBuilder.createTextMessage(
                receiverid, // 聊天对象的ID，如果是单聊，为用户账号，如果是群聊，为群组ID
                SessionTypeEnum.P2P, // 聊天类型，单聊或群组
                sendText.getText().toString()// 文本内容
        );
        NIMClient.getService(MsgService.class).sendMessage(message, false);


    }


    private  void pullMessageHistory(){
        long time = System.currentTimeMillis();
        IMMessage m1 = MessageBuilder.createEmptyMessage(receiverid, SessionTypeEnum.P2P, time);
        NIMClient.getService(MsgService.class).pullMessageHistory(m1, 10, true)
                .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
                                 @Override
                                 public void onResult(int i, List<IMMessage> imMessages, Throwable throwable) {
                                     Collections.reverse(imMessages);




                                    //历史消息,判断id,自己气泡朝右,别人气泡朝左
                                     for (IMMessage message : imMessages) {
                                         String id = message.getFromAccount();
                                         if(MyCache.getAccount().equals(id)) {
                                             TextView messagetemp = new TextView(MessageActivity.this);
                                             messagetemp.setText(message.getFromAccount() + ": " + message.getContent());
                                             messagetemp.setTextSize(20);

                                             mes.addView(messagetemp);

                                         } else {

                                             TextView messagetemp = new TextView(MessageActivity.this);
                                             messagetemp.setText(message.getFromAccount() + ": " + message.getContent());
                                             messagetemp.setTextSize(20);

                                             mes.addView(messagetemp);

                                         }
                                     }
                                 }
                             }
                );
    }

}