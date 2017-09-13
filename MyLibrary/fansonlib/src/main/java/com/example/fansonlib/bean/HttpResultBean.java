package com.example.fansonlib.bean;

/**
 * Created by：fanson
 * Created on：2017/9/1 18:03
 * Describe：
 */

public class HttpResultBean<T> extends BaseBean {

    private T data;
    private int code;
    private String msg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "data=" + getData() +
                ", code=" + getCode() +
                ", msg='" + getMsg() + '\'' +
                '}';
    }
}