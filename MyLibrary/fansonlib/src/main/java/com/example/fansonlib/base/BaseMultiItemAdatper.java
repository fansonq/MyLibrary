package com.example.fansonlib.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.fansonlib.callback.MultiItemTypeSupport;

import java.util.List;

/**
 * Created by：fanson
 * Created on：2017/9/8 9:46
 * Describe：RecyclerView多布局的Base适配器
 */

public abstract class BaseMultiItemAdatper<M, VH extends BaseHolder> extends BaseDataAdapter<M, VH > {

    protected MultiItemTypeSupport<M> mMultiItemTypeSupport;

    public BaseMultiItemAdatper(Context context) {
        super(context);
    }

    public BaseMultiItemAdatper(Context context, List<M> list,MultiItemTypeSupport<M> multiItemTypeSupport){
        super(context,list);
        mMultiItemTypeSupport =multiItemTypeSupport;
    }

    @Override
    public void bottomEnterAnim(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void topEnterAnim(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public abstract  VH createCustomViewHolder(ViewGroup parent, int viewType);

    @Override
    public void bindCustomViewHolder(VH holder, int position) {

    }

    @Override
    public int getCustomViewType(int position) {
        return mMultiItemTypeSupport.getItemViewType(position,mDataList.get(position));
    }
}
