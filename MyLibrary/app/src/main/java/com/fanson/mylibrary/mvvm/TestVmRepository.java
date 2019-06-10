package com.fanson.mylibrary.mvvm;

import android.util.Log;

import com.example.fansonlib.base.BaseRepository;
import com.example.fansonlib.http.HttpResponseCallback;
import com.example.fansonlib.http.HttpUtils;
import com.example.fansonlib.http.retrofit.RetrofitClient;
import com.example.fansonlib.http.retrofit.RetrofitStrategy;
import com.fanson.mylibrary.ApiFactoryImpl;
import com.fanson.mylibrary.ApiStores;
import com.fanson.mylibrary.SimpleBean;
import com.fanson.mylibrary.constant.ConHttp;
import com.fanson.mylibrary.mvp.TestCallback;

import java.util.HashMap;
import java.util.Map;

import static com.example.fansonlib.http.HttpUtils.getHttpUtils;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 16:50
 * Describe：
 */
public class TestVmRepository extends BaseRepository{

    private static final String TAG = TestVmRepository.class.getSimpleName();
    TestCallback mCallback;

    public void getTestData(TestCallback callback){
        mCallback = callback;

        //这段代码放Application初始化即可
        RetrofitClient.init(ApiStores.API_SERVER_URL);
        RetrofitStrategy strategy = new RetrofitStrategy();
        strategy.setApi(new ApiFactoryImpl());
        HttpUtils.init(strategy);

        Map<String,Object> maps = new HashMap<>();
        maps.put("key","fanson");
        getHttpUtils().post(ConHttp.BASE_URL+ConHttp.REQUEST_POST,maps, new HttpResponseCallback<SimpleBean>() {
            @Override
            public void onSuccess(SimpleBean bean) {
                Log.d(TAG,"onSuccess");
//                mCallback.successful(bean);
                mCallback.failure("onSuccess");
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
        mCallback = null;
    }
}
