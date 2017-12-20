package com.example.fansonlib.widget.popupwindow;

import android.os.Build;
import android.view.View;

/**
 * Describe：与basePopupWindow强引用(或者说与PopupController强引用)
 */

public class PopupWindowProxy extends BasePopupWindowProxy {
    private static final String TAG = "PopupWindowProxy";

    public PopupWindowProxy(View contentView, int width, int height, IPopupController mController) {
        super(contentView, width, height, mController);
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
        PopupCompatManager.showAsDropDown(this, anchor, xoff, yoff, gravity);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        PopupCompatManager.showAtLocation(this, parent, gravity, x, y);
    }


    @Override
    public void callSuperShowAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            super.showAsDropDown(anchor, xoff, yoff, gravity);
        } else {
            super.showAsDropDown(anchor, xoff, yoff);
        }
    }

    @Override
    public void callSuperShowAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public boolean callSuperIsShowing() {
        return super.isShowing();
    }
}
