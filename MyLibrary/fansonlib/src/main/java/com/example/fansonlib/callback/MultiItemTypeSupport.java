package com.example.fansonlib.callback;

/**
 * Created by：fanson
 * Created on：2017/9/8 9:41
 * Describe：RecyclerView多布局处理的接口
 */

public interface MultiItemTypeSupport<M> {

    int getLayoutId(int itemType);

    int getItemViewType(int position, M bean);
}
