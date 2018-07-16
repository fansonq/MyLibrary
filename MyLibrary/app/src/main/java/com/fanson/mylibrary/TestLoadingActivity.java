package com.fanson.mylibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.fansonlib.utils.ShowToast;
import com.example.fansonlib.widget.loadingview.FadeScaleViewAnimProvider;
import com.example.fansonlib.widget.loadingview.LoadingStateView;

/**
 * @author Created by：Fanson
 * Created Time: 2018/7/13 11:25
 * Describe：测试LoadingView
 */
public class TestLoadingActivity extends AppCompatActivity{

    private LoadingStateView mLoadingStateView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_loading);

        mLoadingStateView = findViewById(R.id.loadingStateView);
        mLoadingStateView.setViewSwitchAnimProvider(new FadeScaleViewAnimProvider());

        mLoadingStateView.setErrorAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowToast.singleLong("Retry Loading");
            }
        });

        mLoadingStateView.showLoadEmptyView();

//        mLoadingStateView.showLoadErrorView();

//        mLoadingStateView.showLoadingView();

    }
}
