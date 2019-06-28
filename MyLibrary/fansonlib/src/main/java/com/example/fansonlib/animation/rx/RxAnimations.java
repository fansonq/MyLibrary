package com.example.fansonlib.animation.rx;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.example.fansonlib.animation.rx.RxAnimationBuilder.animate;

/**
 * @author Created by：Fanson
 * Created Time: 2019/6/27 16:05
 * Describe：
 */
public class RxAnimations {

    private static final float OPAQUE = 1.0f;
    private static final float TRANSPARENT = 0.0f;

    private static final int IMMEDIATE = 0;

    public static Completable animateTogether(final CompletableSource... completables) {
        return Completable.mergeArray(completables);
    }

    public static Completable hide(final View view) {
        return animate(view, IMMEDIATE).fadeOut().schedule();
    }

    public static Completable hide(final View... views) {
        return Observable.fromArray(views)
                .flatMapCompletable(new Function<View, CompletableSource>() {
                    @Override
                    public CompletableSource apply(View view) throws Exception {
                        return hide();
                    }
                });
    }

    public static Completable hideViewGroupChildren(final ViewGroup viewGroup) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                hideViewGroup(viewGroup);
            }
        });
    }

    public static Completable hideViewGroupChildren(final ViewGroup... viewGroups) {
        return Observable.fromArray(viewGroups)
                .flatMapCompletable(new Function<ViewGroup, CompletableSource>() {
                    @Override
                    public CompletableSource apply(ViewGroup viewGroup) throws Exception {
                        return hideViewGroupChildren();
                    }
                });
    }

    private static void hideViewGroup(final ViewGroup viewGroup) {
        for (int i = 0, childCount = viewGroup.getChildCount(); i < childCount; i++) {
            final View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                hideViewGroup((ViewGroup) child);
            } else {
                child.setAlpha(0f);
            }
        }
    }

    public static Completable show(final View view) {
        return animate(view, IMMEDIATE).fadeIn().schedule();
    }

    public static Completable fadeIn(final View view) {
        return animate(view).fadeIn()
                .onAnimationCancel(new Consumer<View>() {
                    @Override
                    public void accept(View aView) throws Exception {
                        aView.setAlpha(OPAQUE);
                    }
                })
                .schedule();
    }

    public static Completable fadeIn(final View view, final int duration) {
        return animate(view, new DecelerateInterpolator())
                .duration(duration)
                .fadeIn()
                .onAnimationCancel(new Consumer<View>() {
                    @Override
                    public void accept(View aView) throws Exception {
                        aView.setAlpha(OPAQUE);
                    }
                })
                .schedule();
    }

    public static Completable fadeIn(final View view, final int duration, final int delay) {
        return animate(view, duration, delay)
                .interpolator(new DecelerateInterpolator())
                .fadeIn()
                .onAnimationCancel(new Consumer<View>() {
                    @Override
                    public void accept(View aView) throws Exception {
                        aView.setAlpha(OPAQUE);
                    }
                })
                .schedule();
    }

    public static Completable fadeInWithDelay(final int delay, final int duration, final View... views) {
        return Observable.range(0, views.length)
                .flatMapCompletable(new Function<Integer, CompletableSource>() {
                    @Override
                    public CompletableSource apply(Integer integer) throws Exception {
                        return animate(views[integer], new LinearInterpolator()).duration(duration)
                                .delay(integer * delay)
                                .fadeIn().schedule();
                    }
                });

    }

    public static Completable slideHorizontal(final View view, final int duration, final int xOffset) {
        final float endingX = view.getX() + xOffset;
        return animate(view, new AccelerateDecelerateInterpolator())
                .duration(duration)
                .translateBy(xOffset, 0)
                .onAnimationCancel(new Consumer<View>() {
                    @Override
                    public void accept(View v) throws Exception {
                        v.setX(endingX);
                    }
                })
                .schedule(false);
    }

    public static Completable slideVertical(final View view, final int duration, final int yOffset) {
        final float endingY = view.getY() + yOffset;
        return animate(view, new AccelerateDecelerateInterpolator())
                .duration(duration)
                .translateBy(0, yOffset)
                .onAnimationCancel(new Consumer<View>() {
                    @Override
                    public void accept(View v) throws Exception {
                        v.setY(endingY);
                    }
                })
                .schedule(false);
    }

    public static Completable enter(final View view, final int xOffset, final int yOffset) {
        final float startingX = view.getX();
        final float startingY = view.getY();
        return animate(view, new DecelerateInterpolator())
                .fadeIn()
                .translateBy(xOffset, yOffset)
                .onAnimationCancel(new Consumer<View>() {
                    @Override
                    public void accept(View aView) throws Exception {
                        set(aView, startingX, startingY, OPAQUE);
                    }
                })
                .schedule();
    }

    public static Completable enter(final View view, final int delay, final int xOffset, final int yOffset) {
        final float startingX = view.getX();
        final float startingY = view.getY();
        return animate(view, new DecelerateInterpolator())
                .delay(delay)
                .fadeIn()
                .translateBy(xOffset, yOffset)
                .onAnimationCancel(new Consumer<View>() {
                    @Override
                    public void accept(View aView) throws Exception {
                        set(aView, startingX, startingY, OPAQUE);
                    }
                })
                .schedule();
    }

    public static Completable enter(final View view, final int duration, final int xOffset, final int yOffset, final int delay) {
        final float startingX = view.getX();
        final float startingY = view.getY();
        return animate(view, duration, delay)
                .interpolator(new DecelerateInterpolator())
                .fadeIn()
                .translateBy(xOffset, yOffset)
                .onAnimationCancel(new Consumer<View>() {
                    @Override
                    public void accept(View aView) throws Exception {
                        set(aView, startingX, startingY, OPAQUE);
                    }
                })
                .schedule();
    }

    public static Completable enterTogether(final int xOffset, final View... views) {
        return Observable.fromArray(views)
                .flatMapCompletable(new Function<View, CompletableSource>() {
                    @Override
                    public CompletableSource apply(View view) throws Exception {
                        return enter(view, xOffset, 0);
                    }
                });
    }

    public static Completable enterTogether(final int delay, final int xOffset, final View... views) {
        return doAfterDelay(delay, Observable.fromArray(views)
                .flatMapCompletable(new Function<View, CompletableSource>() {
                    @Override
                    public CompletableSource apply(View view) throws Exception {
                        return enter(view, xOffset, 0);
                    }
                }));
    }

    public static Completable enterViewsWithDelay(final int delay, final int duration, final int xOffset, final View... views) {
        return enterViewsWithDelay(0, delay, duration, xOffset, views);
    }

    public static Completable enterViewsWithDelay(final int initialDelay, final int delay, final int duration, final int xOffset, final View... views) {
        return Observable.range(0, views.length)
                .flatMapCompletable(new Function<Integer, CompletableSource>() {
                    @Override
                    public CompletableSource apply(Integer integer) throws Exception {
                        return enter(views[integer], duration, xOffset, 0, integer * delay + initialDelay);
                    }
                });
    }

    public static Completable enterWithRotation(final View view, final int duration, final int xOffset, final int yOffset, final int delay, final int rotation) {
        final float startingX = view.getX();
        final float startingY = view.getY();
        final float startRotation = view.getRotation();
        return animate(view, duration, delay)
                .fadeIn()
                .counterRotateBy(rotation)
                .translateBy(xOffset, yOffset)
                .onAnimationCancel(new Consumer<View>() {
                    @Override
                    public void accept(View aView) throws Exception {
                        set(aView, startingX, startingY, OPAQUE, startRotation);
                    }
                })
                .schedule();
    }

    public static Completable leave(final View view, final int xOffset, final int yOffset) {
        final float startingX = view.getX();
        final float startingY = view.getY();
        return animate(view, new AccelerateInterpolator())
                .fadeOut()
                .translateBy(xOffset, yOffset)
                .onAnimationCancel(new Consumer<View>() {
                    @Override
                    public void accept(View aView) throws Exception {
                        set(aView, startingX, startingY, TRANSPARENT);
                    }
                })
                .schedule(false);
    }

    public static Completable fadeOut(final View view) {
        return animate(view, new AccelerateInterpolator())
                .fadeOut()
                .onAnimationCancel(new Consumer<View>() {
                    @Override
                    public void accept(View aView) throws Exception {
                        aView.setAlpha(TRANSPARENT);
                    }
                })
                .schedule();
    }

    public static Completable fadeOut(final View view, final int duration) {
        return animate(view, new AccelerateInterpolator())
                .duration(duration)
                .fadeOut()
                .onAnimationCancel(new Consumer<View>() {
                    @Override
                    public void accept(View view) throws Exception {
                        view.setAlpha(TRANSPARENT);
                    }
                })
                .schedule();
    }

    public static Completable fadeOut(final View view, final int duration, final int delay) {
        return animate(view, duration, delay)
                .interpolator(new AccelerateInterpolator())
                .fadeOut()
                .onAnimationCancel(new Consumer<View>() {
                    @Override
                    public void accept(View aView) throws Exception {
                        aView.setAlpha(TRANSPARENT);
                    }
                })
                .schedule();
    }

    public static Completable doAfterDelay(final int delay, final Action action) {
        return Completable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .concatWith(Completable.fromAction(action));
    }

    public static Completable doAfterDelay(final int delay, final Completable completable) {
        return Completable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .concatWith(completable);
    }

    public static void set(final View view, final float x, final float y, final float alpha) {
        view.setAlpha(alpha);
        view.setX(x);
        view.setY(y);
    }

    public static void set(final View view, final float x, final float y, final float alpha, final float rotation) {
        set(view, x, y, alpha);
        view.setRotation(rotation);
    }
}