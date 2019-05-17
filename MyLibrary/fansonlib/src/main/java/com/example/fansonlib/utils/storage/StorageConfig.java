package com.example.fansonlib.utils.storage;

/**
 * @author Created by：Fanson
 * Created Time: 2019/5/17 11:55
 * Describe：Key-Value存储的配置
 */
public class StorageConfig {

    /**
     * 存储文件名
     */
    private String mFileName;

    public String getFileName() {
        return mFileName;
    }

    public StorageConfig(Builder builder){
        this.mFileName = builder.mFileName;
    }

    public static class Builder{

        private String mFileName;

        public Builder setFileName(String mFileName) {
            this.mFileName = mFileName;
            return this;
        }

        public StorageConfig build(){
            return new StorageConfig(this);
        }
    }

}
