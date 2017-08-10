package com.fanson.retrofit;

import android.util.Log;

import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.LongConsumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by：fanson
 * Created on：2017/7/26 14:28
 * Description：Retrofit实例
 *
 * @Param
 */
public class RetrofitClient {

    public static Retrofit mRetrofit;

    //基础的Url
    private static String BASE_URL = ApiStores.API_SERVER_URL;

    //超时的时间
    private static final int DEFAULT_TIMEOUT = 20;


    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL);

    /**
     * 获取Retrofit实例
     * @return
     */
    public static Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        return mRetrofit;
    }

    /**
     * 获取带进度条的Retrofit
     * @param progressListener 进度监听
     * @return
     */
    public static Retrofit getRetrofitProgress(final ProgressListener progressListener) {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClientProgress(progressListener))
                    .build();
        }
        return mRetrofit;
    }

    /**
     * 获取OkHttp实例
     * 带进度条
     * @return
     */
    public static OkHttpClient getOkHttpClientProgress(final ProgressListener progressListener){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为20s
                .connectionPool(new ConnectionPool(8, 20, TimeUnit.SECONDS))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        ProgressResponseBody responseBody = new ProgressResponseBody(originalResponse.body(), progressListener);
                        return originalResponse.newBuilder()
                                .body(responseBody)
                                .build();
                    }
                })
                .build();
        return okHttpClient;
    }

    /**
     * 获取OkHttp实例
     * @return
     */
    public static OkHttpClient getOkHttpClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为20s
                .connectionPool(new ConnectionPool(8, 20, TimeUnit.SECONDS))
                .build();
        return okHttpClient;
    }

    public <S> S createService(Class<S> serviceClass) {
        return mRetrofit.create(serviceClass);
    }

    /**
     * 更改BaseUrl
     *
     * @param newBaseUrl
     */
    public static void changeBaseUrl(String newBaseUrl) {
        BASE_URL = newBaseUrl;
        retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL);
    }

    /**
     * 初始化通用的观察者
     * @param observable 观察者
     */
    public static ResourceSubscriber startObservable(Flowable observable, ResourceSubscriber subscriber) {
        return (ResourceSubscriber)observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnLifecycle(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        Log.d("doOnLifecycle","OnSubscribe");
                    }
                }, new LongConsumer() {
                    @Override
                    public void accept(long t) throws Exception {
                        Log.d("doOnLifecycle","OnRequest");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d("doOnLifecycle","OnCancel");
                    }
                })
                .subscribeWith(subscriber);
    }

}
