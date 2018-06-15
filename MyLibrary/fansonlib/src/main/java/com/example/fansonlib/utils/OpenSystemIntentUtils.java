package com.example.fansonlib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author Created by：Fanson
 * Created Time: 2018/6/15 14:42
 * Describe：打开系统的intent工具类
 */
public class OpenSystemIntentUtils {

    public static void openSetting(Context context) {
        //跳转设置界面
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(localIntent);
    }


}
