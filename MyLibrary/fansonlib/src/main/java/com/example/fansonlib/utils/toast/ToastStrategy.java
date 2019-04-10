package com.example.fansonlib.utils.toast;

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
 * @author Created by：Fanson
 * Created Time: 2019/4/9 18:25
 * Describe：Toast策略的实现
 */
public class ToastStrategy implements BaseToastStrategy {

    private static final String TAG = ToastStrategy.class.getSimpleName();

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
     * 图标资源id
     */
    private static int sIconResource = R.mipmap.ic_tip;

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


    public ToastStrategy(){
    }

    @Override
    public void setToastConfig(ToastConfig config) {
        ToastStrategy.Config.getInstance()
                .setTextColor(config.getTextColor())
                .setTextSize(config.getTextSize())
                .setBgColor(config.getBgColor())
                .setIconResource(config.getIconResource())
                .apply();
    }

    /**
     * 获取非连续的Toast实例
     *
     * @param text     提示语
     * @param duration 显示时间：长/短
     * @return toast对象
     */
    private static Toast getSingleToast(String text, int duration) {
        return custom(AppUtils.getAppContext(), text, getDrawable(AppUtils.getAppContext(),sIconResource), duration, true, true);
    }

    @Override
    public void showLong(String message) {
        // 被调用有时会出现android.view.ViewRootImpl$CalledFromWrongThreadException，初步这样捕捉异常处理
        try {
            getSingleToast(message, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showShort(String message) {
        // 被调用有时会出现android.view.ViewRootImpl$CalledFromWrongThreadException，初步这样捕捉异常处理
        try {
            getSingleToast(message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @CheckResult
    private static Toast custom(@NonNull Context context, @NonNull CharSequence message, Drawable icon, int duration, boolean withIcon, boolean shouldTint) {
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
        private int mDefaultTextColor = ToastStrategy.sDefaultTextColor;
        private int mDefaultBgColor = ToastStrategy.sDefaultBgColor;
        private Typeface typeface = ToastStrategy.sCurrentTypeface;
        private int mTextSize = ToastStrategy.sTextSize;
        private boolean mIsTintEnable = true;
        private int mIconResource = R.mipmap.ic_tip;

        @CheckResult
        public static Config getInstance() {
            return new Config();
        }

        /**
         * 重置
         */
        public static void reset() {
            ToastStrategy.sDefaultTextColor = Color.parseColor("#FFFFFF");
            ToastStrategy.sCurrentTypeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL);;
            ToastStrategy.sTextSize = 12;
            ToastStrategy.sIsTintEnable = true;
            ToastStrategy.sIconResource = R.mipmap.ic_tip;
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

        /**
         * 设置图标资源
         *
         * @param iconResource 图标资源
         * @return Config
         */
        public Config setIconResource(int iconResource) {
            this.mIconResource = iconResource;
            return this;
        }

        public void apply() {
            ToastStrategy.sDefaultTextColor = mDefaultTextColor;
            ToastStrategy.sDefaultBgColor = mDefaultBgColor;
            ToastStrategy.sCurrentTypeface = typeface;
            ToastStrategy.sTextSize = mTextSize;
            ToastStrategy.sIsTintEnable = mIsTintEnable;
            ToastStrategy.sIconResource = mIconResource;
        }
    }


}
