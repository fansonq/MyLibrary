package com.example.fansonlib.base;

import android.arch.lifecycle.Observer;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.fansonlib.constant.ConstMvvmLoadState;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 16:39
 * Describe：集成ViewModel的BaseActivity
 */
public abstract class BaseVmActivity<VM extends BaseViewModel,D extends ViewDataBinding> extends BaseActivity<D> implements BaseView {

    protected VM mViewModel;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mViewModel = createViewModel();
        handlerLoadState();
        dataSuccessObserver();
    }

    /**
     * 创建ViewModel实例
     * @return ViewModel实例
     */
    protected abstract VM  createViewModel();

    /**
     * 获取ViewModel实例
     * @return ViewModel实例
     */
    protected VM getViewModel(){
        if (mViewModel == null){
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
                        default:
                            break;
                    }
                }
            });
        }
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
        if (mViewModel != null){
            mViewModel.detachView();
        }
    }
}
