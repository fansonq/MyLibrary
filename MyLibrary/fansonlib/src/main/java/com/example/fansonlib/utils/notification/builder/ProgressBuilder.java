package com.example.fansonlib.utils.notification.builder;

import android.support.v4.app.NotificationCompat;

/**
 * 带进度条的建造
 */

public class ProgressBuilder extends BaseBuilder{
    public int max;
    public int progress;
    public boolean interminate = false;

    public ProgressBuilder setProgress(int max,int progress,boolean interminate){
        this.max = max;
        this.progress = progress;
        this.interminate = interminate;
        return this;
    }

    @Override
    public void build() {
        super.build();
        cBuilder.setProgress(max,progress, interminate);
        cBuilder.setPriority(NotificationCompat.PRIORITY_LOW);
    }
}
