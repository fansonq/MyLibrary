package com.fanson.mylibrary.adapter;

import android.util.Log;

import com.example.fansonlib.base.BaseHolder;
import com.example.fansonlib.base.adapter.ItemViewDelegate;
import com.fanson.mylibrary.R;

/**
 * Created by：fanson
 * Created on：2017/9/18 15:44
 * Describe：
 */

public class CatDelegate implements ItemViewDelegate<IBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_cat;
    }

    @Override
    public boolean isForViewType(IBean item, int position) {
        return item instanceof Cat;
    }

    @Override
    public void convert(BaseHolder holder, IBean iBean, int position) {
        Cat bean = (Cat) iBean;
        holder.setText(R.id.tv2, String.valueOf(bean.getAge()));
        Log.d("TTT","cat");
    }
}
