package com.example.fansonlib.widget.dialogfragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.example.fansonlib.R;
import com.example.fansonlib.base.AppUtils;
import com.example.fansonlib.widget.dialogfragment.base.BaseDialogFragment;
import com.example.fansonlib.widget.dialogfragment.base.ViewHolder;

/**
 * Created by：fanson
 * Created on：2017/9/15 10:39
 * Describe：自定义双按钮的DialogFragment
 */

public class DoubleDialog extends BaseDialogFragment {

    private String mTitle, mContent, mCancelTip, mConfirmTip;
    private boolean mCancelNotDismiss = false;
    private static DoubleDialog mDialog = null;

    public DoubleDialog() {
        this.setAnimStyle(R.style.DialogScaleAnim);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_double_layout;
    }

    /**
     * 传值
     *
     * @param content 内容
     * @return
     */
    public static DoubleDialog newInstance(String content) {
        return newInstance(AppUtils.mApplication.getString(R.string.tip), content);
    }

    /**
     * 传值
     *
     * @param title   标题
     * @param content 内容
     * @return
     */
    public static DoubleDialog newInstance(String title, String content) {
        return newInstance(title, content, false);
    }

    /**
     * 传值
     *
     * @param title            标题
     * @param content          内容
     * @param cancelNotDismiss 点击取消按钮不消失对话框
     * @return
     */
    public static DoubleDialog newInstance(String title, String content, boolean cancelNotDismiss) {
        return newInstance(title, content, null, null, false);
    }

    /**
     * 传值
     *
     * @param title            标题
     * @param content          内容
     * @param cancelTip 取消按钮的文字
     * @param confirmTip 确认按钮的文字
     * @param cancelNotDismiss 点击取消按钮不消失对话框
     * @return
     */
    public static DoubleDialog newInstance(String title, String content, String cancelTip, String confirmTip, boolean cancelNotDismiss) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putString("cancelTip", cancelTip);
        bundle.putString("confirmTip", confirmTip);
        bundle.putBoolean("cancelNotDismiss", cancelNotDismiss);
        if (mDialog == null) {
            mDialog = new DoubleDialog();
        }
        mDialog.setArguments(bundle);
        return mDialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mTitle = bundle.getString("title");
        mContent = bundle.getString("content");
        mCancelTip = bundle.getString("cancelTip");
        mConfirmTip = bundle.getString("confirmTip");
        mCancelNotDismiss = bundle.getBoolean("cancelNotDismiss");
    }

    /**
     * 是否在显示中
     *
     * @return true/false
     */
    public boolean isShowing() {
        if (mDialog == null) {
            return false;
        }
        return mDialog.isResumed();
    }

    @Override
    public void convertView(ViewHolder holder, final BaseDialogFragment dialog) {
        holder.setText(R.id.title, mTitle);
        holder.setText(R.id.message, mContent);
        if (!TextUtils.isEmpty(mCancelTip)) {
            holder.setText(R.id.cancel, mCancelTip);
            holder.setText(R.id.ok, mConfirmTip);
        }
        holder.setOnClickListener(R.id.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mICancelListener != null) {
                    mICancelListener.onCancel();
                }
                if (!mCancelNotDismiss) {
                    dialog.dismiss();
                    mDialog = null;
                }
            }
        });

        holder.setOnClickListener(R.id.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIConfirmListener != null) {
                    mIConfirmListener.onConfirm();
                }
                dialog.dismiss();
                mDialog = null;
            }
        });
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        mDialog.dismiss();
        mDialog = null;
    }

}
