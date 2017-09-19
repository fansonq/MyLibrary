package com.fanson.mylibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.fansonlib.base.BaseDataAdapter;
import com.example.fansonlib.base.BaseHolder;
import com.fanson.mylibrary.R;

/**
 * Created by：fanson
 * Created on：2017/9/19 9:29
 * Describe：
 */

public class TestAdapter extends BaseDataAdapter<Dog>{

    public TestAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder bindHolder(ViewGroup parent, int viewType) {
        BaseHolder baseHolder = new BaseHolder(LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false));
        return baseHolder;
    }

    @Override
    protected void bindData(BaseHolder holder, Dog bean, int position) {
        holder.setText(R.id.tv1,"This is a test item");
    }

    @Override
    public int getLayoutRes(int position) {
        return R.layout.item_dog;
    }
}
