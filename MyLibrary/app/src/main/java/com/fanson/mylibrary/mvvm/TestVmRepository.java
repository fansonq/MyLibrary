package com.fanson.mylibrary.mvvm;

import android.util.Log;

import com.example.fansonlib.base.BaseRepository;
import com.example.fansonlib.http.HttpResponseCallback;
import com.fanson.mylibrary.SimpleBean;
import com.fanson.mylibrary.constant.ConHttp;
import com.fanson.mylibrary.mvp.TestCallback;

import java.util.HashMap;
import java.util.Map;

import static com.example.fansonlib.http.HttpUtils.getHttpUtils;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 16:50
 * Describe：测试Mvvm的Repository
 */
public class TestVmRepository extends BaseRepository{

    private static final String TAG = TestVmRepository.class.getSimpleName();
    TestCallback mCallback;

    public void getTestData(TestCallback callback){
        mCallback = callback;

        Map<String,Object> maps = new HashMap<>();
        maps.put("key","fanson");
        getHttpUtils().post(ConHttp.BASE_URL+ConHttp.REQUEST_POST,maps, new HttpResponseCallback<SimpleBean>() {
            @Override
            public void onSuccess(SimpleBean bean) {
                Log.d(TAG,"onSuccess");
                mCallback.successful(bean);
//                mCallback.failure("onSuccess");
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
