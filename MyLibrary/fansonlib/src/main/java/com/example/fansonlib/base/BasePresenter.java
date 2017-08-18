package com.example.fansonlib.base;


import android.util.Log;

import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.LongConsumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by：fanson
 * Created on：2016/10/15 17:32
 * Describe：基于Rx的Presenter封装,控制订阅的生命周期
 */
public abstract class BasePresenter<T extends BaseView> {

    private static  final String TAG=BasePresenter.class.getSimpleName();
    private T mBaseView;
    private Disposable mDisposable;
    /**
     * 生命周期是否是Resume
     */
    private boolean isResume;

    /**
     * Data是否已回调到View
     */
    private boolean isCallback;

    public void attachView(T _baseView) {
        this.mBaseView = _baseView;
    }

    public void detachView() {
        mBaseView = null;
        unSubscribe();
    }


    public boolean isViewAttached() {
        return mBaseView != null;
    }

    /**
     * 可不用此方法
     * 直接调用mBaseView
     * @return
     */

    public T getBaseView(){
        return this.mBaseView;
    }

    public void checkViewAttached(){
        if(!isViewAttached())
            throw new BaseViewNotAttachedException();
    }

    private static class BaseViewNotAttachedException extends RuntimeException {
        public BaseViewNotAttachedException(){
            super("Please call Presenter attachView(BaseView) before" +
                    " requesting data to the Presenter");
        }
    }

    /**
     * RXjava取消注册，以避免内存泄露
     */
    protected void unSubscribe() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
//
//    protected void addSubscrebe(Disposable disposable) {
//        if (mCompositeDisposable == null) {
//            mCompositeDisposable = new CompositeDisposable();
//        }
//        mCompositeDisposable.add(disposable);
//    }

    protected ResourceSubscriber addSubscrebe(Flowable observable, ResourceSubscriber subscriber) {
        return (ResourceSubscriber)observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnLifecycle(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        Log.d(TAG,"OnSubscribe");
                    }
                }, new LongConsumer() {
                    @Override
                    public void accept(long t) throws Exception {
                        Log.d(TAG,"OnRequest");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG,"OnCancel");
                    }
                })
                .subscribeWith(subscriber);
    }

    protected void onResume(){
        isResume = true;
    }

    protected void onStop(){
        isResume = false;
    }
}
