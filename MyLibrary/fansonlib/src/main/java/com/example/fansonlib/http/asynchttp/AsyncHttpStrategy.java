package com.example.fansonlib.http.asynchttp;

import com.example.fansonlib.http.HttpResponseCallback;
import com.example.fansonlib.http.IHttpStrategy;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;
import io.reactivex.disposables.Disposable;

/**
 * Created by：fanson
 * Created on：2017/9/13 13:58
 * Describe：AsyncHttpClient的策略实现
 */

public class AsyncHttpStrategy implements IHttpStrategy {

    private static AsyncHttpClient httpClient;

    private static String BASE_URL;

    //单例--双重检查锁定
    public static AsyncHttpClient getInstance() {
        if (httpClient == null) {
            synchronized (AsyncHttpStrategy.class) {
                if (httpClient == null) {
                    httpClient = new AsyncHttpClient();
                }
            }
        }
        return httpClient;
    }

    /**
     * 初始化，设置基础URL
     *
     * @param url
     */
    public static void init(String url) {
        BASE_URL = url;
    }

    @Override
    public void get(String url, HttpResponseCallback callback) {

    }

    @Override
    public void post(String url, Map params, final HttpResponseCallback callback) {
        getInstance().post(BASE_URL + url, MapToParams(params), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               callback.onSuccess(new Gson().fromJson(String.valueOf(response),callback.getDataClass()));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public Disposable getCurrentDisposable() {
        return null;
    }

    @Override
    public int setCompositeDisposableType(int type) {
        return 0;
    }

    @Override
    public void cancelCurrent(int type) {

    }

    @Override
    public void cancelAll() {

    }

    /**
     * 将Map类型转为RequestParams
     * @param params Map
     * @return
     */
    private RequestParams MapToParams(Map params){
        RequestParams requestParams = new RequestParams();
        for (Object key : params.keySet()) {
            requestParams.put((String) key, params.get(key));
        }
        return requestParams;
    }
}
