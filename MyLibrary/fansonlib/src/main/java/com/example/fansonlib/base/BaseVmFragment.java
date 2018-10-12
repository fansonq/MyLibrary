package com.example.fansonlib.base;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fansonlib.utils.TUtil;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 16:07
 * Describe：集成ViewModel的BaseMvpFragment
 */
public abstract class BaseVmFragment<VM extends BaseViewModel,D extends ViewDataBinding> extends BaseFragment<D> implements BaseView {

    protected VM mViewModel;

    @Override
    protected View initView(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = VMProviders(this, (Class<VM>) TUtil.getInstance(this, 0));
        if (null != mViewModel) {
            dataObserver();
        }
        return  rootView;
    }

    /**
     * 观察接收数据
     */
    protected abstract void dataObserver();

    /**
     * 创建ViewModelProviders
     *
     * @return ViewModel
     */
    protected <VM extends AndroidViewModel> VM VMProviders(BaseFragment fragment, @NonNull Class<VM> modelClass) {
        return ViewModelProviders.of(fragment).get(modelClass);
    }

}
