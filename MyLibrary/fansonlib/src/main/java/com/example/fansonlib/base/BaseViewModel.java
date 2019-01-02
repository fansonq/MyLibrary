package com.example.fansonlib.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.fansonlib.bean.BaseBean;
import com.example.fansonlib.callback.IBaseViewModel;

import java.lang.ref.SoftReference;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 14:58
 * Describe：ViewModel基类
 */
public abstract class BaseViewModel<V extends BaseView, R extends BaseRepository,B extends BaseBean> extends AndroidViewModel implements IBaseViewModel {

    private static final String TAG = BaseViewModel.class.getSimpleName();

    protected SoftReference<V> mBaseView;
    protected R mRepository;
    protected MutableLiveData<B> mBean;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mRepository = createRepository();
        if (mBean == null) {
            mBean = new MutableLiveData<>();
        }
    }

    protected abstract R createRepository();

    /**
     * 供观察者调用
     * @return 实体类
     */
    public LiveData<B> getData() {
        return mBean;
    }

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
