package com.example.fansonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

/**
 * Created by：fanson
 * Created on：2017/4/28 11:32
 * Describe：显示键盘工具类
 */

public class InputMethodUtils {

    /**
     * 切换键盘显示/隐藏状态
     *
     * @param context
     */
    public static void toggleSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示键盘
     *
     * @param view
     * @return
     */
    public static boolean showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static boolean showInputMethod(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getApplicationContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            return imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
        return false;
    }

    /**
     * 定时显示软键盘
     */
    public static void showInputMethod(final View view, long delayMillis) {
        if (view == null) return;
        // 显示输入法
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                showInputMethod(view);
            }
        }, delayMillis);
    }

    /**
     * 隐藏键盘
     *
     * @param view
     * @return
     */
    public static boolean hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 隐藏键盘
     * @param activity
     * @return
     */
    public static boolean hideSoftInput(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
        return false;
    }

    /**
     * 判断键盘是否打开
     *
     * @param context
     * @return
     */
    public static boolean isActive(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

    /**
     * 解决InputMethodManger内存泄漏问题
     * 引用：http://blog.csdn.net/sodino/article/details/32188809
     * @param destContext
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String [] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0;i < arr.length;i ++) {
            String param = arr[i];
            try{
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
//                        if (Log.isColorLevel()) {
//                            Log.d(ReflecterHelper.class.getSimpleName(), Log.CLR, "fixInputMethodManagerLeak break, context is not suitable, get_context=" + v_get.getContext()+" dest_context=" + destContext);
//                        }
                        break;
                    }
                }
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
    }
}

