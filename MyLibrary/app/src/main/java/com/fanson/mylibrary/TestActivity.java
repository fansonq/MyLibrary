package com.fanson.mylibrary;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.example.fansonlib.base.BaseActivity;
import com.example.fansonlib.http.HttpUtils;
import com.example.fansonlib.http.retrofit.RetrofitClient;
import com.example.fansonlib.http.retrofit.RetrofitStrategy;
import com.example.fansonlib.utils.SpannableStringUtils;
import com.fanson.mylibrary.databinding.ActivityTestBinding;

/**
 * @author Created by：Fanson
 *         Created Time: 2018/6/7 9:13
 *         Describe：
 */

public class TestActivity extends BaseActivity<ActivityTestBinding>{

    private static final String TAG =TestActivity .class.getSimpleName();
    private ViewDataBinding viewDataBinding;
    private Button mBtn;

    @Override
    protected int getContentView() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        /*---Retrofit策略---*/
        RetrofitClient.init(ApiStores.API_SERVER_URL);
        RetrofitStrategy strategy = new RetrofitStrategy();
        strategy.setApi(new ApiFactoryImpl());
        HttpUtils.init(strategy);
        mBtn = findViewById(R.id.btn_go);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyActivity(MainActivity.class);
            }
        });
    }

    @Override
    protected void initData() {
        mBinding.tv1.setText(SpannableStringUtils.getBuilder("测试").append("大号字体")
                .setProportion(1.2f).setForegroundColor(ContextCompat.getColor(this,R.color.light_orange)).create());
    }

    @Override
    protected void listenEvent() {
        mBinding.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyActivity(TestViewActivity.class);
            }
        });
    }
}
