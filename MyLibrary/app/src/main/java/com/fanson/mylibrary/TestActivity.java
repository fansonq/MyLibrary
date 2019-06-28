package com.fanson.mylibrary;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.fansonlib.animation.rx.RxAnimations;
import com.example.fansonlib.base.BaseActivity;
import com.example.fansonlib.http.HttpUtils;
import com.example.fansonlib.http.retrofit.RetrofitClient;
import com.example.fansonlib.http.retrofit.RetrofitStrategy;
import com.example.fansonlib.utils.SpannableStringUtils;
import com.example.fansonlib.utils.log.MyLogUtils;
import com.example.fansonlib.utils.storage.MyKvStorageUtils;
import com.example.fansonlib.utils.storage.StorageConfig;
import com.fanson.mylibrary.databinding.ActivityTestBinding;
import com.fanson.mylibrary.recyclerview.RecyclerViewActivity;

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

        MyLogUtils.getInstance().d("HHHH");

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

        testMmkv();
    }

    @Override
    protected void listenEvent() {
        mBinding.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyActivity(TestViewActivity.class);
            }
        });

        mBinding.td1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.td1.setVectorDrawableTop(R.mipmap.close);
            }
        });

        mBinding.btnRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyActivity(RecyclerViewActivity.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        testRxAnim();
    }

    /**
     * 测试存储框架功能
     */
    private void testMmkv(){
        StorageConfig config = new StorageConfig.Builder().setFileName("FansonLib").build();
        MyKvStorageUtils.init(config);

        MyKvStorageUtils.getInstance().put("test","我是来自测试存储框架功能");

        Log.d(TAG, MyKvStorageUtils.getInstance().getString("test"));
        Log.d(TAG,"bool = "+ MyKvStorageUtils.getInstance().getBoolean("bool"));
    }

    /**
     * 测试Rx封装的动画
     */
    private void testRxAnim(){
//        RxAnimations.enterTogether(10,500,mBtn,mBinding.td1).subscribe();

        RxAnimations.enter(mBtn,10,500).andThen(RxAnimations.fadeIn(mBinding.td1,500)).subscribe();
    }
}
