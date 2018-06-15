package com.fanson.mylibrary;

import android.view.View;

import com.example.fansonlib.base.BaseActivity;
import com.example.fansonlib.http.HttpUtils;
import com.example.fansonlib.http.retrofit.RetrofitClient;
import com.example.fansonlib.http.retrofit.RetrofitStrategy;

/**
 * @author Created by：Fanson
 *         Created Time: 2018/6/7 9:13
 *         Describe：
 */

public class TestActivity extends BaseActivity{



    @Override
    protected int getContentView() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        /*---Retrofit策略---*/
        RetrofitClient.init(ApiStores.API_SERVER_URL);
        RetrofitStrategy strategy = new RetrofitStrategy();
        strategy.setApi(new ApiFactoryImpl());
        HttpUtils.init(strategy);

        findViewById(R.id.btn_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyActivity(MainActivity.class);
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void listenEvent() {

    }
}
