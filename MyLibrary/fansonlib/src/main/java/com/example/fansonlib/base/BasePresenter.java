package com.example.fansonlib.base;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by：fanson
 * Created on：2016/10/15 17:32
 * Describe：基于Rx的Presenter封装,控制订阅的生命周期
 */
public abstract class BasePresenter<T extends BaseView> {

    private T mBaseView;
    private CompositeSubscription mCompositeSubscription;

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
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscrebe(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}
