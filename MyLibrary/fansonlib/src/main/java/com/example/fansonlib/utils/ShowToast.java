package com.example.fansonlib.utils;

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
 * Created by：fanson
 * Created on：2017/8/2 14:40
 * Describe：Toast工具类
 */

public class ShowToast {

    @ColorInt
    private static int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");
    @ColorInt
    private static int INFO_COLOR = Color.parseColor("#3F51B5");
    @ColorInt
    private static int NORMAL_COLOR = Color.parseColor("#353A3E");

    private static final Typeface LOADED_TOAST_TYPEFACE = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
    private static Typeface currentTypeface = LOADED_TOAST_TYPEFACE;
    private static int textSize = 12; // in SP
    private static boolean tintIcon = true;

    private static Toast mToast;

    private static TextView mTvToast;

    /**
     * 获取非连续的Toast实例
     *
     * @param text
     * @param duration
     * @return
     */
    private static Toast getSingleToast(String text, int duration) {
//            mToast = Toast.makeText(AppUtils.getAppContext(), text, duration);
        mToast = custom(AppUtils.getAppContext(), text, getDrawable(AppUtils.getAppContext(), R.mipmap.ic_tip),
                duration, true);
        return mToast;
    }

    /**
     * 获取连续的Toast实例
     *
     * @param text
     * @param duration
     * @return
     */
    private static Toast getToast(String text, int duration) {
//        return Toast.makeText(AppUtils.getAppContext(), text, duration);
        return custom(AppUtils.getAppContext(), text, getDrawable(AppUtils.getAppContext(), R.mipmap.ic_tip),
                duration, true);
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

    /**
     * 连续弹出的Toast（短）
     *
     * @param text 内容
     */
    public static void Short(String text) {
        // 被调用有时会出现android.view.ViewRootImpl$CalledFromWrongThreadException，初步这样捕捉异常处理
        try {
            getToast(text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连续弹出的Toast（长）
     *
     * @param text 内容
     */
    public static void Long(String text) {
        // 被调用有时会出现android.view.ViewRootImpl$CalledFromWrongThreadException，初步这样捕捉异常处理
        try {
            getToast(text, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @CheckResult
    public static Toast custom(@NonNull Context context, @NonNull CharSequence message, Drawable icon, int duration, boolean shouldTint) {
        final Toast currentToast = new Toast(context);
        final View toastLayout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.toast_layout, null);
        final ImageView toastIcon = (ImageView) toastLayout.findViewById(R.id.toast_icon);
        mTvToast = (TextView) toastLayout.findViewById(R.id.toast_text);
        Drawable drawableFrame;

        if (shouldTint) {
            drawableFrame = tint9PatchDrawableFrame(context, INFO_COLOR);
        } else {
            drawableFrame = getDrawable(context, R.drawable.toast_frame);
        }
        setBackground(toastLayout, drawableFrame);

//        if (withIcon) {
        if (icon == null)
            throw new IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true");
        if (tintIcon)
            icon = tintIcon(icon, DEFAULT_TEXT_COLOR);
        setBackground(toastIcon, icon);
//        } else {
//            toastIcon.setVisibility(View.GONE);
//        }

        mTvToast.setTextColor(DEFAULT_TEXT_COLOR);
        mTvToast.setText(message);
        mTvToast.setTypeface(currentTypeface);
        mTvToast.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        currentToast.setView(toastLayout);
        currentToast.setDuration(duration);
        return currentToast;
    }

    public static Drawable tintIcon(@NonNull Drawable drawable, @ColorInt int tintColor) {
        drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static Drawable tint9PatchDrawableFrame(@NonNull Context context, @ColorInt int tintColor) {
        final NinePatchDrawable toastDrawable = (NinePatchDrawable) getDrawable(context, R.drawable.toast_frame);
        return tintIcon(toastDrawable, tintColor);
    }

    public static void setBackground(@NonNull View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(drawable);
        else
            view.setBackgroundDrawable(drawable);
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return context.getDrawable(id);
        else
            return context.getResources().getDrawable(id);
    }

    public static class Config {
        @ColorInt
        private int DEFAULT_TEXT_COLOR = ShowToast.DEFAULT_TEXT_COLOR;
        @ColorInt
        private int INFO_COLOR = ShowToast.INFO_COLOR;
        private Typeface typeface = ShowToast.currentTypeface;
        private int textSize = ShowToast.textSize;
        private boolean tintIcon = ShowToast.tintIcon;

        @CheckResult
        public static Config getInstance() {
            return new Config();
        }

        public static void reset() {
            ShowToast.DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");
            ShowToast.INFO_COLOR = Color.parseColor("#3F51B5");
            ShowToast.currentTypeface = LOADED_TOAST_TYPEFACE;
            ShowToast.textSize = 12;
            ShowToast.tintIcon = true;
        }

        @CheckResult
        public Config setTextColor(@ColorInt int textColor) {
            DEFAULT_TEXT_COLOR = textColor;
            return this;
        }

        @CheckResult
        public Config setInfoColor(@ColorInt int infoColor) {
            INFO_COLOR = infoColor;
            return this;
        }

        @CheckResult
        public Config setToastTypeface(@NonNull Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        @CheckResult
        public Config setTextSize(int sizeInSp) {
            this.textSize = sizeInSp;
            return this;
        }

        @CheckResult
        public Config tintIcon(boolean tintIcon) {
            this.tintIcon = tintIcon;
            return this;
        }

        public void apply() {
            ShowToast.DEFAULT_TEXT_COLOR = DEFAULT_TEXT_COLOR;
            ShowToast.INFO_COLOR = INFO_COLOR;
            ShowToast.currentTypeface = typeface;
            ShowToast.textSize = textSize;
            ShowToast.tintIcon = tintIcon;
        }
    }

}
