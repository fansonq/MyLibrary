package com.example.fansonlib;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by：fanson
 * Created on：2017/8/31 16:09
 * Describe：捕获程式崩溃后重启
 */

public class CrashRestartHelper implements Thread.UncaughtExceptionHandler {

    public static final String TAG = CrashRestartHelper.class.getSimpleName();
    private static CrashRestartHelper INSTANCE = new CrashRestartHelper();
    private Context mAppContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashRestartHelper() {
    }

    public static CrashRestartHelper getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mAppContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG,"App Crash");
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            Intent intent = mAppContext.getPackageManager().getLaunchIntentForPackage(mAppContext.getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            mAppContext.startActivity(intent);
            //设置重启时间
//        PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis()+500, restartIntent); // 0.5秒钟后重启应用
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            System.gc();
        }
    }

    /**
     * 错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 自定义处理错误信息
        return true;
    }
}
