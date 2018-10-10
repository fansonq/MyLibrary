package com.example.fansonlib.widget.loadingview;

import android.view.animation.Animation;

/**
* @author Created by：Fanson
* Created on：2018/7/13 11:13
* Description：动画接口
*/

public interface ViewAnimProvider {
    Animation showAnimation();

    Animation hideAnimation();
}
