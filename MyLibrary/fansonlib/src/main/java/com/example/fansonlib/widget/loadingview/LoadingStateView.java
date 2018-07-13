package com.example.fansonlib.widget.loadingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import com.example.fansonlib.widget.loading.MyProgressWheel;

/**
 * @author Created by：Fanson
 * Created Time: 2018/7/13 10:45
 * Describe：加载页面（加载中，加载失败，无数据）
 */
public class LoadingStateView extends FrameLayout {
    private View contentView;

    //加载数据的界面控件
    private View mEmptyView;
    private View mEmptyContentView;
    private TextView mTvEmpty;
    private ImageView mIvEmpty;

    //加载出错的界面控件
    private View mErrorView;
    private View mErrorContentView;
    private TextView mTvError;
    private ImageView mIvError;

    //加载中的界面控件
    private View mProgressView;
    private View mProgressContentView;
    private TextView mTvProgress;
    private MyProgressWheel mProgressWheel;

    //当前显示的View
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

        mEmptyView.setVisibility(View.GONE);

        mErrorView.setVisibility(View.GONE);

        mProgressView.setVisibility(View.GONE);

        mCurrentShowingView = contentView;
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LoadingStateView, 0, 0);
        int progressViewId;
        Drawable errorDrawable;
        Drawable emptyDrawable;
        int textSize;
        int textColor;
        int drawableColor;
        try {
            errorDrawable = a.getDrawable(R.styleable.LoadingStateView_errorDrawable);
            emptyDrawable = a.getDrawable(R.styleable.LoadingStateView_emptyDrawable);
            progressViewId = a.getResourceId(R.styleable.LoadingStateView_progressView, -1);
            textSize = a.getDimensionPixelSize(R.styleable.LoadingStateView_tipTextSize,16);
            textColor = a.getColor(R.styleable.LoadingStateView_tipTextColor, Color.GRAY);
            drawableColor = a.getColor(R.styleable.LoadingStateView_drawableColor,0);
        } finally {
            a.recycle();
        }

        /**************************************----初始化加载中界面的控件------****************************************************/
        if (progressViewId != -1) {
            mProgressView = inflater.inflate(progressViewId, this, false);
        } else {
            mProgressView = inflater.inflate(R.layout.layout_loading_progress, this, false);
            mProgressWheel = (MyProgressWheel) mProgressView.findViewById(R.id.progressWheel);
            mProgressWheel.setBarColor(drawableColor);
            mTvProgress = (TextView) mProgressView.findViewById(R.id.tv_progress);
            mTvProgress.setTextSize(textSize);
            mTvProgress.setTextColor(textColor);
            mProgressContentView = mProgressView.findViewById(R.id.progress_content);
        }

        addView(mProgressView);
        /******************************************************************************************/


        /***************************************----初始化加载出错界面的控件------***************************************************/
        mErrorView = inflater.inflate(R.layout.layout_loading_error, this, false);
        mErrorContentView = mErrorView.findViewById(R.id.error_content);
        mTvError = (TextView) mErrorView.findViewById(R.id.tv_error);
        mTvError.setTextSize(textSize);
        mTvError.setTextColor(textColor);
        mIvError = (ImageView) mErrorView.findViewById(R.id.iv_error);
        if (errorDrawable != null) {
            mIvError.setImageDrawable(errorDrawable);
        } else {
            mIvError.setImageResource(R.mipmap.ic_loading_error);
        }
        changeImageViewColor(mIvError,drawableColor);
        addView(mErrorView);
        /******************************************************************************************/


        /***************************************----初始化加载后数据为空界面的控件------**************************************************/
        mEmptyView = inflater.inflate(R.layout.layout_loading_empty, this, false);
        mEmptyContentView = mEmptyView.findViewById(R.id.empty_content);
        mTvEmpty = (TextView) mEmptyView.findViewById(R.id.tv_empty);
        mTvEmpty.setTextSize(textSize);
        mTvEmpty.setTextColor(textColor);
        mIvEmpty = (ImageView) mEmptyView.findViewById(R.id.iv_no_data);
        if (emptyDrawable != null) {
            mIvEmpty.setImageDrawable(emptyDrawable);
        } else {
            mIvEmpty.setImageResource(R.mipmap.ic_no_data);
        }
        changeImageViewColor(mIvEmpty,drawableColor);
        addView(mEmptyView);
        /******************************************************************************************/

    }

    private void checkIsContentView(View view) {
        if (contentView == null && view != mErrorView && view != mProgressView && view != mEmptyView) {
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
    public ImageView getEmptyImageView() {
        return mIvEmpty;
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
        if (toBeHided == toBeShown)
            return;
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
                } else
                    toBeHided.setVisibility(GONE);
            }
            if (toBeShown != null) {
                if (toBeShown.getVisibility() != VISIBLE)
                    toBeShown.setVisibility(VISIBLE);
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
    public void setEmptyContentViewMargin(int left, int top, int right, int bottom) {
        ((LinearLayout.LayoutParams) mIvEmpty.getLayoutParams()).setMargins(left, top, right, bottom);
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
        if (mProgressWheel != null)
            ((LinearLayout.LayoutParams) mProgressWheel.getLayoutParams()).setMargins(left, top, right, bottom);
    }

    public void setInfoContentViewMargin(int left, int top, int right, int bottom) {
        setEmptyContentViewMargin(left, top, right, bottom);
        setErrorContentViewMargin(left, top, right, bottom);
        setProgressContentViewMargin(left, top, right, bottom);
    }


    public void showContentView() {
        switchWithAnimation(contentView);
    }

    /**
     * 显示数据为空的界面
     */
    public void showLoadEmptyView() {
        showLoadEmptyView(null);
    }

    /**
     * 显示数据为空的界面
     *
     * @param msg 提示语
     */
    public void showLoadEmptyView(String msg) {
        onHideContentView();
        if (!TextUtils.isEmpty(msg)){
            mTvEmpty.setText(msg);
        }
        switchWithAnimation(mEmptyView);
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
        onHideContentView();
        if (msg != null){
            mTvError.setText(msg);
        }
        switchWithAnimation(mErrorView);
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
        onHideContentView();
        if (msg != null){
            mTvProgress.setText(msg);
        }
        switchWithAnimation(mProgressView);
    }

    /**
     * 设置点击“加载出错界面”的监听
     *
     * @param onErrorButtonClickListener
     */
    public void setErrorAction(final View.OnClickListener onErrorButtonClickListener) {
        mErrorView.setOnClickListener(onErrorButtonClickListener);
    }

    /**
     * 设置点击“加载后数据为空”的监听
     *
     * @param onEmptyButtonClickListener
     */
    public void setEmptyAction(final View.OnClickListener onEmptyButtonClickListener) {
        mEmptyView.setOnClickListener(onEmptyButtonClickListener);
    }

    /**
     * 设置点击“加载后数据为空”和“加载出错界面”的监听
     *
     * @param errorAndEmptyAction
     */
    public void setErrorAndEmptyAction(final View.OnClickListener errorAndEmptyAction) {
        mProgressView.setOnClickListener(errorAndEmptyAction);
        mEmptyView.setOnClickListener(errorAndEmptyAction);
    }

    protected void onHideContentView() {
        //Override me
    }

    /**
     * 更改图片资源的颜色
     * @param imageView
     * @param color 颜色值
     */
    private void changeImageViewColor(ImageView imageView,int color){
        Drawable drawable = imageView.getDrawable();
        DrawableCompat.setTint(drawable,ContextCompat.getColor(getContext(),color));
        imageView.setImageDrawable(drawable);
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

