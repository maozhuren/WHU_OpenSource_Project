package com.example.nim;

import android.app.Application;
import android.os.Environment;

import com.example.nim.common.ScreenUtil;
import com.example.nim.main.LoginActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        // ... your codes
        NIMClient.init(this, loginInfo(), options());
        // ... your codes
    }

    // 如果返回值为null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。否则无需设置。
        // 其中notificationSmallIconId必须提供
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = LoginActivity.class;
        config.notificationSmallIconId = R.drawable.link23;
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log等数据的目录
        // 如果options中没有设置这个值，SDK会使用下面代码示例中的位置作为sdk的数据目录。
        // 该目录目前包含log, file, image, audio, video, thumb这6个目录。
        // 如果第三方APP需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小，该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = ScreenUtil.getScreenMin() / 2;

        return options;
    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {
        return null;
    }
}
