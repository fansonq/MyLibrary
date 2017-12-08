package com.example.fansonlib.http.retrofit;

import com.example.fansonlib.http.HttpResponseCallback;
import com.example.fansonlib.http.IHttpStrategy;

import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by：fanson
 * Created on：2017/9/12 14:56
 * Describe：Retrofit的策略实现
 */

public class RetrofitStrategy<M> implements IHttpStrategy {

    /**
     * 记录所有的网络Disposable
     */
    private CompositeDisposable mCompositeDisposable;
    /**
     * 当前网络的Disposable
     */
    private Disposable mCurrentDisposable;
    /**
     * Api的工厂接口
     */
    private IApiFactory mFactory;

    public RetrofitStrategy() {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
    }

    /**
     * 设置Retrofit的Api，通过传入实现的工厂类
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
                callback.onFailure(t.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
        mCompositeDisposable.add(mCurrentDisposable);
    }

    @Override
    public Disposable getCurrentDisposable() {
        return mCurrentDisposable;
    }

    @Override
    public void cancelCurrent() {
        if (mCompositeDisposable != null&&mCurrentDisposable!=null) {
            mCurrentDisposable.dispose();
            mCompositeDisposable.delete(mCurrentDisposable);
            mCurrentDisposable = null;
        }
    }

    @Override
    public void cancelAll() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable.clear();
        }
    }
}
