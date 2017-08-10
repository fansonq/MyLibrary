package com.example.fansonlib.function.update;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 版本信息
 */
public class UpdateAppBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * update : Yes
     * new_version : xxxxx
     * apk_url : http://cdn.the.url.of.apk/or/patch
     * update_log : xxxx
     * delta : false
     * new_md5 : xxxxxxxxxxxxxx
     * size : 601132
     */
    //是否有新版本
    private String update;
    //新版本号
    private String new_version;
    //新app下载地址
    private String apk_file_url;
    //更新日志
    private String update_log;
    //新app大小
    private String appSize;
    //是否强制更新
    private boolean constraint;
    //md5
    private String new_md5;


    //是否增量 暂时不用
    private boolean delta;


    //网络工具，内部使用
    private HttpManager httpManager;
    //目标地址，内部使用
    private String targetPath;
    private boolean mHideDialog;

    //是否隐藏对话框下载进度条,内部使用
    public boolean isHideDialog() {
        return mHideDialog;
    }

    public void setHideDialog(boolean hideDialog) {
        mHideDialog = hideDialog;
    }

    public boolean isUpdate() {
        return !TextUtils.isEmpty(this.update) && "Yes".equals(this.update);
    }

    public HttpManager getHttpManager() {
        return httpManager;
    }

    public void setHttpManager(HttpManager httpManager) {
        this.httpManager = httpManager;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public boolean isConstraint() {
        return constraint;
    }

    public UpdateAppBean setConstraint(boolean constraint) {
        this.constraint = constraint;
        return this;
    }

    public String getUpdate() {
        return update;
    }

    public UpdateAppBean setUpdate(String update) {
        this.update = update;
        return this;
    }

    public String getNewVersion() {
        return new_version;
    }

    public UpdateAppBean setNewVersion(String new_version) {
        this.new_version = new_version;
        return this;
    }

    public String getApkFileUrl() {
        return apk_file_url;
    }


    public UpdateAppBean setApkFileUrl(String apk_file_url) {
        this.apk_file_url = apk_file_url;
        return this;
    }

    public String getUpdateLog() {
        return update_log;
    }

    public UpdateAppBean setUpdateLog(String update_log) {
        this.update_log = update_log;
        return this;
    }

    public boolean isDelta() {
        return delta;
    }

    public void setDelta(boolean delta) {
        this.delta = delta;
    }

    public String getNewMd5() {
        return new_md5;
    }

    public UpdateAppBean setNewMd5(String new_md5) {
        this.new_md5 = new_md5;
        return this;
    }

    public String getAppSize() {
        return appSize;
    }

    public UpdateAppBean setAppSize(String target_size) {
        this.appSize = target_size;
        return this;
    }
}
