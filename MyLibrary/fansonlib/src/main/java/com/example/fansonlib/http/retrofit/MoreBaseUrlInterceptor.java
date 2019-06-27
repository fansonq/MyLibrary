package com.example.fansonlib.http.retrofit;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Created by：Fanson
 * Created Time: 2019/5/6 16:15
 * Describe：处理Retrofit多个BaseUrl的拦截器
 */
public class MoreBaseUrlInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        //獲取原始的originalRequest
        Request originalRequest = chain.request();
        //獲取老的url
        HttpUrl oldUrl = originalRequest.url();
        //獲取originalRequest的建立者builder
        Request.Builder builder = originalRequest.newBuilder();
        //獲取请求api的集合如:base1,base2
        List<String> urlnameList = originalRequest.headers("urlname");
        if (urlnameList != null && urlnameList.size() > 0) {
            //刪除原有配置中的值,就是namesAndValues集合裡的值
            builder.removeHeader("urlname");
            //獲取頭資訊中配置的value,如:base1或者base2
            String urlname = urlnameList.get(0);
            HttpUrl baseURL=null;
            //根據頭資訊中配置的value,來匹配新的base_url地址
            if ("base1".equals(urlname)) {
                baseURL = HttpUrl.parse("base1");
            } else if ("base2".equals(urlname)) {
                baseURL = HttpUrl.parse("base2");
            }
            //重建新的HttpUrl,需要重新設定的url部分
            HttpUrl newHttpUrl = oldUrl.newBuilder()
                    //http協議如:http或者https
                    .scheme(baseURL.scheme())
                    //主機地址
                    .host(baseURL.host())
                    .port(baseURL.port())
                    .build();
            //獲取處理後的新newRequest
            Request newRequest = builder.url(newHttpUrl).build();
            return  chain.proceed(newRequest);
        }else{
            return chain.proceed(originalRequest);
        }

    }
}
