package com.example.fansonlib.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by：fanson
 * Created on：2016/11/17 16:20
 * Describe：show notification
 */
public class MyNotificationUtil extends Notification {

    private static NotificationManager mNotificationManager;
    /**
     * 新订单的通知ID
     */
    public static final int NEW_ORDER_NOTI_ID = 100;

    /**
     * App回到主界面的通知栏
     */
    public static final int APP_ING_NOTI_ID = 200;

    /**
     * 显示一个普通的通知
     *
     * @param context        上下文
     * @param targetActivity 目标Activity
     * @param iconID         图标ID
     * @param title          标题
     * @param content        内容
     * @param time           显示的时间（0为常驻）
     * @param onGoing        是否不可以被划走
     * @param isSound        是否有声音
     * @param notiID         通知栏ID
     */
    public static void showNotification(Context context, Class<?> targetActivity, int iconID, String title, String content, long time, boolean onGoing, boolean isSound, int notiID) {

        Intent intent = new Intent(context, targetActivity);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(iconID)
                .setContentTitle(title)
                .setContentText(content)
                .setOngoing(onGoing)
                .setContentIntent(contentIntent);
        mNotificationManager = (NotificationManager) context.getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = mBuilder.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification.priority |= Notification.PRIORITY_MAX;
        }
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (isSound) {
            notification.defaults = Notification.DEFAULT_ALL;
        }
        if (time != 0) {
            notification.when = 3000;
        }
        mNotificationManager.notify(notiID, notification);
    }

    /**
     * 清除指定ID的通知
     *
     * @param id
     */
    public static void cancelNoti(int id) {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(id);
        }
    }

    /**
     * 清除指定ID的通知
     */
    public static void cancelAllNoti() {
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }
    }

}
