package com.example.fansonlib.image.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.fansonlib.R;
import com.example.fansonlib.image.BaseImageLoaderStrategy;
import com.example.fansonlib.image.ImageLoaderConfig;
import com.example.fansonlib.image.OnLoadingListener;
import com.example.fansonlib.image.OnProgressListener;
import com.example.fansonlib.image.OnWaitBitmapListener;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.Glide.with;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by：fanson
 * Created on：2017/4/17 13:32
 * Describe：Glide的策略实现类
 */

public class GlideLoaderStrategy implements BaseImageLoaderStrategy {
    private static int MAX_DISK_CACHE = 1024 * 1024 * 50;
    private static int MAX_MEMORY_CACHE = 1024 * 1024 * 10;
    private static final String TAG = GlideLoaderStrategy.class.getSimpleName();

    private RequestOptions mOptions1;
    private RequestOptions mOptionsCircle;

    /**
     * 常量
     */
    static class Contants {
        public static final int BLUR_VALUE = 20; //模糊
        public static final int CORNER_RADIUS = 10; //圆角
        public static final int MARGIN = 5;  //边距
        public static final float THUMB_SIZE = 0.5f; //0-1之间  10%原图的大小
    }

    /**
     * 初始化加载配置
     *
     * @param config ImageLoaderConfig
     */
    @SuppressLint("CheckResult")
    private RequestOptions getOptions1(ImageLoaderConfig config) {
        if (mOptions1 == null) {
            mOptions1 = new RequestOptions();
            mOptions1.error(config.getErrorPicRes())
                    .placeholder(config.getPlacePicRes())
                    //下载的优先级
                    .priority(Priority.NORMAL)
                    //缓存策略
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
        }
        return mOptions1;
    }

    /**
     * 初始化加载配置
     */
    @SuppressLint("CheckResult")
    private RequestOptions getOptionsCircle( ) {
        if (mOptionsCircle == null) {
            mOptionsCircle = new RequestOptions();
            mOptionsCircle.placeholder(R.mipmap.ic_person)
                    .error(R.mipmap.ic_person)
                    //下载的优先级
                    .priority(Priority.NORMAL)
                    //缓存策略
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
        }
        return mOptionsCircle;
    }

    @Override
    public void loadImage(ImageLoaderConfig config, Context context, ImageView view, Object imgUrl) {
        with(context)
                .load(imgUrl)
                .apply(getOptions1(config))
                //先加载缩略图 然后在加载全图
                .thumbnail(Contants.THUMB_SIZE)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view);
    }

    @Override
    public void loadImageWithListener(ImageLoaderConfig config, Context context, ImageView view, Object imgUrl, OnLoadingListener listener1, OnProgressListener listener2) {

    }

    @Override
    public void displayFromDrawable(ImageLoaderConfig config, Context context, int imageId, ImageView imageView) {
        with(context)
                .load(imageId)
                .thumbnail(Contants.THUMB_SIZE)
                .apply(getOptions1(config))
                .into(imageView);
    }

    @Override
    public void displayFromSDCard(ImageLoaderConfig config, String uri, ImageView imageView) {
    }

    @Override
    public void loadCircleImage(ImageLoaderConfig config, Context context, ImageView imageView, String imgUrl) {
        with(context)
                .load(imgUrl)
                .apply(getOptionsCircle())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(bitmapTransform(new RoundedCornersTransformation(imageView.getMaxHeight()/2, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(imageView);
    }

    @Override
    public void loadGifImage(ImageLoaderConfig config, Context context, ImageView imageView, String imgUrl) {
        with(context)
                .load(imgUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(getOptions1(config))
                .into(imageView);
    }

    @Override
    public void loadCornerImage(ImageLoaderConfig config, Context context, ImageView imageView, String imgUrl) {
        with(context)
                .load(imgUrl)
                .thumbnail(Contants.THUMB_SIZE)
                .apply(getOptions1(config))
                .apply(bitmapTransform(new RoundedCornersTransformation(18, 0, RoundedCornersTransformation.CornerType.ALL)))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    @Override
    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public void getBitmap(ImageLoaderConfig config, final Context context, final Object imgUrl, final OnWaitBitmapListener listener, final int index) {
//        Glide.with(context)
//                .load(imgUrl)
//                .asBitmap()//强制Glide返回一个Bitmap对象
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
//                        listener.getBitmap(bitmap, index, imgUrl);
//                    }
//                });
    }
}