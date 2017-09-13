package com.fanson.mylibrary.update;

import android.support.annotation.NonNull;

import com.example.fansonlib.function.update.HttpManager;
import com.fanson.mylibrary.ApiStores;
import com.example.fansonlib.http.retrofit.RetrofitClient;

import java.io.IOException;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.ResponseBody;

/**
 * Created by：fanson
 * Created on：2017/8/8 15:04
 * Describe：
 */

public class AppHttpUtils implements HttpManager{

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
//        Map<String, Object> maps = new HashMap<>();
//        maps.put("tel",name);

       Flowable<ResponseBody> flowable = RetrofitClient.getRetrofit(ApiStores.class).update(url);
        ResourceSubscriber subscriber = new ResourceSubscriber() {
            @Override
            public void onNext(Object response) {
                try {
                    callBack.onResponse(((ResponseBody)response).string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                callBack.onError(t.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };

        RetrofitClient.startObservable(flowable,subscriber);
    }
}
