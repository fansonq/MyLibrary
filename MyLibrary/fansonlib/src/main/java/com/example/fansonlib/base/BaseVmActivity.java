package com.example.fansonlib.base;

import android.databinding.ViewDataBinding;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 16:39
 * Describe：集成ViewModel的BaseActivity
 */
public abstract class BaseVmActivity<VM extends BaseViewModel,D extends ViewDataBinding> extends BaseActivity<D> implements BaseView {

    protected VM mViewModel;

    @Override
    protected void initView() {
        mViewModel = createViewModel();
        dataSuccessObserver();
    }

    protected abstract VM  createViewModel();

    /**
     * 观察接收成功的数据
     */
    protected abstract void dataSuccessObserver();

}
