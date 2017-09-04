package com.fanson.mylibrary.mvp;

import android.util.Log;

import com.example.fansonlib.base.BaseModel;
import com.example.fansonlib.base.BasePresenter;

/**
 * Created by fansonq on 2017/9/2.
 */

public class TestModel extends BaseModel{


    public TestModel(BasePresenter presenter) {
        super(presenter);
    }

    public void method(){
        notifyToObservers("Test1Model发送的数据");
        Log.d("TTT","Test1Model发送的数据");
    }

}
