package com.example.fansonlib.utils.log;

import android.text.TextUtils;
import android.util.Log;

import com.dianping.logan.Logan;
import com.dianping.logan.LoganConfig;
import com.dianping.logan.OnLoganProtocolStatus;
import com.example.fansonlib.base.AppUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author Created by：Fanson
 * Created Time: 2019/3/11 11:30
 * Describe：日志框架的代理类，带有Logan框架
 */
public class MyLogUtils {

    private static final String TAG = MyLogUtils.class.getSimpleName();
    private volatile static MyLogUtils mInstance;
    private static BaseLogStrategy mBaseLogStrategy;

    private static final int LOGAN_TYPE_DEBUG = 1;
    private static final int LOGAN_TYPE_INFORMATION = 2;
    private static final int LOGAN_TYPE_VERBOSE = 3;
    private static final int LOGAN_TYPE_WARM = 4;
    private static final int LOGAN_TYPE_ERROR = 5;

    private static RealSendLogRunnable mRealSendLogRunnable;


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
                    //初始化Logan
                    initLogan();
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
    public BaseLogStrategy d(String object) {
        Logan.w(object, LOGAN_TYPE_DEBUG);
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
     * @return mBaseLogStrategy
     */
    public BaseLogStrategy i(String message) {
        Logan.w(message, LOGAN_TYPE_INFORMATION);
        mBaseLogStrategy.i(message);
        return mBaseLogStrategy;
    }

    /**
     * 输出verbose日志
     *
     * @param message message
     * @return mBaseLogStrategy
     */
    public BaseLogStrategy v(String message) {
        Logan.w(message, LOGAN_TYPE_VERBOSE);
        mBaseLogStrategy.v(message);
        return mBaseLogStrategy;
    }

    /**
     * 输出error日志
     *
     * @param message message
     * @return mBaseLogStrategy
     */
    public BaseLogStrategy e(String message) {
        Logan.w(message, LOGAN_TYPE_ERROR);
        mBaseLogStrategy.e(message);
        return mBaseLogStrategy;
    }

    /**
     * 输出warning日志
     *
     * @param message message
     * @return mBaseLogStrategy
     */
    public BaseLogStrategy w(String message) {
        Logan.w(message, LOGAN_TYPE_WARM);
        mBaseLogStrategy.w(message);
        return mBaseLogStrategy;
    }

    /**
     * 输出json日志
     *
     * @param jsonStr json数据
     * @return mBaseLogStrategy
     */
    public BaseLogStrategy json(String jsonStr) {
        Logan.w(jsonStr, LOGAN_TYPE_DEBUG);
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
        Logan.w(xmlStr, LOGAN_TYPE_DEBUG);
        mBaseLogStrategy.json(xmlStr);
        return mBaseLogStrategy;
    }

    /**
     * 初始化Logan框架
     */
    private static void initLogan() {
        LoganConfig config = new LoganConfig.Builder()
                .setCachePath(AppUtils.getAppContext().getFilesDir().getAbsolutePath())
                .setPath(AppUtils.getAppContext().getExternalFilesDir(null).getAbsolutePath()
                        + File.separator + "logan_v1")
                .setEncryptKey16("0123456789012345".getBytes())
                .setEncryptIV16("0123456789012345".getBytes())
                .build();
        Logan.init(config);
        Logan.init(config);
        Logan.setDebug(true);
        Logan.setOnLoganProtocolStatus(new OnLoganProtocolStatus() {
            @Override
            public void loganProtocolStatus(String cmd, int code) {
                Log.d(TAG, "clogan > cmd : " + cmd + " | " + "code : " + code);
            }
        });
    }

    /**
     * 上传日志到服务器
     *
     * @param ip 服务器IP
     */
    public static void sendLoganToServer(String ip) {
        if (mRealSendLogRunnable == null) {
            mRealSendLogRunnable = new RealSendLogRunnable();
        }
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        String d = dataFormat.format(new Date(System.currentTimeMillis()));
        String[] temp = new String[1];
        temp[0] = d;
        if (!TextUtils.isEmpty(ip)) {
            mRealSendLogRunnable.setIp(ip);
        }
        Logan.s(temp, mRealSendLogRunnable);
    }

    /**
     * 获取日志文件数据
     *
     * @return 日志文件数据
     */
    public static String loganFilesInfo() {
        Map<String, Long> map = Logan.getAllFilesInfo();
        StringBuilder info = null;
        if (map != null) {
            info = new StringBuilder();
            for (Map.Entry<String, Long> entry : map.entrySet()) {
                info.append("文件日期：").append(entry.getKey()).append("  文件大小（bytes）：").append(
                        entry.getValue()).append("\n");
            }
        }
        return info == null ? "" : info.toString();
    }
}



