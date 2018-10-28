package com.example.nim.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamMessageActivity extends ActionBarActivity implements View.OnClickListener{
    // view
    private TextView txt_topbar;
    private Button teamSend_b;
    private EditText teamSendText;
    private LinearLayout teamMes;
    static private int openfileDialogId = 0;
    // data
    private String receiverid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_message_activity);
        setTitle(MyCache.getAccount());
        findViews();
        initData();
        setListener();
        setTeamName();
        pullMessageHistory();

        setToolbar();

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
                        if (message.getSessionType()== SessionTypeEnum.Team && receiverid.equals(message.getSessionId())) {
                            TextView messagetemp = new TextView(TeamMessageActivity.this);
                            messagetemp.setText(message.getFromAccount() + ": " + message.getContent());
                            messagetemp.setTextSize(20);
                            teamMes.addView(messagetemp);
                        }
                    }
                }
            };

    Observer<IMMessage> sendingmessage = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            TextView messagetemp = new TextView(TeamMessageActivity.this);
            messagetemp.setText(message.getFromAccount() + ": " +message.getContent());
            messagetemp.setTextSize(20);
            teamMes.addView(messagetemp);
        }
    };



    private void findViews() {
        txt_topbar = (TextView)findViewById(R.id.txt_topbar);
        teamSend_b = (Button)findViewById(R.id.teamSend_b);
        teamSendText = (EditText)findViewById(R.id.teamSendText);
        teamMes = (LinearLayout)findViewById(R.id.teamMes);
    }

    private void initData() {
        Bundle bundle =getIntent().getExtras();
        receiverid = bundle.getString("teamId");
    }

    private void setListener() {
        teamSend_b.setOnClickListener(TeamMessageActivity.this);
    }

    private void setTeamName(){
        Bundle bundle =getIntent().getExtras();
        txt_topbar.setText(bundle.getString("teamName"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.teamSend_b:
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
                SessionTypeEnum.Team, // 聊天类型，单聊或群组
                teamSendText.getText().toString()// 文本内容
        );
        NIMClient.getService(MsgService.class).sendMessage(message, false);


    }

    private void pullMessageHistory(){
        long time = System.currentTimeMillis();
        IMMessage m1 = MessageBuilder.createEmptyMessage(receiverid, SessionTypeEnum.Team, time);
        NIMClient.getService(MsgService.class).pullMessageHistory(m1, 10, true)
                .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
                                 @Override
                                 public void onResult(int i, List<IMMessage> imMessages, Throwable throwable) {
                                     Collections.reverse(imMessages);
                                     for (IMMessage message : imMessages) {
                                         TextView messagetemp = new TextView(TeamMessageActivity.this);
                                         messagetemp.setText(message.getFromAccount() + ": " + message.getContent());
                                         messagetemp.setTextSize(20);
                                         teamMes.addView(messagetemp);
                                     }
                                 }
                             }
                );
    }
    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.file_toolbar);
        //    toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);//设置导航栏图标
        //    toolbar.setLogo(R.mipmap.ic_launcher);//设置app logo
        //    toolbar.setTitle("Title");//设置主标题
        //    toolbar.setSubtitle("Subtitle");//设置子标题

        toolbar.inflateMenu(R.menu.file_menu);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.file_item1) {
                    showDialog(openfileDialogId);
                } else if (menuItemId == R.id.file_item2) {
                    startActivity(new Intent(TeamMessageActivity.this, FileActivity.class));
                }
                return true;
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id==openfileDialogId){
            Map<String, Integer> images = new HashMap<String, Integer>();
            // 下面几句设置各文件类型的图标， 需要你先把图标添加到资源文件夹


            Dialog dialog = OpenFileDialog.createDialog(id, this, "打开文件", new CallbackBundle() {
                        @Override
                        public void callback(Bundle bundle) {
                            String filepath = bundle.getString("path");
                            setTitle(filepath); // 把文件路径显示在标题上
                        }
                    },
                    ".wav;",
                    images);
            return dialog;
        }
        return null;
    }


}
