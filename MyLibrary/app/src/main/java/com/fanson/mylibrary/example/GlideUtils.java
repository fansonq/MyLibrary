package com.fanson.mylibrary.example;

import android.content.Context;
import android.widget.ImageView;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/18 10:08
 * Describe：
 */
public class GlideUtils {

    /**
     * 策略接口
     **/
    public interface BaseImageLoaderStrategy {

        /**
         * 默认方式加载图片
         * @param context 上下文
         * @param view View控件
         * @param imgUrl 图片URL
         */
        void loadImage(Context context, ImageView view, Object imgUrl);

 }

}
