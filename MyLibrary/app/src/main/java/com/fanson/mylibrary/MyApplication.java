package com.fanson.mylibrary;

import android.app.Application;

import com.example.fansonlib.base.AppUtils;
import com.example.fansonlib.image.ImageLoaderConfig;
import com.example.fansonlib.image.ImageLoaderUtils;
import com.example.fansonlib.utils.log.LogConfig;
import com.example.fansonlib.utils.log.LogUtils;

/**
 * Created by：fanson
 * Created on：2017/9/19 11:17
 * Describe：
 */

public class MyApplication extends Application{

    private static final String TAG =MyApplication .class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.init(getApplicationContext());

        initImageLoader();
        initLog();

    }

    /**
     * 初始化日志框架
     */
    private void initLog() {
        LogConfig config = new LogConfig.Builder()
                .setIsLoggable(BuildConfig.DEBUG)
                .setTag(TAG)
                .build();
        LogUtils.init(config);
    }

    /**
     * 初始化图片框架
     */
    private void initImageLoader(){
        //图片框架使用方式，策略模式
        ImageLoaderConfig loaderConfig = new ImageLoaderConfig.Builder()
                .setMaxDiskCache(1024 * 1024 * 50)
                .setMaxMemoryCache(1024 * 1024 * 10)
                .errorPicRes(R.mipmap.default_image)
                .build();
        ImageLoaderUtils.init();
    }
}
