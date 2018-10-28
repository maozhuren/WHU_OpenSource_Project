package com.example.nim.Fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.nim.R;
import com.example.nim.main.MessageActivity;
import com.example.nim.main.TeamMessageActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by np on 2016/7/21.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RecentcontactFragment extends Fragment {
    private ListView contactList;
    List<String> contactIds = new ArrayList<>();
    List<String> contactIds1 = new ArrayList<>();
    List<String> contents = new ArrayList<>();
    List<SessionTypeEnum> sessionTypes = new ArrayList<>();
    List<String> times = new ArrayList<>();
    List<RecentContact> recents = NIMClient.getService(MsgService.class).queryRecentContactsBlock();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View recentcontactLayout = inflater.inflate(R.layout.recent_contact, container, false);
        contactList = (ListView) recentcontactLayout.findViewById(R.id.contactList);
        for (RecentContact recent : recents){
            if(recent.getSessionType() == SessionTypeEnum.P2P) {
                contactIds.add(recent.getContactId());
            } else if (recent.getSessionType() == SessionTypeEnum.Team){
                Team t = NIMClient.getService(TeamService.class).queryTeamBlock(recent.getContactId());
                contactIds.add(t.getName());
            }
        /*    else if (recent.getSessionType() == SessionTypeEnum.Team) {
                NIMClient.getService(TeamService.class).searchTeam(recent.getContactId())
                        .setCallback(new RequestCallback<Team>() {
                                         @Override
                                         public void onSuccess(Team team) {
                                             contactIds.add("sample");
                                         }

                                         @Override
                                         public void onFailed(int i) {

                                         }

                                         @Override
                                         public void onException(Throwable throwable) {

                                         }
                                     });

            }*/
            contactIds1.add(recent.getContactId());
            contents.add(recent.getContent());
            sessionTypes.add(recent.getSessionType());
        }



        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < contactIds.size(); i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("contactId", contactIds.get(i));
            listItem.put("content", contents.get(i));
            listItems.add(listItem);
        }

        SimpleAdapter recentAd = new SimpleAdapter(getActivity(), listItems,
                R.layout.item, new String[] { "contactId", "content"},
                new int[] {R.id.name, R.id.content});

        contactList.setAdapter(recentAd);







        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                if (sessionTypes.get(arg2) == SessionTypeEnum.P2P) {
                    intent.setClass(getActivity(), MessageActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("friendAccount", contactIds.get(arg2));
                    intent.putExtras(mBundle);
                    startActivity(intent);
                } else if (sessionTypes.get(arg2) == SessionTypeEnum.Team) {
                    intent.setClass(getActivity(), TeamMessageActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("teamId", contactIds1.get(arg2));
                    mBundle.putString("teamName", contactIds.get(arg2));
                    intent.putExtras(mBundle);
                    startActivity(intent);

                }


            }


        });
        return recentcontactLayout;
    }
}
