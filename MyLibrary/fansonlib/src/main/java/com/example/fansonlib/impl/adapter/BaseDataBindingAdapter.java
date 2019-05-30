package com.example.fansonlib.impl.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author Created by：Fanson
 * Created Time: 2019/5/22 16:58
 * Describe：继承BaseQuickAdapter实现支持DataBinding
 */
public abstract  class BaseDataBindingAdapter <T, D extends ViewDataBinding, V extends BaseViewHolder> extends BaseQuickAdapter<T,V> {

    protected Context mContext;

    public BaseDataBindingAdapter(@Nullable List<T> data) {
        super(data);
    }

    public BaseDataBindingAdapter(int layoutResId) {
        super(layoutResId);
    }

    public BaseDataBindingAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }


    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != LOADING_VIEW && viewType != HEADER_VIEW && viewType != EMPTY_VIEW && viewType != FOOTER_VIEW) {
            mContext = parent.getContext();
            D d = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), this.mLayoutResId, parent,false);
            d.executePendingBindings();
            DataBindingViewHolder mvViewHolder = new DataBindingViewHolder(d);
            bindViewClickListener(mvViewHolder);
            mvViewHolder.setDbAdapter(this);
            return (V) mvViewHolder;
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }


    private void bindViewClickListener(final BaseViewHolder baseViewHolder) {
        if (baseViewHolder == null) {
            return;
        }
        final View view = baseViewHolder.itemView;

        if (getOnItemClickListener() != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOnItemClickListener().onItemClick(BaseDataBindingAdapter.this, v,
                            baseViewHolder.getLayoutPosition() - getHeaderLayoutCount());
                }
            });
        }

        if (getOnItemLongClickListener() != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    getOnItemLongClickListener().onItemLongClick(BaseDataBindingAdapter.this, v,
                            baseViewHolder.getLayoutPosition() - getHeaderLayoutCount());
                    return false;
                }
            });
        }

    }

}
