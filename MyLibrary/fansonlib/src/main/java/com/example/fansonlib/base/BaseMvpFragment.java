package com.example.fansonlib.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by：fanson
 * Created on：2017/2/11 16:48
 * Describe：集成Mvp的BaseMvpFragment
 */

public abstract class BaseMvpFragment<P extends BasePresenter,D extends ViewDataBinding> extends BaseFragment<D> implements BaseView {

    /**
     * 泛型，Presenter实例
     */
    protected P mPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }

    /**
     * 实例化Presenter
     * @return Presenter实例
     */
    protected abstract P createPresenter();
}
