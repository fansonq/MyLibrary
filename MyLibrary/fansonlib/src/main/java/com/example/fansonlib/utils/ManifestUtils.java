package com.example.fansonlib.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * @author Created by：Fanson
 * Created Time: 2018/8/22 11:21
 * Describe：修改Manifest中meta-data的数据
 */
public class ManifestUtils {

    private static final String TAG = ManifestUtils.class.getSimpleName();

    /**
     * 修改Manifest中meta-data的数据
     *
     * @param context   上下文
     * @param dataKey   meta-data key值
     * @param dataValue meta-data value值
     */
    public static void editMetaData(Context context, String dataKey, String dataValue) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo == null) {
            return;
        }
        String msg = String.valueOf(appInfo.metaData.get(dataKey));
        Log.d(TAG, "before: " + msg);
        appInfo.metaData.putString(dataKey, dataValue);
        Log.d(TAG, "after: " + appInfo.metaData.get(dataKey));
    }

    /**
     * 获取application应用<meta-data>元素
     *
     * @param context 上下文
     * @param dataKey meta-data key值
     * @return <meta-data>元素的值
     */
    public static String getAppMetaData(Context context, String dataKey) {
        ApplicationInfo appInfo = null;
        String value = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo != null) {
            value = appInfo.metaData.getString(dataKey);
            Log.d(TAG, dataKey + " ：=" + value);
        }
        return value;
    }

    /**
     * 获取渠道值
     *
     * @param context 上下文
     * @return <meta-data>元素的值
     */
    public static int getChannelData(Context context) {
        ApplicationInfo appInfo = null;
        int value = 0;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo != null) {
            value = appInfo.metaData.getInt("CHANNEL_NAME");
            Log.d(TAG, "CHANNEL_NAME ：= " + value);
        }
        return value;
    }

}
