package com.example.fansonlib.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fansonlib.R;
import com.example.fansonlib.image.ImageLoaderUtils;


/**
 * Created by：fanson
 * Created on：2016/12/17 17:58
 * Describe：自定义的RecyclerView
 */
public class AutoLoadRecyclerView extends RecyclerView implements IRvLoadFinishListener {

    private static final String TAG = AutoLoadRecyclerView.class.getSimpleName();
    private IRvLoadMoreListener loadMoreListener;
    private boolean isLoadingMore;
    private Context mContext;
    /**
     * 数据为空的时显示的View
     */
    private View emptyView;
    /**
     * 重试的视图
     */
    private View mRetryView;
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
        addOnScrollListener(new AutoLoadScrollListener( true, true));

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
            default:
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
//        addOnScrollListener(new AutoLoadScrollListener(ImageLoaderProxy.getImageLoader(), pauseOnScroll, pauseOnFling));
        addOnScrollListener(new AutoLoadScrollListener( pauseOnScroll, pauseOnFling));
    }

    public void setLoadMoreListener(IRvLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public void onRvLoadFinish( ) {
        isLoadingMore = false;
    }

    /**
     * 滑动自动加载监听器
     */
    private class AutoLoadScrollListener extends OnScrollListener {

//        private ImageLoader imageLoader;
        private final boolean pauseOnScroll;
        private final boolean pauseOnFling;

        public AutoLoadScrollListener( boolean pauseOnScroll, boolean pauseOnFling) {
            super();
            this.pauseOnScroll = pauseOnScroll;
            this.pauseOnFling = pauseOnFling;
//            this.imageLoader = imageLoader;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            //由于GridLayoutManager是LinearLayoutManager子类，所以也适用
            if (getLayoutManager() instanceof LinearLayoutManager) {
                int lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = AutoLoadRecyclerView.this.getAdapter().getItemCount();

                //有回调接口，并且不是加载状态，并且剩下2个item，并且向下滑动，则自动加载
                if (loadMoreListener != null && !isLoadingMore && lastVisibleItem >= totalItemCount - 2 && dy > 0) {
                    isLoadingMore = true;
                    loadMoreListener.onRvLoadMore(0);
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case SCROLL_STATE_IDLE:
                        ImageLoaderUtils.getInstance().onResumeRequest(mContext);
                        break;
                    case SCROLL_STATE_DRAGGING:
                        if (pauseOnScroll) {
                            ImageLoaderUtils.getInstance().onPauseRequest(mContext);
                        } else {
                            ImageLoaderUtils.getInstance().onResumeRequest(mContext);
                        }
                        break;
                    case SCROLL_STATE_SETTLING:
                        if (pauseOnFling) {
                            ImageLoaderUtils.getInstance().onPauseRequest(mContext);
                        } else {
                            ImageLoaderUtils.getInstance().onResumeRequest(mContext);
                        }
                        break;
                    default:
                        break;
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

    /**
     * 重写设置适配器
     *
     * @param adapter 适配器
     */
    @Override
    public void setAdapter(Adapter adapter) {
        Adapter oldAdapter = getAdapter();
        if (oldAdapter != null && emptyObserver != null) {
            oldAdapter.unregisterAdapterDataObserver(emptyObserver);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        emptyObserver.onChanged();
    }

    /**
     * 观察者模式，当设置适配器后，检测到EmptyView不为空，则显示EmptyView
     */
    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            Adapter adapter = getAdapter();
            if (adapter != null && emptyView != null) {
                if (adapter.getItemCount() == 0) {
                    //TODO EmptyView should be viewstub
                    emptyView.setVisibility(VISIBLE);
                    AutoLoadRecyclerView.this.setVisibility(GONE);
                } else {
                    emptyView.setVisibility(GONE);
                    AutoLoadRecyclerView.this.setVisibility(VISIBLE);
                }
            }
        }
    };

    /**
     * 设置空数据视图
     *
     * @param emptyView 传入空数据布局Layout；若传入为null则使用默认视图
     */
    public void setEmptyView(View emptyView) {
        if (emptyView == null) {
            emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_data, AutoLoadRecyclerView.this, false);
        }
        this.emptyView = emptyView;
        ((ViewGroup) this.getRootView()).addView(emptyView);
    }

    /**
     * 设置重试视图
     *
     * @param retryView 传入重试视图布局Layout；若传入为null则使用默认视图
     * @param listener  监听重试
     */
    public void setRetryView(View retryView, final IRetryListener listener) {
        if (mRetryView == null) {
            mRetryView = LayoutInflater.from(getContext()).inflate(R.layout.layout_retry, AutoLoadRecyclerView.this, false);
        }
        this.mRetryView = retryView;
        ((ViewGroup) this.getRootView()).addView(retryView);
        (mRetryView.findViewById(R.id.td_retry)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickRetry();
            }
        });
    }


}
