package com.fanson.mylibrary.mvp;

import android.util.Log;

import com.example.fansonlib.base.BasePresenterRep;
import com.fanson.mylibrary.SimpleBean;

/**
 * Created by fansonq on 2017/9/2.
 */

public class TestPresenter extends BasePresenterRep<TestModel,SimpleBean,ContractTest.TestView> implements ContractTest.ITestPresenter,TestCallback{


    public TestPresenter(ContractTest.TestView baseView) {
        super(baseView);
    }

    @Override
    protected TestModel createRepository() {
        return new TestModel();
    }


    @Override
    public void successful(SimpleBean bean ) {
        switch (bean.getCode()){
            case 1:
                setValue(bean);
                break;
            case 2:
                getBaseView().showCode102(bean.getMessage());
            default:
                break;
        }

    }

    @Override
    public void failure(String errorMsg) {

    }


    @Override
    public void testPresenterMethod() {
        Log.d("TTT","testMethod");
        mBaseRepository.method(this);
    }
}
