package com.example.fansonlib.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by：fanson
 * Created on：2016/12/19 15:14
 * Describe：RecyclerView.Adapter的扩展,View相关的封装
 */
public abstract class BaseViewAdapter<M> extends RecyclerView.Adapter<BaseHolder> implements View.OnTouchListener {

    private static final String TAG = BaseViewAdapter.class.getSimpleName();

    public static final int VIEW_TYPE_HEADER = 1024;
    public static final int VIEW_TYPE_FOOTER = 1025;

    private boolean isFirst = false;
    private boolean isLast = false;
    private int lastY;
    private RecyclerView.ViewHolder mCurrentViewHolder;


    protected View headerView;
    protected View footerView;

    protected Context context;

    public BaseViewAdapter(Context context) {
        this.context = context;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean isLast() {
        return isLast;
    }

    /**
     * 底部进入的item
     *
     * @param viewHolder
     */
    public abstract void bottomEnterAnim(RecyclerView.ViewHolder viewHolder);

    /**
     * 顶部进入的item
     *
     * @param viewHolder
     */
    public abstract void topEnterAnim(RecyclerView.ViewHolder viewHolder);


    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setOnTouchListener(this);
//        BaseHolder baseHolder = new BaseHolder(LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false));
        if (viewType == VIEW_TYPE_HEADER) {
            return new BaseHolder(headerView);
        } else if (viewType == VIEW_TYPE_FOOTER) {
            return new BaseHolder(footerView);
        } else {
            return bindHolder(parent, viewType);
        }
    }

    /**
     * 创建自定义的ViewHolder
     *
     * @param holder   默认的ViewHolder
     * @param viewType 对应的布局Layout ID，也代表为ViewType
     * @return ViewHolder
     */
    public abstract BaseHolder bindHolder(ViewGroup holder, int viewType);

    @Override
    public final void onBindViewHolder(BaseHolder holder, int position) {
        mCurrentViewHolder = holder;
        if (position == getExtraViewCount() - 1) {
            isLast = true;
        } else {
            isLast = false;
        }
        if (position == 0) {
            isFirst = true;
        } else {
            isFirst = false;
        }
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_HEADER:
            case VIEW_TYPE_FOOTER:
                break;
            default:
                bindCustomViewHolder(holder, position);
                break;
        }
    }

    /**
     * 绑定自定义的ViewHolder
     *
     * @param holder   ViewHolder
     * @param position 位置
     */
    public abstract void bindCustomViewHolder(BaseHolder holder, int position);

    /**
     * 添加HeaderView
     *
     * @param headerView 顶部View对象
     */
    public void addHeaderView(View headerView) {
        if (headerView == null) {
            Log.w(TAG, "add the header view is null");
            return;
        }
        this.headerView = headerView;
        notifyDataSetChanged();
    }

    /**
     * 移除HeaderView
     */
    public void removeHeaderView() {
        if (headerView != null) {
            headerView = null;
            notifyDataSetChanged();
        }
    }

    /**
     * 添加FooterView
     *
     * @param footerView View对象
     */
    public void addFooterView(View footerView) {
        if (footerView == null) {
            Log.w(TAG, "add the footer view is null");
            return;
        }
        this.footerView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * 移除FooterView
     */
    public void removeFooterView() {
        if (footerView != null) {
            footerView = null;
            notifyItemRemoved(getItemCount());
        }
    }

    /**
     * 获取附加View的数量,包括HeaderView和FooterView
     *
     * @return 数量
     */
    public int getExtraViewCount() {
        int extraViewCount = 0;
        if (headerView != null) {
            extraViewCount++;
        }
        if (footerView != null) {
            extraViewCount++;
        }
        return extraViewCount;
    }

    /**
     * 获取顶部附加View数量,即HeaderView数量
     *
     * @return 数量
     */
    public int getHeaderExtraViewCount() {
        return headerView == null ? 0 : 1;
    }

    /**
     * 获取底部附加View数量,即FooterView数量
     *
     * @return 数量, 0或1
     */
    public int getFooterExtraViewCount() {
        return footerView == null ? 0 : 1;
    }

    @Override
    public abstract long getItemId(int position);


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - lastY;
                if (deltaY < 0) {   //swipe up
                    if (!isLast()) {
                        bottomEnterAnim(mCurrentViewHolder);
                    }
                } else {   //swipe down
                    if (!isFirst()) {
                        topEnterAnim(mCurrentViewHolder);
                    }
                }
                break;
        }
        lastY = y;
        return false;
    }
}
