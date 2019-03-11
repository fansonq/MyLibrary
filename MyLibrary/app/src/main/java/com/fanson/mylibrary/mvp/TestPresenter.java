package com.fanson.mylibrary.mvp;

import android.app.Activity;

import com.example.fansonlib.base.BasePresenterRep;
import com.example.fansonlib.utils.log.MyLogUtils;
import com.fanson.mylibrary.SimpleBean;

/**
 * Created by fansonq on 2017/9/2.
 */

public class TestPresenter extends BasePresenterRep<TestRepository,SimpleBean,ContractTest.TestView> implements ContractTest.ITestPresenter{


    public TestPresenter(Activity activity,ContractTest.TestView baseView) {
        super(activity,baseView);
    }

    @Override
    protected TestRepository createRepository() {
        return new TestRepository();
    }


    @Override
    public void testPresenterMethod() {
        MyLogUtils.getInstance().d("testMethod");
        mBaseRepository.method(callback);
    }

    /**
     * 测试网络的Repository层回调
     */
    private TestCallback callback = new TestCallback() {
        @Override
        public void successful(SimpleBean bean) {
            switch (bean.getCode()){
                case 1:
                    getWeakActivity();
                    setValue(bean);
                    break;
                case 2:
//                    getBaseView().showCode102(bean.getMessage());
                default:
                    break;
            }
        }

        @Override
        public void failure(String errorMsg) {

        }
    };
}
