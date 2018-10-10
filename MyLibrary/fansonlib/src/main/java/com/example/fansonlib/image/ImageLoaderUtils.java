package com.example.fansonlib.image;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by：fanson
 * Created on：2017/4/14 17:24
 * Describe：图片加载框架的代理类
 * 用法：在Application声明
 * ImageLoaderConfig loaderConfig = new ImageLoaderConfig.Builder().client(new UniversalLoaderStrategy(getApplicationContext()))
 .setMaxDiskCache(1024 * 1024 * 50)
 .setMaxMemoryCache(1024 * 1024 * 10)
 .build();
 ImageLoaderUtils.init(loaderConfig);
 */

public class ImageLoaderUtils {

    private static ImageLoaderConfig loaderConfig;

    public static void init(ImageLoaderConfig config) {
        loaderConfig = config;
    }

//    private volatile static ImageLoaderUtils mInstance;
//    public static ImageLoaderUtils getInstance(){
//        if (mInstance==null){
//            synchronized (ImageLoaderUtils.class){
//                if (mInstance == null){
//                    mInstance = new ImageLoaderUtils();
//                    return mInstance;
//                }
//            }
//        }
//        return mInstance;
//    }

    /**
     * 常规加载图片
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public static void loadImage( Context context, ImageView imageView, Object imgUrl) {
        loaderConfig.getClient().loadImage(loaderConfig,context, imageView, imgUrl);
    }

    /**
     * 常规加载图片带监听
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public static <L1 extends OnLoadingListener,L2 extends OnProgressListener> void loadImageWithListener( Context context, ImageView imageView, String imgUrl,L1 listener1,L2 listener2) {
        loaderConfig.getClient().loadImageWithListener(loaderConfig,context, imageView, imgUrl,listener1,listener2);
    }

    public static void displayFromDrawable(Context context, int imageID, ImageView imageView) {
        loaderConfig.getClient().displayFromDrawable(loaderConfig ,context, imageID, imageView);
    }

    public static void displayFromSD(String uri, ImageView imageView) {
        loaderConfig.getClient().displayFromSDCard(loaderConfig ,uri, imageView);
    }

    /**
     * 加载圆形图片
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public static void loadCircleImage(Context context, ImageView imageView, String imgUrl ) {
        loaderConfig.getClient().loadCircleImage(loaderConfig ,context, imageView, imgUrl);
    }

    /**
     * 加载圆角图片
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public static  void loadCornerImage(Context context, ImageView imageView, String imgUrl,int radius){
        loaderConfig.getClient().loadCornerImage(loaderConfig ,context,imageView,imgUrl,radius);
    }

    /**
     * 加载Gif图片
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public static  void loadGifImage(Context context, ImageView imageView, Object imgUrl){
        loaderConfig.getClient().loadGifImage(loaderConfig ,context,imageView,imgUrl);
    }

    /**
     * 清除内存
     * @param context
     */
    public static void clearMemory(Context context){
        loaderConfig.getClient().clearMemory(context);
    }

    /**
     * 获取Bitmap对象
     * @param context
     * @param imgUrl
     * @return
     */
    public static void getBitmap(Context context,  String imgUrl,OnWaitBitmapListener listener,int index){
         loaderConfig.getClient().getBitmap(loaderConfig,context,imgUrl,listener,index);
    }


}
