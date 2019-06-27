package com.example.fansonlib.utils.storage;

import android.os.Parcelable;

import com.example.fansonlib.base.AppUtils;
import com.tencent.mmkv.MMKV;

import java.util.Arrays;
import java.util.List;

/**
 * @author Created by：Fanson
 * Created Time: 2019/5/17 10:20
 * Describe：MMKV策略的实现
 */
public class MmkvStrategy implements BaseStorageStrategy{

    private static MMKV mMmkv;

    /**
     * 存储组件的配置
     */
    private StorageConfig mStorageConfig;


    public MmkvStrategy(){

    }

    @Override
    public void setStorageConfig(StorageConfig config) {
        mStorageConfig = config;
        MMKV.initialize(AppUtils.getAppContext().getFilesDir().getAbsolutePath() + mStorageConfig.getFileName());
        mMmkv = MMKV.defaultMMKV();
    }

    @Override
    public void put(String key, String value) {
        mMmkv.encode(key, value);
    }

    @Override
    public void put(String key, boolean value) {
        mMmkv.encode(key, value);
    }

    @Override
    public void put(String key, int value) {
        mMmkv.encode(key, value);
    }

    @Override
    public void put(String key, long value) {
        mMmkv.encode(key, value);
    }

    @Override
    public void put(String key, float value) {
        mMmkv.encode(key, value);
    }

    @Override
    public void put(String key, double value) {
        mMmkv.encode(key, value);
    }

    @Override
    public void put(String key, byte[] value) {
        mMmkv.encode(key, value);
    }

    @Override
    public void put(String key, Parcelable value) {
        mMmkv.encode(key, value);
    }

    @Override
    public String getString(String key) {
        return mMmkv.decodeString(key,"");
    }

    @Override
    public boolean getBoolean(String key) {
        return mMmkv.decodeBool(key,false);
    }

    @Override
    public int getInt(String key) {
        return mMmkv.getInt(key,0);
    }

    @Override
    public long getLong(String key) {
        return mMmkv.decodeLong(key,0);
    }

    @Override
    public float getFloat(String key) {
        return mMmkv.decodeFloat(key,0);
    }

    @Override
    public double getDouble(String key) {
        return mMmkv.decodeDouble(key,0);
    }

    @Override
    public byte[] getByte(String key) {
        return mMmkv.decodeBytes(key);
    }

    @Override
    public Parcelable getParcelable(String key) {
        return mMmkv.decodeParcelable(key,null);
    }

    @Override
    public void remove(String... keys) {
        if (keys == null || keys.length == 0) {
            return;
        }
        mMmkv.removeValuesForKeys(keys);
    }

    @Override
    public void removeIgnore(String... keys) {
        if (keys == null || keys.length == 0) {
            return;
        }
        String[] allKeys = mMmkv.allKeys();
        List<String> list = Arrays.asList(keys);
        for (String key : allKeys) {
            if (!list.contains(key)) {
                mMmkv.remove(key);
            }
        }
    }

    @Override
    public void removeAll() {
        mMmkv.clearAll();
    }
}
