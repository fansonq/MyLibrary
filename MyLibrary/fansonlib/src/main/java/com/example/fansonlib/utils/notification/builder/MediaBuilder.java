package com.example.fansonlib.utils.notification.builder;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.media.app.NotificationCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.fansonlib.utils.notification.MyNotificationUtils;


public class MediaBuilder extends BaseBuilder {

    @Override
    public void build() {
        super.build();
        NotificationCompat.MediaStyle style = new NotificationCompat.MediaStyle();
        style.setMediaSession(new MediaSessionCompat(MyNotificationUtils.mContext, "MediaSession",
                new ComponentName(MyNotificationUtils.mContext, Intent.ACTION_MEDIA_BUTTON), null).getSessionToken());
        //设置要现实在通知右方的图标 最多三个
        style.setShowActionsInCompactView(2, 3);
        style.setShowCancelButton(true);
        cBuilder.setStyle(style);
        cBuilder.setShowWhen(false);
    }

    @Override
    public BaseBuilder addBtn(int icon, CharSequence text, PendingIntent pendingIntent) {
        return super.addBtn(icon, text, pendingIntent);
    }
}
