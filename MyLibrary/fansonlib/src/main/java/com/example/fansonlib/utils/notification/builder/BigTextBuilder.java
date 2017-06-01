package com.example.fansonlib.utils.notification.builder;

import android.support.v4.app.NotificationCompat;

/**
 * 长文的建造
 */

public class BigTextBuilder extends BaseBuilder{
    CharSequence summaryText;

    @Override
    public void build() {
        super.build();
        NotificationCompat.BigTextStyle textStyle = new NotificationCompat.BigTextStyle();
        textStyle.setBigContentTitle(contentTitle).bigText(contentText).setSummaryText(summaryText);
        cBuilder.setStyle(textStyle);
    }
}
