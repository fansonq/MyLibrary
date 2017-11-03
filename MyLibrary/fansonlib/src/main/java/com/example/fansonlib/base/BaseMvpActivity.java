package com.example.fansonlib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.fansonlib.utils.InputMethodUtils;

/**
 * Created by：fanson
 * Created on：2017/2/11 15:44
 * Describe：集成Mvp的BaseActivity
 */

public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity implements BaseView {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    /**
     * 实例化Presenter
     */
    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.detachView();
        }
        InputMethodUtils.fixInputMethodManagerLeak(this);
    }
}
