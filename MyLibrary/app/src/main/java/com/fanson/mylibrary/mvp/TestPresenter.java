package com.fanson.mylibrary.mvp;

import android.util.Log;

import com.example.fansonlib.base.AppUtils;
import com.example.fansonlib.base.BasePresenter;
import com.example.fansonlib.utils.ShowToast;
import com.fanson.retrofit.ApiStores;
import com.fanson.retrofit.BaseBean;
import com.fanson.retrofit.RetrofitClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by fansonq on 2017/9/2.
 */

public class TestPresenter extends BasePresenter{

    public void testMethod(){
        new TestModel(this).method();
        new Test2Model(this).notifyToP();
        Log.d("TTT","testMethod");
    }

    @Override
    protected void receiveObservable(Observable observable, Object object) {
        Log.d("TTT","Test1P收到来至："+observable+"-通知:  "+object.toString());
    }

    /**
     * 测试Mock网络操作
     */
    private void testMock(){
        Map<String,Object> maps = new HashMap<>();
        maps.put("key","fanson");
        Flowable<BaseBean> flowable =  RetrofitClient.getRetrofit().create(ApiStores.class).getName("getName",maps);
        RetrofitClient.startObservable(flowable, new ResourceSubscriber<BaseBean>() {
            @Override
            public void onNext(BaseBean bean) {
                ShowToast.singleLong(bean.getData().getName());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
}
