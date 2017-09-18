package com.example.fansonlib.base;

/**
 * Created by：fanson
 * Created on：2017/9/12 11:29
 * Describe：基于Rx的Presenter封装,控制订阅的生命周期，带有M层
 */

public abstract class BasePresenterM<M extends IBaseModel, T extends BaseView> extends BasePresenter<T> {

    private static final String TAG = BasePresenterM.class.getSimpleName();
    protected M mModel;

    public BasePresenterM() {
        this.mModel = createModel();
    }

    /**
     * 实例化Model
     */
    protected abstract M createModel();

    public void detachView() {
        super.detachView();
        if (mModel != null) {
            mModel.onDestroy();
            mModel = null;
        }
    }

}

