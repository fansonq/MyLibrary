package com.example.fansonlib.callback;

import android.view.View;

/**
 * @author Created by：Fanson
 * Created Time: 2019/7/4 14:34
 * Describe：用于防止连续点击的接口
 */
public abstract  class OnMyClickListener implements View.OnClickListener{

    /**
     * 两次点击按钮之间的点击间隔不能少于1000毫秒
     */
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    /**
     * 记录最后一次点击时间
     */
    private static long lastClickTime;


    /**
     * 我的自定义点击事件
     * @param v 点击的View
     */
    public abstract void onMyClick(View v);


    @Override
    public void onClick(View v) {
        long curClickTime = System.currentTimeMillis();
        if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime;
            onMyClick(v);
        }
    }
}
