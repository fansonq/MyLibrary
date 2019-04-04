package com.example.fansonlib.utils.log;

/**
 * @author Created by：Fanson
 * Created Time: 2019/3/11 10:50
 * Describe：加载日志框架的基本策略接口
 */
public interface BaseLogStrategy {


    /**
     * 设置框架的配置参数
     * @param config 配置参数
     */
    void setLogConfig(LogConfig config);

    /**
     * 输出debug日志
     * @param object object
     */
    void d(Object object);

    /**
     * 输出debug日志
     * @param message message
     * @param object object
     */
    void d(String message,Object... object);

    /**
     * 输出information日志
     * @param message message
     */
    void i(String message);

    /**
     * 输出verbose日志
     * @param message message
     */
    void v(String message);

    /**
     * 输出error日志
     * @param message message
     */
    void e(String message);

    /**
     * 输出warning日志
     * @param message message
     */
    void w(String message);

    /**
     * 输出json日志
     * @param jsonStr json字符串
     */
    void json(String jsonStr);

    /**
     * 输出xml日志
     * @param xmlStr xml字符串
     */
    void xml(String xmlStr);

}
