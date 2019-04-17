package com.example.fansonlib.widget.dialogfragment;

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
 * Created on：2017/9/14 18:25
 * Describe：自定义确认的DialogFragment
 */

public class ConfirmDialog extends BaseDialogFragment {

    private String title, content,mConfirmTip;
    private static   ConfirmDialog mDialog;

    public ConfirmDialog(){
        this.setAnimStyle(R.style.DialogScaleAnim);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_confirm_layout;
    }

    /**
     * 传值
     * @param content 内容
     * @return
     */
    public static ConfirmDialog newInstance( String content) {
        return newInstance(AppUtils.mApplication.getString(R.string.tip),content);
    }

    /**
     * 传值
     * @param title 标题
     * @param content 内容
     * @return
     */
    public static ConfirmDialog newInstance(String title, String content) {
        return newInstance(title,content,null);
    }

    /**
     * 传值
     * @param title 标题
     * @param content 内容
     * @param confirmTip 按钮文字
     * @return
     */
    public static ConfirmDialog newInstance(String title, String content,String confirmTip) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putString("confirmTip", confirmTip);
        if (mDialog==null){
            mDialog = new ConfirmDialog();
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
        title = bundle.getString("title");
        content = bundle.getString("content");
        mConfirmTip = bundle.getString("confirmTip");
    }

    /**
     * 是否在显示中
     * @return true/false
     */
    public  boolean isShowing(){
        return this.isResumed();
    }

    @Override
    public void convertView(ViewHolder holder, final BaseDialogFragment dialog) {
        holder.setText(R.id.title, title);
        holder.setText(R.id.message, content);
        if (!TextUtils.isEmpty(mConfirmTip)){
            holder.setText(R.id.ok, mConfirmTip);
        }

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
}
