package com.example.fansonlib.image;

/**
 * Created by：fanson
 * Created on：2017/4/14 17:31
 * Describe：用到了Builder模式，一步一步的创建一个复杂对象的创建者模式，
 * 它允许用户在不知道内部构建细节的情况下，可以更精细的控制对象的构建流程
 */

public class ImageLoaderConfig {

    /**
     * 具体加载器
     */
    private BaseImageLoaderStrategy mClient;
    /**
     * 占位图
     */
    private int mPlacePicRes;
    /**
     * 错误图
     */
    private int mErrorPicRes;

    /**
     * 图片的Url
     */
    private String mImgUrl;

    public ImageLoaderConfig(Builder builder) {
        this.mClient = builder.mClient;
        this.mPlacePicRes = builder.mPlacePicRes;
        this.mErrorPicRes = builder.mErrorPicRes;
        this.mImgUrl = builder.mImgUrl;
    }

    public BaseImageLoaderStrategy getClient() {
        return mClient;
    }

    public int getPlacePicRes() {
        return mPlacePicRes;
    }

    public int getErrorPicRes() {
        return mErrorPicRes;
    }


    public String getImgUrl() {
        return mImgUrl;
    }

    public static class Builder {
        private BaseImageLoaderStrategy mClient;//具体加载器
        private int mPlacePicRes;//占位图
        private int mErrorPicRes;//错误图
        private String mImgUrl;

        public Builder() {
        }

        public Builder client(BaseImageLoaderStrategy provider) {
            this.mClient = provider;
            return this;
        }

        public Builder errorPicRes(int res) {
            this.mErrorPicRes = res;
            return this;
        }

        public Builder placePicRes(int res) {
            this.mPlacePicRes = res;
            return this;
        }

        public Builder imageUrl(String url){
            this.mImgUrl = url;
            return this;
        }

        public ImageLoaderConfig build() {
            return new ImageLoaderConfig(this);
        }
    }
}
