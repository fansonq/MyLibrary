package com.fanson.mylibrary.mvvm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.example.fansonlib.base.BaseVmActivity;
import com.example.fansonlib.utils.MySnackBarUtils;
import com.example.fansonlib.utils.ShowToast;
import com.fanson.mylibrary.R;
import com.fanson.mylibrary.SimpleBean;
import com.fanson.mylibrary.databinding.ActivityViewmodelBinding;
import com.fanson.mylibrary.mvp.ContractTest;


/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 16:49
 * Describe：测试ViewModel的Activity
 */
public class TestViewModelActivity extends BaseVmActivity<MyVmViewModel, ActivityViewmodelBinding> implements ContractTest.TestView {

    private static final String TAG = TestViewModelActivity.class.getSimpleName();

    @Override
    protected int getContentView() {
        return R.layout.activity_viewmodel;
    }

    @Override
    protected MyVmViewModel createViewModel() {
        return ViewModelProviders.of(this).get(MyVmViewModel.class);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void listenEvent() {
        mBinding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mViewModel.getData(1);
                    }
                },2000);
            }
        });
    }

    @Override
    protected void dataSuccessObserver() {
        mViewModel.getData().observe(this, new Observer<SimpleBean>() {
            @Override
            public void onChanged(@Nullable SimpleBean bean) {
                Log.d(TAG, "请求数据成功，返回数据");
                if (bean != null) {
                    mBinding.tv.setText(bean.getData().getName());
                    ShowToast.singleLong("请求数据成功");
                }
                mViewModel.getData().removeObserver(this);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    public void showFailure(String errorMsg) {
        mBinding.tv.setText(errorMsg);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showTip(String tipContent) {
        MySnackBarUtils.showLong(getWindow().getDecorView(),tipContent).show();
    }
}
