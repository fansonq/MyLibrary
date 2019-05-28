package com.example.fansonlib.widget.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.fansonlib.R;
import com.example.fansonlib.impl.CustomLoadMoreView;

import java.util.List;

/**
 * @author Created by：Fanson
 * Created Time: 2019/5/24 14:30
 * Describe：自定义RecyclerView，继承RecyclerView
 * 泛型B是实体类，泛型A是适配器
 */
public class MyRecyclerView<B, A extends BaseQuickAdapter<B, BaseViewHolder>> extends RecyclerView implements IRvLoadFinishListener, BaseQuickAdapter.RequestLoadMoreListener {

    private static final String TAG = MyRecyclerView.class.getSimpleName();

    /**
     * 默认一页的数量为10
     */
    private static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 默认页码为1
     */
    public static final int DEFAULT_PAGE_NUM = 1;
    /**
     * 请求加载的页码
     */
    private int mRequestPageNum = 1;

    /**
     * 是否刷新（True：重头刷新、False：加载更多）
     */
    private boolean mIsRefresh = false;

    /**
     * 滑动监听接口
     */
    private MyRvScrollListener mRvScrollListener;

    /**
     * 加载更多的监听
     */
    private IRvLoadMoreListener mRvLoadMoreListener;

    /**
     * 下拉刷新的监听
     */
    private IRvRefreshListener mIRvRefreshListener;

    /**
     * 适配器
     */
    private A mAdapter;

    /**
     * 无数据的View
     */
    private View mNoDataView = null;

    /**
     * 错误数据的View
     */
    private View mErrorView = null;

    private View mErrorWithHeadView = null;

    public MyRecyclerView(Context context) {
        super(context, null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet, 0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置RecyclerView
     */
    private void setRecyclerView() {
        if (mAdapter != null) {
            mRvScrollListener = new MyRvScrollListener(getContext());
            addOnScrollListener(mRvScrollListener);
            setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter.setLoadMoreView(new CustomLoadMoreView());
            mAdapter.setPreLoadNumber(2);
            mAdapter.setOnLoadMoreListener(this, this);
            setAdapter(mAdapter);
        }
    }

    /**
     * 注册接口监听：加载更多
     *
     * @param listener 加载更多的监听接口
     */
    public void setLoadMoreListener(IRvLoadMoreListener listener) {
        mRvLoadMoreListener = listener;
    }

    /**
     * 注册接口监听：下拉刷新
     *
     * @param listener 下拉刷新的监听接口
     */
    public void setRefreshListener(IRvRefreshListener listener) {
        mIRvRefreshListener = listener;
    }

    /**
     * 设置当前为刷新
     */
    public void setRefresh() {
        mIsRefresh = true;
    }

    /**
     * 获取当前是否刷新状态
     */
    public boolean getRefresh() {
        return mIsRefresh;
    }

    /**
     * 获取适配器
     *
     * @return 适配器
     */
    public A getRvAdapter() {
        return mAdapter;
    }

    /**
     * 设置适配器
     *
     * @param adapter 适配器
     */
    public void setRvAdapter(A adapter) {
        mAdapter = adapter;
        setRecyclerView();
    }

    /**
     * 添加单类型布局的数据集（不删除原有数据）
     *
     * @param list 数据集
     */
    public void addList(List<B> list) {
        setList(list,  false);
    }

    /**
     * 添加多类型布局的数据集（不删除原有数据）
     *
     * @param list 数据集
     */
    public void addMultiList(List<B> list) {
        setList(list, true);
    }

    /**
     * 添加单类型布局的数据集（删除原有数据）
     *
     * @param list 数据集
     */
    public void setList(List<B> list) {
        setList(list,  false);
    }

    /**
     * 添加多类型布局的数据集（删除原有数据）
     *
     * @param list 数据集
     */
    public void setMultiList(List<B> list) {
        setList(list,  true);
    }

    /**
     * 设置RecyclerView数据
     * 如果刷新停止刷新并设置数据
     * 否则判空设置数据，根据返回数据调试设置加载结束、加载完成
     *
     * @param list        数据
     * @param isMultiItem true:多类型布局，false：单类型布局
     */
    private void setList(List<B> list, boolean isMultiItem) {
        if (mAdapter == null) {
            Log.e(TAG, "适配器没有初始化");
            return;
        }
        if (list == null){
            Log.e(TAG, "数据集为空");
            return;
        }
        if (mRequestPageNum == 1 && list.size() == 0) {
            showNoDataView();
            mIRvRefreshListener.onCompleteRefresh();
            return;
        }

//        //判断是否为多类型布局
//        int ignoreSize = 0;
//        if (isMultiItem) {
//            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i) instanceof MultiItemEntity) {
//                    if (((MultiItemEntity) list.get(i)).getItemType() == 0) {
//                        ignoreSize++;
//                    }
//                }
//            }
//        }
//        int size = list.size() - ignoreSize;

        if (list.size() > 0) {
           hideNoDataView();
            onRvLoadFinish();
            setDataToAdapter(mIsRefresh,list);
            mAdapter.loadMoreComplete();
            mRequestPageNum++;
            if (list.size() < DEFAULT_PAGE_SIZE) {
                mAdapter.loadMoreEnd();
            }
        } else {
            mAdapter.loadMoreEnd();
        }

//        if (list == null) {
//            if (mAdapter.getData().size() == 0) {
//                showNoDataView();
//            } else {
//                mAdapter.loadMoreFail();
//            }
//            if (mIsRefresh) {
//                mIRvRefreshListener.onCompleteRefresh();
//            }
//        } else {
//            if (mIsRefresh) {
//                if (isAdd) {
//                    mAdapter.addData(0, list);
//                } else {
//                    mAdapter.setNewData(list);
//                }
//                mIRvRefreshListener.onCompleteRefresh();
//                scrollToPosition(0);
//                mRequestPageNum = 2;
//            } else {
//                mAdapter.addData(list);
//                mRequestPageNum += 1;
//            }
//
//            int ignoreSize = 0;
//
//            //判断是否为多类型布局
//            if (isMultiItem) {
//                for (int i = 0; i < list.size(); i++) {
//                    if (list.get(i) instanceof MultiItemEntity) {
//                        if (((MultiItemEntity) list.get(i)).getItemType() == 0) {
//                            ignoreSize++;
//                        }
//                    }
//                }
//            }
//
//            int size = list.size() - ignoreSize;
//            if ((size > 0 && size < DEFAULT_PAGE_SIZE) || size == 0) {
//                mAdapter.loadMoreEnd();
//            } else if (size >= DEFAULT_PAGE_SIZE) {
//                onRvLoadFinish();
//                mAdapter.loadMoreComplete();
//            }
//            hideErrorView();
//            if (mAdapter.getData().size() == 0) {
//                showNoDataView();
//            } else {
//                hideNoDataView();
//            }
//        }
    }

    /**
     * 装载数据到适配器
     * @param isRefresh 是否下拉刷新
     * @param list 数据集
     */
    private void setDataToAdapter(boolean isRefresh,List<B> list){
        if (isRefresh){
            mAdapter.setNewData(list);
            if (mIRvRefreshListener!=null){
                mIRvRefreshListener.onCompleteRefresh();
            }
        } else {
            mAdapter.addData(list);
        }
    }

    /**
     * 获取适配器的数据集
     *
     * @return 适配器的数据集
     */
    public List<B> getList() {
        return mAdapter.getData();
    }

    /**
     * 设置空数据界面
     */
    public void setEmptyView() {
        mAdapter.setNewData(null);
        showNoDataView();
    }

    /**
     * 显示无数据页面，如果想自定义，覆写此方法
     */
    private void showNoDataView() {
        if (mAdapter == null) {
            return;
        }
        if (mAdapter.getHeaderLayoutCount() == 0) {
            mAdapter.setFooterView(getNoDataView());
//            ViewGroup.LayoutParams layoutParams = getNoDataView().getLayoutParams();
//            layoutParams.height = this.getMeasuredHeight();
//            getNoDataView().setLayoutParams(layoutParams);
        } else if (mAdapter.getHeaderLayoutCount() > 0) {
            mAdapter.setFooterView(getNoDataView());
        }
    }

    /**
     * 隐藏无数据view
     */
    private void hideNoDataView() {
        if (mAdapter != null) {
            mAdapter.removeAllFooterView();
        }
    }

    /**
     * 显示错误View
     */
    public void showErrorView() {
        if (mAdapter != null) {
            if (mAdapter.getHeaderLayoutCount() == 0) {
                if (mAdapter.getData().size() > 0) {
                    mAdapter.loadMoreFail();
                } else {
                    mAdapter.setFooterView(getErrorView());
                    ViewGroup.LayoutParams layoutParams = getErrorView().getLayoutParams();
                    layoutParams.height = getHeight();
                    getErrorView().setLayoutParams(layoutParams);
                }
            } else if (mAdapter.getHeaderLayoutCount() > 0) {
                if (mAdapter.getData().size() > 0) {
                    mAdapter.loadMoreFail();
                } else {
                    mAdapter.setFooterView(getErrorWithHeadView());
                }
            }
        }
    }

    /**
     * 隐藏错误View
     */
    private void hideErrorView() {
        if (mAdapter != null) {
            mAdapter.removeAllFooterView();
        }
    }

    /**
     * 获取默认的空数据视图
     *
     * @return 空数据视图
     */
    public View getNoDataView() {
        if (mNoDataView == null) {
            mNoDataView = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_no_data, this,false);
        }
        return mNoDataView;
    }

    /**
     * 设置空数据视图（p：需要在加载数据前调用）
     *
     * @param view 用户自定义的空数据视图
     */
    public void setCustomNoDataView(View view) {
        mNoDataView = view;
    }

    public View getErrorView() {
        if (mErrorView == null) {
            mErrorView = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_error, null);
            mErrorView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyRecyclerView.this.mIsRefresh) {
                        mIRvRefreshListener.onCompleteRefresh();
                    }
                    hideErrorView();
                }
            });
        }
        return mErrorView;
    }

    public View getErrorWithHeadView() {
        if (mErrorWithHeadView == null) {
            mErrorWithHeadView = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_error, null);
            mErrorWithHeadView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyRecyclerView.this.mIsRefresh) {
                        mIRvRefreshListener.onCompleteRefresh();
                    }
                }
            });
        }
        return mErrorWithHeadView;
    }

    @Override
    public void onRvLoadFinish() {
        if (mRvScrollListener != null) {
            mRvScrollListener.closeLoadingMore();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (mRvLoadMoreListener != null) {
            mIsRefresh = false;
            mRvLoadMoreListener.onRvLoadMore(mRequestPageNum);
        }
    }
}
