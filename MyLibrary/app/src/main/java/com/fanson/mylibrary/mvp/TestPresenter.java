package com.fanson.mylibrary.mvp;

import android.util.Log;

import com.example.fansonlib.base.BasePresenterWithM;
import com.example.fansonlib.utils.ShowToast;

/**
 * Created by fansonq on 2017/9/2.
 */

public class TestPresenter extends BasePresenterWithM<TestModel,TestView> implements TestCallback{

    private TestModel mTestModel;

    public TestPresenter(){
        mTestModel = new TestModel();
        initModel(mTestModel);
    }

    public void testMethod(){
        mBaseModel.method(this);
        Log.d("TTT","testMethod");
    }

    @Override
    public void successful(String message) {
        ShowToast.singleLong(message);
    }

    @Override
    public void failure(String errorMsg) {

    }
}
