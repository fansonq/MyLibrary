package com.example.fansonlib.image;

import android.graphics.Bitmap;

/**
 * @author Created by：fanson
 *         Created Time: 2017/11/6 18:57
 *         Describe：处理异步获取Bitmap，有了结果后回调的接口
 */

public interface OnWaitBitmapListener {

    /**
     * 得到Bitmap对象
     * @param bitmap Bitmap对象
     */
    void getBitmap(Bitmap bitmap,  Object imgUrl);
}
