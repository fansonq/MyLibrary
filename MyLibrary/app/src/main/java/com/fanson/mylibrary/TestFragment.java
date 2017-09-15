package com.fanson.mylibrary;

import android.os.Bundle;
import android.view.View;

import com.example.fansonlib.base.BaseFragment;
import com.example.fansonlib.widget.dialogfragment.ConfirmDialog;

/**
 * Created by：fanson
 * Created on：2017/5/8 11:21
 * Describe：
 */

public class TestFragment extends BaseFragment{


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected View initView(View rootView, Bundle savedInstanceState) {
        return rootView;
    }

    @Override
    protected void initData() {
        ConfirmDialog.newInstance("提示","你预约成功！")
                .setMargin(60)
                .setOutCancel(false)
                .show(getFragmentManager());
    }
}
