package com.fanson.mylibrary.mvp;

import com.example.fansonlib.base.BasePresenter;

/**
 * Created by fansonq on 2017/9/2.
 */

public class Test2Prensenter extends BasePresenter{

    public void methodTest2(){
        new Test2Model().notifyToP();
    }
}
