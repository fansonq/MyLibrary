package com.example.fansonlib.base;

import android.util.Log;

import java.util.Observable;

/**
 * Created by fansonq on 2017/9/2.
 * 作为被观察者使用，完成操作后调用notifyObservers发送通知到P层
 */

public class BaseModel extends Observable {
    private static final String TAG = BaseModel.class.getSimpleName();

    public BaseModel(BasePresenter presenter) {
        if (presenter!=null){
            addObserver(presenter);
            Log.d(TAG, "Add Observer：" + presenter);
        }
    }

    /**
     * 发布通知给观察者
     *
     * @param object
     */
    public void notifyToObservers(Object object) {
        setChanged();
        notifyObservers(object);
    }

    /**
     * 移除指定的观察者
     *
     * @param presenter 观察者对象
     */
    public void removeObserver(BasePresenter presenter) {
        if (presenter!=null){
            deleteObserver(presenter);
            Log.d(TAG, "Delete Observer：" + presenter);
        }
    }

    /**
     * 移除所有的观察者
     */
    public void removeAllObservers() {
        deleteObservers();
        Log.d(TAG, "Delete All Observers");
    }

}
