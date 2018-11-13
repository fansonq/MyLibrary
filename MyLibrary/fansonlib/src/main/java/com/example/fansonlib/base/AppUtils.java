package com.example.fansonlib.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by fansonq on 2017/6/29.
 * 获取自定义的Application实例
 */

public class AppUtils {

    public static Context mApplication;
    private static Boolean isDebug = null;

    /**
     * 在Application中初始化
     *
     * @param context
     */
    public static void init(Context context) {
        mApplication = context;
        syncIsDebug(context);
    }

    /**
     * 获取Application
     *
     * @return Application
     */
    public static Context getAppContext() {
        return mApplication;
    }

    /**
     * 获取是否Debug模式
     *
     * @return true/false
     */
    public static boolean isDebug() {
        return isDebug == null ? false : isDebug.booleanValue();
    }

    /**
     * Sync lib debug with app's debug value. Should be called in module Application
     *
     * @param context
     */
    public static void syncIsDebug(Context context) {
        if (isDebug == null) {
            isDebug = context.getApplicationInfo() != null &&
                    (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
    }


    /**
     * 获取进程名
     *
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
     * 獲取當前程序的版本號
     *
     * @param mContext
     * @return 版本號；例：23
     */
    public static int getCurrentVersionCode(Context mContext) {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null ? packageInfo.versionCode : 1;
    }

    /**
     * 獲取當前程序的版本名称
     *
     * @param mContext
     * @return 版本；例：1.0.0
     */
    public static String getCurrentVersionName(Context mContext) {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null ? packageInfo.versionName : "1.0.0";
    }

    /**
     * 根据StringID返回字符串
     *
     * @param stringId 资源ID
     * @return 字符串
     */
    public static String getString(int stringId) {
        return mApplication.getString(stringId);
    }

}
