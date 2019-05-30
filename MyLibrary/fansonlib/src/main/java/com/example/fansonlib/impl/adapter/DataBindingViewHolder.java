package com.example.fansonlib.impl.adapter;

import android.databinding.ViewDataBinding;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author Created by：Fanson
 * Created Time: 2019/5/22 17:02
 * Describe：支持DataBinding的ViewHolder
 */
public class DataBindingViewHolder<T extends ViewDataBinding> extends BaseViewHolder {

    private T binding = null;

    public DataBindingViewHolder(T binding) {
        super(binding.getRoot());
//        binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.binding = binding;
    }

    /**
     * 设置DataBinding的适配器
     * @param adapter BaseQuickAdapter
     * @return BaseViewHolder
     */
    public BaseViewHolder setDbAdapter(BaseQuickAdapter adapter) {
        super.setAdapter(adapter);
        return this;
    }

    /**
     * 获取绑定的Binding
     * @return binding
     */
    public T getDataViewBinding() {
        return this.binding;
    }

}
