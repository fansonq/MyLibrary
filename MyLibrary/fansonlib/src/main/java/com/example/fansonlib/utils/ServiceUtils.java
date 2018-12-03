package com.example.fansonlib.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by：fanson
 * Created on：2017/8/21 11:49
 * Describe：服务的工具类
 */

public class ServiceUtils {


    /**
     * 判断服务是否在运行中
     * @param context 即为Context对象
     * @param serviceName 即为Service的全名(包名+服务的类名)
     * @return 是否在运行中
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        boolean isRun = false;
        ActivityManager myManager = (ActivityManager) context
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(100);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().equals(serviceName)) {
                isRun = true;
            }
        }
        return isRun;
    }
}
