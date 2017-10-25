package com.fanson.mylibrary;

import com.example.fansonlib.http.retrofit.IApiFactory;
import com.example.fansonlib.http.retrofit.RetrofitClient;

import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by：fanson
 * Created on：2017/9/13 13:38
 * Describe：工厂生成Retrofit用的Api
 */

public class ApiFactoryImpl implements IApiFactory {

    private Flowable mFlowable;

    @Override
    public Flowable createApi(String url, Map params) {
        switch (url) {
            case "getName":
                mFlowable = RetrofitClient.getRetrofit(ApiStores.class).getName(url, params);
                break;
            case "post.php":
                mFlowable = RetrofitClient.getRetrofit(ApiStores.class).uploadMulti(url,params);
                break;
        }
        return mFlowable;
    }
}
