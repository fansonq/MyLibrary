package com.example.fansonlib.callback;

import android.support.v4.app.Fragment;

/**
 * @author Created by：Fanson
 * Created Time: 2019/4/2 9:50
 * Describe：监听在fragment点击返回键的接口
 */
public interface IBackFragmentListener {

    /**
     * 监听当前Fragment的返回
     * @param fragment 当前fragment
     */
    void currentFragmentBack(Fragment fragment);

}
