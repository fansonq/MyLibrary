package com.example.fansonlib.http.retrofit;

import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by：fanson
 * Created on：2017/9/13 13:36
 * Describe：为了策略封装Retrofit，用工厂接口生成Api
 */

public interface IApiFactory {

    /**
     * 根据Url匹配，生成Api
     *
     * @param url
     * @param params
     */
    Flowable createApi(String url, Map params);

}
