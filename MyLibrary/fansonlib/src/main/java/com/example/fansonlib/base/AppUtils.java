package com.example.fansonlib.base;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by fansonq on 2017/6/29.
 * 获取自定义的Application实例
 */

public class AppUtils {

    public static Context mApplication;

    /**
     * 在Application中初始化
     * @param context
     */
    public  static void init(Context context){
        mApplication = context;
    }

    /**
     * 获取Application
     * @return Application
     */
    public static Context getAppContext(){
        return mApplication;
    }

    /**
     * 获取进程名
     * @param cxt 上下文
     * @param pid android.os.Process.myPid()
     * @return 进程名
     */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    /**
     * 根据StringID返回字符串
     * @param stringId 资源ID
     * @return 字符串
     */
    public static String getString(int stringId){
        return mApplication.getString(stringId);
    }

}
