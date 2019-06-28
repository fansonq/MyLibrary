package com.example.fansonlib.animation.rx;

import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.View;

import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Consumer;

/**
 * @author Created by：Fanson
 * Created Time: 2019/6/27 16:22
 * Describe：
 */
public class AnimationDisposable extends MainThreadDisposable {

    private final ViewPropertyAnimatorCompat animator;
    private final Consumer<View> animationCancelAction;

    public AnimationDisposable(final ViewPropertyAnimatorCompat animator, final Consumer<View> animationCancelAction) {
        this.animator = animator;
        this.animationCancelAction = animationCancelAction;
    }

    @Override
    protected void onDispose() {
        animator.setListener(new ViewPropertyAnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(final View view) {
                try {
                    animationCancelAction.accept(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        animator.cancel();
        animator.setListener(null);
    }
}
