package com.example.fansonlib.image;

import android.content.Context;
import android.widget.ImageView;

import com.example.fansonlib.R;
import com.example.fansonlib.image.glide.GlideLoaderStrategy;

/**
 * @author Created by：fanson
 * Created on：2017/4/14 17:24
 * Describe：图片加载框架的代理类
 */

public class ImageLoaderUtils {

    private static BaseImageLoaderStrategy sImageLoaderStrategy;
    private volatile static ImageLoaderUtils mInstance;

    /**
     * 默认参数配置
     */
    private static ImageLoaderConfig mDefaultConfig = new ImageLoaderConfig.Builder()
            .setMaxDiskCache(1024 * 1024 * 50)
            .setMaxMemoryCache(1024 * 1024 * 10)
            .errorPicRes(R.mipmap.default_image)
            .placePicRes(R.mipmap.default_image)
            .build();


    public static void init() {
        //默认使用Glide框架
        if (sImageLoaderStrategy == null) {
            sImageLoaderStrategy = new GlideLoaderStrategy();
            sImageLoaderStrategy.setLoaderConfig(mDefaultConfig);
        }
    }

    /**
     * 获取实例
     *
     * @return ImageLoaderUtils实例
     */
    public static ImageLoaderUtils getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoaderUtils.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置图片框架策略
     *
     * @param strategy 图片框架策略
     */
    public void setImageLoaderStrategy(BaseImageLoaderStrategy strategy) {
        sImageLoaderStrategy = strategy;
    }

    /**
     * 设置框架的配置
     *
     * @param config 配置
     * @return BaseImageLoaderStrategy
     */
    public BaseImageLoaderStrategy setImageLoaderConfig(ImageLoaderConfig config) {
        sImageLoaderStrategy.setLoaderConfig(config);
        return sImageLoaderStrategy;
    }

    /**
     * 常规加载图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public BaseImageLoaderStrategy loadImage(Context context, ImageView imageView, Object imgUrl) {
        sImageLoaderStrategy.loadImage(context, imageView, imgUrl);
        return sImageLoaderStrategy;
    }

    /**
     * 常规加载图片带监听
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public <L1 extends OnLoadingListener, L2 extends OnProgressListener> BaseImageLoaderStrategy loadImageWithListener(Context context, ImageView imageView, String imgUrl, L1 listener1, L2 listener2) {
        sImageLoaderStrategy.loadImageWithListener(context, imageView, imgUrl, listener1, listener2);
        return sImageLoaderStrategy;
    }

    public BaseImageLoaderStrategy displayFromDrawable(Context context, int imageID, ImageView imageView) {
        sImageLoaderStrategy.displayFromDrawable(context, imageID, imageView);
        return sImageLoaderStrategy;
    }

    public BaseImageLoaderStrategy displayFromSD(String uri, ImageView imageView) {
        sImageLoaderStrategy.displayFromSDCard(uri, imageView);
        return sImageLoaderStrategy;
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public BaseImageLoaderStrategy loadCircleImage(Context context, ImageView imageView, String imgUrl) {
        sImageLoaderStrategy.loadCircleImage(context, imageView, imgUrl);
        return sImageLoaderStrategy;
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public BaseImageLoaderStrategy loadCornerImage(Context context, ImageView imageView, String imgUrl, int radius) {
        sImageLoaderStrategy.loadCornerImage(context, imageView, imgUrl, radius);
        return sImageLoaderStrategy;
    }

    /**
     * 加载Gif图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public BaseImageLoaderStrategy loadGifImage(Context context, ImageView imageView, Object imgUrl) {
        sImageLoaderStrategy.loadGifImage(context, imageView, imgUrl);
        return sImageLoaderStrategy;
    }

    /**
     * 停止加载图片
     * @param context 上下文
     * @return 策略
     */
    public BaseImageLoaderStrategy onPauseRequest(Context context){
        sImageLoaderStrategy.onPauseRequest(context);
        return sImageLoaderStrategy;
    }

    /**
     * 清除内存
     *
     * @param context
     */
    public BaseImageLoaderStrategy clearMemory(Context context) {
        sImageLoaderStrategy.clearMemory(context);
        return sImageLoaderStrategy;
    }

    /**
     * 获取Bitmap对象
     *
     * @param context
     * @param imgUrl
     * @return
     */
    public BaseImageLoaderStrategy getBitmap(Context context, String imgUrl, OnWaitBitmapListener listener) {
        sImageLoaderStrategy.getBitmap(context, imgUrl, listener);
        return sImageLoaderStrategy;
    }


}
