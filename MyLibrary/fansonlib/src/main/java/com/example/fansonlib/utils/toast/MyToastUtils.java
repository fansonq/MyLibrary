package com.example.fansonlib.utils.toast;

/**
 * @author Created by：Fanson
 * Created Time: 2019/4/9 18:30
 * Describe：Toast功能类
 */
public class MyToastUtils {

    private volatile static MyToastUtils mInstance;
    private static BaseToastStrategy mBaseToastStrategy;

    /**
     * 初始化
     * @param config 参数配置
     */
    public static void init(ToastConfig config){
        //默认使用ToastStrategy
        if (mBaseToastStrategy == null){
            mBaseToastStrategy = new ToastStrategy();
            if (config != null){
                mBaseToastStrategy.setToastConfig(config);
            }
            setToastStrategy(mBaseToastStrategy);
        }
    }

    /**
     * 获取实例MyToastUtils
     * @return MyToastUtils
     */
    public static MyToastUtils getInstance(){
        if (mInstance == null){
            synchronized (MyToastUtils.class){
                if (mInstance == null){
                    mInstance = new MyToastUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 更改参数配置
     * @param config 配置
     */
    public void changeConfig(ToastConfig config){
        if (mBaseToastStrategy ==null){
            return;
        }
        mBaseToastStrategy.setToastConfig(config);
    }

    /**
     * 设置策略
     * @param strategy
     */
    public static void setToastStrategy(BaseToastStrategy strategy){
        mBaseToastStrategy = strategy;
    }

    /**
     * 显示toast，长时间
     * @param message 提示语
     * @return mBaseToastStrategy
     */
    public BaseToastStrategy showLong(String message){
        mBaseToastStrategy.showLong(message);
        return mBaseToastStrategy;
    }

    /**
     * 显示toast，短时间
     * @param message 提示语
     * @return mBaseToastStrategy
     */
    public BaseToastStrategy showShort(String message){
        mBaseToastStrategy.showShort(message);
        return mBaseToastStrategy;
    }

}
