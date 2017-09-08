package com.example.fansonlib.callback;

/**
 * Created by：fanson
 * Created on：2017/9/8 9:41
 * Describe：RecyclerView多布局处理的接口
 */

public interface MultiItemTypeDelegate<M> {

    /**
     * 获取布局ID
     * @param itemType
     * @return
     */
    int getLayoutId(int itemType);


    int getItemViewType(M bean, int position);

//    /**
//     * 数据处理
//     * @param holder
//     * @param bean
//     * @param position
//     */
//    void bindData(BaseHolder holder, M bean, int position);
}
