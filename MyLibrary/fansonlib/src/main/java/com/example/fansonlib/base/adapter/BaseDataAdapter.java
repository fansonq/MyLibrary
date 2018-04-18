package com.example.fansonlib.base.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by：fanson
 * Created on：2016/12/19 13:22
 * Describe：Adapter数据相关的封装
 */
public abstract class BaseDataAdapter<M> extends BaseViewAdapter<M> {

    protected List<M> mDataList;

    public BaseDataAdapter(Context context) {
        super(context);
        this.mDataList = new ArrayList<>();
    }

    @Override
    public void bottomEnterAnim(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void topEnterAnim(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public BaseHolder bindHolder(ViewGroup parent, int viewType){
        return  new BaseHolder(LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false));
    }

//    @Override
//    public void bindCustomViewHolder(BaseHolder holder, int position){
//        bindData(holder,mDataList.get(position),position);
//    }

//    /**
//     * 显示数据，处理数据
//     * @param holder
//     * @param bean
//     * @param position
//     */
//    protected abstract void bindData(BaseHolder holder,M bean, int position);

    public BaseDataAdapter(Context context, List<M> list) {
        super(context);
        this.mDataList = new ArrayList<>();
        this.mDataList.addAll(list);
    }

    /**
     * 清空所有数据
     */
    public void clearList(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 填充数据,此操作会清除原来的数据
     *
     * @param list 要填充的数据
     * @return true:填充成功并调用刷新数据
     */
    public boolean fillList(List<M> list) {
        mDataList.clear();
        boolean result = mDataList.addAll(list);
        if (result) {
            notifyDataSetChanged();
        }
        return result;
    }

    /**
     * 追加一条数据
     *
     * @param data 要追加的数据
     * @return true:追加成功并刷新界面
     */
    public boolean appendItem(M data) {
        boolean result = mDataList.add(data);
        if (result) {
            if (getHeaderExtraViewCount() == 0) {
                notifyItemInserted(mDataList.size() - 1);
            } else {
                notifyItemInserted(mDataList.size());
            }
        }
        return result;
    }

    /**
     * 追加集合数据
     *
     * @param list 要追加的集合数据
     * @return 追加成功并刷新
     */
    public boolean appendList(List<M> list) {
        boolean result = mDataList.addAll(list);
        if (result) {
            notifyItemRangeInserted(getItemCount()-list.size(),list.size());
        }
        return result;
    }

    /**
     * 指定位置插入数据
     * @param index
     * @param data
     */
    public void insertByPos(int index,M data) {
        mDataList.add(index,data);
        notifyDataSetChanged();
    }

    /**
     * 在最顶部前置数据
     *
     * @param data 要前置的数据
     */
    public void proposeItem(M data) {
        mDataList.add(0, data);
        if (getHeaderExtraViewCount() == 0) {
            notifyItemInserted(0);
        } else {
            notifyItemInserted(getHeaderExtraViewCount());
        }
    }

    /**
     * 在顶部前置数据集合
     *
     * @param list 要前置的数据集合
     */
    public void proposeList(List<M> list) {
        mDataList.addAll(0, list);
//        notifyDataSetChanged();
        notifyItemRangeInserted(0,list.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerView != null && position == 0) {
            return VIEW_TYPE_HEADER;
        } else if (footerView != null && position == mDataList.size() + getHeaderExtraViewCount()) {
            return VIEW_TYPE_FOOTER;
        } else {
            return getLayoutRes(position);
        }
    }

    /**
     * 返回布局layout
     * @param position 列表位置
     * @return
     *  布局Layout ID
     */
    @LayoutRes
    public abstract int getLayoutRes(int position);

    @Override
    public int getItemCount() {
        return mDataList.size() + getExtraViewCount();
    }

    /**
     * 根据位置获取一条数据
     *
     * @param position View的位置
     * @return 数据
     */
    public M getItem(int position) {
        if (headerView != null && position == 0
                || position >= mDataList.size() + getHeaderExtraViewCount()) {
            return null;
        }
        return headerView == null ? mDataList.get(position) : mDataList.get(position - 1);
    }

    /**
     * 根据ViewHolder获取数据
     *
     * @param holder ViewHolder
     * @return 数据
     */
    public M getItem(BaseHolder holder) {
        return getItem(holder.getAdapterPosition());
    }

    public void updateItem(M data) {
        int index = mDataList.indexOf(data);
        if (index < 0) {
            return;
        }
        mDataList.set(index, data);
        if (headerView == null) {
            notifyItemChanged(index);
        } else {
            notifyItemChanged(index + 1);
        }
    }

    /**
     * 移除一条数据
     *
     * @param position 位置
     */
    public void removeItem(int position) {
        if (headerView == null) {
            mDataList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,mDataList.size()-position);
        } else {
            mDataList.remove(position - 1);
            notifyItemRemoved(position- 1);
            notifyItemRangeChanged(position,mDataList.size()-(position - 1));
        }

    }

    /**
     * 移除一条数据
     *
     * @param data 要移除的数据
     */
    public void removeItem(M data) {
        int index = mDataList.indexOf(data);
        if (index < 0) {
            return;
        }
        mDataList.remove(index);
        if (headerView == null) {
            notifyItemRemoved(index);// 显示动画效果
            notifyItemRangeChanged(index,mDataList.size()-index); // 对于被删掉的位置及其后range大小范围内的view进行重新onBindViewHolder
        } else {
            notifyItemRemoved(index + 1);
            notifyItemRangeChanged(index,mDataList.size()-(index+1));
        }
    }

}
