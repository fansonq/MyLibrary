package com.example.fansonlib.base;

/**
 * Created by：fanson
 * Created on：2016/10/15 17:29
 * Describe：MVP的BaseView
 */
public interface BaseView {

    /**
     * 显示Loading动画
     */
    void showLoading();

    /**
     * 隐藏Loading动画
     */
    void hideLoading();

    /**
     * 显示提示语，为了传给View层的SnackBar使用
     * @param tipContent 提示语
     */
    void showTip(String tipContent);

}
