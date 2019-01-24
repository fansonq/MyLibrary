package com.example.fansonlib.db;

import android.content.Context;
import android.os.Parcelable;

import com.tencent.mmkv.MMKV;

import java.util.Arrays;
import java.util.List;

/**
 * @author Created by：Fanson
 * Created Time: 2019/1/24 17:28
 * Describe：轻量级数据储存工具类（基于MMKV）
 */
public class LiteDataUtils {

    private volatile static LiteDataUtils mLiteDataUtils;
    private static MMKV mmkv;

    /**
     * 双重锁，获取单例
     * @param context 上下文
     * @return mLiteDataUtils
     */
    public static LiteDataUtils getInstance(Context context) {
        if (mLiteDataUtils == null) {
            synchronized (LiteDataUtils.class) {
                if (mLiteDataUtils == null) {
                    mLiteDataUtils = new LiteDataUtils(context.getApplicationContext());
                }
            }
        }
        return mLiteDataUtils;
    }

    /**
     * 构造实例化
     * @param context 上下文
     */
    private LiteDataUtils(Context context) {
        MMKV.initialize(context);
        mmkv = MMKV.defaultMMKV();
    }

    public static void put(String key, int value) {
        mmkv.encode(key, value);
    }

    public static void put(String key, String value) {
        mmkv.encode(key, value);
    }

    public static void put(String key, long value) {
        mmkv.encode(key, value);
    }

    public static void put(String key, boolean value) {
        mmkv.encode(key, value);
    }

    public static void put(String key, float value) {
        mmkv.encode(key, value);
    }

    public static void put(String key, double value) {
        mmkv.encode(key, value);
    }

    public static void put(String key, byte[] value) {
        mmkv.encode(key, value);
    }

    public static  void put(String key, Parcelable value) {
         mmkv.encode(key,value);
    }

    /**
     * 获取Parcelable数据
     * @param key Key
     * @return Parcelable数据
     */
    public static  Parcelable getParcelable(String key ) {
        return mmkv.decodeParcelable(key,null);
    }

    public static String getString(String key) {
        return mmkv.decodeString(key, "");
    }

    public static String getString(String key, String def) {
        return mmkv.decodeString(key, def);
    }

    public static long getLong(String key) {
        return mmkv.decodeLong(key);
    }

    public static float getFloat(String key) {
        return mmkv.decodeFloat(key);
    }

    public static double getDouble(String key) {
        return mmkv.decodeDouble(key);
    }

    public static boolean getBoolean(String key) {
        return mmkv.decodeBool(key);
    }

    public static int getInteger(String key) {
        return mmkv.decodeInt(key);
    }

    public static byte[] getByte(String key) {
        return mmkv.decodeBytes(key);
    }

    public static void remove(String... keys) {
        if (keys == null || keys.length == 0) {
            return;
        }
        mmkv.removeValuesForKeys(keys);
    }

    public static void removeIgnore(String... keys) {
        if (keys == null || keys.length == 0) {
            return;
        }
        String[] allKeys = mmkv.allKeys();
        List<String> list = Arrays.asList(keys);
        for (String key : allKeys) {
            if (!list.contains(key)) {
                mmkv.remove(key);
            }
        }
    }


}
