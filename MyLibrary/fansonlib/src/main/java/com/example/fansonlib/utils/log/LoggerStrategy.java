package com.example.fansonlib.utils.log;

import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * @author Created by：Fanson
 * Created Time: 2019/3/11 11:23
 * Describe：Logger策略的实现类
 */
public class LoggerStrategy implements BaseLogStrategy{

    /**
     * 框架配置
     */
    private LogConfig mLogConfig;

    /**
     * 初始化日志框架
     * @param tag 标识
     * @param isLoggable 是否可用
     */
    public void initLogger(String tag,final boolean isLoggable){
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .methodCount(4)
                .tag(tag)
                .build();
        Logger.addLogAdapter(new DiskLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return isLoggable;
            }
        });
    }

    @Override
    public void setLogConfig(LogConfig config) {
        mLogConfig = config;
        initLogger(mLogConfig.getTag(),mLogConfig.isLoggable());
    }

    @Override
    public void d(Object object) {
        Logger.d(object);
    }

    @Override
    public void d(String message, Object... object) {
        Logger.d(message,object);
    }

    @Override
    public void i(String message) {
        Logger.i(message);
    }

    @Override
    public void v(String message) {
        Logger.v(message);
    }

    @Override
    public void e(String message ) {
        Logger.e(message);
    }

    @Override
    public void w(String message) {
        Logger.w(message);
    }

    @Override
    public void json(String jsonStr) {
        Logger.json(jsonStr);
    }

    @Override
    public void xml(String xmlStr) {
        Logger.json(xmlStr);
    }
}
