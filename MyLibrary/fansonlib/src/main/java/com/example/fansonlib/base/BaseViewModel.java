package com.example.fansonlib.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.fansonlib.callback.IBaseViewModel;

import java.lang.ref.SoftReference;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 14:58
 * Describe：ViewModel基类
 */
public abstract class BaseViewModel<V extends BaseView, R extends BaseRepository> extends AndroidViewModel implements IBaseViewModel {

    private static final String TAG = BaseViewModel.class.getSimpleName();

    protected R mRepository;
    protected SoftReference<V> mBaseView;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mRepository = createRepository();
    }

    protected abstract R createRepository();

    /**
     * 注册设置BaseView，用于回调到View层，如果不需要回调，则不用注册
     * @param baseView baseView
     */
    public void setBaseView(V baseView) {
        attachView(baseView);
    }

    /**
     * 绑定View
     *
     * @param baseView
     */
    public void attachView(V baseView) {
        mBaseView = new SoftReference<>(baseView);
    }

    /**
     * 判断View是否被绑定
     *
     * @return true or false
     */
    public boolean isViewAttached() {
        return (mBaseView != null ? mBaseView : null) != null;
    }

    /**
     * 获取BaseView
     *
     * @return
     */
    public V getBaseView() {
        if (mBaseView != null) {
            return this.mBaseView.get();
        } else {
            throw new BaseViewNotAttachedException();
        }
    }

    private static class BaseViewNotAttachedException extends RuntimeException {
        public BaseViewNotAttachedException() {
            super("Please call BaseViewModel attachView(BaseView) before" +
                    " requesting data to the BaseViewModel");
        }
    }

    /**
     * 解除绑定的View
     */
    public void detachView() {
        if (mBaseView != null) {
            mBaseView.clear();
            mBaseView = null;
        }
    }


    @Override
    public void onCreate() {
        Log.d(TAG,"生命周期onCreate阶段");
    }

    @Override
    public void onStart() {
        Log.d(TAG,"生命周期onStart阶段");
    }

    @Override
    public void onResume() {
        Log.d(TAG,"生命周期onResume阶段");
    }


    @Override
    public void onPause() {
        Log.d(TAG,"生命周期onPause阶段");
    }

    @Override
    public void onStop() {
        Log.d(TAG,"生命周期onStop阶段");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"生命周期onDestroy阶段");
    }
}
