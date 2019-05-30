package com.example.fansonlib.base;

import android.arch.lifecycle.Observer;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fansonlib.constant.ConstMvvmLoadState;


/**
 * @author Created by：Fanson
 *         Created Time: 2018/10/11 16:07
 *         Describe：集成ViewModel的BaseMvpFragment
 */
public abstract class BaseVmFragment<VM extends BaseViewModel, D extends ViewDataBinding> extends BaseFragment<D> implements BaseView {

    /**
     * 泛型，ViewModel实例
     */
    protected VM mViewModel;

    @Override
    protected View initView(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = createViewModel();
        handlerLoadState();
        dataSuccessObserver();
        return rootView;
    }

    /**
     * 创建ViewModel实例
     *
     * @return ViewModel实例
     */
    protected abstract VM createViewModel();

    /**
     * 获取ViewModel实例
     *
     * @return ViewModel实例
     */
    protected VM getViewModel() {
        if (mViewModel == null) {
            mViewModel = createViewModel();
        }
        return mViewModel;
    }

    /**
     * 处理请求网络时的状态
     */
    private void handlerLoadState(){
        if (mViewModel != null) {
            mViewModel.mLoadState.observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String state) {
                    if (TextUtils.isEmpty(state)){
                        return;
                    }
                    switch (state) {
                        case ConstMvvmLoadState.SUCCESS_STATE:
                            hideLoading();
                            break;
                        case ConstMvvmLoadState.ERROR_STATE:
                            hideLoading();
                            break;
                        case ConstMvvmLoadState.COMPLETE_STATE:
                            hideLoading();
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    /**
     * 观察接收ViewModel返回的成功数据
     */
    protected abstract void dataSuccessObserver();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewModel != null) {
            mViewModel.detachView();
        }
    }
}
