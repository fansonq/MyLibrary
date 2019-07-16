package com.example.fansonlib.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.fansonlib.image.ImageLoaderUtils;

/**
 * @author Created by：Fanson
 * Created Time: 2019/5/24 14:32
 * Describe：我的自定义RecyclerView的滑动监听
 */
public class MyRvScrollListener extends RecyclerView.OnScrollListener {

    private Context mContext;
    /**
     * 记录：是否正在加载中
     */
    private boolean mIsLoadingMore = false;
    /**
     * 加载更多的监听接口
     */
    private BaseQuickAdapter.RequestLoadMoreListener mLoadMoreListener;

    public MyRvScrollListener(Context context){
        mContext = context;
    }

    /**
     * 关闭加载中的状态
     */
    public void closeLoadingMore(){
        mIsLoadingMore = false;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
            //空闲状态
            case RecyclerView.SCROLL_STATE_IDLE:
                ImageLoaderUtils.getInstance().onResumeRequest(mContext);
                break;
            //屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；
            case RecyclerView.SCROLL_STATE_DRAGGING:
                ImageLoaderUtils.getInstance().onResumeRequest(mContext);
                break;
            //随用户的操作，屏幕上产生的惯性滑动；
            case RecyclerView.SCROLL_STATE_SETTLING:
                ImageLoaderUtils.getInstance().onPauseRequest(mContext);
                break;
            default:
                break;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //由于GridLayoutManager是LinearLayoutManager子类，所以也适用
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            int lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            int totalItemCount = recyclerView.getAdapter().getItemCount();
            //有回调接口，并且不是加载状态，并且剩下2个item，并且向下滑动，则自动加载
            if (mLoadMoreListener != null && !mIsLoadingMore && lastVisibleItem >= totalItemCount - 2 && dy > 0) {
                mIsLoadingMore = true;
                mLoadMoreListener.onLoadMoreRequested();
            }
        }
    }
}
