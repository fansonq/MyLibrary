package com.example.fansonlib.base;

import com.example.fansonlib.http.HttpUtils;

/**
 * @author fansonq
 * @date 2017/9/2
 * 数据层处理的BaseRepository
 */

public abstract class BaseRepository implements IBaseRepository {

    private static final String TAG = BaseRepository.class.getSimpleName();


    public BaseRepository() {
    }


    @Override
    public void onDestroy() {
        HttpUtils.getHttpUtils().cancelCurrent(null);
    }

}
