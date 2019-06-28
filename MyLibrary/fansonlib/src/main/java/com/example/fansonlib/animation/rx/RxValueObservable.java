package com.example.fansonlib.animation.rx;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Function;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

/**
 * @author Created by：Fanson
 * Created Time: 2019/6/27 15:16
 * Describe：
 */
public class RxValueObservable extends Observable<Object> {

    private final ValueAnimator valueAnimator;
    private final boolean isReversed;

    private final Function<ValueAnimator, Object> valueFactory;

    public static RxValueObservable from(final ValueAnimator valueAnimator) {
        return new RxValueObservable(valueAnimator, false, new Function<ValueAnimator, Object>() {
            @Override
            public Object apply(ValueAnimator valueAnimator) {
                return valueAnimator.getAnimatedValue();
            }
        });
    }

    public static RxValueObservable fromReversed(final ValueAnimator valueAnimator) {
        return new RxValueObservable(valueAnimator, true, new Function<ValueAnimator, Object>() {
            @Override
            public Object apply(ValueAnimator valueAnimator) {
                return valueAnimator.getAnimatedValue();
            }
        });
    }

    public static RxValueObservable fractionFrom(final ValueAnimator valueAnimator) {
        return new RxValueObservable(valueAnimator, false, new Function<ValueAnimator, Object>() {
            @Override
            public Object apply(ValueAnimator valueAnimator) {
                return valueAnimator.getAnimatedFraction();
            }
        });
    }

    public static RxValueObservable fractionFromReversed(final ValueAnimator valueAnimator) {
        return new RxValueObservable(valueAnimator, true, new Function<ValueAnimator, Object>() {
            @Override
            public Object apply(ValueAnimator valueAnimator) {
                return valueAnimator.getAnimatedFraction();
            }
        });
    }

    public static RxValueObservable from(final ValueAnimator valueAnimator, final boolean isReversed,
                                         final Function<ValueAnimator, Object> valueFactory) {
        return new RxValueObservable(valueAnimator, isReversed, valueFactory);
    }

    private RxValueObservable(final ValueAnimator valueAnimator, final boolean isReversed,
                              final Function<ValueAnimator, Object> valueFactory) {
        this.valueAnimator = valueAnimator;
        this.isReversed = isReversed;
        this.valueFactory = valueFactory;
    }

    @Override
    protected void subscribeActual(final Observer<? super Object> observer) {
        verifyMainThread();

        final UpdateListener updateListener = new UpdateListener(observer, valueFactory);
        final EndListener endListener = new EndListener(observer, valueAnimator);
        final UpdateDisposable updateDisposable = new UpdateDisposable(valueAnimator, updateListener, endListener);

        observer.onSubscribe(updateDisposable);
        valueAnimator.addUpdateListener(updateListener);
        valueAnimator.addListener(endListener);

        startAnimator();
    }

    private void startAnimator() {
        if (isReversed) {
            valueAnimator.reverse();
        } else {
            valueAnimator.start();
        }
    }

    private static final class UpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private final Observer<? super Object> observer;
        private final Function<ValueAnimator, Object> valueFactory;

        public UpdateListener(final Observer<? super Object> observer, final Function<ValueAnimator, Object> valueFactory) {
            this.observer = observer;
            this.valueFactory = valueFactory;
        }

        @Override
        public void onAnimationUpdate(final ValueAnimator valueAnimator) {
            try {
                observer.onNext(valueFactory.apply(valueAnimator));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static final class EndListener extends AnimatorListenerAdapter {

        private final Observer<? super Object> observer;
        private final ValueAnimator valueAnimator;

        public EndListener(final Observer<? super Object> observer, final ValueAnimator valueAnimator) {
            this.observer = observer;
            this.valueAnimator = valueAnimator;
        }

        @Override
        public void onAnimationEnd(final Animator animation) {
            valueAnimator.removeListener(this);
            observer.onComplete();
        }
    }

    private static final class UpdateDisposable extends MainThreadDisposable {

        private final ValueAnimator valueAnimator;
        private final UpdateListener updateListener;
        private final EndListener endListener;

        public UpdateDisposable(final ValueAnimator valueAnimator, final UpdateListener updateListener,
                                final EndListener endListener) {
            this.valueAnimator = valueAnimator;
            this.updateListener = updateListener;
            this.endListener = endListener;
        }

        @Override
        protected void onDispose() {
            valueAnimator.end();
            valueAnimator.removeUpdateListener(updateListener);
            valueAnimator.removeListener(endListener);
        }
    }
}

