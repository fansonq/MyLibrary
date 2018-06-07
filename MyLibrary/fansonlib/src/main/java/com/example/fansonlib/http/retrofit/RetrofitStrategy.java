package com.example.fansonlib.http.retrofit;

import android.util.Log;
import android.util.SparseArray;

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
     * 通过SparseArray存放CompositeDisposable
     */
    private SparseArray<CompositeDisposable> mCompositeDisposableArray;

    private int mTypeArray;

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
        if (mCompositeDisposableArray == null){
            mCompositeDisposableArray = new SparseArray<>();
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
         RetrofitClient.startObservable(mFactory.createApi(url, params), new ResourceSubscriber<M>() {
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
//        mCompositeDisposable.add(mCurrentDisposable);
//        mCompositeDisposableArray.put(mTypeArray,mCompositeDisposable);
    }

    @Override
    public Disposable getCurrentDisposable() {
        return mCurrentDisposable;
    }

    @Override
    public int setCompositeDisposableType(int type) {
        mTypeArray = type;
        return type;
    }

    @Override
    public void cancelCurrent(int type) {
        if (mCompositeDisposableArray != null && mCompositeDisposableArray.size() >0) {
            Disposable compositeDisposable = mCompositeDisposableArray.get(type);
            compositeDisposable.dispose();
            compositeDisposable = null;
            mCompositeDisposableArray.remove(type);
            mCompositeDisposableArray.clear();
            mCompositeDisposableArray = null;
            Log.d("TTT","cancel");
        }
    }

    @Override
    public void cancelAll() {
//        if (mCompositeDisposableArray != null && mCompositeDisposableArray.size() >0) {
//            for (int i=0;i<mCompositeDisposableArray.size();i++){
//                CompositeDisposable compositeDisposable = mCompositeDisposableArray.get(i);
//                mCompositeDisposableArray.remove(i);
//                compositeDisposable.dispose();
//                compositeDisposable=null;
//
//            }
//        }
    }
}
