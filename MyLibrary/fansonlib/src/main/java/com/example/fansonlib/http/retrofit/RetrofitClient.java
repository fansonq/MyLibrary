package com.example.fansonlib.http.retrofit;

import com.example.fansonlib.base.AppUtils;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
 * @author Created by：fanson
 * Created on：2017/7/26 14:28
 * Description：Retrofit实例
 */
public class RetrofitClient {

    public volatile static Retrofit mRetrofit;

    /**
     * 基础的Url
     */
    private static String BASE_URL;

    /**
     * 超时的时间
     */
    private static final int DEFAULT_TIMEOUT = 10;

    /**
     * 记录：是否改变了BaseUrl
     */
    private static boolean mIsChangeBaseUrl = false;

    private static Retrofit.Builder mRetrofitBuilder;

    /**
     * 初始化，设置基础URL
     *
     * @param url 基础URL
     */
    public static void init(String url) {
        BASE_URL = url;
    }

    /**
     * 获取Retrofit实例
     *
     * @return Retrofit
     */
    public static Retrofit getRetrofit() {
        if (mRetrofit == null) {
            synchronized (RetrofitClient.class) {
                if (mRetrofit == null) {
                    mRetrofit = createRetrofitBuilder().build();
                }
            }
        }
        return mRetrofit;
    }

    /**
     * 获取Retrofit实例
     *
     * @param serviceClass API服务类
     * @param <S>
     * @return
     */
    public static <S> S getRetrofit(Class<S> serviceClass) {
        if (mRetrofit == null) {
            synchronized (RetrofitClient.class) {
                if (mRetrofit == null) {
                    mRetrofit = createRetrofitBuilder() .build();
                }
            }
        }
        return mRetrofit.create(serviceClass);
    }

    /**
     * 获取带进度条的Retrofit
     *
     * @param progressListener 进度监听
     * @return
     */
    public static <S> S getRetrofitProgress(Class<S> serviceClass, final ProgressListener progressListener) {
        if (mRetrofitBuilder == null) {
            mRetrofitBuilder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClientProgress(progressListener));
        }
        mRetrofit = mRetrofitBuilder.build();
        return mRetrofit.create(serviceClass);
    }

    /**
     * 获取OkHttp实例
     * 带进度条
     *
     * @return
     */
    public static OkHttpClient getOkHttpClientProgress(final ProgressListener progressListener) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为20s
                .connectionPool(new ConnectionPool(8, 20, TimeUnit.SECONDS))
                // 我们对其设置为不使用代理的模式，它就不会从系统中，读取代理信息，进行网络请求。而是会忽略掉它，
                // 直接发送网络请求。以这样的方式，就可以阻止第三方使用 Fiddler 或 Charles 进行抓包
                .proxy(Proxy.NO_PROXY)
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
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为20s
                .connectionPool(new ConnectionPool(8, 20, TimeUnit.SECONDS))
                // 我们对其设置为不使用代理的模式，它就不会从系统中，读取代理信息，进行网络请求。而是会忽略掉它，
                // 直接发送网络请求。以这样的方式，就可以阻止第三方使用 Fiddler 或 Charles 进行抓包
                .proxy(Proxy.NO_PROXY);
        if (AppUtils.isDebug()) {
            //显示日志
            okHttpClientBuilder.addInterceptor(new LoggingInterceptor());
        }
        //在Release build的时候Stetho自己会disable所有的功能，所以release包的网络请求和数据都是安全的
        okHttpClientBuilder.addNetworkInterceptor(new StethoInterceptor());
        return okHttpClientBuilder.build();
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
        mRetrofit = createRetrofitBuilder().baseUrl(BASE_URL).build();
    }

    /**
     * 创建RetrofitBuilder
     *
     * @return Retrofit.Builder
     */
    public static Retrofit.Builder createRetrofitBuilder() {
        if (mRetrofitBuilder == null) {
            mRetrofitBuilder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient());
        }
        return mRetrofitBuilder;
    }


    /**
     * 初始化通用的观察者
     *
     * @param observable 观察者
     */
    public static ResourceSubscriber startObservable(Flowable observable, ResourceSubscriber subscriber) {
        return (ResourceSubscriber) observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //订阅后可以进行取消订阅
//                .doOnLifecycle(new Consumer<Subscription>() {
//                    @Override
//                    public void accept(Subscription subscription) throws Exception {
//                        Log.d("doOnLifecycle","OnSubscribe");
//                    }
//                }, new LongConsumer() {
//                    @Override
//                    public void accept(long t) throws Exception {
//                        Log.d("doOnLifecycle","OnRequest");
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        Log.d("doOnLifecycle","OnCancel");
//                    }
//                })
                .subscribeWith(subscriber);
    }


}
