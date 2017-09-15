package com.example.fansonlib.widget.dialogfragment.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

/**
 * Created by：fanson
 * Created on：2017/9/14 18:08
 * Describe：
 */

public class MyDialogFragment extends BaseDialogFragment {
    private ViewConvertListener convertListener;

    public static MyDialogFragment init() {
        return new MyDialogFragment();
    }

    @Override
    public int intLayoutId() {
        return layoutId;
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialogFragment dialog) {
        if (convertListener != null) {
            convertListener.convertView(holder, dialog);
        }
    }


    public MyDialogFragment setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public MyDialogFragment setConvertListener(ViewConvertListener convertListener) {
        this.convertListener = convertListener;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            convertListener = savedInstanceState.getParcelable("listener");
        }
    }

    /**
     * 保存接口
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("listener", convertListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        convertListener = null;
    }
}

