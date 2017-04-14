package com.example.fansonlib.animation;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;

import com.example.fansonlib.R;


public class AnimHelper {

    public static LayoutAnimationController getLayoutAnimation(Context hostActivity) {
        Animation animation = AnimationUtils.loadAnimation(hostActivity, R.anim.item_from_right_anim);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
        layoutAnimationController.setInterpolator(new AccelerateInterpolator());
        layoutAnimationController.setDelay(0.2f);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return layoutAnimationController;
    }

    //the viewholder will come in with a bit delay,which you can control by param of duration
    //this method should be called when you swipe up!
    public static void BottomInDelayAnim(RecyclerView.ViewHolder holder, Context context, int duration) {
        View itemView = holder.itemView;
        clear(itemView);
        itemView.setTranslationY(MetricUtils.getScrHeight(context));
        itemView.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(duration)
                .start();
    }

    //the viewholder will come in with a bit delay,which you can control by param of duration
    //this method should be called when you swipe down!
    public static void TopInDelayAnim(RecyclerView.ViewHolder holder, Context context, int duration) {
        View itemView = holder.itemView;
        clear(itemView);
        itemView.setTranslationY(-holder.itemView.getRootView().getHeight());
        itemView.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(duration)
                .start();
    }

    //the viewholder will come in from sereen's left,which you can control the time by param of duration
    public static void LeftSlideInAnim(RecyclerView.ViewHolder holder, int duration) {
        View itemView = holder.itemView;
        clear(itemView);
        ViewCompat.setTranslationX(itemView, -itemView.getRootView().getWidth());
        ViewCompat.animate(itemView)
                .translationX(0)
                .setDuration(duration)
                .setInterpolator(new DecelerateInterpolator(2.f))
                .start();
    }

    //the viewholder will come in from sereen's right,which you can control the time by param of duration
    public static void RightSlideInAnim(RecyclerView.ViewHolder holder, int duration) {
        View itemView = holder.itemView;
        ViewCompat.setTranslationX(itemView, itemView.getRootView().getWidth());
        ViewCompat.animate(itemView)
                .translationX(0)
                .setDuration(duration)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    //the viewholder will come in with a fade transition,which you can control the time by param of duration
    public static void FadeInAnim(RecyclerView.ViewHolder holder, int duration) {
        View view = holder.itemView;
        clear(view);
        ViewCompat.setAlpha(view, 0);
        ViewCompat.animate(view)
                .alpha(1)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(duration)
                .start();
    }

    //the viewholder will come in with a scale,which you can control the time by param of duration
    public static void ScaleOutAnim(RecyclerView.ViewHolder holder, int duration) {
        View view = holder.itemView;
        clear(view);
        ViewCompat.setScaleX(view, 0);
        ViewCompat.setScaleY(view, 0);
        ViewCompat.animate(view)
                .scaleX(1)
                .scaleY(1)
                .setInterpolator(new DecelerateInterpolator(2.f))
                .setDuration(duration)
                .start();
    }

    //clockwise rotation 360 degree,which by the view's vertical center
    public static void RotationXCenterCW(final RecyclerView.ViewHolder holder, int duration) {
        View view = holder.itemView;
        clear(view);
        ViewCompat.animate(view)
                .rotationXBy(360)
                .setInterpolator(new DecelerateInterpolator(2.f))
                .setDuration(duration)
                .start();
    }

    //anti-clockwise rotation 360 degree,which by the view's vertical center
    public static void RotationXCenterACW(final RecyclerView.ViewHolder holder, int duration) {
        View view = holder.itemView;
        clear(view);
        ViewCompat.animate(view)
                .rotationXBy(-360)
                .setInterpolator(new DecelerateInterpolator(2.f))
                .setDuration(duration)
                .start();
    }

    //anti-clockwise rotation 90 degree,which by the view's bottom
    public static void RotationXBottomACW(final RecyclerView.ViewHolder holder, int duration) {
        View view = holder.itemView;
        clear(view);
        view.setPivotX(0);
        view.setPivotY(view.getHeight());
        ViewCompat.setRotationX(view, 90);
        ViewCompat.animate(view)
                .rotationXBy(-90)
                .setInterpolator(new DecelerateInterpolator(2.f))
                .setDuration(duration)
                .start();
    }

    //clockwise rotation 90 degree,which by the view's bottom
    public static void RotationXBottomCW(final RecyclerView.ViewHolder holder, int duration) {
        View view = holder.itemView;
        clear(view);
        view.setPivotX(0);
        view.setPivotY(view.getHeight());
        ViewCompat.setRotationX(view, 270);
        ViewCompat.animate(view)
                .rotationXBy(90)
                .setInterpolator(new DecelerateInterpolator(2.f))
                .setDuration(duration)
                .start();
    }

    //clockwise rotation 90 degree,which by the view's top
    public static void RotationXTopCW(final RecyclerView.ViewHolder holder, int duration) {
        View view = holder.itemView;
        clear(view);
        view.setPivotX(0);
        view.setPivotY(0);
        ViewCompat.setRotationX(view, 270);
        ViewCompat.animate(view)
                .rotationXBy(90)
                .setInterpolator(new DecelerateInterpolator(2.f))
                .setDuration(duration)
                .start();
    }

    //anti-clockwise rotation 90 degree,which by the view's top
    public static void RotationXTopACW(final RecyclerView.ViewHolder holder, int duration) {
        View view = holder.itemView;
        clear(view);
        view.setPivotX(0);
        view.setPivotY(0);
        ViewCompat.setRotationX(view, 90);
        ViewCompat.animate(view)
                .rotationXBy(-90)
                .setInterpolator(new DecelerateInterpolator(2.f))
                .setDuration(duration)
                .start();
    }

    //before a new animation started,the old animation should be cleared!
    //this method is able to clear all property of the View,which you code in param!

    public static void clear(View v) {
        ViewCompat.setAlpha(v, 1);
        ViewCompat.setScaleY(v, 1);
        ViewCompat.setScaleX(v, 1);
        ViewCompat.setTranslationY(v, 0);
        ViewCompat.setTranslationX(v, 0);
        ViewCompat.setRotation(v, 0);
        ViewCompat.setRotationY(v, 0);
        ViewCompat.setRotationX(v, 0);
        ViewCompat.setPivotY(v, v.getMeasuredHeight() / 2);
        ViewCompat.setPivotX(v, v.getMeasuredWidth() / 2);
        ViewCompat.animate(v).setInterpolator(null).setStartDelay(0);
    }
}
