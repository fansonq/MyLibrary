package com.example.fansonlib.widget.recyclerview;

/**
 * @author Created by：Fanson
 * Created Time: 2019/5/27 17:39
 * Describe：监听RecyclerView，下拉刷新的接口
 */
public interface IRvRefreshListener {

    /**
     * 完成下拉刷新
     */
    void onCompleteRefresh();

    /**
     * 开启下拉刷新
     */
    void onStartRefresh();
}
