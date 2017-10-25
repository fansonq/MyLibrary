package com.example.fansonlib.widget.customeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

/**
 * Created by：fanson
 * Created on：2017/10/18 12:51
 * Describe：这只是一个简单的ImageView，可以存放Bitmap和Path等信息
 */

public class ImageEditor extends android.support.v7.widget.AppCompatImageView {

    private String absolutePath;
    private Bitmap bitmap;

    public ImageEditor(Context context) {
        this(context, null);
    }

    public ImageEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageEditor(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}

