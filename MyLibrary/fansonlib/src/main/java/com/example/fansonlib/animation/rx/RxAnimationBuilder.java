package com.example.fansonlib.animation.rx;

import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.functions.Consumer;

/**
 * @author Created by：Fanson
 * Created Time: 2019/6/27 16:14
 * Describe：
 */
public class RxAnimationBuilder {

    private static final float OPAQUE = 1f;
    private static final float TRANSPARENT = 0f;

    private static final int DEFAULT_DURATION = 300;
    private static final int DEFAULT_DELAY = 0;

    public static RxAnimationBuilder animate(final View view) {
        return new RxAnimationBuilder(view, DEFAULT_DURATION, DEFAULT_DELAY, defaultInterpolator());
    }

    public static RxAnimationBuilder animate(final View view, final int duration) {
        return new RxAnimationBuilder(view, duration, DEFAULT_DELAY, new AccelerateDecelerateInterpolator());
    }

    public static RxAnimationBuilder animate(final int delay, final View view) {
        return new RxAnimationBuilder(view, DEFAULT_DURATION, delay, new AccelerateDecelerateInterpolator());
    }

    public static RxAnimationBuilder animate(final View view, final int duration, final int delay) {
        return new RxAnimationBuilder(view, duration, delay, new AccelerateDecelerateInterpolator());
    }

    public static RxAnimationBuilder animate(final View view, final Interpolator interpolator) {
        return new RxAnimationBuilder(view, DEFAULT_DURATION, DEFAULT_DELAY, interpolator);
    }

    public static RxAnimationBuilder animate(final View view, final int duration, final int delay, final Interpolator interpolator) {
        return new RxAnimationBuilder(view, duration, delay, interpolator);
    }

    private RxAnimationBuilder(final View view, final int duration, final int delay, final Interpolator interpolator) {
        this.viewWeakRef = new WeakReference<>(view);
        this.preTransformActions = new LinkedList<>();
        this.animateActions = new LinkedList<>();

        this.animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat animate) throws Exception {
                animate.setDuration(duration).setStartDelay(delay).setInterpolator(interpolator);
            }
        });
    }

    private final List<Consumer<ViewPropertyAnimatorCompat>> preTransformActions;
    private final List<Consumer<ViewPropertyAnimatorCompat>> animateActions;

    private Consumer<View> onAnimationCancelAction = new Consumer<View>() {
        @Override
        public void accept(View view) throws Exception {

        }
    };

    final WeakReference<View> viewWeakRef;

    public RxAnimationBuilder duration(final int duration) {
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat) throws Exception {
                viewPropertyAnimatorCompat.setDuration(duration);
            }
        });
        return this;
    }

    public RxAnimationBuilder delay(final int delay) {
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat) throws Exception {
                viewPropertyAnimatorCompat.setStartDelay(delay);
            }
        });
        return this;
    }

    public RxAnimationBuilder interpolator(final Interpolator interpolator) {
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat) throws Exception {
                viewPropertyAnimatorCompat.setInterpolator(interpolator);
            }
        });
        return this;
    }

    public RxAnimationBuilder fadeIn() {
        preTransformActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat) throws Exception {
                viewPropertyAnimatorCompat.alpha(0f);
            }
        });
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat animate) throws Exception {
                animate.alpha(OPAQUE);
            }
        });
        return this;
    }

    public RxAnimationBuilder fadeOut() {
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat animate) throws Exception {
                animate.alpha(TRANSPARENT);
            }
        });
        return this;
    }

    public RxAnimationBuilder rotate(final float rotation) {
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat animate) throws Exception {
                animate.rotation(rotation);
            }
        });
        return this;
    }

    public RxAnimationBuilder rotateBy(final float rotation) {
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat animate) throws Exception {
                animate.rotationBy(rotation);
            }
        });
        return this;
    }

    public RxAnimationBuilder counterRotateBy(final float rotation) {
        preTransformActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat preTransform) throws Exception {
                preTransform.rotationBy(-rotation);
            }
        });
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat animate) throws Exception {
                animate.rotationBy(rotation);
            }
        });
        return this;
    }

    public RxAnimationBuilder translateX(final int dX) {
        preTransformActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat) throws Exception {
                viewPropertyAnimatorCompat.xBy(-dX);
            }
        });
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat animate) throws Exception {
                animate.xBy(dX);
            }
        });
        return this;
    }

    public RxAnimationBuilder translateY(final int dY) {
        preTransformActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat) throws Exception {
                viewPropertyAnimatorCompat.yBy(-dY);
            }
        });
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat animate) throws Exception {
                animate.yBy(dY);
            }
        });
        return this;
    }

    public RxAnimationBuilder elevationBy(final int dZ) {
        preTransformActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat preTransform) throws Exception {
                preTransform.zBy(-dZ);
            }
        });
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat) throws Exception {
                viewPropertyAnimatorCompat.zBy(dZ);
            }
        });
        return this;
    }

    public RxAnimationBuilder translateBy(final int dX, final int dY) {
        preTransformActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat preTransform) throws Exception {
                preTransform.xBy(-dX).yBy(-dY);
            }
        });
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat animate) throws Exception {
                animate.xBy(dX).yBy(dY);
            }
        });
        return this;
    }

    public RxAnimationBuilder translateBy(final int dX, final int dY, final int dZ) {
        preTransformActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat) throws Exception {
                viewPropertyAnimatorCompat.xBy(-dX).yBy(-dY).zBy(-dZ);
            }
        });
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat animate) throws Exception {
                animate.xBy(dX).yBy(dY).zBy(dZ);
            }
        });
        return this;
    }

    public RxAnimationBuilder scaleX(final float dX) {
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat animate) throws Exception {
                animate.scaleXBy(dX);
            }
        });
        return this;
    }

    public RxAnimationBuilder scaleY(final float dY) {
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat) throws Exception {
                viewPropertyAnimatorCompat.scaleYBy(dY);
            }
        });
        return this;
    }

    public RxAnimationBuilder scale(final float dX, final float dY) {
        animateActions.add(new Consumer<ViewPropertyAnimatorCompat>() {
            @Override
            public void accept(ViewPropertyAnimatorCompat animate) throws Exception {
                animate.scaleXBy(dX).scaleYBy(dY);
            }
        });
        return this;
    }

    public RxAnimationBuilder onAnimationCancel(final Consumer<View> onAnimationCancelAction) {
        this.onAnimationCancelAction = onAnimationCancelAction;
        return this;
    }

    public Completable schedule() {
        return schedule(true);
    }

    public Completable schedule(final boolean preTransform) {
        return AnimateCompletable.forView(viewWeakRef,
                preTransform ? preTransformActions : null,
                animateActions,
                onAnimationCancelAction);
    }

    private static Interpolator defaultInterpolator() {
        return new AccelerateDecelerateInterpolator();
    }


}
