package com.example.fansonlib.bean;

import java.io.IOException;

/**
 * Created by：fanson
 * Created on：2017/9/1 18:00
 * Describe：
 */

public class HttpResultException extends IOException {

    private String errMsg;
    private int errCode;

    public HttpResultException(String errMsg, int errCode){
        this.errMsg = errMsg;
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}