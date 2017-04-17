package com.example.fansonlib.image;

import android.content.Context;
import android.widget.ImageView;


/**
 * Created by：fanson
 * Created on：2017/4/14 17:29
 * Describe：加载图片的方式抽象出来
 */

public interface BaseImageLoaderStrategy {

    void loadImage(Context context,  ImageView view, String url);

}
