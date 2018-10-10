package com.example.fansonlib.base.adapter;

/**
 * Created by zhy on 16/6/22.
 */
public interface ItemViewDelegate<T>
{

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(BaseHolder holder, T t, int position);

}
