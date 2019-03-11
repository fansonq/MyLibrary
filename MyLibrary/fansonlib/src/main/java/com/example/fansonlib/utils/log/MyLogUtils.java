package com.example.fansonlib.utils.log;

/**
 * @author Created by：Fanson
 * Created Time: 2019/3/11 11:30
 * Describe：日志框架的代理类
 */
public class MyLogUtils {

    private static final String TAG = MyLogUtils.class.getSimpleName();
    private volatile static MyLogUtils mInstance;
    private static BaseLogStrategy mBaseLogStrategy;


    /**
     * 默认参数配置
     */
    private static LogConfig mDefaultConfig = new LogConfig.Builder()
            .setIsLoggable(true)
            .setTag(TAG)
            .build();

    /**
     * 初始化日志框架
     */
    public static void init(LogConfig config) {
        //默认使用Logger框架
        if (mBaseLogStrategy == null) {
            mBaseLogStrategy = new LoggerStrategy();
            if (config == null) {
                mBaseLogStrategy.setLogConfig(mDefaultConfig);
            } else {
                setImageLoaderConfig(config);
            }
        }
    }

    /**
     * 单例获取实例
     *
     * @return mInstance
     */
    public static MyLogUtils getInstance() {
        if (mInstance == null) {
            synchronized (MyLogUtils.class) {
                if (mInstance == null) {
                    mInstance = new MyLogUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置日志策略
     *
     * @param strategy 日志策略
     */
    public static void setLogStrategy(BaseLogStrategy strategy) {
        mBaseLogStrategy = strategy;
    }

    /**
     * 设置框架的配置
     *
     * @param config 配置
     * @return mBaseLogStrategy
     */
    public static void setImageLoaderConfig(LogConfig config) {
        mBaseLogStrategy.setLogConfig(config);
    }


    /**
     * 输出debug日志
     *
     * @param object object
     * @return mBaseLogStrategy
     */
    public  BaseLogStrategy d(Object object) {
        mBaseLogStrategy.d(object);
        return mBaseLogStrategy;
    }

    /**
     * 输出debug日志
     *
     * @param message message
     * @param object  object
     * @return mBaseLogStrategy
     */
    public BaseLogStrategy d(String message, Object... object) {
      mBaseLogStrategy.d(message, object);
        return mBaseLogStrategy;
    }

    /**
     * 输出information日志
     *
     * @param message message
     * @param object  object
     * @return mBaseLogStrategy
     */
    public BaseLogStrategy i(String message, Object... object) {
        mBaseLogStrategy.i(message, object);
        return mBaseLogStrategy;
    }

    /**
     * 输出verbose日志
     *
     * @param message message
     * @param object  object
     * @return mBaseLogStrategy
     */
    public BaseLogStrategy v(String message, Object... object) {
        mBaseLogStrategy.v(message, object);
        return mBaseLogStrategy;
    }

    /**
     * 输出error日志
     *
     * @param message message
     * @param object  object
     * @return mBaseLogStrategy
     */
    public BaseLogStrategy e(String message, Object... object) {
        mBaseLogStrategy.e(message, object);
        return mBaseLogStrategy;
    }

    /**
     * 输出warning日志
     *
     * @param message message
     * @param object  object
     * @return mBaseLogStrategy
     */
    public BaseLogStrategy w(String message, Object... object) {
        mBaseLogStrategy.w(message, object);
        return mBaseLogStrategy;
    }

    /**
     * 输出json日志
     *
     * @param jsonStr json数据
     * @return mBaseLogStrategy
     */
    public BaseLogStrategy json(String jsonStr) {
        mBaseLogStrategy.json(jsonStr);
        return mBaseLogStrategy;
    }

    /**
     * 输出xml日志
     *
     * @param xmlStr xml数据
     * @return mBaseLogStrategy
     */
    public BaseLogStrategy xml(String xmlStr) {
        mBaseLogStrategy.json(xmlStr);
        return mBaseLogStrategy;
    }


}
