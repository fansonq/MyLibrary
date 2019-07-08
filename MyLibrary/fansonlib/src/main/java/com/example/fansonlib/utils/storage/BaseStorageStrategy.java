package com.example.fansonlib.utils.storage;

import android.os.Parcelable;

/**
 * @author Created by：Fanson
 * Created Time: 2019/5/17 9:51
 * Describe：Key-Value存储的策略接口
 */
public interface BaseStorageStrategy {


    /**
     * 设置框架的配置参数
     * @param config 配置参数
     */
    void setStorageConfig(StorageConfig config);

    /**
     * 存储String类型
     * @param key Key值
     * @param value Value值
     */
    void put(String key,String value);

    /**
     * 存储boolean类型
     * @param key Key值
     * @param value Value值
     */
    void put(String key,boolean value);

    /**
     * 存储int类型
     * @param key Key值
     * @param value Value值
     */
    void put(String key,int value);

    /**
     * 存储long类型
     * @param key Key值
     * @param value Value值
     */
    void put(String key,long value);

    /**
     * 存储float类型
     * @param key Key值
     * @param value Value值
     */
    void put(String key,float value);

    /**
     * 存储double类型
     * @param key Key值
     * @param value Value值
     */
    void put(String key,double value);

    /**
     * 存储byte[]类型
     * @param key Key值
     * @param value Value值
     */
    void put(String key,byte[] value);

    /**
     * 存储Parcelable类型
     * @param key Key值
     * @param value Value值
     */
    void put(String key,Parcelable value);

    /**
     * 获取String类型的值
     * @param key key值
     * @return String类型的值
     */
    String getString(String key);

    /**
     * 获取String类型的值
     * @param key key值
     * @param defValue value默认值
     * @return String类型的值
     */
    String getString(String key,String defValue);

    /**
     * 获取boolean类型的值
     * @param key key值
     * @return boolean类型的值
     */
    boolean getBoolean(String key );

    /**
     * 获取boolean类型的值
     * @param key key值
     * @param defValue value默认值
     * @return boolean类型的值
     */
    boolean getBoolean(String key,boolean defValue);

    /**
     * 获取int类型的值
     * @param key key值
     * @return int类型的值
     */
    int getInt(String key);

     /**
     * 获取int类型的值
     * @param key key值
      * @param defValue value默认值
     * @return int类型的值
     */
     int getInt(String key,int defValue);

    /**
     * 获取long类型的值
     * @param key key值
     * @return long类型的值
     */
    long getLong(String key );

    /**
     * 获取long类型的值
     * @param key key值
     * @param defValue value默认值
     * @return long类型的值
     */
    long getLong(String key ,long defValue);

    /**
     * 获取float类型的值
     * @param key key值
     * @return float类型的值
     */
    float getFloat(String key);

    /**
     * 获取double类型的值
     * @param key key值
     * @return double类型的值
     */
    double getDouble(String key );

    /**
     * 获取byte[]类型的值
     * @param key Key值
     * @return byte[]类型的值
     */
    byte[] getByte(String key);

    /**
     * 获取Parcelable类型的值
     * @param key Key值
     * @return Parcelable类型的值
     */
    Parcelable getParcelable(String key);

    /**
     * 清除指定数据
     * @param keys Keys值
     */
    void remove(String... keys);

    /**
     * 清除数据（忽略指定数据）
     * @param keys Keys值
     */
    void removeIgnore(String... keys);

    /**
     * 清除所有数据
     */
    void removeAll();

}
