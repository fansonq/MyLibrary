package com.example.fansonlib.http;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.fansonlib.R;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by fanson on 2016/8/23.
 */
public class ImageLoaderProxy {

    private static final int MAX_DISK_CACHE = 1024 * 1024 * 50;
    private static final int MAX_MEMORY_CACHE = 1024 * 1024 * 10;

    private static boolean isShowLog = false;

    private volatile static ImageLoader imageLoader;

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
     * 自定义Option
     *
     * @param url
     * @param target
     * @param options
     * @param imageLoadingListener
     */
    public static void displayImage(String url, ImageView target, DisplayImageOptions options, ImageLoadingListener imageLoadingListener) {
        imageLoader.displayImage(url, target, options, imageLoadingListener);
    }

    public static void displayImage(String url, ImageView target, DisplayImageOptions options) {
        imageLoader.displayImage(url, target, options);
    }

    /**
     * 头像专用
     *
     * @param url
     * @param target
     */
    public static void displayHeadIcon(String url, ImageView target) {
        imageLoader.displayImage(url, target, getOptions4Header());
    }

    /**
     * 图片详情页专用
     *
     * @param url
     * @param target
     * @param loadingListener
     */
    public static void displayImage4Detail(String url, ImageView target, SimpleImageLoadingListener loadingListener) {
        imageLoader.displayImage(url, target, getOption4ExactlyType(), loadingListener);
    }

    /**
     * 图片列表页专用
     *
     * @param url
     * @param target
     * @param loadingResource
     * @param loadingListener
     * @param progressListener
     */
    public static void displayImageList(String url, ImageView target, int loadingResource, SimpleImageLoadingListener loadingListener, ImageLoadingProgressListener progressListener) {
        imageLoader.displayImage(url, target, getOptions4PictureList(loadingResource), loadingListener, progressListener);
    }

    /**
     * 自定义加载中图片
     *
     * @param url
     * @param target
     * @param loadingResource
     */
    public static void displayImageWithLoadingPicture(String url, ImageView target, int loadingResource) {
        imageLoader.displayImage(url, target, getOptions4PictureList(loadingResource));
    }

    /**
     * 从内存卡中异步加载本地图片
     *
     * @param uri
     * @param imageView
     */
    public static void displayFromSDCard(String uri, ImageView imageView, DisplayImageOptions options) {
        // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
        imageLoader.displayImage("file://" + uri, imageView);
    }

    /**
     * 从drawable中异步加载本地图片
     *
     * @param imageId
     * @param imageView
     */
    public static void displayFromDrawable(int imageId, ImageView imageView, DisplayImageOptions options) {
        // String imageUri = "drawable://" + R.drawable.image; // from drawables
        // (only images, non-9patch)
        imageLoader.displayImage("drawable://" + imageId,
                imageView);
    }


    /**
     * 当使用WebView加载大图的时候，使用本方法现下载到本地然后再加载
     *
     * @param url
     * @param loadingListener
     */
    public static void loadImageFromLocalCache(String url, SimpleImageLoadingListener loadingListener) {
        imageLoader.loadImage(url, getOption4ExactlyType(), loadingListener);
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
     * 加载头像专用Options，默认加载中、失败和空url为 ic_person_photo
     *
     * @return
     */
    public static DisplayImageOptions getOptions4Header() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.mipmap.ic_person_photo)
                .showImageOnFail(R.mipmap.ic_person_photo)
                .showImageOnLoading(R.mipmap.ic_person_photo)
                .build();
    }

    /**
     * 加载图片列表专用，加载前会重置View
     * {@link com.nostra13.universalimageloader.core.DisplayImageOptions.Builder#resetViewBeforeLoading} = true
     *
     * @param loadingResource
     * @return
     */
    public static DisplayImageOptions getOptions4PictureList(int loadingResource) {
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

}

