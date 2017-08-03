package com.example.fansonlib.callback;

/**
 * Created by：fanson
 * Created on：2017/3/28 16:11
 * Describe：Dialog的回调，双按钮（确认和取消）
 */

public interface OnDialogDoubleListener extends OnDialogConfirmListener{

    /**
     * 点击Negative按钮
     */
    void onCancel();

    /**
     * 点击Positive按钮
     */
    void onConfrim();

}
