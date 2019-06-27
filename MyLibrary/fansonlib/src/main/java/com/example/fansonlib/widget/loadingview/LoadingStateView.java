package com.example.fansonlib.widget.loadingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fansonlib.R;
import com.example.fansonlib.utils.DimensUtils;
import com.example.fansonlib.widget.loading.MyProgressWheel;

/**
 * @author Created by：Fanson
 *         Created Time: 2018/7/13 10:45
 *         Describe：加载页面（加载中，加载失败，无数据）
 */
public class LoadingStateView extends FrameLayout {

    private View contentView;
    private Context mContext;
    private int mProgressViewId;
    private TextView mTvReload;
    private int mTextColor;
    private int mTextSize;
    private int mDrawableColor = 0;
    private LayoutInflater mInflater;
    private Drawable mErrorDrawable;
    private Drawable mNoDataDrawable;
    private View.OnClickListener mOnErrorButtonClickListener = null;
    private View.OnClickListener mOnNoDataButtonClickListener = null;

    //加载数据为空的界面控件
    private View mNoDataView = null;
    private TextView mTvNoData;
    private ImageView mIvNoData;

    //加载出错的界面控件
    private View mErrorView = null;
    private TextView mTvError;
    private ImageView mIvError;

    //加载中的界面控件
    private View mLoadingView = null;
    private TextView mTvProgress;
    private MyProgressWheel mProgressWheel;

    /**
     * 当前显示的View
     */
    private View mCurrentShowingView;

    private boolean mShouldPlayAnim = true;
    private Animation mHideAnimation;
    private Animation mShowAnimation;


    public LoadingStateView(Context context) {
        this(context, null);
    }


    public LoadingStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoadingStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        parseAttrs(context, attrs);
//        mNoDataView.setVisibility(View.GONE);
//        mErrorView.setVisibility(View.GONE);
//        mProgressView.setVisibility(View.GONE);
        mCurrentShowingView = contentView;

        setViewSwitchAnimProvider(new FadeScaleViewAnimProvider());
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LoadingStateView, 0, 0);
        try {
            mErrorDrawable = a.getDrawable(R.styleable.LoadingStateView_errorDrawable);
            mNoDataDrawable = a.getDrawable(R.styleable.LoadingStateView_emptyDrawable);
            mProgressViewId = a.getResourceId(R.styleable.LoadingStateView_progressView, -1);
            mTextSize = DimensUtils.pxToSp(mContext, a.getDimension(R.styleable.LoadingStateView_tipTextSize, 14));
            mTextColor = a.getColor(R.styleable.LoadingStateView_tipTextColor, Color.GRAY);
            mDrawableColor = a.getColor(R.styleable.LoadingStateView_drawableColor, 0);
        } finally {
            a.recycle();
        }
    }

    /**
     * 初始化加载中的View
     */
    private void initLoadingView() {
        if (mProgressViewId != -1) {
            mLoadingView = mInflater.inflate(mProgressViewId, this, false);
        } else {
            mLoadingView = mInflater.inflate(R.layout.layout_loading_progress, this, false);
            mProgressWheel = (MyProgressWheel) mLoadingView.findViewById(R.id.progressWheel);
            mProgressWheel.setBarColor(mDrawableColor);
            mTvProgress = (TextView) mLoadingView.findViewById(R.id.tv_progress);
            mTvProgress.setTextSize(mTextSize);
            mTvProgress.setTextColor(mTextColor);
            addView(mLoadingView);
        }
    }

    /**
     * 初始化加载出错的View
     */
    private void initErrorView() {
        if (mErrorView == null) {
            mErrorView = mInflater.inflate(R.layout.layout_loading_error, this, false);
            mTvError = (TextView) mErrorView.findViewById(R.id.tv_error);
            mTvReload = mErrorView.findViewById(R.id.tv_reload);
            mTvError.setTextSize(mTextSize);
            mTvError.setTextColor(mTextColor);
            mTvReload.setTextColor(mTextColor);
            mIvError = (ImageView) mErrorView.findViewById(R.id.iv_error);
            if (mErrorDrawable != null) {
                mIvError.setImageDrawable(mErrorDrawable);
            } else {
                mIvError.setImageResource(R.mipmap.ic_loading_error);
            }
            if (mDrawableColor != 0) {
                mIvError.setColorFilter(mDrawableColor);
            }
            addView(mErrorView);
        }
    }

    /**
     * 初始化加载数据为空的View
     */
    private void initNoDataView() {
        if (mNoDataView == null) {
            mNoDataView = mInflater.inflate(R.layout.layout_loading_no_data, this, false);
            mTvNoData = (TextView) mNoDataView.findViewById(R.id.tv_empty);
            mTvNoData.setTextSize(mTextSize);
            mTvNoData.setTextColor(mTextColor);
            mIvNoData = (ImageView) mNoDataView.findViewById(R.id.iv_no_data);
            if (mNoDataDrawable != null) {
                mIvNoData.setImageDrawable(mNoDataDrawable);
            } else {
                mIvNoData.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.ic_no_data));
            }
            if (mDrawableColor != 0) {
                mIvNoData.setColorFilter(mDrawableColor);
            }
            addView(mNoDataView);
        }
    }

    private void checkIsContentView(View view) {
        if (contentView == null && view != mErrorView && view != mLoadingView && view != mNoDataView) {
            contentView = view;
            mCurrentShowingView = contentView;
        }
    }

    /**
     * 获取加载出错的图片资源
     *
     * @return ImageView
     */
    public ImageView getErrorImageView() {
        return mIvError;
    }

    /**
     * 获取加载后数据为空的图片资源
     *
     * @return ImageView
     */
    public ImageView getNoDataImageView() {
        return mIvNoData;
    }

    /**
     * 设置界面切换的动画
     *
     * @param viewSwitchAnimProvider
     */
    public void setViewSwitchAnimProvider(ViewAnimProvider viewSwitchAnimProvider) {
        if (viewSwitchAnimProvider != null) {
            this.mShowAnimation = viewSwitchAnimProvider.showAnimation();
            this.mHideAnimation = viewSwitchAnimProvider.hideAnimation();
        }
    }

    /**
     * 是否播放动画
     *
     * @return true or false
     */
    public boolean isShouldPlayAnim() {
        return mShouldPlayAnim;
    }

    /**
     * 设置是否播放动画
     */
    public void setShouldPlayAnim(boolean shouldPlayAnim) {
        this.mShouldPlayAnim = shouldPlayAnim;
    }

    /**
     * 获取显示界面的动画
     *
     * @return Animation
     */
    public Animation getShowAnimation() {
        return mShowAnimation;
    }

    /**
     * 设置显示界面时的动画
     *
     * @param showAnimation
     */
    public void setShowAnimation(Animation showAnimation) {
        this.mShowAnimation = showAnimation;
    }

    /**
     * 获取隐藏界面的动画
     *
     * @return Animation
     */
    public Animation getHideAnimation() {
        return mHideAnimation;
    }

    /**
     * 设置隐藏界面时的动画
     *
     * @param hideAnimation
     */
    public void setHideAnimation(Animation hideAnimation) {
        this.mHideAnimation = hideAnimation;
    }

    private void switchWithAnimation(final View toBeShown) {
        final View toBeHided = mCurrentShowingView;
        if (toBeHided == toBeShown) {
            return;
        }
        if (mShouldPlayAnim) {
            if (toBeHided != null) {
                if (mHideAnimation != null) {
                    mHideAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            toBeHided.setVisibility(GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    mHideAnimation.setFillAfter(false);
                    toBeHided.startAnimation(mHideAnimation);
                } else {
                    toBeHided.setVisibility(GONE);
                }
            }
            if (toBeShown != null) {
                if (toBeShown.getVisibility() != VISIBLE) {
                    toBeShown.setVisibility(VISIBLE);
                }
                mCurrentShowingView = toBeShown;
                if (mShowAnimation != null) {
                    mShowAnimation.setFillAfter(false);
                    toBeShown.startAnimation(mShowAnimation);
                }
            }
        } else {
            if (toBeHided != null) {
                toBeHided.setVisibility(GONE);
            }
            if (toBeShown != null) {
                mCurrentShowingView = toBeShown;
                toBeShown.setVisibility(VISIBLE);
            }
        }

    }

    /**
     * 设置数据为空的界面的间隔
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setNoDataContentViewMargin(int left, int top, int right, int bottom) {
        ((LinearLayout.LayoutParams) mIvNoData.getLayoutParams()).setMargins(left, top, right, bottom);
    }

    /**
     * 设置加载出错的界面的间隔
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setErrorContentViewMargin(int left, int top, int right, int bottom) {
        ((LinearLayout.LayoutParams) mIvError.getLayoutParams()).setMargins(left, top, right, bottom);
    }

    /**
     * 设置加载中的界面的间隔
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setProgressContentViewMargin(int left, int top, int right, int bottom) {
        if (mProgressWheel != null) {
            ((LinearLayout.LayoutParams) mProgressWheel.getLayoutParams()).setMargins(left, top, right, bottom);
        }
    }

    public void setInfoContentViewMargin(int left, int top, int right, int bottom) {
        setNoDataContentViewMargin(left, top, right, bottom);
        setErrorContentViewMargin(left, top, right, bottom);
        setProgressContentViewMargin(left, top, right, bottom);
    }


    public void showContentView() {
        switchWithAnimation(contentView);
    }

    /**
     * 显示数据为空的界面
     */
    public void showLoadNoDataView() {
        showLoadNoDataView(null);
    }

    /**
     * 显示数据为空的界面
     *
     * @param msg 提示语
     */
    public void showLoadNoDataView(String msg) {
        onHideOtherView();
        initNoDataView();
        if (mOnNoDataButtonClickListener != null) {
            mNoDataView.setOnClickListener(mOnNoDataButtonClickListener);
        }
        if (!TextUtils.isEmpty(msg)) {
            mTvNoData.setText(msg);
        }
        switchWithAnimation(mNoDataView);
        mCurrentShowingView = mNoDataView;
    }

    /**
     * 显示加载出错的界面
     */
    public void showLoadErrorView() {
        showLoadErrorView(null);
    }

    /**
     * 显示加载出错的界面
     *
     * @param msg 提示语
     */
    public void showLoadErrorView(String msg) {
        onHideOtherView();
        initErrorView();
        if (mOnErrorButtonClickListener != null) {
            mErrorView.setOnClickListener(mOnErrorButtonClickListener);
        }
        if (msg != null) {
            mTvError.setText(msg);
        }
        switchWithAnimation(mErrorView);
        mCurrentShowingView = mErrorView;
    }

    /**
     * 显示加载中的界面
     */
    public void showLoadingView() {
        showLoadingView(null);
    }

    /**
     * 显示加载中的界面
     *
     * @param msg 提示语
     */
    public void showLoadingView(String msg) {
        onHideOtherView();
        initLoadingView();
        if (msg != null) {
            mTvProgress.setText(msg);
        }
        switchWithAnimation(mLoadingView);
        mCurrentShowingView = mLoadingView;
    }

    /**
     * 隐藏错误的界面
     */
    public void hideErrorView() {
        if (mErrorView != null) {
            mErrorView.setVisibility(GONE);
            mCurrentShowingView = contentView;
        }
    }

    /**
     * 隐藏暂无数据的界面
     */
    public void hideNoDataView() {
        if (mNoDataView != null) {
            mNoDataView.setVisibility(GONE);
            mCurrentShowingView = contentView;
        }
    }

    /**
     * 隐藏加载中的界面
     */
    public void hideLoadingView() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(GONE);
            mCurrentShowingView = contentView;
        }
    }

    /**
     * 设置点击“加载出错界面”的监听
     *
     * @param onErrorButtonClickListener
     */
    public void setErrorAction(final View.OnClickListener onErrorButtonClickListener) {
        if (mErrorView == null) {
            mOnErrorButtonClickListener = onErrorButtonClickListener;
        } else {
            mErrorView.setOnClickListener(onErrorButtonClickListener);
        }
    }

    /**
     * 设置点击“加载后数据为空”的监听
     *
     * @param onNoDataButtonClickListener
     */
    public void setNoDataAction(final View.OnClickListener onNoDataButtonClickListener) {
        if (mNoDataView == null) {
            mOnNoDataButtonClickListener = onNoDataButtonClickListener;
        } else {
            mNoDataView.setOnClickListener(onNoDataButtonClickListener);
        }
    }

    /**
     * 设置点击“加载后数据为空”和“加载出错界面”的监听
     *
     * @param errorAndNoDataAction
     */
    public void setErrorAndNoDataAction(final View.OnClickListener errorAndNoDataAction) {
        mLoadingView.setOnClickListener(errorAndNoDataAction);
        mNoDataView.setOnClickListener(errorAndNoDataAction);
    }

    /**
     * 隐藏其他正在显示的View
     */
    public void onHideOtherView() {
        if (mLoadingView != null && mLoadingView.getVisibility() == VISIBLE) {
            hideLoadingView();
            return;
        }
        if (mNoDataView != null && mNoDataView.getVisibility() == VISIBLE) {
            hideNoDataView();
            return;
        }
        if (mErrorView != null && mErrorView.getVisibility() == VISIBLE) {
            hideNoDataView();
        }
    }

    /**
     * addView
     */
    @Override
    public void addView(View child) {
        checkIsContentView(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        checkIsContentView(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        checkIsContentView(child);
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        checkIsContentView(child);
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }
}

