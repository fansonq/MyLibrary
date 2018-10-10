package com.fanson.mylibrary.adapter;

import android.util.Log;

import com.example.fansonlib.base.adapter.BaseHolder;
import com.example.fansonlib.base.adapter.ItemViewDelegate;
import com.fanson.mylibrary.R;

/**
 * Created by：fanson
 * Created on：2017/9/18 14:02
 * Describe：
 */

public class DogDelegate implements ItemViewDelegate<IBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dog;
    }

    @Override
    public boolean isForViewType(IBean item, int position) {
        return item instanceof Dog;
    }

    @Override
    public void convert(BaseHolder holder, IBean iBean, int position) {
        Dog bean = (Dog) iBean;
        holder.setText(R.id.tv1, bean.getName());
        Log.d("TTT","dog");
    }

}
