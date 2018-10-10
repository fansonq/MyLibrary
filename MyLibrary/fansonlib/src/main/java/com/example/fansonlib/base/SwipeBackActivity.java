package com.example.fansonlib.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;


/**
 * Created by fansonq on 2017/6/29.
 * 可侧滑返回的Activity
 */

public abstract class SwipeBackActivity<D extends ViewDataBinding> extends BaseActivity<D>{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SwipeBackHelper.onCreate(this);
//        SwipeBackHelper.getCurrentPage(this)//获取当前页面
//                .setSwipeBackEnable(true)//设置是否可滑动
//                .setSwipeEdge(200)//可滑动的范围。px。200表示为左边200px的屏幕
//                .setSwipeEdgePercent(0.2f)//可滑动的范围。百分比。0.2表示为左边20%的屏幕
//                .setSwipeSensitivity(0.5f)//对横向滑动手势的敏感程度。0为迟钝 1为敏感
//                .setScrimColor(Color.BLUE)//底层阴影颜色
//                .setClosePercent(0.8f)//触发关闭Activity百分比
//                .setSwipeRelateEnable(false)//是否与下一级activity联动(微信效果)。默认关
//                .setSwipeRelateOffset(500)//activity联动时的偏移量。默认500px。
//                .setDisallowInterceptTouchEvent(true)//不抢占事件，默认关（事件将先由子View处理再由滑动关闭处理）
//                .addListener(new SwipeListener() {//滑动监听
//                    @Override
//                    public void onScroll(float percent, int px) {//滑动的百分比与距离
//                    }
//                    @Override
//                    public void onEdgeTouch() {//当开始滑动
//                    }
//                    @Override
//                    public void onScrollToClose() {//当滑动关闭
//                    }
//                });
//
//        SwipeBackHelper.getCurrentPage(this)
//                .setSwipeBackEnable(true)
//                .setSwipeSensitivity(0.5f)
//                .setSwipeRelateEnable(true)
//                .setSwipeRelateOffset(300);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        SwipeBackHelper.onPostCreate(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        SwipeBackHelper.onDestroy(this);
    }


}
