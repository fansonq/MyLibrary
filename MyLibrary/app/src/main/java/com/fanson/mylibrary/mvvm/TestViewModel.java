package com.fanson.mylibrary.mvvm;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.fansonlib.base.BaseViewModel;
import com.example.fansonlib.bean.LoadStateBean;
import com.example.fansonlib.constant.ConstLoadState;
import com.fanson.mylibrary.SimpleBean;
import com.fanson.mylibrary.mvp.TestCallback;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/11 17:05
 * Describe：测试ViewModel
 */
public class TestViewModel extends BaseViewModel<TestVmRepository,SimpleBean> implements TestCallback {

    private static final String TAG = TestViewModel.class.getSimpleName();

    public TestViewModel(@NonNull Application application) {
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
    public void getDataFromR(int id){
        getRepository().getTestData(this);
    }

    @Override
    public void successful(SimpleBean bean) {
        Log.d(TAG,"successful");
        MutableLiveData<SimpleBean> liveData = ((MutableLiveData<SimpleBean>)getData());
        liveData.setValue(bean);
//        if (isViewAttached()){
//            getBaseView().showTip(bean.getData().getName());
//        }
    }

    @Override
    public void failure(String errorMsg) {
        Log.d(TAG,"failure");
        postState(new LoadStateBean(ConstLoadState.COMPLETE_STATE));
//        if (isViewAttached()){
//            getBaseView().showFailure(errorMsg);
//        }
    }

}