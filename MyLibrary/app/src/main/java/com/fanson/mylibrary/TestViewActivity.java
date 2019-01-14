package com.fanson.mylibrary;

import android.os.Bundle;

import com.example.fansonlib.base.BaseActivity;
import com.fanson.mylibrary.databinding.ActivityTestViewBinding;

/**
 * @author Created by：Fanson
 * Created Time: 2019/1/14 14:06
 * Describe：测试View的Activity
 */
public class TestViewActivity extends BaseActivity<ActivityTestViewBinding>{


    @Override
    protected int getContentView() {
        return R.layout.activity_test_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void listenEvent() {

    }
}
