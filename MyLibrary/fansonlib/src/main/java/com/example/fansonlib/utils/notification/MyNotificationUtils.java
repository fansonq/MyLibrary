package com.example.fansonlib.utils.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.fansonlib.utils.notification.builder.BigPicBuilder;
import com.example.fansonlib.utils.notification.builder.BigTextBuilder;
import com.example.fansonlib.utils.notification.builder.MailboxBuilder;
import com.example.fansonlib.utils.notification.builder.MediaBuilder;
import com.example.fansonlib.utils.notification.builder.ProgressBuilder;
import com.example.fansonlib.utils.notification.builder.SingleLineBuilder;

/**
 * Created by：fanson
 * Created on：2017/6/1 14:54
 * Describe：通知工具类
 */

public class MyNotificationUtils {

    public static Context mContext;

    private static NotificationManager mNotificationManager;

    private static String mChannelID = "1";

    private static String mChannelName = "channel_name";

    /**
     * 初始化
     *
     * @param context 传入的必须是Application
     */
    public static void init(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(mChannelID, mChannelName, NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * 单行，普通的建造
     *
     * @param id
     * @param smallIcon
     * @param contentTitle
     * @param contentText
     * @param contentIntent
     * @return
     */
    public static SingleLineBuilder buildSimple(int id, int smallIcon, CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
        SingleLineBuilder builder = new SingleLineBuilder();
        builder.setBase(smallIcon, contentTitle, contentText)
                .setId(id)
                .setContentIntent(contentIntent);
        return builder;
    }

    /**
     * 带进度条的建造
     *
     * @param id
     * @param smallIcon
     * @param contentTitle
     * @param progress
     * @param max
     * @return
     */
    public static ProgressBuilder buildProgress(int id, int smallIcon, CharSequence contentTitle, int progress, int max) {
        ProgressBuilder builder = new ProgressBuilder();
        builder.setBase(smallIcon, contentTitle, progress + "/" + max)
                .setId(id);
        builder.setProgress(max, progress, false);
        return builder;
    }

    /**
     * 带图片的建造
     *
     * @param id
     * @param smallIcon
     * @param contentTitle
     * @param contentText
     * @param summaryText
     * @return
     */
    public static BigPicBuilder buildBigPic(int id, int smallIcon, CharSequence contentTitle, CharSequence contentText, CharSequence summaryText) {
        BigPicBuilder builder = new BigPicBuilder();
        builder.setBase(smallIcon, contentTitle, contentText).setId(id);
        builder.setSummaryText(summaryText);
        return builder;
    }

    /**
     * 长文的建造
     *
     * @param id
     * @param smallIcon
     * @param contentTitle
     * @param contentText
     * @return
     */
    public static BigTextBuilder buildBigText(int id, int smallIcon, CharSequence contentTitle, CharSequence contentText) {
        BigTextBuilder builder = new BigTextBuilder();
        builder.setBase(smallIcon, contentTitle, contentText).setId(id);
        //builder.setSummaryText(summaryText);
        return builder;
    }

    /**
     * 多条信息的建造
     *
     * @param id
     * @param smallIcon
     * @param contentTitle
     * @return
     */
    public static MailboxBuilder buildMailBox(int id, int smallIcon, CharSequence contentTitle) {
        MailboxBuilder builder = new MailboxBuilder();
        builder.setBase(smallIcon, contentTitle, "").setId(id);
        return builder;
    }

    /**
     * @param id
     * @param smallIcon
     * @param contentTitle
     * @param contentText
     * @return
     */
    public static MediaBuilder buildMedia(int id, int smallIcon, CharSequence contentTitle, CharSequence contentText) {
        MediaBuilder builder = new MediaBuilder();
        builder.setBase(smallIcon, contentTitle, contentText).setId(id);
        return builder;
    }

    /**
     * 建造PendingIntent
     *
     * @param clazz 指定跳转的类
     * @return
     */
    public static PendingIntent buildIntent(Class clazz) {
        return buildIntentWithBundle(clazz, null);
    }

    /**
     * 建造PendingIntent带参数
     *
     * @param clazz 指定跳转的类
     * @param bundle 参数
     * @return
     */
    public static PendingIntent buildIntentWithBundle(Class clazz, Bundle bundle) {
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        Intent intent = new Intent(MyNotificationUtils.mContext, clazz);
        if (bundle!=null){
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(MyNotificationUtils.mContext, 0, intent, flags);
    }

    /**
     * 开启通知
     *
     * @param id
     * @param notification
     */
    public static void notify(int id, Notification notification) {
        mNotificationManager.notify(id, notification);
    }

    /**
     * 移除指定通知
     *
     * @param id ID
     */
    public static void cancel(int id) {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(id);
        }
    }

    /**
     * 移除所有通知
     */
    public static void cancelAll() {
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }
    }

}