package com.fanson.mylibrary.mvp;

import com.example.fansonlib.base.BaseView;

/**
 * Created by：fanson
 * Created on：2017/9/19 16:29
 * Describe：
 */

public class ContractTest {

    public interface TestView extends BaseView {

        void showCode102(String message);

        void showFailure(String errorMsg);
    }

    public interface ITestPresenter   {

        void testPresenterMethod();
    }
}
