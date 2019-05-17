package com.example.fansonlib.utils.storage;

import android.os.Parcelable;

/**
 * @author Created by：Fanson
 * Created Time: 2019/5/17 10:36
 * Describe：Key-Value形式的存储工具类
 */
public class MyKvStorageUtils {

    private volatile static MyKvStorageUtils mInstance;
    private static BaseStorageStrategy mBaseStorageStrategy;


    /**
     * 初始化
     * @param config 参数配置
     */
    public static void init(StorageConfig config) {
        //默认使用MmkvStrategy
        if (mBaseStorageStrategy == null) {
            mBaseStorageStrategy = new MmkvStrategy();
            if (config != null){
                mBaseStorageStrategy.setStorageConfig(config);
            }
            setStorageStrategy(mBaseStorageStrategy);
        }
    }

    /**
     * 获取实例MyKVStorageUtils
     *
     * @return MyKVStorageUtils
     */
    public static MyKvStorageUtils getInstance() {
        if (mInstance == null) {
            synchronized (MyKvStorageUtils.class) {
                if (mInstance == null) {
                    mInstance = new MyKvStorageUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置策略
     *
     * @param strategy
     */
    public static void setStorageStrategy(BaseStorageStrategy strategy) {
        mBaseStorageStrategy = strategy;
    }

    /**
     * 更改参数配置
     * @param config 配置
     */
    public void changeConfig(StorageConfig config){
        if (mBaseStorageStrategy ==null){
            return;
        }
        mBaseStorageStrategy.setStorageConfig(config);
    }


    public BaseStorageStrategy put(String key, String value) {
        mBaseStorageStrategy.put(key, value);
        return mBaseStorageStrategy;
    }

    public BaseStorageStrategy put(String key, boolean value) {
        mBaseStorageStrategy.put(key, value);
        return mBaseStorageStrategy;
    }

    public BaseStorageStrategy put(String key, int value) {
        mBaseStorageStrategy.put(key, value);
        return mBaseStorageStrategy;
    }

    public BaseStorageStrategy put(String key, long value) {
        mBaseStorageStrategy.put(key, value);
        return mBaseStorageStrategy;
    }

    public BaseStorageStrategy put(String key, float value) {
        mBaseStorageStrategy.put(key, value);
        return mBaseStorageStrategy;
    }

    public BaseStorageStrategy put(String key, double value) {
        mBaseStorageStrategy.put(key, value);
        return mBaseStorageStrategy;
    }

    public BaseStorageStrategy put(String key, byte[] value) {
        mBaseStorageStrategy.put(key, value);
        return mBaseStorageStrategy;
    }

    public BaseStorageStrategy put(String key, Parcelable value) {
        mBaseStorageStrategy.put(key, value);
        return mBaseStorageStrategy;
    }

    public String getString(String key) {
        return mBaseStorageStrategy.getString(key);
    }

    public boolean getBoolean(String key) {
        return mBaseStorageStrategy.getBoolean(key);
    }

    public int getInt(String key) {
        return mBaseStorageStrategy.getInt(key);
    }

    public long getLong(String key) {
        return mBaseStorageStrategy.getLong(key);
    }

    public float getFloat(String key) {
        return mBaseStorageStrategy.getFloat(key);
    }

    public double getDouble(String key) {
        return mBaseStorageStrategy.getDouble(key);
    }

    public byte[] getByte(String key) {
        return mBaseStorageStrategy.getByte(key);
    }

    public Parcelable getParcelable(String key) {
        return mBaseStorageStrategy.getParcelable(key);
    }

    /**
     * 清除指定数据
     * @param keys Keys值
     */
    public void remove(String... keys) {
        mBaseStorageStrategy.remove(keys);
    }

    /**
     * 清除数据（忽略指定数据）
     * @param keys Keys值
     */
    public void removeIgnore(String... keys) {
        mBaseStorageStrategy.removeIgnore(keys);
    }

    /**
     * 清除所有数据
     */
    public void removeAll() {
        mBaseStorageStrategy.removeAll();
    }
}
