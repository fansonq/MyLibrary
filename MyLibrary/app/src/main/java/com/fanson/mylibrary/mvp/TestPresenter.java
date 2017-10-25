package com.fanson.mylibrary.mvp;

import android.util.Log;

import com.example.fansonlib.base.BasePresenterWithM;

/**
 * Created by fansonq on 2017/9/2.
 */

public class TestPresenter extends BasePresenterWithM<TestModel,ContractTest.TestView> implements ContractTest.ITestPresenter,TestCallback{

    public TestPresenter(ContractTest.TestView view){
        attachView(view);
    }

    public void testMethod(){
        mBaseModel.method(this);
        Log.d("TTT","testMethod");
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
        mBaseModel.method(this);
        Log.d("TTT","testMethod");
    }
}
