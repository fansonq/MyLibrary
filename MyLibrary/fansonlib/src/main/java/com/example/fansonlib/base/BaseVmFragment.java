package com.example.fansonlib.base;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fansonlib.bean.LoadStateBean;
import com.example.fansonlib.constant.ConstLoadState;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 16:07
 * Describe：集成ViewModel的BaseMvpFragment
 */
public abstract class BaseVmFragment<VM extends BaseViewModel, D extends ViewDataBinding> extends BaseFragment<D> implements BaseView {

    /**
     * 泛型，ViewModel实例
     */
    protected VM mViewModel;
    /**
     * ViewModel集合，不包含类的泛型VM
     */
    private List<BaseViewModel> mViewModelList;

    @Override
    protected View initView(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = createViewModel();
        getLifecycle().addObserver(mViewModel);
        registerLoadState(mViewModel);
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
     * 获取ViewModel集合
     *
     * @return mViewModelList
     */
    public List<BaseViewModel> getViewModelList() {
        return mViewModelList;
    }

    /**
     * 添加ViewModel实例到集合，统一初始化并管理
     *
     * @param vmClass ViewModel类
     * @param <M>     继承BaseViewModel的ViewModel实例
     */
    protected <M extends BaseViewModel> void addViewModel(Class<M> vmClass) {
        if (mViewModelList == null) {
            mViewModelList = new ArrayList<>();
        }
        //判断已经添加过的ViewModel，则不再添加
        for (int i= 0;i<mViewModelList.size();i++){
            if (mViewModelList.get(i).getClass().equals(vmClass)){
                return;
            }
        }
        mViewModelList.add(ViewModelProviders.of(this).get(vmClass));
        getLifecycle().addObserver(mViewModelList.get(mViewModelList.size() - 1));
        registerLoadState(mViewModelList.get(mViewModelList.size() - 1));
    }

    /**
     * 注册请求网络时的状态监听
     */
    protected void registerLoadState(BaseViewModel baseViewModel) {
        if (baseViewModel == null) {
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
     *
     * @param errorMsg 出错原因
     */
    protected void showErrorState(String errorMsg) {
    }

    /**
     * 显示空数据的状态
     */
    protected void showNoDataState() {
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
            mViewModel.destroy();
        }
        if (mViewModelList != null) {
            for (int i = 0; i < mViewModelList.size(); i++) {
                getLifecycle().removeObserver(mViewModelList.get(i));
            }
            mViewModelList.clear();
            mViewModelList = null;
        }
    }
}
