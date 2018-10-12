package com.fanson.mylibrary.mvvm;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.fansonlib.base.BaseViewModel;
import com.fanson.mylibrary.SimpleBean;
import com.fanson.mylibrary.mvp.ContractTest;
import com.fanson.mylibrary.mvp.TestCallback;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 17:05
 * Describe：
 */
public class MyVmViewModel extends BaseViewModel<ContractTest.TestView,TestVmRepository> implements TestCallback {

    private MutableLiveData<SimpleBean> mBean;


    public MyVmViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected TestVmRepository createRepository() {
        return new TestVmRepository();
    }

    public LiveData<SimpleBean> getData() {
        if (mBean == null) {
            mBean = new MutableLiveData<>();
        }
        return mBean;
    }

    public void getData(int id){
        mRepository.getTestData(this);
    }


    @Override
    public void successful(SimpleBean bean) {
        mBean.setValue(bean);
    }

    @Override
    public void failure(String errorMsg) {
        if (isViewAttached()){
            getBaseView().showFailure(errorMsg);
        }
    }
}
