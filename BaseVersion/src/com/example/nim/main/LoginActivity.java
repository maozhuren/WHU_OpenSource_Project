package com.example.nim.main;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nim.MyCache;
import com.example.nim.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class LoginActivity extends ActionBarActivity {
    private EditText accountEdit;
    private EditText pswEdit;
    private Button loginBtn;
    private Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        NIMClient.init(getApplicationContext(), null, null);
        setContentView(R.layout.login_activity);
        findViews();
    }

    private void findViews() {
        accountEdit = (EditText) findViewById(R.id.account_edit);
        pswEdit = (EditText) findViewById(R.id.token_edit);
        loginBtn = (Button) findViewById(R.id.login);
        registerBtn = (Button) findViewById(R.id.register);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    /**
     * 登录事件响应函数
     */
    private void login() {
        LoginInfo info = new LoginInfo(accountEdit.getText().toString().toLowerCase(), pswEdit.getText().toString()); // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo loginInfo) {
                        MyCache.setAccount(accountEdit.getText().toString().toLowerCase());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                    // overwrite methods
                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }
}
