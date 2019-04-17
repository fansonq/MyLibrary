package com.example.fansonlib.utils.toast;

/**
* @author Created by：Fanson
* Created on：2019/4/9 18:24
* Description：Toast的策略接口
*/
public interface BaseToastStrategy {

    /**
     * 设置框架的配置参数
     * @param config 配置参数
     */
    void setToastConfig(ToastConfig config);

    /**
     * 长时间的显示
     * @param message 提示语
     */
    void showLong(String message);

    /**
     * 短时间的显示
     * @param message 提示语
     */
    void showShort(String message);


}
