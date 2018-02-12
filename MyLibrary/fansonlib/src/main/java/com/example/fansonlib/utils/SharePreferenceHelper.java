package com.example.fansonlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by：fanson
 * Created on：2016/10/19 18:14
 * Describe：SharePreference tools
 */
public class SharePreferenceHelper {

    //保存数据的文件名
    private static final String FILE_NAME = "MyShare";

    private volatile static SharePreferenceHelper sharePreferenceHelper;

    private static SharedPreferences mSharedPreferences;

    private static SharedPreferences.Editor editor;

    public SharePreferenceHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }


    //双重锁定
    public static SharePreferenceHelper getInstance(Context context) {
        if (sharePreferenceHelper == null) {
            synchronized (SharePreferenceHelper.class) {
                if (sharePreferenceHelper == null) {
                    sharePreferenceHelper = new SharePreferenceHelper(context.getApplicationContext());
                }
            }
        }
        return sharePreferenceHelper;
    }

    /**
     * 获取String类型
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    /**
     * 保存String类型
     * @param key
     * @param data
     */
    public static void putString(String key, String data) {
        editor.putString(key, data);
    }

    /**
     * 获取Boolean类型
     * @param key
     * @param defValue
     * @return
     */
    public static Boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    /**
     * 保存Boolean类型
     * @param key
     * @param data
     */
    public static void putBoolean(String key, boolean data) {
        editor.putBoolean(key, data);
    }

    /**
     * 获取int类型
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    /**
     * 保存int类型
     * @param key
     * @param data
     */
    public static void putInt(String key, int data) {
        editor.putInt(key, data);
    }

    /**
     * 保存long类型
     * @param key
     * @param data
     */
    public static void putLong(String key, long data) {
        editor.putLong(key, data);
    }

    /**
     * 获取long类型
     * @param key
     * @param defValue
     * @return
     */
    public static long getLong(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    /**
     * 清除指定key的数据，需要调用apply
     * @param key
     */
    public static void removeKey(String key) {
        editor.remove(key);
    }

    /**
     * 清空所有数据
     */
    public static void clear() {
        editor.clear();
        editor.apply();
    }

    /**
     * 提交数据，与commit区别是：
     * 1.apply没有返回值而commit返回boolean表明修改是否提交成功；
     * 2.apply异步，commit同步，因此apply效率高过commit；
     */
    public static void apply() {
        editor.apply();
    }

}
