package com.fanson.mylibrary.mvvm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
 * Describe：ViewModel的Activity
 */
public class ViewModelActivity extends BaseVmActivity<MyVmViewModel, ActivityViewmodelBinding> implements ContractTest.TestView {

    private static final String TAG = "MyViewModelActivity";

    @Override
    protected int getContentView() {
        return R.layout.activity_viewmodel;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void listenEvent() {
        mBinding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getData(1);
            }
        });
    }

    @Override
    protected MyVmViewModel createViewModel() {
        return ViewModelProviders.of(this).get(MyVmViewModel.class);
    }

    @Override
    protected void dataSuccessObserver() {
        mViewModel.setBaseView(this);
        mViewModel.getData().observe(this, new Observer<SimpleBean>() {
            @Override
            public void onChanged(@Nullable SimpleBean bean) {
                Log.d(TAG, "onChanged");
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
}
