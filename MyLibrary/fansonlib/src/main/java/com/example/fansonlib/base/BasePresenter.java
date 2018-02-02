package com.example.fansonlib.base;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.util.Log;

import java.lang.ref.SoftReference;

/**
 * @author Created by：fanson
 *         Created on：2018/1/30 17:37
 *         Description：基于Rx的Presenter封装,加入LiveData控制生命周期
 * @Param B是实体类集成LiveData，T是绑定的视图
 */
public abstract class BasePresenter<B, T extends BaseView> extends LiveData<B> implements LifecycleObserver {

    private static final String TAG = BasePresenter.class.getSimpleName();
    protected T mBaseView;
    protected B mBaseBean;
    protected BasePresenter presenter;
    private Context mSoftContext;

    /**
     * 获取软引用持有的Context
     * @return Context软引用
     */
    public Context getSoftContext(){
        if (mSoftContext!=null){
            return mSoftContext;
        }else {
            throw new NullPointerException();
        }
    }

    public BasePresenter(T baseView) {
        initPresenter(baseView);
    }

    public BasePresenter(Context context, T baseView) {
        mSoftContext  = new SoftReference<>(context).get();
        initPresenter(baseView);
    }

    private void initPresenter(T baseView) {
        presenter = this;
        // 与View绑定
        attachView(baseView);
        // 将View绑定到观察模式中
        if (baseView instanceof LifecycleOwner) {
            ((LifecycleOwner) baseView).getLifecycle().addObserver(this);
        }
    }


/*-----------------------------------BaseView----------------------------------*/

    /**
     * 绑定View
     *
     * @param baseView
     * @param
     */
    public void attachView(T baseView) {
        this.mBaseView = baseView;
    }

    /**
     * 解除绑定的View
     */
    public void detachView() {
        if (mBaseView != null) {
            mBaseView = null;
        }
    }

    /**
     * 判断View是否被绑定
     *
     * @return true or false
     */
    public boolean isViewAttached() {
        return (mBaseView != null ? mBaseView : null) != null;
    }

    /**
     * 获取BaseView
     *
     * @return
     */
    public T getBaseView() {
        if (mBaseView != null) {
            return this.mBaseView;
        } else {
            throw new BaseViewNotAttachedException();
        }
    }

    private static class BaseViewNotAttachedException extends RuntimeException {
        public BaseViewNotAttachedException() {
            super("Please call Presenter attachView(BaseView) before" +
                    " requesting data to the Presenter");
        }
    }
 /*-----------------------------BaseView--------------------------------------*/



 /*-------------------------------Lifecycle------------------------------------*/

    /**
     * 观察到View的生命周期，处于OnCreate状态中
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onPresenterCreate() {
        Log.d(TAG, "BasePresenter On Create");
    }

    /**
     * 观察到View的生命周期，处于OnResume状态中
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onPresenterResume() {
        Log.d(TAG, "BasePresenter On Resume");
    }

    /**
     * 观察到View的生命周期，处于OnPause状态中
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPresenterPause() {
        Log.d(TAG, "BasePresenter On Pause");
    }

    /**
     * 观察到View的生命周期，处于OnStop状态中
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onPresenterStop() {
        Log.d(TAG, "BasePresenter On Stop");
    }

    /**
     * 观察到View的生命周期，处于OnDestroy状态中
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onPresenterDestroy() {
        Log.d(TAG, "BasePresenter On Destroy");
    }
 /*-------------------------------Lifecycle------------------------------------*/



 /*----------------------------------LiveData-----------------------------------*/

    /**
     * LiveData功能
     * 组件处于激活状态时，会收到数据更新回调
     */
    @Override
    protected void onActive() {
        super.onActive();
        Log.d(TAG, "BasePresenter onActive ：组件处于激活状态");
    }

    /**
     * LiveData功能
     * 组件处于“非”激活状态时，不会收到数据更新回调
     */
    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d(TAG, "BasePresenter onInactive ： 组件处于“非”激活状态");
    }
  /*-----------------------------------LiveData-----------------------------------*/
}
