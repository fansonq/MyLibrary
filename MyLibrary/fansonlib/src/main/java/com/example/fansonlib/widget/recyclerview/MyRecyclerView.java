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
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.example.fansonlib.R;
import com.example.fansonlib.impl.CustomLoadMoreView;
import com.example.fansonlib.widget.loadingview.LoadingStateView;

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
     * 界面是否绘制完成
     */
    private boolean mInited = false;
    /**
     * 记录：界面没初始化之前，需要显示的状态视图
     */
    private int mNeedShowStatus = 0;
    private static final int STATUS_LOADING = 1;
    private static final int STATUS_NO_DATA = 2;
    private static final int STATUS_ERROR = 3;

    /**
     * 点击空数据视图，可以重试加载，默认支持
     */
    private boolean mClickEmptyLoadEnable = true;
    /**
     * 重试加载，是否需要LoadingView，如果不需要，则交给用户处理
     */
    private boolean mNeedRetryLoadView = true;

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
     * 重试加载的监听
     */
    private IRvRetryListener mIRvRetryListener;

    /**
     * 适配器
     */
    private A mAdapter;

    /**
     * 默认加载状态的视图View
     */
    private LoadingStateView mLoadingStateView;
    /**
     * 加载中的View
     */
    private View mLoadingView = null;
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
            setHasFixedSize(true);
            setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter.setLoadMoreView(new CustomLoadMoreView());
            mAdapter.setPreLoadNumber(2);
            mAdapter.setOnLoadMoreListener(this, this);
            setAdapter(mAdapter);
        }
    }

    /**
     * 设置RecyclerView加载状态，底部view
     *
     * @param loadingView 设置加载状态的视图（加载中，加载出错，加载完成）
     */
    public void setLoadStateBottomView(LoadMoreView loadingView) {
        if (mAdapter != null) {
            mAdapter.setLoadMoreView(loadingView);
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
     * 注册接口监听：重试
     *
     * @param listener 重试加载的接口
     */
    public void setRetryListener(IRvRetryListener listener) {
        mIRvRetryListener = listener;
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
        setList(list, false);
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
        setList(list, false);
    }

    /**
     * 添加多类型布局的数据集（删除原有数据）
     *
     * @param list 数据集
     */
    public void setMultiList(List<B> list) {
        setList(list, true);
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
        if (list == null) {
            Log.e(TAG, "数据集为空");
            showErrorView();
            return;
        }
        if (mRequestPageNum == 1 && list.size() == 0) {
            showNoDataView();
            if (mIRvRefreshListener != null) {
                mIRvRefreshListener.onCompleteRefresh();
            }
            return;
        }

        if (list.size() > 0) {
            hideNoDataView();
            onRvLoadFinish();
            setDataToAdapter(mIsRefresh, list);
            mAdapter.loadMoreComplete();
            mRequestPageNum++;
            if (list.size() < DEFAULT_PAGE_SIZE) {
                mAdapter.loadMoreEnd();
            }
        } else {
            mAdapter.loadMoreEnd();
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
    }

    /**
     * 装载数据到适配器
     *
     * @param isRefresh 是否下拉刷新
     * @param list      数据集
     */
    private void setDataToAdapter(boolean isRefresh, List<B> list) {
        if (isRefresh) {
            mAdapter.addData(0,list);
            if (mIRvRefreshListener != null) {
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
        if (!mInited) {
            mNeedShowStatus = STATUS_NO_DATA;
            return;
        }
        if (mAdapter == null) {
            return;
        }
        if (mAdapter.getHeaderLayoutCount() == 0) {
            mAdapter.setFooterView(getNoDataView());
            ViewGroup.LayoutParams layoutParams = getNoDataView().getLayoutParams();
            layoutParams.height = getHeight();
            getNoDataView().setLayoutParams(layoutParams);
        } else if (mAdapter.getHeaderLayoutCount() > 0) {
            mAdapter.setFooterView(getNoDataView());
        }
    }

    /**
     * 获取默认的空数据视图
     *
     * @return 空数据视图
     */
    public View getNoDataView() {
        if (mNoDataView == null) {
            mNoDataView = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_status, null);
            mLoadingStateView = mNoDataView.findViewById(R.id.loadingStateView);
            mLoadingStateView.showLoadNoDataView();
            mLoadingStateView.setNoDataAction(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickEmptyLoadEnable){
                        retryLoad();
                    }
                }
            });
        } else {
            mNoDataView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickEmptyLoadEnable){
                        retryLoad();
                    }
                }
            });
        }
        return mNoDataView;
    }

    /**
     * 设置空数据视图（p：需要在加载数据前调用）
     *
     * @param view 用户自定义的空数据视图
     */
    public void setNoDataView(View view) {
        mNoDataView = view;
    }

    /**
     * 设置空数据视图layoutId（p：需要在加载数据前调用）
     *
     * @param layoutId 用户自定义的空数据视图
     */
    public void setNoDataView(int layoutId) {
        mNoDataView = LayoutInflater.from(getContext()).inflate(layoutId, null);
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
        if (!mInited) {
            mNeedShowStatus = STATUS_ERROR;
            return;
        }
        if (mAdapter == null) {
            return;
        }
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

    /**
     * 隐藏错误View
     */
    private void hideErrorView() {
        if (mAdapter != null) {
            mAdapter.removeAllFooterView();
        }
    }

    /**
     * 设置错误数据视图（p：需要在加载数据前调用）
     *
     * @param view 用户自定义的错误数据视图
     */
    public void setErrorView(View view) {
        mErrorView = view;
    }

    /**
     * 设置错误数据视图layout（p：需要在加载数据前调用）
     *
     * @param layoutId 用户自定义的错误数据视图layout
     */
    public void setErrorView(int layoutId) {
        mErrorView = LayoutInflater.from(getContext()).inflate(layoutId, null);
    }

    /**
     * 获取错误视图
     *
     * @return mErrorView
     */
    public View getErrorView() {
        if (mErrorView == null) {
            mErrorView = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_status, null);
            mLoadingStateView = mErrorView.findViewById(R.id.loadingStateView);
            mLoadingStateView.showLoadErrorView();
            mLoadingStateView.setErrorAction(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    retryLoad();
                }
            });
        } else {
            mErrorView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    retryLoad();
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

    /**
     * 显示加载中的视图
     */
    public void showLoadingView() {
        if (!mInited) {
            mNeedShowStatus = STATUS_LOADING;
            return;
        }
        if (mAdapter == null) {
            return;
        }
        if (mAdapter.getHeaderLayoutCount() == 0) {
            if (getHeight() == 0) {
                return;
            }
            mAdapter.setFooterView(getLoadingView());
            ViewGroup.LayoutParams layoutParams = getLoadingView().getLayoutParams();
            layoutParams.height = getHeight();
            getLoadingView().setLayoutParams(layoutParams);
        } else if (mAdapter.getHeaderLayoutCount() > 0) {
            mAdapter.setFooterView(getErrorWithHeadView());
        }
    }

    /**
     * 获取加载中的视图
     *
     * @return 加载中的视图
     */
    public View getLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_status, null);
            mLoadingStateView = mLoadingView.findViewById(R.id.loadingStateView);
            mLoadingStateView.showLoadingView();
        }
        return mLoadingView;
    }

    /**
     * 设置加载中视图layout
     *
     * @param view 用户自定义的加载中视图layout
     */
    public void setLoadingView(View view) {
        mLoadingView = view;
    }

    /**
     * 设置加载中视图layout
     *
     * @param layoutId 用户自定义的加载中视图layout
     */
    public void setLoadingView(int layoutId) {
        mLoadingView = LayoutInflater.from(getContext()).inflate(layoutId, null);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        mInited = true;
        if (mNeedShowStatus == 0) {
            return;
        }
        switch (mNeedShowStatus) {
            case STATUS_LOADING:
                showLoadingView();
                break;
            case STATUS_NO_DATA:
                showNoDataView();
                break;
            case STATUS_ERROR:
                showErrorView();
                break;
            default:
                break;
        }
        mNeedShowStatus = 0;
    }

    /**
     * 重试加载
     */
    private void retryLoad() {
        if (mNeedRetryLoadView) {
            showLoadingView();
        }
        if (mIRvRetryListener != null) {
            mIsRefresh = true;
            mRequestPageNum = 1;
            mIRvRetryListener.onRvRetryLoad();
        }
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

    /**
     * 设置点击空数据视图，可以重试加载的功能
     * @param enable true/false
     */
    public void setClickEmptyLoadEnable(boolean enable){
        mClickEmptyLoadEnable = enable;
    }

    /**
     * 设置点击重试，是否出现LoadingView
     * @param enable true/false
     */
    public void setRetryLoadViewEnable(boolean enable){
        mNeedRetryLoadView = enable;
    }

    /**
     * 设置重新加载的配置
     */
    public void setRefreshOpinion(){
        mRequestPageNum = 1;
        mIsRefresh = true;
    }
}
