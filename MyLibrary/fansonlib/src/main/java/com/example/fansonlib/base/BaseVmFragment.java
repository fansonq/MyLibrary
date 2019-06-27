package com.example.fansonlib.base;

import android.arch.lifecycle.Observer;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fansonlib.bean.LoadStateBean;
import com.example.fansonlib.constant.ConstLoadState;


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
        getLifecycle().addObserver(mViewModel);
        registerLoadState();
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
     * 注册请求网络时的状态监听
     */
    private void registerLoadState() {
        getViewModel().mLoadState.observe(this, new Observer<LoadStateBean>() {
            @Override
            public void onChanged(@Nullable LoadStateBean stateBean) {
                handlerLoadState(stateBean);
            }
        });
    }

    /**
     * 处理请求网络时的状态
     */
    protected void handlerLoadState(LoadStateBean stateBean) {
        if (stateBean == null) {
            return;
        }
        if (TextUtils.isEmpty(stateBean.getState())) {
            return;
        }
        switch (stateBean.getState()) {
            case ConstLoadState.SUCCESS_STATE:
                hideLoading();
                break;
            case ConstLoadState.ERROR_STATE:
                hideLoading();
                showFailureState(stateBean.getContent());
                break;
            case ConstLoadState.EMPTY_STATE:
                hideLoading();
                break;
            case ConstLoadState.COMPLETE_STATE:
                hideLoading();
                break;
            default:
                break;
        }
    }

    /**
     * 显示失败的状态
     * @param errorMsg 出错原因
     */
    protected  void showFailureState(String errorMsg){
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
            getLifecycle().removeObserver(mViewModel);
            mViewModel.detachView();
        }
    }
}
