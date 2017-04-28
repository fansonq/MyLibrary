package com.example.fansonlib.widget.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by：fanson
 * Created on：2017/4/28 11:30
 * Describe：
 */

public class PopupWindowProxy extends PopupWindow {
    private final boolean isFixAndroidN = Build.VERSION.SDK_INT == 24;
    private final boolean isOverAndroidN = Build.VERSION.SDK_INT > 24;


    private IPopupController mController;

    public PopupWindowProxy(Context context, IPopupController mController) {
        super(context);
        this.mController = mController;
    }

    public PopupWindowProxy(Context context, AttributeSet attrs, IPopupController mController) {
        super(context, attrs);
        this.mController = mController;
    }

    public PopupWindowProxy(Context context, AttributeSet attrs, int defStyleAttr, IPopupController mController) {
        super(context, attrs, defStyleAttr);
        this.mController = mController;
    }

    public PopupWindowProxy(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, IPopupController mController) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mController = mController;
    }

    public PopupWindowProxy(IPopupController mController) {
        this.mController = mController;
    }

    public PopupWindowProxy(View contentView, IPopupController mController) {
        super(contentView);
        this.mController = mController;
    }

    public PopupWindowProxy(int width, int height, IPopupController mController) {
        super(width, height);
        this.mController = mController;
    }

    public PopupWindowProxy(View contentView, int width, int height, IPopupController mController) {
        super(contentView, width, height);
        this.mController = mController;
    }

    public PopupWindowProxy(View contentView, int width, int height, boolean focusable, IPopupController mController) {
        super(contentView, width, height, focusable);
        this.mController = mController;
    }


    /**
     * fix showAsDropDown when android api ver is over N
     * <p>
     * https://code.google.com/p/android/issues/detail?id=221001
     *
     * @param anchor
     * @param xoff
     * @param yoff
     * @param gravity
     */
    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (isFixAndroidN && anchor != null) {
            int[] a = new int[2];
            anchor.getLocationInWindow(a);
            Activity activity = (Activity) anchor.getContext();
            super.showAtLocation((activity).getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, a[1] + anchor.getHeight());
        } else {
            if (isOverAndroidN){
                setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            super.showAsDropDown(anchor, xoff, yoff, gravity);
        }
    }

    @Override
    public void dismiss() {
        if (mController == null) return;

        boolean performDismiss = mController.onBeforeDismiss();
        if (!performDismiss) return;
        boolean dismissAtOnce = mController.callDismissAtOnce();
        if (dismissAtOnce) {
            callSuperDismiss();
        }
    }

    void callSuperDismiss() {
        super.dismiss();
    }

}
