package com.example.fansonlib.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fansonlib.eventBean.EventNetWork;

import de.greenrobot.event.EventBus;


/**
 * Created by fanson on 2016/8/25.
 */
public abstract class BaseFragment extends Fragment {

    protected Activity hostActivity;

    /**
     * 是否初始化的标识
     * 默认false
     */
    private boolean isInited = false;

//    private OnFragmentInteractionListener mListener;

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
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), container, false);
        initView(rootView, savedInstanceState);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            if (!isHidden()) {
                isInited = true;
                initData();
                listenEvent();
            }
        }
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
    public void onDestroy() {
        super.onDestroy();
//        SampleApplicationLike.getRefWatcher(getActivity()).watch(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
        hostActivity = null;
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
     * 获取宿主的Acitivity
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

    //    /**
//     * 用来区别是不是被显示到前台
//     * @param hidden
//     */
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!isInited && !hidden) {
//            isInited = true;
//            initData();
//            listenEvent();
//        }
//    }

    /**
     * onCreateView中初始化View
     *
     * @param rootView
     * @param savedInstanceState
     */
    protected abstract View initView(View rootView, Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 在此类注册了EventBus，如果此类中不实现四个方法中的其中一个不行，空参数也不行
     * @param event
     */
    public void onEvent(EventNetWork event){

    }


}
