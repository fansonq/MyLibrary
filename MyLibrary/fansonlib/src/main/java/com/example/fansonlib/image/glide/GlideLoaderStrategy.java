package com.example.fansonlib.image.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fansonlib.R;
import com.example.fansonlib.image.BaseImageLoaderStrategy;
import com.example.fansonlib.image.ImageLoaderConfig;
import com.example.fansonlib.image.OnLoadingListener;
import com.example.fansonlib.image.OnProgressListener;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by：fanson
 * Created on：2017/4/17 13:32
 * Describe：Glide的策略实现类
 */

public class GlideLoaderStrategy implements BaseImageLoaderStrategy {
    private static int MAX_DISK_CACHE = 1024 * 1024 * 50;
    private static int MAX_MEMORY_CACHE = 1024 * 1024 * 10;

    /**
     * 常量
     */
    static class Contants {
        public static final int BLUR_VALUE = 20; //模糊
        public static final int CORNER_RADIUS = 20; //圆角
        public static final float THUMB_SIZE = 0.5f; //0-1之间  10%原图的大小
    }

    @Override
    public void loadImage(ImageLoaderConfig config, Context context, ImageView view, Object imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .error(config.getErrorPicRes())
                .placeholder(config.getPlacePicRes())
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .into(view);
    }

    @Override
    public void loadImageWithListener(ImageLoaderConfig config, Context context, ImageView view, Object imgUrl, OnLoadingListener listener1, OnProgressListener listener2) {

    }

    @Override
    public void displayFromDrawable(ImageLoaderConfig config,Context context,int imageId, ImageView imageView) {
    }

    @Override
    public void displayFromSDCard(ImageLoaderConfig config,String uri, ImageView imageView) {

    }

    @Override
    public void loadCircleImage(ImageLoaderConfig config,Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .placeholder(config.getPlacePicRes())
                .error(config.getErrorPicRes())
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(new CropCircleTransformation(context))
                .into(imageView);
    }

    @Override
    public void loadGifImage(ImageLoaderConfig config,Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .asGif()
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .error(config.getErrorPicRes())
                .placeholder(config.getPlacePicRes())
                .into(imageView);
    }

    @Override
    public void loadCornerImage(ImageLoaderConfig config,Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .error(config.getErrorPicRes())
                .placeholder(config.getPlacePicRes())
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(
                        new RoundedCornersTransformation(
                                context, Contants.CORNER_RADIUS, Contants.CORNER_RADIUS))
                .into(imageView);
    }
}
