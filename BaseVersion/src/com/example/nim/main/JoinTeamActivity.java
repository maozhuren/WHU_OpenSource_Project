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
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

/**
 * Created by apple on 2016/7/23.
 */
public class JoinTeamActivity extends ActionBarActivity {
    private EditText teamId;
    private EditText requestTeamMsg;
    private Button joinTeam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_team_activity);

        teamId = (EditText)findViewById(R.id.teamId);
        requestTeamMsg = (EditText)findViewById(R.id.requestTeamMsg);
        joinTeam = (Button)findViewById(R.id.joinTeam);


        joinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendJoinRequest();
            }
        });
    }


    private void sendJoinRequest(){
        String tid = teamId.getText().toString();
        String postscript = requestTeamMsg.getText().toString();
        NIMClient.getService(TeamService.class).applyJoinTeam(tid, postscript)
                .setCallback(new RequestCallback<Team>() {
                    @Override
                    public void onSuccess(Team team) {
                        Toast.makeText(JoinTeamActivity.this, "加群申请发送成功", Toast.LENGTH_SHORT).show();
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
