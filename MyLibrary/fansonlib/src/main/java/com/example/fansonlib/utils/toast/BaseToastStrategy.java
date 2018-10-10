package com.example.fansonlib.utils.toast;

/**
 * Created by fansonq on 2017/10/21.
 * Toast的策略接口
 */

public interface BaseToastStrategy {

    /**
     * 长时间的显示
     */
    void showLong();

    /**
     * 短时间的显示
     */
    void showShort();


}
