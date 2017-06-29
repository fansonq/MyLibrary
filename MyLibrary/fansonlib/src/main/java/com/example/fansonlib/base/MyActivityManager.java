package com.example.fansonlib.base;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fansonq on 2017/6/28.
 * 管理App中Activity的工具类
 */

public class MyActivityManager {

    public volatile static MyActivityManager mActivityManager;

    /**
     * 装载Activity
     */
    public static List<Activity> mActivities = new LinkedList<>();

    public static MyActivityManager getAppManager() {
        if (mActivityManager == null) {
            synchronized (MyActivityManager.class) {
                if (mActivityManager == null) {
                    mActivityManager = new MyActivityManager();
                }
            }
        }
        return mActivityManager;
    }

    /**
     * 获取Activity的个数
     *
     * @return Activity的个数
     */
    public int getSize() {
        return mActivities.size();
    }

    /**
     * 获取栈顶的Activity
     *
     * @return 栈顶的Activity
     */
    public synchronized Activity getForwardActivity() {
        return getSize() > 0 ? mActivities.get(getSize() - 1) : null;
    }

    /**
     * 添加一个Activity
     *
     * @param activity 指定Activity
     */
    public synchronized void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    /**
     * 移除栈堆的一个Activity
     *
     * @param activity 指定Activity
     */
    public synchronized void removeActivity(Activity activity) {
        if (mActivities.contains(activity)) {
            mActivities.remove(activity);
        }
    }

    /**
     * 清除所有的Activity
     */
    public synchronized void clearAllActivity() {
        Activity activity;
        for (int i = mActivities.size(); i > -1; i--) {
            activity = mActivities.get(i);
            mActivities.remove(activity);
            activity.finish();
        }
        mActivities.clear();
    }


    /**
     * 清除所有Activity（除了栈顶的以外）
     */
    public synchronized void clearExceptTop() {
        Activity activity;
        for (int i = mActivities.size() - 2; i > -1; i--) {
            activity = mActivities.get(i);
            mActivities.remove(activity);
            activity.finish();
        }
    }

    /**
     * 获取当前的Activity
     * @return 栈顶的Activity
     */
    public static synchronized Activity getCurrentActivity( ){
        if (mActivities==null||mActivities.isEmpty()){
            return null;
        }
        return mActivities.get(mActivities.size()-1);
    }

    /**
     * 移除当前的Activity
     */
    public static synchronized void finishCurrentActivity(){
        if (mActivities==null||mActivities.isEmpty()){
            return ;
        }
        finishActivity(mActivities.get(mActivities.size()-1));
    }

    /**
     * 移除指定的Activity
     * @param activity 指定的Activity
     */
    public static synchronized void finishActivity(Activity activity){
        if (mActivities==null||mActivities.isEmpty()){
            return;
        }
        if (activity!=null){
            mActivities.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (mActivities == null||mActivities.isEmpty()) {
            return;
        }
        for (Activity activity : mActivities) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 按照指定类名找到activity
     *
     * @param cls 指定的类名
     * @return 指定类名的activity
     */
    public static Activity findActivity(Class<?> cls) {
        Activity targetActivity = null;
        if (mActivities != null) {
            for (Activity activity : mActivities) {
                if (activity.getClass().equals(cls)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }

    /**
     * @return 获取当前最顶部的acitivity 名字
     */
    public String getTopActivityName() {
        Activity activity ;
        synchronized (mActivities) {
            final int size = mActivities.size() - 1;
            if (size < 0) {
                return null;
            }
            activity = mActivities.get(size);
        }
        return activity.getClass().getName();
    }

}
