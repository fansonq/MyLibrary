package com.example.fansonlib.widget.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.fansonlib.R;
import com.example.fansonlib.base.AppUtils;

/**
 * Created by：fanson
 * Created on：2016/12/17 10:34
 * Describe：LoadingView
 */
public class MyLoadingView extends RelativeLayout implements View.OnClickListener {

    /**
     * 默认提示TextView
     */
    private String mWarnText;
    /**
     * 默认加载中TextView
     */
    private String mLoadingText;

    /**
     * 重试的Button
     */
    private Button mBtnRetry;
    /**
     * 需要绑定的View
     */
    private View mBindView;

    private View currentProgressView;

    /**
     * 是否设置自定义加载view
     */
    private boolean hasCustomLoadingView = false;

    private View mCustomLoadingView;
    private Context mContext;
    private OnRetryClickListener onRetryListener;

    private ProgressAlertDialog mProgressDialog;

    //LoadingView的模式
    private int mLoadingModel = MODEL_DEFAULT;
    //默认模式
    public static final int MODEL_DEFAULT = 1;
    //弹出框模式
    public static final int MODEL_ALERT = 2;

    public MyLoadingView(Context context) {
        super(context);
    }

    public MyLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyLoadingView, 0, 0);
        mWarnText = typedArray.getString(R.styleable.MyLoadingView_empty_warn_txt);
        mLoadingText = typedArray.getString(R.styleable.MyLoadingView_empty_loading_txt);
        typedArray.recycle();

        if (TextUtils.isEmpty(mWarnText)) {
            mWarnText = AppUtils.getAppContext().getString(R.string.no_data);
        }

        if (TextUtils.isEmpty(mLoadingText)) {
            mLoadingText = AppUtils.getAppContext().getString(R.string.loading);
        }

        setVisibility(GONE);
    }

    /**
     * 加载中
     */
    public void loading() {
        if (mBindView != null) {
            mBindView.setVisibility(GONE);
        }
        hideRetryBtn();
        switch (mLoadingModel) {
            case MODEL_DEFAULT:
                setVisibility(VISIBLE);
                createProgressView();
                break;
            case MODEL_ALERT:
                setVisibility(GONE);
                createDialogProgress();
                mProgressDialog.show();
                break;
        }
    }

    /**
     * 创建默认的进度框
     */
    private void createProgressView() {
        currentProgressView = LayoutInflater.from(mContext).inflate(R.layout.progressbar, null);
        currentProgressView.setVisibility(VISIBLE);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.addRule(RelativeLayout.ABOVE, R.id.id_empty_btn_view);
        addView(currentProgressView, params);
    }

    /**
     * 创建Dialog形式的进度框
     */
    private void createDialogProgress() {
        mProgressDialog = new ProgressAlertDialog(getContext());
        mProgressDialog.setCancelable(true);
    }

    /**
     * 创建重试的视图
     */
    private void createRetryView() {
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(R.mipmap.ic_retry, null);
        } else {
            drawable = getResources().getDrawable(R.mipmap.ic_retry);
        }
        drawable.setBounds(0, 0, 100, 100);
        mBtnRetry = new Button(getContext());
        mBtnRetry.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
        mBtnRetry.setText(AppUtils.getAppContext().getString(R.string.retry_loading));
        mBtnRetry.setTextSize(15);
        mBtnRetry.setTextColor(ContextCompat.getColor(mContext, R.color.grey_dark));
        mBtnRetry.setCompoundDrawables(drawable, null, null, null);
        mBtnRetry.setCompoundDrawablePadding(20);
        mBtnRetry.setGravity(Gravity.CENTER);
        LayoutParams mTipLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTipLp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mBtnRetry.setId(R.id.id_empty_tv_view);
        mBtnRetry.setOnClickListener(this);
        addView(mBtnRetry, mTipLp);
    }

    /**
     * 加载成功（消失加载框）
     */
    public void success() {
        if (mBindView != null) {
            mBindView.setVisibility(VISIBLE);
        }
        if (mLoadingModel == MODEL_ALERT) {
            mProgressDialog.cancel(); //如果你在创建AlertDialog的时候调用了setOnCancelListener 这个mCancelMessage变量有作用，否则dismiss和cancel等同
        }
        setVisibility(GONE);
    }

    /**
     * 加载失败,并显示重新加载的图标
     *
     * @param msg 加载失败提示语
     */
    public void failRetry(String msg) {
        createRetryView();
        if (TextUtils.isEmpty(msg)) {
            mWarnText = AppUtils.getAppContext().getString(R.string.retry_loading);
        } else {
            mWarnText = msg;
        }

        if (mLoadingModel == MODEL_ALERT) {
            mProgressDialog.cancel();
        }

        if (mBindView != null) {
            mBindView.setVisibility(GONE);
        }
        hideCustomView();
        hideProgress();
        setVisibility(VISIBLE);
        showRetryBtn(); //显示Retry图标
        if (!hasCustomLoadingView) {
            mBtnRetry.setText(mWarnText);
        } else {
            hideCustomView();
        }
    }

    /**
     * 设置绑定view
     *
     * @param view
     */
    public void bindView(View view) {
        this.mBindView = view;
    }

    /**
     * 设置自定义加载view
     *
     * @param view
     */
    public void setCustomLoadingView(View view) {
        if (view != null) {
            mCustomLoadingView = view;
            hasCustomLoadingView = true;
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.addRule(RelativeLayout.ABOVE, R.id.id_empty_btn_view);
            addView(view, params);
            invalidate();
        }
    }

    /**
     * 注册监听接口（点击重载）
     *
     * @param listener
     */
    public void setOnBtnClickListener(OnRetryClickListener listener) {
        this.onRetryListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (onRetryListener != null) {
            onRetryListener.onRetry();
        } else {
            throw new IllegalArgumentException("must be set click");
        }
    }

    /**
     * 设置弹出框模式加载中文字
     *
     * @param loadingText
     */
    public void setLoadingText(String loadingText) {
        if (!TextUtils.isEmpty(loadingText)) {
            if (mProgressDialog != null) {
                mProgressDialog.setLoadingText(loadingText);
            }
        }
    }

    /**
     * 设置弹出框模式加载中文字颜色
     *
     * @param colorId 颜色资源ID
     */
    public void setLoadingTextColor(int colorId) {
        if (mProgressDialog != null) {
            mProgressDialog.setLoadingTextColor(colorId);
        }
    }

    /**
     * 设置Progress的颜色
     *
     * @param colorId 颜色资源ID
     */
    public void setProgressWheelColor(int colorId) {
        if (mProgressDialog != null) {
            mProgressDialog.setProgressWheelColor(colorId);
        }
    }

    /**
     * 设置LoadingView是Dialog模式，还是普通View模式（默认普通View模式）
     *
     * @param model 模式
     */
    public void setLoadingModel(int model) {
        this.mLoadingModel = model;
    }

    /**
     * 隐藏默认进度框
     */
    private void hideProgress() {
        if (currentProgressView != null) {
            currentProgressView.setVisibility(GONE);
        }
    }

    /**
     * 隐藏重试按钮
     */
    private void hideRetryBtn() {
        if (mBtnRetry != null) {
            mBtnRetry.setVisibility(GONE);
        }
    }

    /**
     * 显示重试按钮
     */
    private void showRetryBtn() {
        if (mBtnRetry != null) {
            mBtnRetry.setVisibility(VISIBLE);
        }
    }

    /**
     * 显示自定义View
     */
    private void showCustomView() {
        if (mCustomLoadingView != null) {
            mCustomLoadingView.setVisibility(VISIBLE);
        }
    }

    /**
     * 隐藏自定义View
     */
    private void hideCustomView() {
        if (mCustomLoadingView != null) {
            mCustomLoadingView.setVisibility(GONE);
        }
    }

    public interface OnRetryClickListener {
        /**
         * 重新加载回调接口
         */
        void onRetry();
    }
}
