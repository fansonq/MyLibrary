package com.fanson.mylibrary.mvvm;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.fansonlib.base.BaseViewModel;
import com.example.fansonlib.constant.ConstMvvmLoadState;
import com.example.fansonlib.utils.toast.MyToastUtils;
import com.fanson.mylibrary.SimpleBean;
import com.fanson.mylibrary.mvp.ContractTest;
import com.fanson.mylibrary.mvp.TestCallback;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 17:05
 * Describe：测试ViewModel
 */
public class MyVmViewModel extends BaseViewModel<ContractTest.TestView,TestVmRepository,SimpleBean> implements TestCallback {

    private static final String TAG = MyVmViewModel.class.getSimpleName();

    public MyVmViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected TestVmRepository createRepository() {
        return new TestVmRepository();
    }

    /**
     * 请求网络加载数据
     * @param id 字段ID
     */
    public void getData(int id){
        getRepository().getTestData(this);
    }

    @Override
    public void successful(SimpleBean bean) {
        Log.d(TAG,"successful");
        mBean.setValue(bean);
        if (isViewAttached()){
            getBaseView().showTip(bean.getData().getName());
        }
    }

    @Override
    public void failure(String errorMsg) {
        Log.d(TAG,"failure");
        MyToastUtils.getInstance().showShort(errorMsg);
        postState(ConstMvvmLoadState.COMPLETE_STATE);
//        if (isViewAttached()){
//            getBaseView().showFailure(errorMsg);
//        }
    }

    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
        Log.d(TAG,"onAny event = "+event.name());
    }

}
