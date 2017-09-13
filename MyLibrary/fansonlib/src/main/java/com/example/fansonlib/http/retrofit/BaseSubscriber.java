package com.example.fansonlib.http.retrofit;


import android.util.Log;


/**
* Created by：fanson
* Created on：2017/07/26 14:27
* Description：
 * @Param
*/
public abstract class BaseSubscriber<M>{

    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();


    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof retrofit2.HttpException) {
            retrofit2.HttpException httpException = (retrofit2.HttpException) e;
            //httpException.response().errorBody().string()
            int code = httpException.code();
            String msg = httpException.getMessage();
            Log.d("code=","" + code);
            if (code == 504) {
                msg = "网络不给力";
            }
            if (code == 502 || code == 404) {
                msg = "服务器异常，请稍后再试";
            }
            onFailure(msg);
        } else {
            onFailure(e.getMessage());
        }
        onFinish();
    }

    public void onNext(M model) {
        onSuccess(model);
    }

    public void onCompleted() {
        onFinish();
    }
}
