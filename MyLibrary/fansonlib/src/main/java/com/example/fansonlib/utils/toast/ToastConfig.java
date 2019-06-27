package com.example.fansonlib.utils.toast;

/**
 * @author Created by：Fanson
 * Created Time: 2019/4/9 18:51
 * Describe：Toast配置
 */
public class ToastConfig {

    /**
     * 背景颜色
     */
    private int mBgColor;

    /**
     * 文字颜色
     */
    private int mTextColor;

    /**
     * 文字大小
     */
    private int mTextSize;

    /**
     * 图标资源
     */
    private int mIconResource;

    public ToastConfig(Builder builder){
        this.mBgColor = builder.mBuilderBgColor;
        this.mTextColor = builder.mBuilderTextColor;
        this.mTextSize = builder.mBuilderTextSize;
        this.mIconResource = builder.mBuilderIconResource;
    }


    public int getBgColor(){
       return mBgColor;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public int getIconResource() {
        return mIconResource;
    }

    public static class Builder{

        private int mBuilderBgColor;
        private int mBuilderTextColor;
        private int mBuilderTextSize;
        private int mBuilderIconResource;

        public Builder setBgColor(int bgColor){
            mBuilderBgColor = bgColor;
            return this;
        }

        public Builder setTextColor(int textColor){
            mBuilderTextColor = textColor;
            return this;
        }

        public Builder setTextSize(int textSize){
            mBuilderTextSize = textSize;
            return this;
        }

        public Builder setIconResource(int iconResource){
            mBuilderIconResource = iconResource;
            return this;
        }

        public ToastConfig build(){
            return new ToastConfig(this);
        }
    }



}
