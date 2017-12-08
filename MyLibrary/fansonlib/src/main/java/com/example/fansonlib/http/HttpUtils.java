package com.example.fansonlib.http;

import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by：fanson
 * Created on：2017/9/12 14:39
 * Describe：网络框架的代理类
 */

public class HttpUtils implements IHttpStrategy {

    private static IHttpStrategy mStrategy; //被代理的对象

    private volatile static HttpUtils instance;

    public static HttpUtils getHttpUtils(){
        if (instance == null){
            synchronized (HttpUtils.class){
                if (instance == null){
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化立即启动
     * 传入被代理的对象，Volly实现类 okHttp实现类 等等
     */
    public static void init(IHttpStrategy strategy){
        mStrategy = strategy;
    }

    @Override
    public void get(String url,  HttpResponseCallback callback) {
        mStrategy.get(url,callback);
    }

    @Override
    public void post(String url, Map params, HttpResponseCallback callback) {
        mStrategy.post(url,params,callback);
    }

    @Override
    public Disposable getCurrentDisposable() {
        return mStrategy.getCurrentDisposable();
    }

    @Override
    public void cancelCurrent() {
        mStrategy.cancelCurrent();
    }

    @Override
    public void cancelAll() {
        mStrategy.cancelAll();
    }
}
