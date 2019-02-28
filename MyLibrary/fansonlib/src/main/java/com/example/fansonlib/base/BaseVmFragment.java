package com.example.fansonlib.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 16:07
 * Describe：集成ViewModel的BaseMvpFragment
 */
public abstract class BaseVmFragment<VM extends BaseViewModel,D extends ViewDataBinding> extends BaseFragment<D> implements BaseView {

    protected VM mViewModel;

    @Override
    protected View initView(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = createViewModel();
        dataSuccessObserver();
        return  rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 创建ViewModel实例
     * @return ViewModel实例
     */
    protected abstract VM  createViewModel();

    /**
     * 观察接收成功的数据
     */
    protected abstract void dataSuccessObserver();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewModel != null){
            mViewModel.detachView();
        }
    }
}
