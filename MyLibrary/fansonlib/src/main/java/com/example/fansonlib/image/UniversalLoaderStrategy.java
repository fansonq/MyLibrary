package com.example.fansonlib.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

/**
 * Created by：fanson
 * Created on：2017/4/14 18:09
 * Describe：
 */

public class UniversalLoaderStrategy implements BaseImageLoaderStrategy {

    private static int MAX_DISK_CACHE = 1024 * 1024 * 50; // 磁盘缓存大小
    private static int MAX_MEMORY_CACHE = 1024 * 1024 * 10; // 内存大小
    public static final int CORNER_RADIUS = 20; // 圆角
    private volatile static ImageLoader imageLoader;

    private CircleBitmapDisplayer mCircleBitmapDisplayer;
    private RoundedBitmapDisplayer mRoundedBitmapDisplayer;

    public static ImageLoader getImageLoader() {
        if (imageLoader == null) {
            synchronized (ImageLoaderProxy.class) {
                imageLoader = ImageLoader.getInstance();
            }
        }
        return imageLoader;
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder build = new ImageLoaderConfiguration.Builder(context);
        build.tasksProcessingOrder(QueueProcessingType.LIFO);
        build.diskCacheSize(MAX_DISK_CACHE);
        build.memoryCacheSize(MAX_MEMORY_CACHE);
        build.memoryCache(new LruMemoryCache(MAX_MEMORY_CACHE));
        getImageLoader().init(build.build());
    }

    /**
     * 设置图片放缩类型为模式EXACTLY，用于图片详情页的缩放
     *
     * @return
     */
    public static DisplayImageOptions getOption4ExactlyType() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    /**
     * 自定义Option，加载本地图片（无内存和硬盘的缓存）
     *
     * @return
     */
    public static DisplayImageOptions getOptionCustom() {
        return new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    /**
     * 加载头像专用Options，默认加载中、失败和空url为 ic_loading_small
     *
     * @return
     */
    public static DisplayImageOptions getOptions4Header(int errorResource,int loadingResource) {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(errorResource)
                .showImageOnFail(errorResource)
                .showImageOnLoading(loadingResource)
                .build();
    }

    /**
     * 加载图片列表专用，加载前会重置View
     *
     * @return
     */
    public static DisplayImageOptions getOptions4PictureList(int errorResource,int loadingResource) {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .showImageOnLoading(loadingResource)
                .showImageForEmptyUri(loadingResource)
                .showImageOnFail(loadingResource)
                .build();
    }

    /**
     * @param bitmapDisplayer normal 或圆形、圆角 bitmapDisplayer
     *
     * @return
     */
    private DisplayImageOptions getCircleOption( BitmapDisplayer bitmapDisplayer) {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(bitmapDisplayer)
                .build();
    }

    public UniversalLoaderStrategy(Context context) {
        UniversalLoaderStrategy.initImageLoader(context);
    }

    @Override
    public void loadImage(ImageLoaderConfig config,Context context, ImageView view, Object imgUrl) {
        imageLoader.displayImage((String) imgUrl, view, getOptions4Header(config.getErrorPicRes(),config.getPlacePicRes()));
    }

    @Override
    public void  loadImageWithListener(ImageLoaderConfig config, Context context, ImageView view, Object imgUrl, final OnLoadingListener listener1, final OnProgressListener listener2) {
        imageLoader.displayImage((String) imgUrl, view, getOptions4Header(config.getErrorPicRes(), config.getPlacePicRes()), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                listener1.loadStart();
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                listener1.loadFailed();
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                listener1.loadSuccess();
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String s, View view, int current, int total) {
                listener2.onProgressUpdate(current,total);
            }
        });
    }

    @Override
    public void displayFromDrawable(ImageLoaderConfig config,Context context,int imageId, ImageView imageView) {
        imageLoader.displayImage("drawable://" + imageId, imageView, getOptionCustom());
    }

    @Override
    public void displayFromSDCard(ImageLoaderConfig config,String uri, ImageView imageView) {
        imageLoader.displayImage("file://" + uri, imageView, getOptionCustom());
    }

    @Override
    public void loadCircleImage(ImageLoaderConfig config,Context context, ImageView imageView, String imgUrl) {
        mCircleBitmapDisplayer = new CircleBitmapDisplayer();
        imageLoader.displayImage(imgUrl,imageView,getCircleOption(mCircleBitmapDisplayer));
    }

    @Override
    public void loadGifImage(ImageLoaderConfig config,Context context, ImageView imageView, String imgUrl) {

    }

    @Override
    public void loadCornerImage(ImageLoaderConfig config,Context context, ImageView imageView, String imgUrl) {
        //避免使用RoundedBitmapDisplayer，会创建新的ARGB_8888格式的Bitmap对象
        mRoundedBitmapDisplayer = new RoundedBitmapDisplayer(CORNER_RADIUS);
        imageLoader.displayImage(imgUrl,imageView,getCircleOption(mRoundedBitmapDisplayer));
    }

}
