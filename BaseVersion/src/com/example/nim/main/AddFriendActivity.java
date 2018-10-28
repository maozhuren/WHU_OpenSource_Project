package com.example.nim.main;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nim.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;

/**
 * Created by apple on 2016/7/23.
 */
public class AddFriendActivity extends ActionBarActivity {
    private EditText friendAccount;
    private EditText requestMsg;
    private Button addFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend_activity);

        friendAccount = (EditText)findViewById(R.id.friendAccount);
        requestMsg = (EditText)findViewById(R.id.requestMsg);
        addFriend = (Button)findViewById(R.id.addFriend);


        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendAddRequest();
            }
        });
    }


    private void sendAddRequest(){
        final VerifyType verifyType = VerifyType.VERIFY_REQUEST; // 发起好友验证请求
        String msg = requestMsg.getText().toString();
        String account = friendAccount.getText().toString();
        NIMClient.getService(FriendService.class).addFriend(new AddFriendData(account, verifyType, msg))
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddFriendActivity.this,"好友请求发送成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
    }
}
