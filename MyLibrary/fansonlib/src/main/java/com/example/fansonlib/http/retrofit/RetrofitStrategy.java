package com.example.fansonlib.http.retrofit;

import android.support.v4.util.ArrayMap;

import com.example.fansonlib.http.HttpResponseCallback;
import com.example.fansonlib.http.IHttpStrategy;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;

/**
 * Created by：fanson
 * Created on：2017/9/12 14:56
 * Describe：Retrofit的策略实现
 */

public class RetrofitStrategy<M> implements IHttpStrategy {

    /**
     * 记录所有的网络请求的处理
     */
    private ArrayMap<Object, Disposable> mDisposableMaps ;
    /**
     * 当前网络的Disposable
     */
    private Disposable mCurrentDisposable;
    /**
     * Api的工厂接口
     */
    private IApiFactory mFactory;

    public RetrofitStrategy() {
        if (mDisposableMaps == null) {
            mDisposableMaps = new ArrayMap<>();
        }
    }

    /**
     * 设置Retrofit的Api，通过传入实现的工厂类
     *
     * @param factory
     */
    public void setApi(IApiFactory factory) {
        mFactory = factory;
    }

    @Override
    public void get(String url, HttpResponseCallback callback) {

    }

    @Override
    public void post(String url, Map params, final HttpResponseCallback callback) {
        mCurrentDisposable = RetrofitClient.startObservable(mFactory.createApi(url, params), new ResourceSubscriber<M>() {
            @Override
            public void onNext(M bean) {
                callback.onSuccess(bean);
            }

            @Override
            public void onError(Throwable t) {
                //TODO 最佳方案重写封装ResourceSubscriber
                if (t instanceof UnknownHostException || t instanceof HttpException){
                    callback.onFailure("无法链接到服务器");
                }else {
                    callback.onFailure(t.getMessage());
                }

            }

            @Override
            public void onComplete() {

            }
        });
        mDisposableMaps.put(url,mCurrentDisposable);
    }

    @Override
    public Disposable getCurrentDisposable() {
        return mCurrentDisposable;
    }

    @Override
    public void cancelCurrent(String url) {
        if (mDisposableMaps.isEmpty()){
            return;
        }
        mCurrentDisposable = mDisposableMaps.get(url);
        mDisposableMaps.remove(url);
        if (mCurrentDisposable!=null){
            mCurrentDisposable.dispose();
            mCurrentDisposable=null;
        }
    }

    @Override
    public void cancelAll() {
        if (mDisposableMaps.isEmpty()){
            return;
        }
        Iterator<Object> iterator = mDisposableMaps.keySet().iterator();
        while (iterator.hasNext()){
            Object key =  iterator.next();
            mCurrentDisposable = mDisposableMaps.get(key);
            if (mCurrentDisposable!=null){
                mCurrentDisposable.dispose();
                mCurrentDisposable = null;
            }
        }
        mDisposableMaps.clear();
    }
}
