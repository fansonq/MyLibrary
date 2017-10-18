package com.fanson.mylibrary.adapter;

import android.content.Context;

import com.example.fansonlib.base.adapter.BaseMultiAdapter;

/**
 * Created by：fanson
 * Created on：2017/9/18 13:56
 * Describe：
 */

public class TestMultiAdapter extends BaseMultiAdapter{

    public TestMultiAdapter(Context context) {
        super(context);

        addItemViewDelegate(new DogDelegate());
        addItemViewDelegate(new CatDelegate());
    }

}
