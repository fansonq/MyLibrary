package com.example.fansonlib.utils.log;

/**
 * @author Created by：Fanson
 * Created Time: 2019/3/11 13:42
 * Describe：日志框架的参数的配置（建造者模式）
 */
public class LogConfig {

    /**
     * 是否可用（release/debug）
     */
    private boolean isLoggable;

    /**
     * 全局的Tag
     */
    private String tag;


    public LogConfig(Builder builder){
        this.isLoggable = builder.isLoggable;
        this.tag  = builder.tag;
    }


    public boolean isLoggable() {
        return isLoggable;
    }

    public String getTag() {
        return tag;
    }




    public static class Builder{
        /**
         * 是否可用（release/debug）
         */
        private boolean isLoggable;

        /**
         * 全局的Tag
         */
        private String tag;

        public Builder setIsLoggable(boolean isLoggable){
            this.isLoggable = isLoggable;
            return this;
        }

        public Builder setTag(String tag){
            this.tag = tag;
            return this;
        }

        public LogConfig build() {
            return new LogConfig(this);
        }
    }
}
