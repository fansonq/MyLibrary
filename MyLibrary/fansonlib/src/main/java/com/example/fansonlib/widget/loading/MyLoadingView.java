package com.example.fansonlib.widget.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fansonlib.R;

/**
 * Created by：fanson
 * Created on：2016/12/17 10:34
 * Describe：
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
     * 默认提示文本
     */
    private TextView mWarnView;
    /**
     * 默认重新加载btn
     */
    private Button mLoadDataBtn;
    /**
     * 需要绑定的View
     */
    private View mBindView;

    /**
     * 是否设置自定义加载view
     */
    private boolean hasCustomLoadingView = false;

    private View mCustomLoadingView;

    private OnBtnClickListener onBtnClickListener;

    private ProgressAlertDialog mProgressDialog;

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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyLoadingView, 0, 0);
        mWarnText = typedArray.getString(R.styleable.MyLoadingView_empty_warn_txt);
        String buttonText = typedArray.getString(R.styleable.MyLoadingView_empty_button_txt);
        mLoadingText = typedArray.getString(R.styleable.MyLoadingView_empty_loading_txt);
        typedArray.recycle();

        if (TextUtils.isEmpty(mWarnText)) {
            mWarnText = "暂无数据...";
        }

        if (TextUtils.isEmpty(buttonText)) {
            buttonText = "重新加载";
        }

        if (TextUtils.isEmpty(mLoadingText)) {
            mLoadingText = "加载中...";
        }

        mLoadDataBtn = new Button(getContext());
        mLoadDataBtn.setText(buttonText);
//        mLoadDataBtn.setBackgroundColor(getResources().getColor(R.color.transparent));
        mLoadDataBtn.setTextSize(15);
        LayoutParams mLoadingDataLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLoadingDataLp.addRule(RelativeLayout.CENTER_IN_PARENT);
//        mLoadingDataLp.addRule(RelativeLayout.BELOW, R.id.id_hh_empty_tv_view);
        mLoadDataBtn.setId(R.id.id_empty_btn_view);
        addView(mLoadDataBtn, mLoadingDataLp);

        mWarnView = new TextView(getContext());
        mWarnView.setText(mWarnText);
        mWarnView.setTextSize(15);
        LayoutParams mWarnLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWarnLp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mWarnLp.addRule(RelativeLayout.ABOVE, R.id.id_empty_btn_view);
        mWarnView.setId(R.id.id_empty_tv_view);
        addView(mWarnView, mWarnLp);

        mProgressDialog = new ProgressAlertDialog(getContext());

        mLoadDataBtn.setOnClickListener(this);

        setVisibility(GONE);
    }

    /**
     * 加载中
     */
    public void loading() {
        if (mBindView != null) {
            mBindView.setVisibility(GONE);
        }
        if (mLoadingModel == MODEL_DEFAULT) {
            setVisibility(VISIBLE);
            mLoadDataBtn.setVisibility(INVISIBLE);
            if (!hasCustomLoadingView) {
                mWarnView.setText(mLoadingText);
            } else {
                if (mCustomLoadingView != null) {
                    mWarnView.setVisibility(GONE);
                    mCustomLoadingView.setVisibility(VISIBLE);
                }
            }
        } else if (mLoadingModel == MODEL_ALERT) {
            setVisibility(GONE);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
        }

    }

    /**
     * 加载成功
     */
    public void success() {
        if (mBindView != null) {
            mBindView.setVisibility(VISIBLE);
        }
        if (mLoadingModel == MODEL_ALERT) {
            mProgressDialog.cancel();
        }
        setVisibility(GONE);
    }

    /**
     * dismiss dialog
     */
    public void dismiss() {
        if (mBindView != null) {
            mBindView.setVisibility(VISIBLE);
        }
        if (mLoadingModel == MODEL_ALERT && mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        setVisibility(GONE);
    }

    /**
     * 加载失败
     */
    public void empty() {
        empty("");
    }

    /**
     * 加载失败
     *
     * @param msg 加载失败提示语
     */
    public void empty(String msg) {

        if (!TextUtils.isEmpty(msg)) {
            mWarnText = msg;
        }

        if (mLoadingModel == MODEL_ALERT) {
            mProgressDialog.cancel();
        }

        if (mBindView != null) {
            mBindView.setVisibility(GONE);
        }
        setVisibility(VISIBLE);
        mLoadDataBtn.setVisibility(VISIBLE);

        if (!hasCustomLoadingView) {
            mWarnView.setText(mWarnText);
        } else {
            if (mCustomLoadingView != null) {
                mWarnView.setVisibility(VISIBLE);
                mWarnView.setText(mWarnText);
                mCustomLoadingView.setVisibility(GONE);
            }
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

    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
        this.onBtnClickListener = onBtnClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onBtnClickListener != null) {
            onBtnClickListener.onBtnClick();
        } else {
            throw new IllegalArgumentException("must be set click");
        }
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

            mWarnView.setVisibility(GONE);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.addRule(RelativeLayout.ABOVE, R.id.id_empty_btn_view);
            addView(view, params);
            invalidate();
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

    public void setLoadingModel(int model) {
        this.mLoadingModel = model;
    }

    public interface OnBtnClickListener {
        /**
         * 重新加载回调接口
         */
        void onBtnClick();
    }
}
