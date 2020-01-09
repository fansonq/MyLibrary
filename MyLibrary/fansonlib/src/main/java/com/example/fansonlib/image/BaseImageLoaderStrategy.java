package com.example.fansonlib.image;

import android.content.Context;
import android.widget.ImageView;


/**
 * @author  Created by：fanson
 * Created on：2017/4/14 17:29
 * Describe：加载图片框架的策略接口
 */

public interface BaseImageLoaderStrategy<L1 extends OnLoadingListener,L2 extends OnProgressListener> {

    /**
     * 设置图片加载的配置参数（全局）
     * @param config 配置参数
     */
    void setLoaderConfig(ImageLoaderConfig config);

    /**
     * 设置图片加载的配置参数（临时）
     * @param config 配置参数
     */
    void setTempLoaderConfig(ImageLoaderConfig config);

    /**
     * 恢复原始的图片加载配置
     */
    void resetLoaderConfig();

    /**
     * 默认方式加载图片
     * @param context
     * @param view
     * @param imgUrl
     */
    void loadImage(Context context, ImageView view, Object imgUrl);

    /**
     * 加载图片带监听
     * @param context
     * @param view
     * @param imgUrl
     * @param listener1
     */
    void loadImageWithListener( Context context, ImageView view, Object imgUrl, L1 listener1,L2 listener2);

    /**
     * 从drawable中异步加载本地图片
     *
     * @param imageId
     * @param imageView
     */
    void displayFromDrawable(Context context,int imageId, ImageView imageView);

    /**
     * 从内存卡中异步加载本地图片
     *
     * @param uri
     * @param imageView
     */
    void displayFromSDCard(String uri, ImageView imageView);

    /**
     * 加载圆角图片
     */
    void loadCircleImage(Context context, ImageView imageView, String imgUrl );

    /**
     * 加载Gif
     * @param context
     * @param imageView
     * @param imgUrl
     */
    void loadGifImage(Context context, ImageView imageView, Object imgUrl);

    /**
     * 加载圆角图片
     * @param context
     * @param imageView
     * @param imgUrl
     */
    void loadCornerImage(Context context, ImageView imageView, String imgUrl,int radius);

    /**
     * 恢复加载图片
     * @param context 上下文
     */
    void onResumeRequest(Context context);

    /**
     * 停止加载图片
     * @param context 上下文
     */
    void onPauseRequest(Context context);

    /**
     * 清除内存
     * @param context 上下文
     */
    void clearMemory(Context context);

    /**
     * 获取Bitmap对象
     */
    void getBitmap( Context context, Object imgUrl,OnWaitBitmapListener listener);

}
