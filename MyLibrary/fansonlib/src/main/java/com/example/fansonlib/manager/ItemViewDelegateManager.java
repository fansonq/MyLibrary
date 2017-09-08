package com.example.fansonlib.manager;


import android.support.v4.util.SparseArrayCompat;

import com.example.fansonlib.callback.MultiItemTypeDelegate;

/**
 * Created by：fanson
 * Created on：2017/9/8 13:31
 * Describe：
 */

public class ItemViewDelegateManager<M> {

    SparseArrayCompat<MultiItemTypeDelegate<M>> delegates = new SparseArrayCompat();

    /**
     * 获取ItemView代理数量
     *
     * @return
     */
    public int getItemViewDelegateCount() {
        return delegates.size();
    }

}
