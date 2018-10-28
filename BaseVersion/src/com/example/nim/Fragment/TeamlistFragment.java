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
import com.example.nim.main.TeamMessageActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by np on 2016/7/21.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TeamlistFragment extends Fragment{
    private ListView groupList;
    ArrayAdapter<String> groupAd;
    private List<String> teamNames = new ArrayList<String>();
    List<Team> teams = NIMClient.getService(TeamService.class).queryTeamListBlock();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View teamlistLayout = inflater.inflate(R.layout.teamlist_layout, container, false);
        groupList = (ListView) teamlistLayout.findViewById(R.id.teamList);
        for (Team team : teams) {
            teamNames.add(team.getName());
        }

        groupAd = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,teamNames);
        groupList.setAdapter(groupAd);

        NIMClient.getService(SystemMessageObserver.class)
                .observeReceiveSystemMsg(observer, true);

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(getActivity(), TeamMessageActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("teamId", teams.get(arg2).getId());
                mBundle.putString("teamName", teamNames.get(arg2));
                intent.putExtras(mBundle);
                startActivity(intent);

            }


        });

        return teamlistLayout;
    }

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
                    if (message.getType() == SystemMessageType.ApplyJoinTeam){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("加群请求");
                        builder.setMessage(message.getFromAccount() + "请求加入团体" + message.getTargetId() + "。\n附言：" + message.getContent());
                        builder.setPositiveButton("接受", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 通过对方的好友请求
                                NIMClient.getService(TeamService.class)
                                        .passApply(message.getTargetId(), message.getFromAccount());
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 拒绝对方的好友请求
                                NIMClient.getService(TeamService.class)
                                        .rejectApply(message.getTargetId(), message.getFromAccount(), null);
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }
            };

}
