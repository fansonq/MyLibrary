package com.example.fansonlib.image.glide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.fansonlib.R;
import com.example.fansonlib.image.BaseImageLoaderStrategy;
import com.example.fansonlib.image.ImageLoaderConfig;
import com.example.fansonlib.image.OnLoadingListener;
import com.example.fansonlib.image.OnProgressListener;
import com.example.fansonlib.image.OnWaitBitmapListener;

import java.lang.ref.WeakReference;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.Glide.with;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * @author Created by：fanson
 * Created on：2017/4/17 13:32
 * Describe：Glide的策略实现类
 */

public class GlideLoaderStrategy implements BaseImageLoaderStrategy {

    private static final String TAG = GlideLoaderStrategy.class.getSimpleName();

    private static int MAX_DISK_CACHE = 1024 * 1024 * 50;
    private static int MAX_MEMORY_CACHE = 1024 * 1024 * 10;

    private ImageLoaderConfig mImageLoaderConfig;

    private RequestOptions mOptions1;
    private RequestOptions mOptionsCircle;

    /**
     * 常量
     */
    static class Constants {
        /**
         *  模糊
         */
        public static final int BLUR_VALUE = 20;
        /**
         * 圆角
         */
        public static final int CORNER_RADIUS = 10;
        /**
         * 边距
         */
        public static final int MARGIN = 5;
        /**
         * 0-1之间  10%原图的大小
         */
        public static final float THUMB_SIZE = 0.5f;
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
    private RequestOptions getOptionsCircle() {
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

    /**
     * 初始化tGif加载配置
     *
     * @param config ImageLoaderConfig
     */
    @SuppressLint("CheckResult")
    private RequestOptions getGifOptions(ImageLoaderConfig config) {
        if (mOptions1 == null) {
            mOptions1 = new RequestOptions();
            mOptions1.error(config.getErrorPicRes())
                    .placeholder(config.getPlacePicRes())
                    //下载的优先级
                    .priority(Priority.NORMAL)
                    //缓存策略
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        return mOptions1;
    }

    @Override
    public void setLoaderConfig(ImageLoaderConfig config) {
        mImageLoaderConfig = config;
    }

    @Override
    public void loadImage(Context context, ImageView view, Object imgUrl) {
        if (isValidContextForGlide(context) && (isValidImageViewForGlide(view) != null)) {
            with(context)
                    .load(imgUrl)
                    .apply(getOptions1(mImageLoaderConfig))
                    //先加载缩略图 然后在加载全图
                    .thumbnail(Constants.THUMB_SIZE)
//                .transition(DrawableTransitionOptions.withCrossFade())
                    .into(view);
        }

    }

    @Override
    public void loadImageWithListener(Context context, ImageView view, Object imgUrl, OnLoadingListener listener1, OnProgressListener listener2) {

    }

    @Override
    public void displayFromDrawable(Context context, int imageId, ImageView view) {
        if (isValidContextForGlide(context) && (isValidImageViewForGlide(view) != null)) {
            with(context)
                    .load(imageId)
                    .thumbnail(Constants.THUMB_SIZE)
                    .apply(getOptions1(mImageLoaderConfig))
                    .into(view);
        }
    }

    @Override
    public void displayFromSDCard(String uri, ImageView imageView) {
    }

    @Override
    public void loadCircleImage(Context context, ImageView view, String imgUrl) {
        if (isValidContextForGlide(context) && (isValidImageViewForGlide(view) != null)) {
            with(context)
                    .load(imgUrl)
                    .apply(getOptionsCircle())
                    .apply(bitmapTransform(new CropCircleTransformation()))
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .apply(bitmapTransform(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(view);
        }
    }

    @Override
    public void loadGifImage(Context context, ImageView view, Object imgUrl) {
        if (isValidContextForGlide(context) && (isValidImageViewForGlide(view) != null)) {
            with(context)
                    .load(imgUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(getGifOptions(mImageLoaderConfig))
                    .into(view);
        }
    }

    @Override
    public void loadCornerImage(Context context, ImageView view, String imgUrl, int radius) {
        if (isValidContextForGlide(context) && (isValidImageViewForGlide(view) != null)) {
            with(context)
                    .load(imgUrl)
                    .thumbnail(Constants.THUMB_SIZE)
                    .apply(getOptions1(mImageLoaderConfig))
                    .apply(bitmapTransform(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL)))
//                .transition(DrawableTransitionOptions.withCrossFade())
                    .into(view);
        }
    }

    @Override
    public void onPauseRequest(Context context) {
        if (isValidContextForGlide(context)) {
            Glide.with(context).pauseRequests();
        }
    }


    @Override
    public void clearMemory(Context context) {
        if (context == null) {
            return;
        }
        Glide.get(context).clearMemory();
    }

    @Override
    public void getBitmap(final Context context, final Object imgUrl, final OnWaitBitmapListener listener) {
        if (!isValidContextForGlide(context)) {
            return;
        }
        Glide.with(context).asBitmap()
                .load(imgUrl)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        listener.getBitmap(bitmap, imgUrl);
                    }
                });
    }

    /**
     * 检测Glide使用的Context是否可用
     *
     * @param context 上下文
     */
    private boolean isValidContextForGlide(Context context) {
        boolean valid = true;
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing()) {
                valid = false;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * 检测Glide使用的ImageView是否可用
     *
     * @param view 图片控件
     * @return ImageView/null
     */
    private ImageView isValidImageViewForGlide(ImageView view) {
        if (view == null) {
            return null;
        }
        final WeakReference<ImageView> weakReference = new WeakReference<>(view);
        ImageView target = weakReference.get();
        if (target != null) {
            return target;
        }
        return null;
    }

}