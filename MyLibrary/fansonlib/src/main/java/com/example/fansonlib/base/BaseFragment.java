package com.example.fansonlib.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fansonlib.callback.IBackFragmentListener;
import com.example.fansonlib.callback.IFragmentListener;
import com.example.fansonlib.constant.BaseConFragmentCode;
import com.example.fansonlib.manager.MyFragmentManager;


/**
 * @author Created by：Fanson
 * Created on：2016/8/25.
 * Description：基类Fragment（带DataBinding）
 */
public abstract class BaseFragment<D extends ViewDataBinding> extends Fragment implements MyFragmentManager.OnBackClickListener, BaseView {

    /**
     * 宿主Activity
     */
    protected Activity hostActivity;
    /**
     * DataBinding
     */
    protected D mBinding;

    /**
     * 处理Activity和Activity通信的接口
     */
    public IFragmentListener mFragmentListener;
    /**
     * 监听Fragment的返回
     */
    private IBackFragmentListener mIBackFragmentListener;

    /**
     * 标记是否已加载过数据
     */
    protected boolean mIsLoadedData = false;

    /**
     * 记录Fragment是否已onCreateView
     */
    protected boolean mIsCreatedView = false;
    /**
     * 记录Fragment是否已onStart
     */
    protected boolean mIsStart = false;
    /**
     * 记录Fragment是否可见，默认false
     */
    protected boolean mIsVisibleToUser = false;
    /**
     * 记录Fragment是否onResume
     */
    protected boolean mIsOnResume = false;

    /**
     * 根View
     */
    protected View rootView;

    /**
     * 加载Fragment Layout的Id；
     *
     * @return Fragment的Layout
     */
    protected abstract int getLayoutId();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
        mFragmentListener = (IFragmentListener) context;
        mIBackFragmentListener = (IBackFragmentListener) hostActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        rootView = mBinding.getRoot();
        mIsCreatedView = true;
        initView(rootView, inflater, container, savedInstanceState);
        return rootView;
    }

    /**
     * onCreateView中初始化View
     *
     * @param rootView           rootView
     * @param inflater           inflater
     * @param container          container
     * @param savedInstanceState savedInstanceState
     * @return View
     */
    protected abstract View initView(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 监听事件
     */
    protected void listenEvent() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (savedInstanceState == null) {
//            if (!isHidden()) {
//                isInited = true;
        initData();
        listenEvent();
//            }
//        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsStart = true;
        mIBackFragmentListener.currentFragmentBack(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsOnResume = true;
        firstVisibleToUser();
    }

    @Override
    public void onStop() {
        super.onStop();
        mIsStart = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsVisibleToUser = false;
        mIsCreatedView = false;
        mIsOnResume = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hostActivity = null;
        mFragmentListener = null;
        mIBackFragmentListener = null;
    }

    /**
     * 用来区别是不是被显示到前台
     *
     * @param hidden 是否隐藏
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mIsVisibleToUser = !hidden;
//        if (!isInited && !hidden) {
//            initData();
//            listenEvent();
//        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        firstVisibleToUser();
    }

    /**
     * 判断界面第一次对用户可见
     */
    private void firstVisibleToUser() {
        if (mIsCreatedView && mIsVisibleToUser && !mIsLoadedData) {
            mIsLoadedData = true;
            firstLoadData();
        }
    }

    /**
     * 第一次加载数据
     */
    protected void firstLoadData() {
    }

    /**
     * 获取DataBinding
     *
     * @return DataBinding的绑定
     */
    protected D getBinding() {
        return (D) mBinding;
    }

    /**
     * Called when the fragment attaches to the context
     *
     * @param context
     */
    protected void onAttachToContext(Context context) {
        this.hostActivity = (Activity) context;
    }

    /**
     * 此方法的目的是子类使用此方法findViewById不再需要强转，注意：接受类型一定不要写错
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T> T findMyViewId(int id) {
        T view = (T) rootView.findViewById(id);
        return view;
    }

    /**
     * 获取宿主的Activity
     *
     * @return hostActivity
     */
    protected Activity getHostActivity() {
        return hostActivity;
    }

    /**
     * 销毁hostActivity
     */
    protected void finishHostActivity() {
        if (hostActivity != null) {
            hostActivity.finish();
        }
    }

    @Override
    public void showLoading() {
        if (mFragmentListener != null) {
            mFragmentListener.onFragmentCallback(BaseConFragmentCode.SHOW_LOADING);
        }
    }

    @Override
    public void hideLoading() {
        if (mFragmentListener != null) {
            mFragmentListener.onFragmentCallback(BaseConFragmentCode.HIDE_LOADING);
        }
    }

    @Override
    public void showTip(String tipContent) {
    }

    @Override
    public boolean onBackClick() {
        if (getFragmentManager() == null) {
            return false;
        }
        {
            mFragmentListener.onFragmentCallback(BaseConFragmentCode.POP_FRAGMENT);
            return true;
        }
    }

    /**
     * 通过Class跳转界面
     *
     * @param targetActivity 目标Activity
     */
    public void startMyActivity(Class<?> targetActivity) {
        startActivity(targetActivity, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     *
     * @param targetActivity 目标Activity
     * @param bundle         携带数据Bundle
     */
    public void startActivity(Class<?> targetActivity, Bundle bundle) {
        Intent intent = new Intent(hostActivity, targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

}
