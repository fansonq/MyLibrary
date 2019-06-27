package com.example.fansonlib.callback;

/**
 * @author Created by：Fanson
 * Created Time: 2019/4/2 9:53
 * Describe：监听Activity和Fragment的通信接口
 */
public interface IFragmentListener {

    /**
     * activity 和 fragment 的接口回調
     * @param objects
     */
    void onFragmentCallback(Object... objects);
}
