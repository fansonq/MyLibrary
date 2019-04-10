package com.example.fansonlib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fansonlib.R;
import com.example.fansonlib.base.AppUtils;

/**
 * @author Created by：fanson
 * Created on：2017/8/2 14:40
 * Describe：Toast工具类
 */

public class ShowToast {

    /**
     * 文字颜色
     */
    private static int sDefaultTextColor = Color.parseColor("#FFFFFF");

    /**
     * 背景颜色
     */
    private static int sDefaultBgColor = Color.parseColor("#FFFFFF");

    /**
     * 提示语的字体
     */
    private static Typeface sCurrentTypeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL);;

    /**
     * 提示语文字大小，默认12sp
     */
    private static int sTextSize = 12;

    /**
     * 绘色功能Tint是否可用
     */
    private static boolean sIsTintEnable = true;

    /**
     * 是否覆盖上一条toast
     */
    private static boolean sAllowQueue = false;

    /**
     * 暂存最后一个toast对象
     */
    private static Toast lastToast = null;


    /**
     * 获取非连续的Toast实例
     *
     * @param text     提示语
     * @param duration 显示时间：长/短
     * @return toast对象
     */
    private static Toast getSingleToast(String text, int duration) {
        return custom(AppUtils.getAppContext(), text, getDrawable(AppUtils.getAppContext(), R.mipmap.ic_tip), duration, true, true);
    }

    /**
     * 非连续弹出的Toast（短）
     *
     * @param message 内容
     */
    public static void singleShort(String message) {
        // 被调用有时会出现android.view.ViewRootImpl$CalledFromWrongThreadException，初步这样捕捉异常处理
        try {
            getSingleToast(message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 非连续弹出的Toast（长）
     *
     * @param text 内容
     */
    public static void singleLong(String text) {
        // 被调用有时会出现android.view.ViewRootImpl$CalledFromWrongThreadException，初步这样捕捉异常处理
        try {
            getSingleToast(text, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @CheckResult
    public static Toast custom(@NonNull Context context, @NonNull CharSequence message, Drawable icon, int duration, boolean withIcon, boolean shouldTint) {
        @SuppressLint("ShowToast") final Toast currentToast = Toast.makeText(context, "", duration);
        final View toastLayout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_toast, null);
        final ImageView toastIcon = (ImageView) toastLayout.findViewById(R.id.iv_toast);
        final TextView mTvToast = (TextView) toastLayout.findViewById(R.id.tv_toast);
        Drawable drawableFrame;

        if (shouldTint) {
            drawableFrame = tint9PatchDrawableFrame(context, sDefaultBgColor);
        } else {
            drawableFrame = getDrawable(context, R.drawable.toast_frame);
        }
        setBackground(toastLayout, drawableFrame);

        if (withIcon) {
            if (icon == null) {
                throw new IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true");
            }
            setBackground(toastIcon, sIsTintEnable ? tintIcon(icon, sDefaultTextColor) : icon);
        } else {
            toastIcon.setVisibility(View.GONE);
        }

        mTvToast.setTextColor(sDefaultTextColor);
        mTvToast.setText(message);
        mTvToast.setTypeface(sCurrentTypeface);
        mTvToast.setTextSize(TypedValue.COMPLEX_UNIT_SP, sTextSize);

        currentToast.setView(toastLayout);

        if (!sAllowQueue) {
            if (lastToast != null) {
                lastToast.cancel();
            }
            lastToast = currentToast;
        }

        return currentToast;
    }

    /**
     * 将drawable变色
     * @param drawable drawable
     * @param tintColor 更改后的颜色
     * @return drawable
     */
    public static Drawable tintIcon(@NonNull Drawable drawable, @ColorInt int tintColor) {
        drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static Drawable tint9PatchDrawableFrame(@NonNull Context context, @ColorInt int tintColor) {
        final NinePatchDrawable toastDrawable = (NinePatchDrawable) getDrawable(context, R.drawable.toast_frame);
        return tintIcon(toastDrawable, tintColor);
    }

    public static void setBackground(@NonNull View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }


    /**
     * 配置参数
     */
    public static class Config {
        private int mDefaultTextColor = ShowToast.sDefaultTextColor;
        private int mDefaultBgColor = ShowToast.sDefaultBgColor;
        private Typeface typeface = ShowToast.sCurrentTypeface;
        private int mTextSize = ShowToast.sTextSize;
        private boolean mIsTintEnable = true;

        @CheckResult
        public static Config getInstance() {
            return new Config();
        }

        /**
         * 重置
         */
        public static void reset() {
            ShowToast.sDefaultTextColor = Color.parseColor("#FFFFFF");
            ShowToast.sCurrentTypeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL);;
            ShowToast.sTextSize = 12;
            ShowToast.sIsTintEnable = true;
        }

        /**
         * 设置文字颜色
         *
         * @param textColor 文字颜色
         * @return Config
         */
        public Config setTextColor(@ColorInt int textColor) {
            mDefaultTextColor = textColor;
            return this;
        }

        /**
         * 设置背景颜色
         *
         * @param bgColor 背景颜色
         * @return Config
         */
        public Config setBgColor(@ColorInt int bgColor) {
            mDefaultBgColor = bgColor;
            return this;
        }

        /**
         * 设置文字字体
         * @param typeface 文字字体
         * @return Config
         */
        public Config setTextTypeface(@NonNull Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        /**
         * 设置文字大小
         *
         * @param sizeInSp 文字大小sp
         * @return Config
         */
        public Config setTextSize(int sizeInSp) {
            this.mTextSize = sizeInSp;
            return this;
        }

        /**
         * 设置绘色功能tint
         *
         * @param isEnable 是否支持
         * @return Config
         */
        public Config setTintEnable(boolean isEnable) {
            this.mIsTintEnable = isEnable;
            return this;
        }

        public void apply() {
            ShowToast.sDefaultTextColor = mDefaultTextColor;
            ShowToast.sDefaultBgColor = mDefaultBgColor;
            ShowToast.sCurrentTypeface = typeface;
            ShowToast.sTextSize = mTextSize;
            ShowToast.sIsTintEnable = mIsTintEnable;
        }
    }

}
