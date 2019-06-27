package com.fanson.mylibrary.mvp;

import android.util.Log;

import com.example.fansonlib.base.BaseRepository;
import com.example.fansonlib.http.HttpResponseCallback;
import com.fanson.mylibrary.SimpleBean;

import java.util.HashMap;
import java.util.Map;

import static com.example.fansonlib.http.HttpUtils.getHttpUtils;

/**
 * Created by fansonq on 2017/9/2.
 */

public class TestRepository extends BaseRepository {

    TestCallback mCallback;

    /**
     * 测试Mock网络操作
     */
    public void method(final TestCallback callback){
        this.mCallback = callback;
        Log.d("TTT","Test1Model发送的数据");
        Map<String,Object> maps = new HashMap<>();
        maps.put("key","fanson");


        /*---AsyncHttp策略---*/
//        AsyncHttpStrategy.init(ApiStores.API_SERVER_URL);
//        HttpUtils.init(new AsyncHttpStrategy());

        getHttpUtils().get("getName", new HttpResponseCallback<SimpleBean>() {
            @Override
            public void onSuccess(SimpleBean bean) {
                Log.d("TTT","onSuccess");
                mCallback.successful(bean);
            }

            @Override
            public void onFailure(String errorMsg) {
                mCallback.failure(errorMsg);
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getHttpUtils().cancelCurrent("getName");
        mCallback = null;
    }
}
