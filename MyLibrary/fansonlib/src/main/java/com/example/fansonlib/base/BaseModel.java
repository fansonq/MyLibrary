package com.example.fansonlib.base;

import android.util.Log;

import com.example.fansonlib.http.HttpUtils;

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
 * Created by fansonq on 2017/9/2.
 * 数据层操作的BaseModel
 */

public class BaseModel implements IBaseModel{
    private static final String TAG = BaseModel.class.getSimpleName();
    private Disposable mDisposable;

    /**
     * RXjava取消注册，以避免内存泄露
     */
    protected void unSubscribe() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        HttpUtils.getHttpUtils().cancelCurrent();
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

    public void onDestroy() {
        unSubscribe();
    }

}
