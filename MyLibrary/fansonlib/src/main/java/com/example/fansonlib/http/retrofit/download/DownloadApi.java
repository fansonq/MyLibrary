package com.example.fansonlib.http.retrofit.download;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author Created by：Fanson
 *         Created Time: 2018/6/8 13:50
 *         Describe：下载用到的API
 */

public interface DownloadApi {

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@NonNull @Url String url);

}
