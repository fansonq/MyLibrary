package com.example.fansonlib.utils;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * @author Created by：fanson
 * Created on：2017/12/20 11:25
 * Description：屏幕工具类
 */
public class DimensUtils {

    private static int mStatusHeight = -1;

    /**
     * dip转px
     */
    public static int dipToPx(Context context, float dip) {
        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转dip
     */
    public static int pxToDip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕高度（像素）
     *
     * @param context 上下文
     * @return 屏幕高度（像素）
     */
    public static int getHeightPixel(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取屏幕宽度（像素）
     *
     * @param context 上下文
     * @return 屏幕宽度（像素）
     */
    public static int getWidthPixel(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度（dp）
     *
     * @param context 上下文
     * @return 屏幕高度（dp）
     */
    public static int getHeightDp(Context context) {
        // 屏幕高度算法:屏幕高度（像素）/屏幕密度
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (dm.heightPixels / dm.density);
    }

    /**
     * 获取屏幕宽度（dp）
     *
     * @param context 上下文
     * @return 屏幕宽度（dp）
     */
    public static int getWidthDp(Context context) {
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (dm.widthPixels / dm.density);

    }

    /**
     * 获取屏幕密度
     *
     * @param context 上下文
     * @return 屏幕密度(int)
     */
    public static int getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) dm.density;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusHeight(Context context) {
        if (mStatusHeight != -1) {
            return mStatusHeight;
        }
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                mStatusHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mStatusHeight;
    }

    /**
     * 获取标题栏高度
     *
     * @param activity Activity
     * @return 标题栏高度
     */
    public static int getTitleHeight(Activity activity) {
        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        //statusBarHeight是上面所求的状态栏的高度
        int titleHeight = contentTop - getStatusHeight(activity);
        return titleHeight > 0 ? titleHeight : 0;
    }

    /**
     * 获取虚拟导航键的高度
     * @param context  上下文
     * @return 虚拟导航键的高度
     */
    public static int getVirtualBarHeight(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - display.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity Activity
     * @return 当前屏幕截图（不含状态栏）
     */
    public static Bitmap getSnapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        if (bmp == null) {
            return null;
        }
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, bmp.getWidth(), bmp.getHeight() - statusBarHeight);
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);
        return bp;
    }

    /**
     * 获取actionbar的像素高度，默认使用android官方兼容包做actionbar兼容
     *
     * @return
     */
    public static int getActionBarHeight(Context context) {
        int actionBarHeight = 0;
        if (context instanceof AppCompatActivity && ((AppCompatActivity) context).getSupportActionBar() != null) {
            Log.d("isAppCompatActivity", "==AppCompatActivity");
            actionBarHeight = ((AppCompatActivity) context).getSupportActionBar().getHeight();
        } else if (context instanceof Activity && ((Activity) context).getActionBar() != null) {
            Log.d("isActivity", "==Activity");
            actionBarHeight = ((Activity) context).getActionBar().getHeight();
        } else if (context instanceof ActivityGroup) {
            Log.d("ActivityGroup", "==ActivityGroup");
            if (((ActivityGroup) context).getCurrentActivity() instanceof AppCompatActivity && ((AppCompatActivity) ((ActivityGroup) context).getCurrentActivity()).getSupportActionBar() != null) {
                actionBarHeight = ((AppCompatActivity) ((ActivityGroup) context).getCurrentActivity()).getSupportActionBar().getHeight();
            } else if (((ActivityGroup) context).getCurrentActivity() instanceof Activity && ((Activity) ((ActivityGroup) context).getCurrentActivity()).getActionBar() != null) {
                actionBarHeight = ((Activity) ((ActivityGroup) context).getCurrentActivity()).getActionBar().getHeight();
            }
        }
        if (actionBarHeight != 0)
            return actionBarHeight;
        final TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true)) {
            if (context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        } else {
            if (context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        Log.d("actionBarHeight", "====" + actionBarHeight);
        return actionBarHeight;
    }


}
