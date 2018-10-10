package com.example.fansonlib.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by：fanson
 * Created on：2017/9/15 18:21
 * Describe：处理多布局的适配器
 */

public abstract class BaseMultiAdapter<M> extends BaseDataAdapter<M> {
    protected Context mContext;
//    protected List<M> mDatas;

    protected ItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClickListener mOnItemClickListener;


    public BaseMultiAdapter(Context context) {
        super(context);
        mContext = context;
//        mDatas = datas;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    @Override
    public int getLayoutRes(int position) {
        if (!useItemViewDelegateManager()) return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(mDataList.get(position), position);
    }

    @Override
    public BaseHolder bindHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        BaseHolder holder = BaseHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder,holder.getConvertView());
        setListener(parent, holder, viewType);
        return holder;
    }

    public void onViewHolderCreated(BaseHolder holder, View itemView){

    }

    public void convert(BaseHolder holder, M t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final BaseHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder , position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }

    @Override
    public void bindCustomViewHolder(BaseHolder holder, int position) {
        convert(holder, mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        int itemCount = mDataList.size();
        return itemCount;
    }


    public List<M> getDatas() {
        return mDataList;
    }

    public BaseMultiAdapter addItemViewDelegate(ItemViewDelegate<M> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public BaseMultiAdapter addItemViewDelegate(int viewType, ItemViewDelegate<M> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder,  int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder,  int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
