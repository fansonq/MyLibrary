package com.example.fansonlib.function.update;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Map;

/**
 * app版本更新接口
 */
public interface HttpManager extends Serializable {


    /**
     * 异步post
     *
     * @param url      post请求地址
     * @param params   post请求参数
     * @param callBack 回调
     */
    void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack);



    /**
     * 网络请求回调
     */
    interface Callback {
        /**
         * 结果回调
         *
         * @param result 结果
         */
        void onResponse(String result);

        /**
         * 错误回调
         *
         * @param error 错误提示
         */
        void onError(String error);
    }
}
