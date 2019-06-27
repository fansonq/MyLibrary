package com.example.fansonlib.utils;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/23 14:11
 * Describe：处理刘海屏的工具类
 */
public class NotchScreenUtils {

    /**
     * vivo是否有刘海
     */
    public static final int VIVO_NOTCH = 0x00000020;
    /**
     * vivo是否有圆角
     */
    public static final int VIVO_FILLET = 0x00000008;

    /**
     * 判断华为手机是否为刘海屏
     *
     * @param context 上下文
     * @return true为有刘海，false则没有
     */
    public static boolean hasNotchAtHuawei(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "hasNotchAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "hasNotchAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "hasNotchAtHuawei Exception");
        } finally {
            return ret;
        }
    }

    /**
     * 获取华为的刘海尺寸：width、height
     *
     * @param context 上下文
     * @return int[0]值为刘海宽度， int[1]值为刘海高度
     */
    public static int[] getNotchSizeAtHuawei(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "getNotchSizeAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "getNotchSizeAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "getNotchSizeAtHuawei Exception");
        } finally {
            return ret;
        }
    }

    /**
     * 判断vivo手机是否为刘海屏
     *
     * @param context 上下文
     * @return true为有刘海，false则没有
     */
    private static boolean hasNotchInVivo(Context context) {
        boolean hasNotch = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class ftFeature = cl.loadClass("android.util.FtFeature");
            Method[] methods = ftFeature.getDeclaredMethods();
            if (methods != null) {
                for (int i = 0; i < methods.length; i++) {
                    Method method = methods[i];
                    if (method.getName().equalsIgnoreCase("isFeatureSupport")) {
                        hasNotch = (boolean) method.invoke(ftFeature, VIVO_NOTCH);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            hasNotch = false;
        }
        return hasNotch;
    }

    /**
     * 获取vivo的刘海尺寸：width、height
     *
     * @param context 上下文
     * @return int[0]值为刘海宽度dp， int[1]值为刘海高度dp
     */
    public static int[] getNotchSizeAtVivo(Context context) {
        int[] ret = new int[]{0, 0};
        // vivo不提供接口获取刘海尺寸，目前vivo的刘海宽为100dp,高为状态栏高度
        ret[0] = 100;
        ret[1] = DimensUtils.getStatusHeight(context);
        return ret;
    }

    /**
     * 判断oppo手机是否为刘海屏
     *
     * @param context 上下文
     * @return true为有刘海，false则没有
     */
    public static boolean hasNotchInScreenAtOPPO(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    /**
     * 获取oppo的刘海尺寸：width、height
     *
     * @param context 上下文
     * @return int[0]值为刘海宽度px， int[1]值为刘海高度px
     */
    public static int[] getNotchSizeAtOppo(Context context) {
        int[] ret = new int[]{0, 0};
        // OPPO不提供接口获取刘海尺寸，目前其有刘海屏的机型尺寸规格都是统一的。不排除以后机型会有变化。
        // 其显示屏宽度为1080px，高度为2280px。刘海区域则都是宽度为324px, 高度为80px。
        ret[0] = 324;
        // 官方文档表示 OPPO 手机的刘海高度和状态栏的高度是一致
        ret[1] = 80;
        return ret;
    }


    /**
     * 获取小米的刘海尺寸：width、height
     * MIUI 10 新增了获取刘海宽和高的方法，需升级至8.6.26开发版及以上版本
     *
     * @param context 上下文
     * @return int[0]值为刘海宽度px， int[1]值为刘海高度px
     */
    public static int[] getNotchSizeAtMiui(Context context) {
        int[] ret = new int[]{0, 0};
        int notchWeight = context.getResources().getIdentifier("notch_width", "dimen", "android");
        if (notchWeight > 0) {
            ret[0] = context.getResources().getDimensionPixelSize(notchWeight);
        }
        int notchHeight = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (notchHeight > 0) {
            ret[1] = context.getResources().getDimensionPixelSize(notchHeight);
        }
        return ret;
    }

}
