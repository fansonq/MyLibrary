package com.example.fansonlib.image;

import android.content.Context;
import android.widget.ImageView;


/**
 * Created by：fanson
 * Created on：2017/4/14 17:29
 * Describe：加载图片框架的策略接口
 */

public interface BaseImageLoaderStrategy<L1 extends OnLoadingListener,L2 extends OnProgressListener> {

    void loadImage(ImageLoaderConfig config,Context context, ImageView view, Object imgUrl);

    /**
     * 加载图片带监听
     * @param config
     * @param context
     * @param view
     * @param imgUrl
     * @param listener1
     */
    void loadImageWithListener(ImageLoaderConfig config, Context context, ImageView view, Object imgUrl, L1 listener1,L2 listener2);

    /**
     * 从drawable中异步加载本地图片
     *
     * @param imageId
     * @param imageView
     */
    void displayFromDrawable(ImageLoaderConfig config,Context context,int imageId, ImageView imageView);

    /**
     * 从内存卡中异步加载本地图片
     *
     * @param uri
     * @param imageView
     */
    void displayFromSDCard(ImageLoaderConfig config,String uri, ImageView imageView);

    /**
     * 加载圆角图片
     */
    void loadCircleImage(ImageLoaderConfig config,Context context, ImageView imageView, String imgUrl);

    /**
     * 加载Gif
     * @param context
     * @param imageView
     * @param imgUrl
     */
    void loadGifImage(ImageLoaderConfig config,Context context, ImageView imageView, String imgUrl);

    /**
     * 加载圆角图片
     * @param context
     * @param imageView
     * @param imgUrl
     */
    void loadCornerImage(ImageLoaderConfig config,Context context, ImageView imageView, String imgUrl);

    /**
     * 获取Bitmap对象
     */
    void getBitmap(ImageLoaderConfig config, Context context, Object imgUrl,OnWaitBitmapListener listener,int index);

}
