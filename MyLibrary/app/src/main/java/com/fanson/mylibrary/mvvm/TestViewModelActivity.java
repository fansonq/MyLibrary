package com.fanson.mylibrary.mvvm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.example.fansonlib.base.BaseVmActivity;
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
                mViewModel.getData(1);
            }
        });
    }

    @Override
    protected void dataSuccessObserver() {
        mViewModel.setBaseView(this);
        mViewModel.getData().observe(this, new Observer<SimpleBean>() {
            @Override
            public void onChanged(@Nullable SimpleBean bean) {
                Log.d(TAG, "请求数据成功，返回数据");
                if (bean != null) {
                    mBinding.tv.setText(bean.getData().getName());
                }
            }
        });
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

    }
}
