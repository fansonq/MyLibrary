package com.example.fansonlib.http.retrofit.customConverter;

import android.util.Log;

import com.example.fansonlib.bean.HttpResultBean;
import com.example.fansonlib.bean.HttpResultError;
import com.example.fansonlib.bean.HttpResultException;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by：fanson
 * Created on：2017/9/1 17:54
 * Describe：重定义Gson解析
 */

public final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private static final String TAG = GsonResponseBodyConverter.class.getSimpleName();
    private final Gson gson;
    private final Type type;

    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    /**
     * 针对数据返回成功、错误不同类型字段处理
     */
    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        Log.d(TAG,"response = "+response);
        try {
            // 这里的type实际类型是 HttpResult<PhoneBean>  PhoneBean就是retData要获取的对象。
            HttpResultBean result = gson.fromJson(response, HttpResultBean.class);
            int code = result.getCode();
            if (code == 1) {
                return gson.fromJson(response, type);
            } else {
                Log.d("HttpManager", "返回err==：" + response);
                HttpResultError errResponse = gson.fromJson(response, HttpResultError.class);
                if (code == -1) {
                    throw new HttpResultException(errResponse.getMsg(), code);
                } else {
                    throw new HttpResultException(errResponse.getMsg(), code);
                }
            }
        } finally {
            value.close();
        }
    }



    /**
     * 针对数据返回成功、错误不同类型字段处理
     */
    public T convert(String response) throws IOException {
//        try {
        // 这里的type实际类型是 HttpResult<PhoneBean>  PhoneBean就是retData要获取的对象。
        HttpResultBean result = gson.fromJson(response, HttpResultBean.class);
//            int code = result.getCode();
//            if (code == 1) {
                return gson.fromJson(response, type);
//            } else {
//                Log.d("HttpManager", "返回err==：" + response);
//                HttpResultError errResponse = gson.fromJson(response, HttpResultError.class);
//                if (code == -1) {
//                    throw new HttpResultException(errResponse.getMsg(), code);
//                } else {
//                    throw new HttpResultException(errResponse.getMsg(), code);
//                }
//            }
//        } finally {
//            value.close();
    }

}

