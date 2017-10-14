package com.example.fansonlib.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.fansonlib.R;

/**
 * Created by fansonq on 2017/9/24.
 * 常见动画工具类
 */

public class AnimUtils {
    private int lastPosition = -1;

    /**
     * 底部升起
     * @param view 对应的View
     * @param position 位置
     */
    public  void bottomInAnim(final View view, final int position){
        if (position>lastPosition){
            Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.item_bottom_in);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(anim);
            lastPosition = position;
        }
    }
}
