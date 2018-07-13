package com.fanson.mylibrary.adapter;

import android.content.Context;

import com.example.fansonlib.base.adapter.BaseDataAdapter;
import com.example.fansonlib.base.adapter.BaseHolder;
import com.fanson.mylibrary.BR;
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
    public void bindCustomViewHolder(BaseHolder holder, int position) {
        holder.getBinding().setVariable(BR.dogBean,getItem(position));
        holder.getBinding().executePendingBindings();
//        holder.setText(R.id.tv2,"This is a test item");
    }

    @Override
    public int getLayoutRes(int position) {
        return R.layout.item_dog;
    }
}
