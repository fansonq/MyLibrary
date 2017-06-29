package com.example.fansonlib.base;

import android.content.Context;

/**
 * Created by fansonq on 2017/6/29.
 * 获取自定义的Application实例
 */

public class AppUtils {

    public static Context mApplication;

    //在Application中初始化
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

}
