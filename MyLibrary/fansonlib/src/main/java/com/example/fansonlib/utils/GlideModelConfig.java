package com.example.fansonlib.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;

/**
 * Created by：fanson
 * Created on：2017/2/24 17:54
 * Describe：
 */

public class GlideModelConfig implements com.bumptech.glide.module.GlideModule {

    int diskSize = 1024 * 1024 * 250;//磁盘缓存空间，如果不设置，默认是：250 * 1024 * 1024 即250MB
    int memorySize = (int) (Runtime.getRuntime().maxMemory()) / 6;  // 取1/4最大内存作为最大缓存

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 定义缓存大小和位置
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskSize));  //手机磁盘
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "cache", diskSize)); //sd卡磁盘

        // 默认内存和图片池大小
      /*MemorySizeCalculator calculator = new MemorySizeCalculator(context);
      int defaultMemoryCacheSize = calculator.getMemoryCacheSize(); // 默认内存大小
      int defaultBitmapPoolSize = calculator.getBitmapPoolSize(); // 默认图片池大小
      builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize)); // 该两句无需设置，是默认的
      builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));*/

        // 自定义内存和图片池大小
        builder.setMemoryCache(new LruResourceCache(memorySize));
        builder.setBitmapPool(new LruBitmapPool(memorySize));

        // 定义图片格式
//        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }
}
