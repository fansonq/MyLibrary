package com.fanson.mylibrary;

import android.app.Application;

import com.example.fansonlib.base.AppUtils;

/**
 * Created by：fanson
 * Created on：2017/9/19 11:17
 * Describe：
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.init(getApplicationContext());
    }
}
