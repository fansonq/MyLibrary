package com.fanson.mylibrary;


import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * Created by：fanson
 * Created on：2017/7/26 14:28
 * Description：Retrofit的Api
 *
 * @Param
 */

public interface ApiStores {

    //baseUrl
    String API_SERVER_URL = "https://easy-mock.com/mock/59aa96c7e0dc663341997dcc/fansonq/test/";

    @GET
    Flowable<ResponseBody> update(@Url String url);

    @POST
    Flowable<SimpleBean> getName(@Url String url, @QueryMap Map<String, Object> maps);

    /**
     * 下载文件
     * 大文件官方建议用 @Streaming 来进行注解，不然会出现IO异常，小文件可以忽略不注入
     * @param fileUrl 文件url
     * @return
     */
    @Streaming
    @GET
    Flowable<ResponseBody> download(@Url String fileUrl);

    //上传文件
    @Multipart
    @POST
    Call<ResponseBody> upload(@Url String fileUrl, @Part("description") Map<String, Object> description, @Part RequestBody file);

    //上传多文件
    @Multipart
    @POST
    Flowable<ResponseBody> uploadMulti(@Url String fileUrl,@Part MultipartBody.Part imgs);

}
