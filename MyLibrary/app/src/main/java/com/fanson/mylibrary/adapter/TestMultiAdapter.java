package com.fanson.mylibrary.adapter;

import android.content.Context;

import com.example.fansonlib.base.adapter.BaseMultiAdapter;

import java.util.List;

/**
 * Created by：fanson
 * Created on：2017/9/18 13:56
 * Describe：
 */

public class TestMultiAdapter<IBean> extends BaseMultiAdapter{

    public TestMultiAdapter(Context context, List<IBean> list) {
        super(context, list);

        addItemViewDelegate(new DogDelegate());
        addItemViewDelegate(new CatDelegate());
    }
}
