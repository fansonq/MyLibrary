package com.example.fansonlib.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
* @author Created by：Fanson
* Created on：2016/8/25.
* Description：基类Fragment（带DataBinding）
*/
public abstract class BaseFragment<D extends ViewDataBinding> extends Fragment {

    protected Activity hostActivity;
    protected D mBinding;

    /**
     * 是否初始化的标识
     * 默认false
     */
    private boolean isInited = false;

    /**
     * 是否可见的标识
     * 默认false
     */
    protected boolean mIsVisible = true;

    /**
     * 根View
     */
    protected View rootView;

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,getLayoutId(),container,false);
        rootView = mBinding.getRoot();
        initView(rootView,inflater,container, savedInstanceState);
        return rootView;
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsVisible = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
        hostActivity = null;
    }

    /**
     * 获取DataBinding
     * @return DataBinding的绑定
     */
    public  D  getBinding(){
        return (D) mBinding;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("onAttach... activity = ", "" + activity.toString());
        onAttachToContext(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
     * 获取宿主的Actiivity
     *
     * @return
     */
    protected Context getHoldingContext() {
        return hostActivity;
    }

    /**
     * 加载Fragment Layout的Id；
     * return返回的是：Fragment的Layout
     *
     * @return
     */
    protected abstract int getLayoutId();

    protected void listenEvent() {
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
     * 用来区别是不是被显示到前台
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mIsVisible = !hidden;
//        if (!isInited && !hidden) {
//            isInited = true;
//            initData();
//            listenEvent();
//        }
    }

    /**
     * onCreateView中初始化View
     * @param rootView rootView
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState savedInstanceState
     * @return View
     */
    protected abstract View initView(View rootView,LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    protected abstract void initData();


    /**
     * 通过Class跳转界面
     * @param targetActivity 目标Activity
     */
    public void startMyActivity(Class<?> targetActivity) {
        startActivity(targetActivity, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     * @param targetActivity 目标Activity
     * @param bundle 携带数据Bundle
     */
    public void startActivity(Class<?> targetActivity, Bundle bundle) {
        Intent intent = new Intent(hostActivity, targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

}
