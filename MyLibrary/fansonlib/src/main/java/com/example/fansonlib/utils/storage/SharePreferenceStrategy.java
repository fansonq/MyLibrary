package com.example.fansonlib.utils.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;

import com.example.fansonlib.base.AppUtils;
import com.example.fansonlib.utils.SharePreferenceHelper;

/**
 * @author Created by：Fanson
 * Created Time: 2019/5/17 11:15
 * Describe：SharePreference策略的实现
 */
public class SharePreferenceStrategy implements BaseStorageStrategy{


    private volatile static SharePreferenceHelper sharePreferenceHelper;

    private static SharedPreferences mSharedPreferences;

    private static SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    public void setStorageConfig(StorageConfig config) {
        mSharedPreferences = AppUtils.getAppContext().getSharedPreferences(config.getFileName(), Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    @Override
    public void put(String key, String value) {
        editor.putString(key,value).apply();
    }

    @Override
    public void put(String key, boolean value) {
        editor.putBoolean(key,value).apply();
    }

    @Override
    public void put(String key, int value) {
        editor.putInt(key,value).apply();
    }

    @Override
    public void put(String key, long value) {
        editor.putLong(key,value).apply();
    }

    @Override
    public void put(String key, float value) {
        editor.putFloat(key,value).apply();
    }

    @Override
    public void put(String key, double value) {

    }

    @Override
    public void put(String key, byte[] value) {

    }

    @Override
    public void put(String key, Parcelable value) {

    }

    @Override
    public String getString(String key) {
       return mSharedPreferences.getString(key, "");
    }

    @Override
    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    @Override
    public int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    @Override
    public long getLong(String key) {
        return mSharedPreferences.getLong(key, 0);
    }

    @Override
    public float getFloat(String key) {
        return mSharedPreferences.getFloat(key, 0);
    }

    @Override
    public double getDouble(String key) {
        return 0;
    }

    @Override
    public byte[] getByte(String key) {
        return new byte[0];
    }

    @Override
    public Parcelable getParcelable(String key) {
        return null;
    }

    @Override
    public void remove(String... keys) {
        if (keys == null || keys.length == 0) {
            return;
        }
        for (String key: keys){
            editor.remove(key);
        }
        editor.apply();
    }

    @Override
    public void removeIgnore(String... keys) {

    }

    @Override
    public void removeAll() {
        editor.clear();
        editor.apply();
    }
}
