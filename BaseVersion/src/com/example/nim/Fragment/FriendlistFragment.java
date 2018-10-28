package com.example.nim.Fragment;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nim.R;
import com.example.nim.main.MessageActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.model.AddFriendNotify;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

import java.util.List;

/**
 * Created by np on 2016/7/21.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FriendlistFragment extends Fragment {
    private ListView friendList;
    ArrayAdapter<String> friendAd;
    List<String> friends = NIMClient.getService(FriendService.class).getFriendAccounts();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View friendlistLayout = inflater.inflate(R.layout.friendlist_layout, container, false);
        friendList = (ListView) friendlistLayout.findViewById(R.id.friendList);
        friendAd = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, friends);
        friendList.setAdapter(friendAd);

        NIMClient.getService(SystemMessageObserver.class)
                .observeReceiveSystemMsg(observer, true);


        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(getActivity(), MessageActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("friendAccount", friends.get(arg2));
                intent.putExtras(mBundle);
                startActivity(intent);

            }
        });
        return friendlistLayout;
    }
/*
    @Override
    public void onResume() {
        super.onResume();
         friends = NIMClient.getService(FriendService.class).getFriendAccounts();
        friendAd = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, friends);
        friendList.setAdapter(friendAd);
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NIMClient.getService(SystemMessageObserver.class)
                .observeReceiveSystemMsg(observer, false);

    }

    Observer<SystemMessage> observer =
            new Observer<SystemMessage>() {
                @Override
                public void onEvent(final SystemMessage message) {
                    if (message.getType() == SystemMessageType.AddFriend) {
                        AddFriendNotify attachData = (AddFriendNotify) message.getAttachObject();
                        if (attachData != null) {
                            // 针对不同的事件做处理
                            if (attachData.getEvent() == AddFriendNotify.Event.RECV_ADD_FRIEND_DIRECT) {
                                // 对方直接添加你为好友
                            } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_AGREE_ADD_FRIEND) {
                                // 对方通过了你的好友验证请求
                            } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_REJECT_ADD_FRIEND) {
                                // 对方拒绝了你的好友验证请求
                            } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_ADD_FRIEND_VERIFY_REQUEST) {
                                // 对方请求添加好友，一般场景会让用户选择同意或拒绝对方的好友请求。
                                // 通过message.getContent()获取好友验证请求的附言
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("好友请求");
                                builder.setMessage(message.getFromAccount() + "请求加你为好友。\n附言：" + message.getContent());
                                builder.setPositiveButton("接受", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 通过对方的好友请求
                                        NIMClient.getService(FriendService.class).ackAddFriendRequest(message.getFromAccount(), true);
                                        dialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 拒绝对方的好友请求
                                        NIMClient.getService(FriendService.class).ackAddFriendRequest(message.getFromAccount(), false);
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();
                            }
                        }
                    }
                }
            };
}
