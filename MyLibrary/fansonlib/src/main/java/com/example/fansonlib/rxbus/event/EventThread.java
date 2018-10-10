package com.example.fansonlib.rxbus.event;

import com.example.fansonlib.rxbus.SubscriberMethod;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @author Created by：fanson
 *         Created on：2017/12/1 10:50
 *         Description：事件线程
 */
public enum EventThread {
    /**
     * 主线程
     */
    MAIN_THREAD,
    /**
     * 新的线程
     */
    NEW_THREAD,
    /**
     * 读写线程
     */
    IO,
    /**
     * current thread
     */
    CURRENT_THREAD;

    /**
     * 用于处理订阅事件在那个线程中执行
     *
     * @param observable       d
     * @param subscriberMethod d
     * @return Observable
     */
    public static Flowable postToObservable(Flowable observable, SubscriberMethod subscriberMethod) {
        Scheduler scheduler;
        switch (subscriberMethod.threadMode) {
            case MAIN_THREAD:
                scheduler = AndroidSchedulers.mainThread();
                break;

            case NEW_THREAD:
                scheduler = Schedulers.newThread();
                break;

            case IO:
                scheduler = Schedulers.io();
                break;

            case CURRENT_THREAD:
                scheduler = Schedulers.trampoline();
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscriberMethod.threadMode);
        }
        return observable.observeOn(scheduler);
    }
}
