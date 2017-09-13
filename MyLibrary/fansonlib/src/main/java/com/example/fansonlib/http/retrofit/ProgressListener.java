package com.example.fansonlib.http.retrofit;

/**
 * Created by：fanson
 * Created on：2017/7/26 15:03
 * Describe：进度的监听接口
 */

public interface ProgressListener {

    void update(long bytesRead, long contentLength, boolean done);

}
