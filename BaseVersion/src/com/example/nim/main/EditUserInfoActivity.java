package com.example.nim.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.nim.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import java.util.HashMap;
import java.util.Map;


public class EditUserInfoActivity extends Activity {
    private EditText editName;
    private EditText editBirthday;
    private EditText editEmail;
    private EditText editTel;
    private EditText editSign;
    private RadioGroup radioGroup;
    private RadioButton maleBtn, femaleBtn;
    private Button finishEdit;

    private int gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        editName = (EditText)findViewById(R.id.editName);
        editBirthday = (EditText)findViewById(R.id.editBirthday);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editTel = (EditText)findViewById(R.id.editTel);
        editSign = (EditText)findViewById(R.id.editSign);
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        maleBtn = (RadioButton)findViewById(R.id.maleBtn);
        femaleBtn = (RadioButton)findViewById(R.id.femaleBtn);
        finishEdit = (Button)findViewById(R.id.finishEdit);

        gender = 0;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == maleBtn.getId()){
                    gender = 1;
                } else if(checkedId == femaleBtn.getId()){
                    gender = 2;
                }
            }
        });

        finishEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });

        editBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBirthday.setHint(null);
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEmail.setHint(null);
            }
        });

    }



    private void updateUserInfo(){
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(6);
        fields.put(UserInfoFieldEnum.Name, editName.getText().toString());
        fields.put(UserInfoFieldEnum.BIRTHDAY, editBirthday.getText().toString());
        fields.put(UserInfoFieldEnum.EMAIL, editEmail.getText().toString());
        fields.put(UserInfoFieldEnum.MOBILE, editTel.getText().toString());
        fields.put(UserInfoFieldEnum.SIGNATURE, editSign.getText().toString());
        fields.put(UserInfoFieldEnum.GENDER, gender);
        NIMClient.getService(UserService.class).updateUserInfo(fields)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditUserInfoActivity.this, "修改个人资料成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {
                        Toast.makeText(EditUserInfoActivity.this, "请检查格式", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
