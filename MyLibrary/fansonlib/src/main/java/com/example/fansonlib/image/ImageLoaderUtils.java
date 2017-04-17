package com.example.fansonlib.image;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by：fanson
 * Created on：2017/4/14 17:24
 * Describe：
 */

public class ImageLoaderUtils {

    private volatile static ImageLoaderUtils mInstance;
    private static ImageLoaderConfig loaderConfig;

    public static void init(ImageLoaderConfig config){
        loaderConfig = config;
    }

    public static ImageLoaderUtils getInstance(){
        if (mInstance==null){
            synchronized (ImageLoaderUtils.class){
                if (mInstance == null){
                    mInstance = new ImageLoaderUtils();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }

    public static void loadImage(Context context, ImageView imageView,String url){
        loaderConfig.getClient().loadImage(context,imageView,url);
    }

}
