package com.fanson.mylibrary.mvp;

import android.util.Log;

import com.example.fansonlib.base.BaseModel;
import com.example.fansonlib.base.BasePresenter;

/**
 * Created by fansonq on 2017/9/2.
 */

public class Test2Model extends BaseModel{

    public Test2Model(BasePresenter presenter) {
        super(presenter);
    }

    public void notifyToP(){
        notifyToObservers("Test2Model发送的数据");
        Log.d("TTT","Test2Model发送数据");
    }
}
