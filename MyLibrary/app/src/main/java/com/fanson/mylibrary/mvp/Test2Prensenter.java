package com.fanson.mylibrary.mvp;

import android.util.Log;

import com.example.fansonlib.base.BasePresenter;

import java.util.Observable;

/**
 * Created by fansonq on 2017/9/2.
 */

public class Test2Prensenter extends BasePresenter{

    public void methodTest2(){
        new Test2Model(this).notifyToP();
    }


    @Override
    protected void receiveObservable(Observable observable, Object object) {
        Log.d("TTT","Test2P收到数据："+object.toString());
    }
}
