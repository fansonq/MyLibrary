package com.example.fansonlib.view;

import android.content.Context;
import android.view.View;

import com.example.fansonlib.R;
import com.wang.avi.AVLoadingIndicatorView;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author Created by：Fanson
 * Created Time: 2019/4/2 15:56
 * Describe：数据加载框LoadingWindow
 */
public class LoadingWindow extends BasePopupWindow {

    private static final String TAG = LoadingWindow.class.getSimpleName();
    private AVLoadingIndicatorView mLoadingView;

    /**
     * 实例化
     * @param context 上下文
     * @param color loading颜色
     */
    public LoadingWindow(Context context,int color) {
        super(context);
        mLoadingView = findViewById(R.id.loadingView);
        mLoadingView.setIndicatorColor(color);
        mLoadingView.show();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.window_loading);
    }

    /**
     * 移除自己这个对话框
     */
    public void hide( ) {
        if (this.isShowing()) {
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mLoadingView != null) {
            mLoadingView.hide();
            mLoadingView = null;
        }
    }

}

