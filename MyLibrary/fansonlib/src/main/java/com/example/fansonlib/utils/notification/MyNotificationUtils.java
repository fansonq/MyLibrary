package com.example.fansonlib.utils.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    public final static String CHANNEL_ID_CHAT = "chat";
    public final static String CHANNEL_ID_RECOMMEND= "recommend";
    public final static String CHANNEL_ID_SERVICE= "service";
    private final static String CHANNEL_NAME_CHAT = "聊天消息";
    private final static String CHANNEL_NAME_RECOMMEND = "推荐消息";
    private final static String CHANNEL_NAME_SERVICE = "系统推送消息";

    /**
     * 初始化
     *
     * @param context 传入的必须是Application
     */
    public static void init(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        createNotificationChannel(CHANNEL_ID_SERVICE,CHANNEL_NAME_SERVICE);
    }

    /**
     * 创建通知渠道
     * @param channelId 渠道ID
     * @param channelName 渠道名称
     */
    public static void createNotificationChannel(String channelId,String channelName){
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * 单行，普通的建造
     *
     * @param id 通知ID
     * @param channelId Android8.0新增的渠道ID
     * @param smallIcon 通知图标
     * @param contentTitle 通知标题
     * @param contentText  通知内容
     * @param contentIntent 跳转的意图
     * @return
     */
    public static SingleLineBuilder buildSimple(int id, String channelId,  int smallIcon, CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
        SingleLineBuilder builder = new SingleLineBuilder();
        builder.setBase(smallIcon, contentTitle, contentText)
                .setId(id)
                .setChannelId(channelId)
                .setContentIntent(contentIntent);
        return builder;
    }

    /**
     * 单行，普通的建造
     *
     * @param id 通知ID
     * @param channelId Android8.0新增的渠道ID
     * @param soundUri 自定义声音文件Uri
     * @param smallIcon 通知图标
     * @param contentTitle 通知标题
     * @param contentText  通知内容
     * @param contentIntent 跳转的意图
     * @return
     */
    public static SingleLineBuilder buildSimple(int id, String channelId, Uri soundUri, int smallIcon, CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
        SingleLineBuilder builder = new SingleLineBuilder();
        builder.setBase(smallIcon, contentTitle, contentText)
                .setId(id)
                .setChannelId(channelId)
                .setSound(soundUri)
                .setContentIntent(contentIntent);
        return builder;
    }

    /**
     * 带进度条的建造
     *
     * @param id 通知ID
     * @param channelId Android8.0新增的渠道ID
     * @param smallIcon 通知图标
     * @param contentTitle 通知标题
     * @param progress 进度条
     * @param max 进度条最大值
     * @return
     */
    public static ProgressBuilder buildProgress(int id, String channelId, int smallIcon, CharSequence contentTitle, int progress, int max) {
        ProgressBuilder builder = new ProgressBuilder();
        builder.setBase(smallIcon, contentTitle, progress + "/" + max)
                .setChannelId(channelId)
                .setId(id);
        builder.setProgress(max, progress, false);
        return builder;
    }

    /**
     * 带图片的建造
     *
     * @param id 通知ID
     * @param channelId Android8.0新增的渠道ID
     * @param smallIcon 通知图标
     * @param contentTitle 通知标题
     * @param contentText  通知内容
     * @param summaryText
     * @return
     */
    public static BigPicBuilder buildBigPic(int id, String channelId,  int smallIcon, CharSequence contentTitle, CharSequence contentText, CharSequence summaryText) {
        BigPicBuilder builder = new BigPicBuilder();
        builder.setBase(smallIcon, contentTitle, contentText).setChannelId(channelId).setId(id);
        builder.setSummaryText(summaryText);
        return builder;
    }

    /**
     * 长文的建造
     *
     * @param id 通知ID
     * @param channelId Android8.0新增的渠道ID
     * @param smallIcon 通知图标
     * @param contentTitle 通知标题
     * @param contentText  通知内容
     * @return
     */
    public static BigTextBuilder buildBigText(int id, String channelId, int smallIcon, CharSequence contentTitle, CharSequence contentText) {
        BigTextBuilder builder = new BigTextBuilder();
        builder.setBase(smallIcon, contentTitle, contentText).setId(id).setChannelId(channelId);
        //builder.setSummaryText(summaryText);
        return builder;
    }

    /**
     * 多条信息的建造
     *
     * @param id 通知ID
     * @param channelId Android8.0新增的渠道ID
     * @param smallIcon 通知图标
     * @param contentTitle 通知标题
     * @return
     */
    public static MailboxBuilder buildMailBox(int id, String channelId, int smallIcon, CharSequence contentTitle) {
        MailboxBuilder builder = new MailboxBuilder();
        builder.setBase(smallIcon, contentTitle, "").setId(id).setChannelId(channelId);
        return builder;
    }

    /**
     * @param id
     * @param smallIcon
     * @param contentTitle
     * @param contentText
     * @return
     */
    public static MediaBuilder buildMedia(int id, String channelId,  int smallIcon, CharSequence contentTitle, CharSequence contentText) {
        MediaBuilder builder = new MediaBuilder();
        builder.setBase(smallIcon, contentTitle, contentText).setId(id).setChannelId(channelId);
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