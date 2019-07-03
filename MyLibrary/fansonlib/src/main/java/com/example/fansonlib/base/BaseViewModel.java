package com.example.fansonlib.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.fansonlib.bean.BaseBean;
import com.example.fansonlib.bean.LoadStateBean;
import com.example.fansonlib.callback.IBaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 14:58
 * Describe：ViewModel基类
 */
public abstract class BaseViewModel<R extends BaseRepository, B extends BaseBean> extends AndroidViewModel implements IBaseViewModel {

    private static final String TAG = BaseViewModel.class.getSimpleName();

    private R mRepository;
    protected MutableLiveData<B> mBean;

    /**
     * Repository集合，不包含类的泛型R
     */
    private List<BaseRepository> mRepositoryList;

    /**
     * 处理网络请求时的状态
     */
    public MutableLiveData<LoadStateBean> mLoadState;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mLoadState = new MutableLiveData<>();
        mRepository = createRepository();
    }

    /**
     * 获取Repository实例
     *
     * @return mRepository
     */
    protected R getRepository() {
        if (mRepository == null) {
            mRepository = createRepository();
        }
        return mRepository;
    }

    /**
     * 创建Repository
     *
     * @return Repository实例
     */
    protected abstract R createRepository();

    /**
     * 获取Repository集合
     *
     * @return mRepositoryList
     */
    public List<BaseRepository> getRepositoryList() {
        return mRepositoryList;
    }

    /**
     * 添加Repository实例到集合，统一初始化并管理
     *
     * @param rClass Repository类
     * @param <Y>    继承BaseRepository的Repository实例
     */
    protected <Y extends BaseRepository> BaseRepository getRepository(Class<Y> rClass) {
        if (mRepositoryList == null) {
            mRepositoryList = new ArrayList<>();
        }
        try {
            //判断已经添加过的Repository，若存在则返回
            for (int i = 0; i < mRepositoryList.size(); i++) {
                if (mRepositoryList.get(i).getClass().equals(rClass)) {
                    return mRepositoryList.get(i);
                }
            }
            mRepositoryList.add(rClass.newInstance());
            return mRepositoryList.get(mRepositoryList.size()-1);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 供观察者调用
     *
     * @return 实体类
     */
    public LiveData<B> getData() {
        if (mBean == null) {
            mBean = new MutableLiveData<>();
        }
        return mBean;
    }

    /**
     * 发送请求状态到ViewModel层
     *
     * @param state 网络请求时的状态
     */
    protected void postState(LoadStateBean state) {
        if (mLoadState != null) {
            mLoadState.postValue(state);
        }
    }

    /**
     * 销毁资源
     */
    public void destroy() {
        if (mRepository != null) {
            mRepository.onDestroy();
            mRepository = null;
        }
        if (mRepositoryList != null) {
            for (int i = 0; i < mRepositoryList.size(); i++) {
                mRepositoryList.get(i).onDestroy();
            }
            mRepositoryList.clear();
            mRepositoryList = null;
        }
    }

    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
        Log.d(TAG, "onAny event = " + event.name());
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "生命周期onCreate阶段");
    }

    @Override
    public void onStart() {
        Log.d(TAG, "生命周期onStart阶段");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "生命周期onResume阶段");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "生命周期onPause阶段");
    }

    @Override
    public void onStop() {
        Log.d(TAG, "生命周期onStop阶段");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "生命周期onDestroy阶段");
    }


}
