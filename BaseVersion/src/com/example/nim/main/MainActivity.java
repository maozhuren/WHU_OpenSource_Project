package com.example.nim.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.nim.Fragment.FriendlistFragment;
import com.example.nim.Fragment.ProfileFragment;
import com.example.nim.Fragment.RecentcontactFragment;
import com.example.nim.Fragment.TeamlistFragment;
import com.example.nim.MyCache;
import com.example.nim.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import article2.ItemDetailActivity;
import article2.ItemListActivity;

public class MainActivity extends Activity implements View.OnClickListener{

    //UI Object
    private TextView txt_topbar;
    private TextView txt_friend;
    private TextView txt_message;
    private TextView txt_team;
    private TextView txt_profile;

    //Fragment Object
    private FragmentManager fManager;
    private TeamlistFragment teamlistFragment;
    private FriendlistFragment friendlistFragment;
    private RecentcontactFragment recentcontactFragment;
    private ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setToolbar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            fManager = getFragmentManager();
        }
        bindViews();
        txt_message.performClick();   //模拟一次点击，既进去后选择第一项
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logout();
    }

    //UI组件初始化与事件绑定
    private void bindViews() {
        txt_topbar = (TextView) findViewById(R.id.txt_topbar);
        txt_friend = (TextView) findViewById(R.id.txt_friend);
        txt_message = (TextView) findViewById(R.id.txt_message);
        txt_team = (TextView) findViewById(R.id.txt_team);
        txt_profile=(TextView)findViewById(R.id.txt_profile);

        txt_friend.setOnClickListener(this);
        txt_team.setOnClickListener(this);
        txt_message.setOnClickListener(this);
        txt_profile.setOnClickListener(this);
    }

    //重置所有文本的选中状态
    private void setSelected(){
        txt_friend.setSelected(false);
        txt_message.setSelected(false);
        txt_team.setSelected(false);
        txt_profile.setSelected(false);
    }

    //隐藏所有Fragment
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(teamlistFragment != null)fragmentTransaction.hide(teamlistFragment);
        if(friendlistFragment != null)fragmentTransaction.hide(friendlistFragment);
        if(recentcontactFragment != null)fragmentTransaction.hide(recentcontactFragment);
        if(profileFragment!=null)fragmentTransaction.hide(profileFragment);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()){
            case R.id.txt_team:
                setSelected();
                txt_team.setSelected(true);
                if(teamlistFragment == null){
                    teamlistFragment = new TeamlistFragment();
                    fTransaction.add(R.id.ly_content, teamlistFragment);
                    txt_topbar.setText("团体");
                }else{
                    fTransaction.show(teamlistFragment);
                    txt_topbar.setText("团体");
                }
                break;
            case R.id.txt_message:
                setSelected();
                txt_message.setSelected(true);
                if(recentcontactFragment == null){
                    recentcontactFragment = new RecentcontactFragment();
                    fTransaction.add(R.id.ly_content,recentcontactFragment);
                    txt_topbar.setText("消息");
                }else{
                    fTransaction.show(recentcontactFragment);
                    txt_topbar.setText("消息");
                }
                break;
            case R.id.txt_friend:
                setSelected();
                txt_friend.setSelected(true);
                if(friendlistFragment == null){
                    friendlistFragment = new FriendlistFragment();
                    fTransaction.add(R.id.ly_content,friendlistFragment);
                    txt_topbar.setText("朋友");
                }else{
                    fTransaction.show(friendlistFragment);
                    txt_topbar.setText("朋友");
                }
                break;
            case R.id.txt_profile:
                setSelected();
                txt_profile.setSelected(true);
                if(profileFragment == null){
                    profileFragment = new ProfileFragment();
                    fTransaction.add(R.id.ly_content,profileFragment);
                    txt_topbar.setText("个人");
                }else{
                    fTransaction.show(profileFragment);
                    txt_topbar.setText("个人");
                }
                break;
        }
        fTransaction.commit();
    }
    private void logout() {
        NIMClient.getService(AuthService.class).logout();
        MyCache.clear();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    private void setToolbar(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            toolbar.setNavigationIcon(R.mipmap.tab_my_normal);//设置导航栏图标
        //    toolbar.setLogo(R.mipmap.ic_launcher);//设置app logo
        //    toolbar.setTitle("Title");//设置主标题
        //    toolbar.setSubtitle("Subtitle");//设置子标题

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this, ItemListActivity.class);
                startActivity(intent);
            }
        });

        toolbar.inflateMenu(R.menu.tool_menu);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_item1) {
                    startActivity(new Intent(MainActivity.this, AddFriendActivity.class));
                } else if (menuItemId == R.id.action_item2) {
                    startActivity(new Intent(MainActivity.this, JoinTeamActivity.class));
                } else if (menuItemId == R.id.action_item3) {
                     startActivity(new Intent(MainActivity.this, EditUserInfoActivity.class));
                }
                return true;
            }
        });


    }

}
