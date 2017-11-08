package com.example.fansonlib.image.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.fansonlib.image.BaseImageLoaderStrategy;
import com.example.fansonlib.image.ImageLoaderConfig;
import com.example.fansonlib.image.OnLoadingListener;
import com.example.fansonlib.image.OnProgressListener;
import com.example.fansonlib.image.OnWaitBitmapListener;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.Glide.with;

/**
 * Created by：fanson
 * Created on：2017/4/17 13:32
 * Describe：Glide的策略实现类
 */

public class GlideLoaderStrategy implements BaseImageLoaderStrategy {
    private static int MAX_DISK_CACHE = 1024 * 1024 * 50;
    private static int MAX_MEMORY_CACHE = 1024 * 1024 * 10;
    private ArrayList<String> mBitmapIndexList;
    private ArrayList<Object> mBitmapUrlList;
    private Object mNextUrl;

    private static final String TAG = GlideLoaderStrategy.class.getSimpleName();

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
        with(context)
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
    public void displayFromDrawable(ImageLoaderConfig config, Context context, int imageId, ImageView imageView) {
    }

    @Override
    public void displayFromSDCard(ImageLoaderConfig config, String uri, ImageView imageView) {

    }

    @Override
    public void loadCircleImage(ImageLoaderConfig config, Context context, ImageView imageView, String imgUrl) {
        with(context)
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
    public void loadGifImage(ImageLoaderConfig config, Context context, ImageView imageView, String imgUrl) {
        with(context)
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
    public void loadCornerImage(ImageLoaderConfig config, Context context, ImageView imageView, String imgUrl) {
        with(context)
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

    @Override
    public void getBitmap(ImageLoaderConfig config, final Context context, final Object imgUrl, final OnWaitBitmapListener listener, final int index) {
        Glide.with(context)
                .load(imgUrl)
                .asBitmap()//强制Glide返回一个Bitmap对象
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
//                        if (index != 0) {
                            listener.getBitmap(bitmap, index, imgUrl);
//                            int nextIndex = askIndexIsOk(index);
//                            if (nextIndex!= 0) {
//                                listener.getBitmap(bitmap, nextIndex, mNextUrl);
//                            }
//                        } else {
//                            if (mBitmapIndexList == null) {
//                                mBitmapIndexList = new ArrayList<>();
//                                mBitmapUrlList = new ArrayList<>();
//                            }
//                            mBitmapIndexList.add(String.valueOf(index));
//                            mBitmapUrlList.add(imgUrl);
//                        }
                    }
                });
    }

    /**
     * 访问下一个应该回调的数据是否已在集合里，若是，则执行回调
     *
     * @param currentIndex 当前回调返回的index
     * @return 下一个应该回调的数据
     */
    private int askIndexIsOk(int currentIndex) {
        String nextIndex;
        for (int i = 0; i < mBitmapIndexList.size(); i++) {
            if ((currentIndex + 1) == Integer.parseInt(mBitmapIndexList.get(i))) {
                nextIndex = mBitmapIndexList.get(i);
                mNextUrl = mBitmapUrlList.get(i);
                mBitmapIndexList.remove(nextIndex);
                mBitmapUrlList.remove(i);
                return Integer.parseInt(nextIndex);
            }
        }
        return 0;
    }
}
