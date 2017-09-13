package com.example.fansonlib.bean;

/**
 * Created by：fanson
 * Created on：2017/9/1 18:05
 * Describe：网络访问失败返回的数据
 */

public class HttpResultError extends BaseBean {

    /**
     * msg : 你輸入的驗證碼錯誤，請重新輸入
     * code : 105
     * data : {}
     */
    private String msg;
    private String code;
    private DataBean data;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }

    public DataBean getData() {
        return data;
    }

    public static class DataBean {
    }

    @Override
    public String toString() {
        return "HttpResultError{" +
                "msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}