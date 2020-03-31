package com.example.fansonlib.image;

/**
 * @author Created by：fanson
 * Created on：2017/4/14 17:31
 * Describe：加载图片框架的一些参数的配置；
 * 用到了Builder模式，一步一步的创建一个复杂对象的创建者模式，
 * 它允许用户在不知道内部构建细节的情况下，可以更精细的控制对象的构建流程
 */

public class ImageLoaderConfig {

    /**
     * 占位图
     */
    private int mPlacePicRes;
    /**
     * 常规错误图
     */
    private int mDefErrorPicRes;
    /**
     * 圆形错误图
     */
    private int mCircleErrorRes;

    /**
     * 图片的Url
     */
    private String mImgUrl;

    /**
     * 最大磁盘缓存
     */
    private int maxDishCache;

    /**
     * 最大内存缓存
     */
    private int maxMemoryCache;

    public ImageLoaderConfig(Builder builder) {
        this.mPlacePicRes = builder.mPlacePicRes;
        this.mDefErrorPicRes = builder.mErrorPicRes;
        this.mCircleErrorRes = builder.mCircleErrorPicRes;
        this.mImgUrl = builder.mImgUrl;
        this.maxDishCache = builder.maxDiskCache;
        this.maxMemoryCache = builder.maxMemoryCache;
    }

    public int getPlacePicRes() {
        return mPlacePicRes;
    }

    public int getErrorPicRes() {
        return mDefErrorPicRes;
    }

    public int getCircleErrorRes() {
        return mCircleErrorRes;
    }

    public int getMaxDishCache() {
        return maxDishCache;
    }

    public int getMaxMemoryCache() {
        return maxMemoryCache;
    }

    public void getMaxMemoryCache(int maxMemoryCache) {
        this.maxMemoryCache = maxMemoryCache;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public static class Builder {
        /**
         * 占位图
         */
        private int mPlacePicRes;
        /**
         * 错误图
         */
        private int mErrorPicRes;
        private int mCircleErrorPicRes;
        private String mImgUrl;
        private int maxDiskCache = 1024 * 1024 * 500;
        private int maxMemoryCache = (int) (Runtime.getRuntime().maxMemory() / 8);

        public Builder() {
        }

        public Builder errorPicRes(int res) {
            this.mErrorPicRes = res;
            return this;
        }

        public Builder placePicRes(int res) {
            this.mPlacePicRes = res;
            return this;
        }

        public Builder imageUrl(String url) {
            this.mImgUrl = url;
            return this;
        }

        public Builder setMaxDiskCache(int max) {
            this.maxDiskCache = max;
            return this;
        }

        public Builder setMaxMemoryCache(int max) {
            this.maxMemoryCache = max;
            return this;
        }

        public Builder circleErrorPicRes(int mCircleErrorPicRes) {
            this.mCircleErrorPicRes = mCircleErrorPicRes;
            return this;
        }

        public ImageLoaderConfig build() {
            return new ImageLoaderConfig(this);
        }
    }


}
