package com.example.fansonlib.base;

import android.arch.lifecycle.Observer;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.fansonlib.bean.LoadStateBean;
import com.example.fansonlib.constant.ConstLoadState;

/**
 * @author Created by：Fanson
 *         Created Time: 2018/10/11 16:39
 *         Describe：集成ViewModel的BaseActivity
 */
public abstract class BaseVmActivity<VM extends BaseViewModel, D extends ViewDataBinding> extends BaseActivity<D> implements BaseView {

    protected VM mViewModel;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mViewModel = createViewModel();
        getLifecycle().addObserver(mViewModel);
        registerLoadState(mViewModel);
        dataSuccessObserver();
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
    protected void registerLoadState(BaseViewModel baseViewModel) {
        if (baseViewModel == null){
            return;
        }
        baseViewModel.mLoadState.observe(this, new Observer<LoadStateBean>() {
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
                showErrorState(stateBean.getContent());
                break;
            case ConstLoadState.EMPTY_STATE:
                hideLoading();
                showNoDataState();
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
    protected  void showErrorState(String errorMsg){
    }

    /**
     * 显示空数据的状态
     */
    protected void showNoDataState() {
    }

    /**
     * 观察接收成功的数据
     */
    protected abstract void dataSuccessObserver();

    @Override
    public void showTip(String tipContent) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewModel != null) {
            getLifecycle().removeObserver(mViewModel);
            mViewModel.detachView();
        }
    }


}
