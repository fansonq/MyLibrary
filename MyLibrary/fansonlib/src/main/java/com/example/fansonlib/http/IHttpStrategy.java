package com.example.fansonlib.http;

import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by：fanson
 * Created on：2017/9/12 13:23
 * Describe：加载网络框架的策略接口
 */

public interface IHttpStrategy<M> {

    /**
     * 标准形式的封装GET
     * @param url
     * @param callback
     */
    void get(String url, HttpResponseCallback callback);

    /**
     * 标准形式的封装POST
     * @param url
     * @param params
     * @param callback
     */
    void post(String url, Map  params, HttpResponseCallback<M> callback);

    /**
     * 获取当前网络的Disposable
     */
    Disposable getCurrentDisposable();

    /**
     * 设置存储CompositeDisposable的Key值
     * @param type CompositeDisposable的Key值
     */
    int setCompositeDisposableType(int type);

    /**
     * 取消当前的网络操作
     * @param type CompositeDisposable的Key值
     */
    void cancelCurrent(int type);

    /**
     * 取消所有的网络操作
     */
    void cancelAll();

}
