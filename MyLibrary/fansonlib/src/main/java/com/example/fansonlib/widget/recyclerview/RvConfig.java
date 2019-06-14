package com.example.fansonlib.widget.recyclerview;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.loadmore.LoadMoreView;

/**
 * @author Created by：Fanson
 * Created Time: 2019/6/13 17:32
 * Describe：MyRecyclerView的配置（造建者模式）
 */
public class RvConfig {

    /**
     * 滑动中是否支持加载图片
     */
    private boolean mScrollLoadEnable ;

    /**
     * 布局管理器
     */
    private RecyclerView.LayoutManager  mLayoutManager ;

    /**
     * RecyclerView的FooterView视图（加载中，加载出错，空数据）
     */
    private LoadMoreView mLoadMoreView;

    /**
     * 滑动到指定的剩余数量，开始加载下一页
     */
    private int mPreLoadNumber;

    public RvConfig(Builder builder){
        this.mPreLoadNumber = builder.mPreLoadNumber;
        this.mScrollLoadEnable = builder.mScrollLoadEnable;
        this.mLayoutManager = builder.mLayoutManager;
        this.mLoadMoreView = builder.mLoadMoreView;
    }

    public boolean getScrollLoadEnable() {
        return mScrollLoadEnable;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public LoadMoreView getLoadMoreView() {
        return mLoadMoreView;
    }

    public int getPreLoadNumber() {
        return mPreLoadNumber;
    }

    public static class Builder{

        /**
         * 滑动中是否支持加载图片
         */
        private boolean mScrollLoadEnable;

        /**
         * 滑动到指定的剩余数量，开始加载下一页
         */
        private int mPreLoadNumber;

        /**
         * 布局管理器
         */
        private RecyclerView.LayoutManager  mLayoutManager;

        /**
         * RecyclerView的FooterView视图（加载中，加载出错，空数据）
         */
        private LoadMoreView mLoadMoreView;


        /**
         *  设置滑动中是否支持加载图片
         * @param enable true/false
         * @return
         */
        public Builder setScrollLoadEnable(boolean enable){
            mScrollLoadEnable = enable;
            return this;
        }

        /**
         *  设置预加载的开始位置
         * @param number 倒数位置
         * @return
         */
        public Builder setPreLoadNumber(int number){
            mPreLoadNumber = number;
            return this;
        }

        /**
         *  设置布局管理器
         * @param manager 布局管理器
         * @return
         */
        public Builder setLayoutManager(RecyclerView.LayoutManager manager){
            mLayoutManager = manager;
            return this;
        }


        /**
         *  设置FooterView视图（加载中，加载出错，空数据）
         * @param view FooterView视图
         * @return
         */
        public Builder setLoadMoreView(LoadMoreView view){
            mLoadMoreView = view;
            return this;
        }

        public RvConfig build(){
            return new RvConfig(this);
        }


    }



}
