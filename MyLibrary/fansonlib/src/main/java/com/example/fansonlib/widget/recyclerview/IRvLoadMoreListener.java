package com.example.fansonlib.widget.recyclerview;

/**
 * @author  Created by：fanson
 * Created on：2016/12/17 17:59
 * Description：监听RecyclerView，加载更多的接口
 */
public interface IRvLoadMoreListener {

    /**
     * 加载更多
     * @param pageNum 页码
     */
    void onRvLoadMore(int pageNum);
}
