package com.example.fansonlib.http.retrofit;

import com.example.fansonlib.utils.log.MyLogUtils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Created by：Fanson
 * Created Time: 2018/11/12 10:40
 * Describe：网络交互拦截Log日志的拦截器
 */
public class LoggingInterceptor implements Interceptor {

    private static final String TAG = "NetLog";
    private StringBuilder mStringBuilder;

    @Override
    public Response intercept(Chain chain) throws IOException {
        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();
        Response response;
        long t1 = System.nanoTime();//请求发起的时间

        String method = request.method();
        if ("POST".equals(method)) {
            RequestBody requestBody = request.body();

            if (requestBody instanceof FormBody) {

                if (mStringBuilder == null) {
                    mStringBuilder = new StringBuilder();
                } else {
                    mStringBuilder.setLength(0);
                }
                
                FormBody body = (FormBody) request.body();
                if (body != null) {
                    for (int i = 0; i < body.size(); i++) {
                        mStringBuilder.append(body.encodedName(i)).append("=").append(body.encodedValue(i)).append(",");
                    }
                    mStringBuilder.delete(mStringBuilder.length() - 1, mStringBuilder.length());
                    MyLogUtils.getInstance().d(String.format("发送请求 %s on %s %n%s %nRequestParams:【%s】",
                            request.url(), chain.connection(), request.headers(), mStringBuilder.toString()));
                }
            }
        } else {
            //"GET"方式
            MyLogUtils.getInstance().d( String.format("发送请求 %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));
        }
        response = chain.proceed(request);
        long t2 = System.nanoTime();//收到响应的时间
        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024 * 10);
        MyLogUtils.getInstance().d(String.format("接收响应: [%s] %n返回json:【%s】 %.1fms %n%s",
                        response.request().url(),
                        responseBody.string(),
                        (t2 - t1) / 1e6d,
                        response.headers()
                ));
        return response;
    }

}

