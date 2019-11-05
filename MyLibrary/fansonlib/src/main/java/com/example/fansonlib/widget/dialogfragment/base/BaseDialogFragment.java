package com.example.fansonlib.widget.dialogfragment.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.fansonlib.R;

/**
 * Created by：fanson
 * Created on：2017/9/14 18:04
 * Describe：DialogFragment的基类
 */

public abstract class BaseDialogFragment extends DialogFragment {

    private static final String TAG = BaseDialogFragment.class.getSimpleName();
    private static final String MARGIN = "margin";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String DIM = "dim_amount";
    private static final String BOTTOM = "show_bottom";
    private static final String CANCEL = "out_cancel";
    private static final String ANIM = "anim_style";
    private static final String LAYOUT = "layout_id";

    private FragmentManager mFragmentManager;

    private int margin = 60;//左右边距,默认60dp
    private int width = 0;//宽度
    private int height = 0;//高度
    private float dimAmount = 0.5f;//灰度深浅
    private boolean showBottom;//是否底部显示
    private boolean outCancel = false;//是否点击外部取消
    protected IConfirmListener mIConfirmListener; // 确认的按钮监听
    protected ICancelListener mICancelListener;   // 取消的按钮监听
    @StyleRes
    private int animStyle;
    @LayoutRes
    protected int layoutId;

    public abstract int intLayoutId();

    public abstract void convertView(ViewHolder holder, BaseDialogFragment dialog);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BaseDialogFragment);
        layoutId = intLayoutId();

        //恢复保存的数据
        if (savedInstanceState != null) {
            margin = savedInstanceState.getInt(MARGIN);
            width = savedInstanceState.getInt(WIDTH);
            height = savedInstanceState.getInt(HEIGHT);
            dimAmount = savedInstanceState.getFloat(DIM);
            showBottom = savedInstanceState.getBoolean(BOTTOM);
            outCancel = savedInstanceState.getBoolean(CANCEL);
            animStyle = savedInstanceState.getInt(ANIM);
            layoutId = savedInstanceState.getInt(LAYOUT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        convertView(ViewHolder.create(view), this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MARGIN, margin);
        outState.putInt(WIDTH, width);
        outState.putInt(HEIGHT, height);
        outState.putFloat(DIM, dimAmount);
        outState.putBoolean(BOTTOM, showBottom);
        outState.putBoolean(CANCEL, outCancel);
        outState.putInt(ANIM, animStyle);
        outState.putInt(LAYOUT, layoutId);
    }

    private void initParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount;
            //是否在底部显示
            if (showBottom) {
                lp.gravity = Gravity.BOTTOM;
                if (animStyle == 0) {
                    animStyle = R.style.DefaultAnimation;
                }
            }

            //设置dialog宽度
            if (width == 0) {
                lp.width = Utils.getScreenWidth(getContext()) - 2 * Utils.dp2px(getContext(), margin);
            } else if (width == -1) {
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            } else {
                lp.width = Utils.dp2px(getContext(), width);
            }

            //设置dialog高度
            if (height == 0) {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else if (height == -1) {
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            } else {
                lp.height = Utils.dp2px(getContext(), height);
            }

            //设置dialog进入、退出的动画
            window.setWindowAnimations(animStyle);
            window.setAttributes(lp);
        }
        setCancelable(outCancel);
    }

    /**
     * dialog左右两边到屏幕边缘的距离（单位：dp），默认0dp
     *
     * @param margin
     * @return
     */
    public BaseDialogFragment setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    /**
     * dialog宽度（单位：dp），默认为屏幕宽度
     *
     * @param width
     * @return
     */
    public BaseDialogFragment setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * dialog高度（单位：dp），默认为WRAP_CONTENT
     *
     * @param height
     * @return
     */
    public BaseDialogFragment setHeight(int height) {
        this.height = height;
        return this;
    }

    /**
     * 调节灰色背景透明度[0-1]，默认0.5f
     *
     * @param dimAmount
     * @return
     */
    public BaseDialogFragment setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    /**
     * 是否在底部显示dialog，默认flase
     *
     * @param showBottom
     * @return
     */
    public BaseDialogFragment setShowBottom(boolean showBottom) {
        this.showBottom = showBottom;
        return this;
    }

    /**
     * 点击dialog外是否可取消，false
     *
     * @param outCancel
     * @return
     */
    public BaseDialogFragment setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    /**
     * 设置dialog进入、退出的动画style(底部显示的dialog有默认动画)
     *
     * @param animStyle
     * @return
     */
    public BaseDialogFragment setAnimStyle(@StyleRes int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    /**
     * 注册"确认"按钮监听接口
     *
     * @param listener
     * @return
     */
    public BaseDialogFragment setConfirmListener(IConfirmListener listener) {
        this.mIConfirmListener = listener;
        return this;
    }

    /**
     * 注册"取消"按钮监听接口
     *
     * @param listener
     * @return
     */
    public BaseDialogFragment setCancelListener(ICancelListener listener) {
        this.mICancelListener = listener;
        return this;
    }

    public BaseDialogFragment show(FragmentManager manager) {
        try {
            mFragmentManager = manager;
            FragmentTransaction ft = manager.beginTransaction();
            manager.executePendingTransactions();

            if (this.isAdded() ) {
                ft.show(this);
            } else {
                ft.add(this, TAG);
                ft.commitAllowingStateLoss();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e(TAG, "Show DialogFragment IllegalStateException");
        }
        return this;
    }

    /**
     * 重写show方法，防止连续add
     * @param manager FragmentManager
     * @param tag 标识
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commit();
            super.show(manager, tag);
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e(TAG, "dismiss DialogFragment IllegalStateException");
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mIConfirmListener != null) {
            mIConfirmListener = null;
        }
        if (mICancelListener != null) {
            mICancelListener = null;
        }
        mFragmentManager = null;
    }
}
