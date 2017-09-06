package com.example.fansonlib.base;


import android.util.Log;

import org.reactivestreams.Subscription;

import java.util.Observable;
import java.util.Observer;

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
public abstract class BasePresenter<T extends BaseView> implements Observer {

    private static final String TAG = BasePresenter.class.getSimpleName();
    private T mBaseView;
    private Disposable mDisposable;
    private BaseModel mBaseModel;


    private BasePresenter presenter;

    public BasePresenter() {
        presenter = this;
    }

    /**
     * 生命周期是否是Resume
     */
    private boolean isResume;

    /**
     * Data是否已回调到View
     */
    private boolean isCallback;

    /**
     * 绑定View
     *
     * @param _baseView
     * @param
     */
    public void attachView(T _baseView) {
        this.mBaseView = _baseView;
    }

    /**
     * 创建Model
     */
    public void isNeedModel() {
        mBaseModel = new BaseModel(presenter);
    }

    public void detachView() {
        mBaseView = null;
        unSubscribe();
        if (mBaseModel != null) {
            mBaseModel.deleteObserver(this);
        }
    }


    public boolean isViewAttached() {
        return mBaseView != null;
    }

    /**
     * 可不用此方法
     * 直接调用mBaseView
     *
     * @return
     */

    public T getBaseView() {
        return this.mBaseView;
    }

    public void checkViewAttached() {
        if (!isViewAttached())
            throw new BaseViewNotAttachedException();
    }

    private static class BaseViewNotAttachedException extends RuntimeException {
        public BaseViewNotAttachedException() {
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
        return (ResourceSubscriber) observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnLifecycle(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        Log.d(TAG, "OnSubscribe");
                    }
                }, new LongConsumer() {
                    @Override
                    public void accept(long t) throws Exception {
                        Log.d(TAG, "OnRequest");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "OnCancel");
                    }
                })
                .subscribeWith(subscriber);
    }

    protected void onResume() {
        isResume = true;
    }

    protected void onStop() {
        isResume = false;
    }

    /**
     * 收到被观察者发布过来的通知
     *
     * @param observable 被观察者对象
     * @param object     数据
     */
    @Override
    public void update(Observable observable, Object object) {
        receiveObservable(observable, object);
    }

    /**
     * 收到被观察者发布过来的通知，该抽象方法在P层实现处理
     *
     * @param observable 被观察者对象
     * @param object     数据
     */
    protected abstract void receiveObservable(Observable observable, Object object);
}
