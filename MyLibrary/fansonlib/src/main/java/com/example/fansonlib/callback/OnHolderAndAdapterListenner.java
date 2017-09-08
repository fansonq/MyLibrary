package com.example.fansonlib.callback;

/**
 * Created by：fanson
 * Created on：2017/9/8 14:43
 * Describe：用于Holder和Adapter的通信
 */

public interface OnHolderAndAdapterListenner {

    /**
     * 用于Holder和Adapter的通信
     * @param objects
     */
    void onHAListener(Object... objects);

}
