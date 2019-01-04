package com.example.fansonlib.utils.toast;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * @author Created by：Fanson
 * Created Time: 2019/1/4 9:01
 * Describe：Drawable工具类
 */
public class DrawableUtils {

    /**
     * 将drawable染色，此方法可向下兼容
     * @param drawable Drawable
     * @param colorId 颜色ID
     * @return Drawable
     */
    public static Drawable tintDrawable(Drawable drawable, int colorId) {
        if (drawable == null){
            return null;
        }
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(colorId));
        return wrappedDrawable;
    }

}
