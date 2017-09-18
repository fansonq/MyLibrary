package com.fanson.mylibrary.mvp;

import android.util.Log;

import com.example.fansonlib.base.BasePresenterM;
import com.example.fansonlib.utils.ShowToast;

/**
 * Created by fansonq on 2017/9/2.
 */

public class TestPresenter extends BasePresenterM<TestModel,TestView> implements TestCallback{

    public void testMethod(){
        mModel.method(this);
        Log.d("TTT","testMethod");
    }

    @Override
    public void successful(String message) {
        ShowToast.singleLong(message);
    }

    @Override
    public void failure(String errorMsg) {

    }

    @Override
    protected TestModel createModel() {
        return new TestModel();
    }
}
