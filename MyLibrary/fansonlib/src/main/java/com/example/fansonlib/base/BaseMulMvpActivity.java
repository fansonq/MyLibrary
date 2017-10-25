package com.example.fansonlib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by：fanson
 * Created on：2017/3/30 18:28
 * Describe：多个Presenter
 */

public abstract class BaseMulMvpActivity<P1 extends BasePresenter,P2 extends BasePresenter> extends BaseActivity implements BaseView{

    protected P1 mPresenter1;
    protected P2 mPresenter2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter1 = createPresenter1();
        mPresenter2 = createPresenter2();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter1.onResume();
        mPresenter2.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter1.onStop();
        mPresenter2.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter1 != null)
            mPresenter1.detachView();
        if (mPresenter2!=null){
            mPresenter2.detachView();
        }
    }

    /**
     * 实例化Presenter1
     */
    protected abstract P1 createPresenter1();

    /**
     * 实例化Presenter2
     */
    protected abstract P2 createPresenter2();

}
