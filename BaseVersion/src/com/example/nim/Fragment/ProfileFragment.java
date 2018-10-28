package com.example.nim.Fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nim.MyCache;
import com.example.nim.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by np on 2016/7/21.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ProfileFragment extends Fragment {
    private ListView profileList;
    ArrayAdapter<String> profileAd;
    private List<String> profileItem = new ArrayList<String>();
    private String userAccount;
    private String userName;
    private String userBirthday;
    private String userEmail;
    private String userMobile;
    private String userSign;
    private GenderEnum gender;
    private String userGender;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profileLayout = inflater.inflate(R.layout.profile_layout, container, false);
        profileList = (ListView) profileLayout.findViewById(R.id.profileList);
        getInfo();
        getGender(gender);
        profileAd = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getItem());
        profileList.setAdapter(profileAd);
        return profileLayout;
    }

    private List<String> getItem() {
        profileItem.clear();
        profileItem.add("账号：" + userAccount);
        profileItem.add("名称：" + userName);
        profileItem.add("性别：" + userGender);
        profileItem.add("生日：" + userBirthday);
        profileItem.add("邮箱：" + userEmail);
        profileItem.add("手机：" + userMobile);
        profileItem.add("个性签名：" + userSign);
        return profileItem;
    }

    private String getGender(GenderEnum gender){
        switch (gender) {
            case MALE:
                userGender = "男";
                break;
            case FEMALE:
                userGender = "女";
                break;
            default:
                userGender="未知";
                break;
        }
        return userGender;
    }

    @Override
    public void onResume() {
        super.onResume();
        getInfo();
        getGender(gender);
        profileAd = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getItem());
        profileList.setAdapter(profileAd);
    }

    private void getInfo(){

        userAccount= MyCache.getAccount();

        NimUserInfo n = NIMClient.getService(UserService.class).getUserInfo(userAccount);
        userName = n.getName();
        userBirthday = n.getBirthday();
        userEmail = n.getEmail();
        userMobile = n.getMobile();
        userSign = n.getSignature();
        gender = n.getGenderEnum();

    }

}

