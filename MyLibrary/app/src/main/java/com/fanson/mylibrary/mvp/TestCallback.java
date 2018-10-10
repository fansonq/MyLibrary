package com.fanson.mylibrary.mvp;

import com.example.fansonlib.callback.BaseModelCallback;
import com.fanson.mylibrary.SimpleBean;

/**
 * Created by：fanson
 * Created on：2017/9/12 10:45
 * Describe：
 */

public interface TestCallback extends BaseModelCallback{

    void successful(SimpleBean bean);

    void failure(String errorMsg);
}
