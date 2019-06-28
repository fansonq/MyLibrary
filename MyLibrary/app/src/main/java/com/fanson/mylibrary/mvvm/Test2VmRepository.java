package com.fanson.mylibrary.mvvm;

import android.util.Log;

import com.example.fansonlib.base.BaseRepository;
import com.example.fansonlib.http.HttpResponseCallback;
import com.fanson.mylibrary.bean.TestVmBean;
import com.fanson.mylibrary.constant.ConHttp;

import java.util.HashMap;
import java.util.Map;

import static com.example.fansonlib.http.HttpUtils.getHttpUtils;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 16:50
 * Describe：测试Mvvm的Repository2
 */
public class Test2VmRepository extends BaseRepository {

    private static final String TAG = Test2VmRepository.class.getSimpleName();
    Test2Callback mCallback;

    public void getTestData(Test2Callback callback) {
        mCallback = callback;

        Map<String, Object> maps = new HashMap<>();
        maps.put("key", "fanson");
        getHttpUtils().post(ConHttp.BASE_URL + ConHttp.REQUEST_POST2, maps, new HttpResponseCallback<TestVmBean>() {
            @Override
            public void onSuccess(TestVmBean bean) {
                Log.d(TAG, "onSuccess = "+bean.getMessage());
                mCallback.onTest2Success(bean);
//                mCallback.failure("哈哈哈，出错啦");
            }

            @Override
            public void onFailure(String errorMsg) {
                mCallback.onTest2Failure(errorMsg);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCallback = null;
    }
}
