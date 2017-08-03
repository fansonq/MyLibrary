package com.example.fansonlib.utils;

import android.widget.Toast;

import com.example.fansonlib.base.AppUtils;

/**
 * Created by：fanson
 * Created on：2017/8/2 14:40
 * Describe：Toast工具类
 */

public class ShowToast {

    private static Toast mToast;

    /**
     * 获取非连续的Toast实例
     * @param text
     * @param duration
     * @return
     */
    public static Toast getSingleToast(String text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(AppUtils.getAppContext(), text, duration);
        } else {
            mToast.setText(text);
        }
        return mToast;
    }

    /**
     * 获取连续的Toast实例
     * @param text
     * @param duration
     * @return
     */
    public static Toast getToast(String text, int duration) {
        return Toast.makeText(AppUtils.getAppContext(), text, duration);
    }

    /**
     * 非连续弹出的Toast（短）
     * @param message 内容
     */
    public static void showSingleShort(String message) {
        getSingleToast(message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 非连续弹出的Toast（长）
     * @param text 内容
     */
    public static void showSingleLong(String text) {
        getSingleToast(text, Toast.LENGTH_LONG).show();
    }

    /**
     * 连续弹出的Toast（短）
     * @param text 内容
     */
    public static void showShort(String text) {
        getToast(text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 连续弹出的Toast（长）
     * @param text 内容
     */
    public static void showLong(String text) {
        getToast(text, Toast.LENGTH_LONG).show();
    }

}
