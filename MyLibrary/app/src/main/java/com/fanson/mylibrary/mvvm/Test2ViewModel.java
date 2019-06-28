package com.fanson.mylibrary.mvvm;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.fansonlib.base.BaseViewModel;
import com.example.fansonlib.bean.LoadStateBean;
import com.example.fansonlib.constant.ConstLoadState;
import com.fanson.mylibrary.SimpleBean;
import com.fanson.mylibrary.bean.TestVmBean;
import com.fanson.mylibrary.mvp.TestCallback;

/**
 * @author Created by：Fanson
 * Created Time: 2019/6/28 9:47
 * Describe：测试ViewModel2
 */
public class Test2ViewModel extends BaseViewModel<Test2VmRepository,TestVmBean> implements Test2Callback, TestCallback {

    private static final String TAG = Test2ViewModel.class.getSimpleName();

    public Test2ViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected Test2VmRepository createRepository() {
        return new Test2VmRepository();
    }

    /**
     * 请求网络加载数据
     * @param id 字段ID
     */
    public void getDataFromR(int id){
        getRepository().getTestData(this);
    }

    /**
     * 使用Repository-2 请求网络加载数据2
     */
    public void getDataFromR2(){
        addRepository(TestVmRepository.class,SimpleBean.class);
        ((TestVmRepository)getRepositoryList().get(0)).getTestData(this);
    }

    @Override
    public void onTest2Success(TestVmBean bean) {
        Log.d(TAG,"Test 2 Vm successful");
        ((MutableLiveData<TestVmBean>)getData()).setValue(bean);
    }

    @Override
    public void onTest2Failure(String errorMsg) {
        Log.d(TAG,"onTest2Failure = "+errorMsg);
        postState(new LoadStateBean(ConstLoadState.ERROR_STATE));
    }

    @Override
    public void successful(SimpleBean bean) {
        Log.d(TAG,"Test 1 Vm successful");
        (getBeanList().get(0)).setValue(bean);
    }

    @Override
    public void failure(String errorMsg) {
        Log.d(TAG,"failure");
        postState(new LoadStateBean(ConstLoadState.COMPLETE_STATE));
    }

}
