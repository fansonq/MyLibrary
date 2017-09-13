package com.example.fansonlib.http;

import java.lang.reflect.ParameterizedType;

/**
 * Created by：fanson
 * Created on：2017/9/12 13:26
 * Describe：自定义网络的回调接口
 */

public abstract class HttpResponseCallback<M> {

    private final Class<M> beanClazz;

    public HttpResponseCallback() {
        // 这部分的功能用来支持Asynchttp的泛型解析
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        beanClazz = (Class<M>) type.getActualTypeArguments()[0];
    }

    /**
     * 获取具体的实体类
     *
     * @return
     */
    public Class<M> getDataClass() {
        return beanClazz;
    }

    /**
     * 网络操作成功
     *
     * @param bean 实体类
     */
    public abstract void onSuccess(M bean);

    /**
     * 网络操作失败
     *
     * @param errorMsg 错误信息
     */
    public abstract void onFailure(String errorMsg);
}
