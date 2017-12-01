package com.fanson.mylibrary.update;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.example.fansonlib.widget.popupwindow.BasePopup;
import com.fanson.mylibrary.R;

/**
 * Created by：fanson
 * Created on：2017/9/6 11:12
 * Describe：
 */

public class TestWindow extends BasePopup{
    private View popupView;

    public TestWindow(Activity context) {
        super(context);
        initMyView();
    }

    private void initMyView() {
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_test, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }
}
