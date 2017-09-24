package com.example.fansonlib.base;


import java.lang.ref.WeakReference;

/**
 * Created by：fanson
 * Created on：2016/10/15 17:32
 * Describe：基于Rx的Presenter封装,控制订阅的生命周期
 */
public abstract class BasePresenter<T extends BaseView> {

    private static final String TAG = BasePresenter.class.getSimpleName();
    protected WeakReference<T> mBaseView;
    protected BasePresenter presenter;

    public BasePresenter() {
        presenter = this;
    }

    /**
     * 生命周期是否是Resume
     */
    protected boolean isResume;

    /**
     * Data是否已回调到View
     */
    protected boolean isCallback;

    /**
     * 绑定View
     *
     * @param _baseView
     * @param
     */
    public void attachView(T _baseView) {
        this.mBaseView = new WeakReference<>(_baseView);
    }

    public void detachView() {
        if (mBaseView != null) {
            mBaseView.clear();
            mBaseView = null;
        }
    }


    public boolean isViewAttached() {
        return (mBaseView != null ? mBaseView.get() : null) != null;
    }

    /**
     * 获取BaseView
     *
     * @return
     */
    public T getBaseView() {
        if (isViewAttached()) {
            return this.mBaseView.get();
        } else {
            throw new BaseViewNotAttachedException();
        }
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


    protected void onResume() {
        isResume = true;
    }

    protected void onStop() {
        isResume = false;
    }
}
