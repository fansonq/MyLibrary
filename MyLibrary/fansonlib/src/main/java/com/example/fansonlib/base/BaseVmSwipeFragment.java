package com.example.fansonlib.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.fansonlib.R;
import com.example.fansonlib.callback.LoadMoreListener;
import com.example.fansonlib.impl.CustomLoadMoreView;
import com.example.fansonlib.widget.loadingview.FadeScaleViewAnimProvider;
import com.example.fansonlib.widget.loadingview.LoadingStateView;
import com.example.fansonlib.widget.recyclerview.AutoLoadRecyclerView;

/**
 * @author Created by：Fanson
 * Created Time: 2019/4/2 10:45
 * Describe：带下拉刷新的BaseMvpFragment（Id：swipeRefresh）
 */
public abstract class BaseVmSwipeFragment <VM extends BaseViewModel, D extends ViewDataBinding, A extends BaseQuickAdapter> extends BaseVmFragment<VM, D> implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener {

    private static final String TAG = BaseVmSwipeFragment.class.getSimpleName();

    protected A mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    protected AutoLoadRecyclerView mRecyclerView;
    protected LoadingStateView mLoadingStateView;
    /**
     * 标识是否上拉刷新，默认否
     */
    protected boolean mIsPull = false;
    /**
     * 请求数据的页码
     */
    protected int mRequestPageNum = 1;
    /**
     * 默认页码第一页
     */
    protected static final int DEFAULT_PAGE_NUM = 1;
    private static final int PAGE_SIZE = 10;

    @Override
    protected View initView(View view, LayoutInflater inflater, @Nullable ViewGroup container, Bundle bundle) {
        super.initView(view, inflater, container, bundle);
        initSwipeRefresh();
        return rootView;
    }

    @Override
    protected void listenEvent() {
        super.listenEvent();
        initLoadingStateView();
    }

    /**
     * 初始化SwipeRefresh，注意：继承者的布局id必须为“swipeRefresh”
     */
    protected void initSwipeRefresh() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * 初始化RecyclerView，注意：继承者的布局id必须为“recyclerView”
     */
    protected void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(hostActivity));
        mRecyclerView.setOnPauseListenerParams(true, true);
        mAdapter = initAdapter();
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.isFirstOnly(false);
        mAdapter.setDuration(500);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化加载视图的点击事件
     */
    private void initLoadingStateView(){
        mLoadingStateView = findMyViewId(R.id.loadingStateView);
        mLoadingStateView.setViewSwitchAnimProvider(new FadeScaleViewAnimProvider());
        //错误，重试
        mLoadingStateView.setErrorAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingStateView.showLoadingView();
                retryLoadData();
            }
        });
        //空数据，重试
        mLoadingStateView.setNoDataAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingStateView.showLoadingView();
                retryLoadData();
            }
        });
    }

    /**
     * 停止下拉刷新的动画
     */
    public void stopRefresh() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            mIsPull = false;
        }
    }

    /**
     * 网络加载数据成功后调用，处理适配器的逻辑（页码添加；显示空数据界面；下拉刷新逻辑；）
     */
    protected void handleAdapterData(int dataSize) {
        if (mRequestPageNum == 1 && dataSize == 0) {
            mAdapter.setNewData(null);
            showNoDataLayout();
            stopRefresh();
            return;
        }
        if (dataSize > 0) {
            hideNoDataLayout();
            mRecyclerView.loadFinish(null);
            setDataToAdapter(mIsPull);
            mAdapter.loadMoreComplete();
            if (dataSize < PAGE_SIZE) {
                mAdapter.loadMoreEnd();
            }
        } else {
            mAdapter.loadMoreEnd();
        }
        stopRefresh();
        //请求成功，页码+1
        ++mRequestPageNum;
    }

    @Override
    public void onRefresh() {
        setRefreshOpinion();
        pullRefresh();
    }

    /**
     * 从头刷新的配置
     */
    public void setRefreshOpinion() {
        mIsPull = true;
        mRequestPageNum = 1;
        mSwipeRefreshLayout.setRefreshing(true);
    }

    /**
     * 初始化适配器
     * @return 适配器
     */
    protected abstract A initAdapter();

    /**
     * 装载数据到适配器
     * @param isRefresh 是否触发下拉刷新
     */
    protected abstract void setDataToAdapter(boolean isRefresh);

    /**
     * 重试加载数据
     */
    protected abstract void retryLoadData();

    /**
     * 下拉刷新获取数据
     */
    protected abstract void pullRefresh();

    /**
     * 上拉加载更多数据
     *
     * @param requestPageNum 请求数据的页码
     */
    protected abstract void scrollLoadMoreData(int requestPageNum);

    @Override
    public void loadMore() {
        if (mRequestPageNum>1){
            scrollLoadMoreData(mRequestPageNum);
        }
    }

    /**
     * 显示无数据界面
     */
    public abstract void showNoDataLayout() ;

    /**
     * 隐藏无数据界面
     */
    public abstract void hideNoDataLayout();
}

