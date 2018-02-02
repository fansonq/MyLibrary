package com.example.fansonlib.base;

import android.util.Log;

/**
 * @author Created by：fanson
 *         Created on：2017/9/12 11:29
 *         Describe：基于Rx的Presenter封装,控制订阅的生命周期，带有Repository层
 *         R是获取数据层的对象，B是实体类用于LiveData，T是绑定的视图
 */

public abstract class BasePresenterRep<R extends IBaseRepository, B, T extends BaseView> extends BasePresenter<B, T> {

    private static final String TAG = BasePresenterRep.class.getSimpleName();
    protected R mBaseRepository;

    public BasePresenterRep(T baseView) {
        super(baseView);
        mBaseRepository = createRepository();
    }

    /**
     * 创建Repository对象
     *
     * @return Repository对象
     */
    protected abstract R createRepository();

    @Override
    public void detachView() {
        super.detachView();
        Log.i(TAG,"BasePresenterRep detachView");
        if (mBaseRepository != null) {
            mBaseRepository.onDestroy();
            mBaseRepository = null;
        }
    }

}

