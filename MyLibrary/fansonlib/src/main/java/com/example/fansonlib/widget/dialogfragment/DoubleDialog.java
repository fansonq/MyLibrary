package com.example.fansonlib.widget.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private String title, content;

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
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        DoubleDialog dialog = new DoubleDialog();
        dialog.setArguments(bundle);
        return dialog;
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
    }

    @Override
    public void convertView(ViewHolder holder, final BaseDialogFragment dialog) {
        holder.setText(R.id.title, title);
        holder.setText(R.id.message, content);
        holder.setOnClickListener(R.id.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mICancelListener != null) {
                    mICancelListener.onCancel();
                }
                dialog.dismiss();
            }
        });

        holder.setOnClickListener(R.id.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIConfirmListener != null) {
                    mIConfirmListener.onConfirm();
                }
                dialog.dismiss();
            }
        });
    }

}
