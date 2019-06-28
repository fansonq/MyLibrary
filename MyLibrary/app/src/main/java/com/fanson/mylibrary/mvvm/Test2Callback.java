package com.fanson.mylibrary.mvvm;

import com.example.fansonlib.callback.BaseModelCallback;
import com.fanson.mylibrary.bean.TestVmBean;

/**
 * @author Created by：Fanson
 * Created Time: 2019/6/28 10:14
 * Describe：
 */
public interface Test2Callback extends BaseModelCallback {

    /**
     * 成功的返回
     * @param bean 实体类
     */
    void onTest2Success(TestVmBean bean);

    /**
     * 失败的返回
     * @param errorMsg 提示语
     */
    void onTest2Failure(String errorMsg);
}
