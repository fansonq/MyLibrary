package com.example.fansonlib.image;

/**
 * Created by：fanson
 * Created on：2017/4/17 17:53
 * Describe：图片加载的监听
 */

public interface OnLoadingListener {

    void loadStart();

    void loadSuccess();

    void loadFailed();

}
