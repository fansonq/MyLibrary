package com.fanson.mylibrary.mvp;

import android.util.Log;

import com.example.fansonlib.base.BasePresenterM;

/**
 * Created by fansonq on 2017/9/2.
 */

public class TestPresenter extends BasePresenterM<TestModel,ContractTest.TestView> implements ContractTest.ITestPresenter,TestCallback{

    public TestPresenter(ContractTest.TestView view) {
        attachView(view);
    }

    @Override
    public void successful(String message) {
        getBaseView().testSuccess(message);
    }

    @Override
    public void failure(String errorMsg) {

    }

    @Override
    protected TestModel createModel() {
        return new TestModel();
    }

    @Override
    public void testPresenterMethod() {
        mModel.method(this);
        Log.d("TTT","testMethod");
    }
}
