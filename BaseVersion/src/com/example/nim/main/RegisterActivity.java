package com.example.nim.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nim.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends Activity {
    private EditText account;
    private EditText pwd;
    private Button registerBtn;
    private String result = "";
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        account = (EditText) findViewById(R.id.account_register);
        pwd = (EditText) findViewById(R.id.token_register);
        registerBtn = (Button) findViewById(R.id.register_finish);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    public void run() {

                        send(account.getText().toString(),pwd.getText().toString());
                        Message m = handler.obtainMessage();
                        handler.sendMessage(m);
                    }
                }).start();
            }
        });


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (result != null) {

                    try {
                        JSONObject resultObj = new JSONObject(result);
                        String statusCode =resultObj.getString("code");
                        Toast.makeText(RegisterActivity.this, statusCode, Toast.LENGTH_LONG);
                        switch(statusCode){
                            case "200":
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                break;
                            case "414":
                                Toast.makeText(RegisterActivity.this, "账号已被注册", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_LONG).show();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.handleMessage(msg);
            }
        };


    }
    public void send(String account, String pwd) {
        String target= "http://120.27.99.18:8080/file/ServletRegister?account="+account +"&pwd="+pwd;
        URL url;
        try {
            url = new URL(target);
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            InputStreamReader in = new InputStreamReader(
                    urlConn.getInputStream());
            BufferedReader buffer = new BufferedReader(in);
            String inputLine = null;

            while ((inputLine = buffer.readLine()) != null) {
                result += inputLine + "\n";
            }
            in.close();
            urlConn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
