package com.fanson.mylibrary.mvvm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.fansonlib.base.AppUtils;
import com.example.fansonlib.base.BaseVmActivity;
import com.example.fansonlib.bean.LoadStateBean;
import com.example.fansonlib.utils.MySnackBarUtils;
import com.example.fansonlib.utils.log.LogConfig;
import com.example.fansonlib.utils.log.MyLogUtils;
import com.example.fansonlib.utils.toast.MyToastUtils;
import com.fanson.mylibrary.R;
import com.fanson.mylibrary.SimpleBean;
import com.fanson.mylibrary.databinding.ActivityViewmodelBinding;


/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 16:49
 * Describe：测试ViewModel的Activity
 */
public class TestViewModelActivity extends BaseVmActivity<MyVmViewModel, ActivityViewmodelBinding>  {

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
        LogConfig config = new LogConfig.Builder()
                .setTag(TAG)
                .setIsLoggable(AppUtils.isDebug())
                .build();
        MyLogUtils.init(config);

        initDelayLoadData(20000);
    }

    @Override
    protected void startDelayLoad() {
        super.startDelayLoad();
        getViewModel().getData(1);
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
                        getViewModel().getData(1);
                    }
                },2000);
            }
        });
    }

    @Override
    protected void dataSuccessObserver() {
        getViewModel().getData().observe(this, new Observer<SimpleBean>() {
            @Override
            public void onChanged(@Nullable SimpleBean bean) {
                MyLogUtils.getInstance().d( "请求数据成功，返回数据");
                if (bean != null) {
                    mBinding.tv.setText(bean.getData().getName());
                    MyToastUtils.init(null);
                    MyToastUtils.getInstance().showLong("请求数据成功");
                    hideLoading();
                }
            }
        });
    }

    @Override
    protected void handlerLoadState(LoadStateBean stateBean) {
        super.handlerLoadState(stateBean);
        mBinding.tv.setText(stateBean.getContent());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void showErrorState(String errorMsg) {
        super.showErrorState(errorMsg);
        MyLogUtils.getInstance().e(errorMsg);
        mBinding.tv.setText(errorMsg);
    }

    @Override
    public void showTip(String tipContent) {
        MySnackBarUtils.showLong(getWindow().getDecorView(),tipContent).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       MyLogUtils.getInstance().d( MyLogUtils.loganFilesInfo());
    }
}
