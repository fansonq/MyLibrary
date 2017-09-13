package com.example.fansonlib.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.example.fansonlib.callback.LoadFinishCallBack;
import com.example.fansonlib.callback.LoadMoreListener;
import com.example.fansonlib.utils.ImageLoaderProxy;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by：fanson
 * Created on：2016/12/17 17:58
 * Describe：
 */
public class AutoLoadRecyclerView extends RecyclerView implements LoadFinishCallBack {

    private static final String TAG = AutoLoadRecyclerView.class.getSimpleName();
    private LoadMoreListener loadMoreListener;
    private boolean isLoadingMore;
    private Context mContext;
    private boolean move = false;
    private int mIndex = 0;
    private LayoutManager mLayoutManager;
    //类型、方向、列数
    private int type = TYPE_GRID;
    private int orientation = ORIENTATION_VERTICAL;
    private int column = 1;
    //类型
    public static final int TYPE_GRID = 0;
    //方向
    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;

    public AutoLoadRecyclerView(Context context) {
        this(context, null);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        isLoadingMore = false;
        mContext = context;
        addOnScrollListener(new AutoLoadScrollListener(null, true, true));

        init();
    }

    /**
     * 根据属性初始化RecyclerView
     */
    private void init() {
        // RecyclerView的类型和方向
        switch (orientation) {
            case ORIENTATION_VERTICAL:
                mLayoutManager = new GridLayoutManager(mContext, column);
                break;
            case ORIENTATION_HORIZONTAL:
                mLayoutManager = new GridLayoutManager(mContext, column, GridLayoutManager.HORIZONTAL, false);
                break;
        }
    }

    /**
     * 如果需要显示图片，需要设置这几个参数，快速滑动时，暂停图片加载
     *
     * @param pauseOnScroll
     * @param pauseOnFling
     */
    public void setOnPauseListenerParams(boolean pauseOnScroll, boolean pauseOnFling) {
        addOnScrollListener(new AutoLoadScrollListener(ImageLoaderProxy.getImageLoader(), pauseOnScroll, pauseOnFling));
//        addOnScrollListener(new AutoLoadScrollListener(MyGlideUtils.getInstance(), pauseOnScroll, pauseOnFling));
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public void loadFinish(Object obj) {
        isLoadingMore = false;
    }

    /**
     * 滑动自动加载监听器
     */
    private class AutoLoadScrollListener extends OnScrollListener {

        private ImageLoader imageLoader;
        private final boolean pauseOnScroll;
        private final boolean pauseOnFling;

        public AutoLoadScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
            super();
            this.pauseOnScroll = pauseOnScroll;
            this.pauseOnFling = pauseOnFling;
            this.imageLoader = imageLoader;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            //由于GridLayoutManager是LinearLayoutManager子类，所以也适用
            if (getLayoutManager() instanceof LinearLayoutManager) {
                int lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = AutoLoadRecyclerView.this.getAdapter().getItemCount();

                //有回调接口，并且不是加载状态，并且剩下2个item，并且向下滑动，则自动加载
                if (loadMoreListener != null && !isLoadingMore && lastVisibleItem >= totalItemCount -
                        2 && dy > 0) {
                    isLoadingMore = true;
                    loadMoreListener.loadMore();
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            if (imageLoader != null) {
                switch (newState) {
                    case SCROLL_STATE_IDLE:
//                        imageLoader.resumeRequests(mContext);
                        imageLoader.resume();
                        break;
                    case SCROLL_STATE_DRAGGING:
                        if (pauseOnScroll) {
//                            imageLoader.pauseRequests(mContext);
                            imageLoader.pause();
                        } else {
//                            imageLoader.resumeRequests(mContext);
                            imageLoader.resume();
                        }
                        break;
                    case SCROLL_STATE_SETTLING:
                        if (pauseOnFling) {
//                            imageLoader.pauseRequests(mContext);
                            imageLoader.pause();
                        } else {
//                            imageLoader.resumeRequests(mContext);
                            imageLoader.resume();
                        }
                        break;
                }
            }
        }
    }

    /**
     * 滚动到指定位置（注意：对瀑布流无效果）
     */
    public void moveToPosition(int position) {
        if (position < 0 || position >= getAdapter().getItemCount()) {
            Log.e(TAG, "滚动的指定位置超出范围了");
            return;
        }
        mIndex = position;
        stopScroll();

        GridLayoutManager glm = (GridLayoutManager) mLayoutManager;
        int firstItem = glm.findFirstVisibleItemPosition();
        int lastItem = glm.findLastVisibleItemPosition();
        if (position <= firstItem) {
            this.scrollToPosition(position);
        } else if (position <= lastItem) {
            int top = this.getChildAt(position - firstItem).getTop();
            this.scrollBy(0, top);
        } else {
            this.scrollToPosition(position);
            move = true;
        }
    }

    /**
     * 平滑滚动到指定位置（注意：对瀑布流无效果）
     */
    public void smoothMoveToPosition(int position) {
        if (position < 0 || position >= getAdapter().getItemCount()) {
            Log.e(TAG, "滚动的指定位置超出范围了");
            return;
        }
        mIndex = position;
        stopScroll();
        GridLayoutManager glm = (GridLayoutManager) mLayoutManager;
        int firstItem = glm.findFirstVisibleItemPosition();
        int lastItem = glm.findLastVisibleItemPosition();
        if (position <= firstItem) {
            this.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            int top = this.getChildAt(position - firstItem).getTop();
            this.smoothScrollBy(0, top);
        } else {
            this.smoothScrollToPosition(position);
            move = true;
        }
    }




}
