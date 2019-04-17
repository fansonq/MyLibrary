package com.fanson.mylibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.fansonlib.utils.toast.MyToastUtils;
import com.example.fansonlib.widget.loadingview.FadeScaleViewAnimProvider;
import com.example.fansonlib.widget.loadingview.LoadingStateView;

/**
 * @author Created by：Fanson
 * Created Time: 2018/7/13 11:25
 * Describe：测试LoadingView
 */
public class TestLoadingActivity extends AppCompatActivity {

    private LoadingStateView mLoadingStateView;
    private Button mBtnNoData,mBtnLoading,mBtnHide;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_loading);
        mBtnNoData = findViewById(R.id.btnNoData);
        mBtnLoading =  findViewById(R.id.btnLoading);
        mBtnHide = findViewById(R.id.btn_hide);

        mLoadingStateView = findViewById(R.id.loadingStateView);
        mLoadingStateView.setViewSwitchAnimProvider(new FadeScaleViewAnimProvider());

        mLoadingStateView.setNoDataAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToastUtils.init(null);
                MyToastUtils.getInstance().showLong("hideNoDataView");
                    mLoadingStateView.hideNoDataView();

            }
        });

        mLoadingStateView.setErrorAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingStateView.hideErrorView();
            }
        });

        mBtnNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingStateView.showLoadNoDataView();
            }
        });

        mBtnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingStateView.onHideOtherView();
            }
        });

        mBtnLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingStateView.showLoadingView();
            }
        });

        mLoadingStateView.showLoadNoDataView();

//        mLoadingStateView.showLoadErrorView();

//        mLoadingStateView.showLoadingView();

    }
}
