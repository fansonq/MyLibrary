package com.example.fansonlib.manager;

import android.app.Activity;

import java.lang.ref.SoftReference;
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
    public static List<SoftReference<Activity>> mActivities = new LinkedList<>();

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
        return getSize() > 0 ? mActivities.get(getSize() - 1).get() : null;
    }

    /**
     * 添加一个Activity
     *
     * @param activity 指定Activity
     */
    public synchronized void addActivity(SoftReference<Activity> activity) {
        mActivities.add(activity);
    }

    /**
     * 移除栈堆的一个Activity
     *
     * @param activity 指定Activity
     */
    public synchronized void removeActivity(SoftReference<Activity> activity) {
        if (mActivities.contains(activity)) {
            mActivities.remove(activity);
        }
    }

    /**
     * 清除所有的Activity
     */
    public synchronized void clearAllActivity() {
        SoftReference<Activity> activity;
        for (int i = mActivities.size()-1; i > -1; i--) {
            activity = mActivities.get(i);
            mActivities.remove(activity);
            activity.get().finish();
        }
        mActivities.clear();
    }


    /**
     * 清除所有Activity（除了栈顶的以外）
     */
    public synchronized void clearExceptTop() {
        SoftReference<Activity> activity;
        for (int i = mActivities.size() - 2; i > -1; i--) {
            activity = mActivities.get(i);
            mActivities.remove(activity);
            activity.get().finish();
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
        return mActivities.get(mActivities.size()-1).get();
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
    public static synchronized void finishActivity(SoftReference<Activity> activity){
        if (mActivities==null||mActivities.isEmpty()){
            return;
        }
        if (activity!=null){
            mActivities.remove(activity);
            activity.get().finish();
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
        for (SoftReference<Activity> activity : mActivities) {
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
            for (SoftReference<Activity> activity : mActivities) {
                if (activity.getClass().equals(cls)) {
                    targetActivity = activity.get();
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
        SoftReference<Activity> activity ;
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
