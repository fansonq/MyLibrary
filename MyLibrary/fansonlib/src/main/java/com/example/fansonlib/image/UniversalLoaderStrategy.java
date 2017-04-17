package com.example.fansonlib.image;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by：fanson
 * Created on：2017/4/14 18:09
 * Describe：
 */

public class UniversalLoaderStrategy implements BaseImageLoaderStrategy{


    @Override
    public void loadImage(Context context, ImageView view, String url) {
        ImageLoader.getInstance().displayImage(url,view);
    }
}
