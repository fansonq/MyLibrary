package com.example.fansonlib.utils.notification;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;
import static android.support.v4.app.NotificationCompat.VISIBILITY_SECRET;

/**
 * @author Created by：fanson
 * Created on：2017/6/1 14:54
 * Describe：通知工具类
 */

public class MyNotificationUtils extends ContextWrapper {

    public static final String CHANNEL_ID = "default";
    private static final String CHANNEL_NAME = "Default_Channel";
    private NotificationManager mManager;
    private int[] flags;
    private volatile static MyNotificationUtils mNotificationUtils;
    private boolean ongoing = false;
    private RemoteViews remoteViews = null;
    private PendingIntent intent = null;
    private String ticker = "";
    private int priority = Notification.PRIORITY_DEFAULT;
    private boolean onlyAlertOnce = false;
    private long when = 0;
    private Uri sound = null;
    private int defaults = 0;
    private long[] pattern = null;

    /**
     * 获取单例
     *
     * @param context 上下文
     * @return 实例
     */
    public static MyNotificationUtils getInstance(Context context) {
        if (mNotificationUtils == null) {
            synchronized (MyNotificationUtils.class) {
                if (mNotificationUtils == null) {
                    mNotificationUtils = new MyNotificationUtils(context);
                }
            }
        }
        return mNotificationUtils;
    }

    public MyNotificationUtils(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 8.0以上需要特殊处理，也就是targetSDKVersion为26以上
            createNotificationChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        //第一个参数：channel_id
        //第二个参数：channel_name
        //第三个参数：设置通知重要性级别
        //注意：该级别必须要在 NotificationChannel 的构造函数中指定，总共要五个级别；
        //范围是从 NotificationManager.IMPORTANCE_NONE(0) ~ NotificationManager.IMPORTANCE_HIGH(4)
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.canBypassDnd();//是否绕过请勿打扰模式
        channel.enableLights(true);//闪光灯
        channel.setLockscreenVisibility(VISIBILITY_SECRET);//锁屏显示通知
        channel.setLightColor(Color.RED);//闪关灯的灯光颜色
        channel.canShowBadge();//桌面launcher的消息角标
        channel.enableVibration(true);//是否允许震动
        channel.getAudioAttributes();//获取系统通知响铃声音的配置
        channel.getGroup();//获取通知取到组
        channel.setBypassDnd(true);//设置可绕过 请勿打扰模式
        channel.setVibrationPattern(new long[]{100, 100, 200});//设置震动模式
        channel.shouldShowLights();//是否会有灯光
        getManager().createNotificationChannel(channel);
    }

    /**
     * 获取创建一个NotificationManager的对象
     *
     * @return NotificationManager对象
     */
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    /**
     * 清空所有的通知
     */
    public void clearNotification() {
        getManager().cancelAll();
    }

    /**
     * 获取Notification
     *
     * @param title   title
     * @param content content
     */
    public Notification getNotification(String title, String content, int icon) {
        Notification build;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 8.0以上需要特殊处理，也就是targetSDKVersion为26以上
            //通知用到NotificationCompat()这个V4库中的方法。但是在实际使用时发现书上的代码已经过时并且Android8.0已经不支持这种写法
            Notification.Builder builder = getChannelNotification(title, content, icon);
            build = builder.build();
        } else {
            NotificationCompat.Builder builder = getNotificationCompat(title, content, icon);
            build = builder.build();
        }
        if (flags != null && flags.length > 0) {
            for (int a = 0; a < flags.length; a++) {
                build.flags |= flags[a];
            }
        }
        return build;
    }

    /**
     * 建议使用这个发送通知
     * 调用该方法可以发送通知
     *
     * @param notifyId notifyId
     * @param title    title
     * @param content  content
     */
    public void sendNotification(int notifyId, String title, String content, int icon) {
        Notification build;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 8.0以上需要特殊处理，也就是targetSDKVersion为26以上
            //通知用到NotificationCompat()这个V4库中的方法。但是在实际使用时发现书上的代码已经过时并且Android8.0已经不支持这种写法
            Notification.Builder builder = getChannelNotification(title, content, icon);
            build = builder.build();
        } else {
            NotificationCompat.Builder builder = getNotificationCompat(title, content, icon);
            build = builder.build();
        }
        if (flags != null && flags.length > 0) {
            for (int a = 0; a < flags.length; a++) {
                build.flags |= flags[a];
            }
        }
        getManager().notify(notifyId, build);
    }

    /**
     * 调用该方法可以发送通知
     *
     * @param notifyId notifyId
     * @param title    title
     * @param content  content
     */
    public void sendNotificationCompat(int notifyId, String title, String content, int icon) {
        NotificationCompat.Builder builder = getNotificationCompat(title, content, icon);
        Notification build = builder.build();
        if (flags != null && flags.length > 0) {
            for (int a = 0; a < flags.length; a++) {
                build.flags |= flags[a];
            }
        }
        getManager().notify(notifyId, build);
    }


    private NotificationCompat.Builder getNotificationCompat(String title, String content, int icon) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        } else {
            //注意用下面这个方法，在8.0以上无法出现通知栏。8.0之前是正常的。这里需要增强判断逻辑
            builder = new NotificationCompat.Builder(getApplicationContext());
            builder.setPriority(PRIORITY_DEFAULT);
        }
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(icon);
        builder.setPriority(priority);
        builder.setOnlyAlertOnce(onlyAlertOnce);
        builder.setOngoing(ongoing);
        if (remoteViews != null) {
            builder.setContent(remoteViews);
        }
        if (intent != null) {
            builder.setContentIntent(intent);
        }
        if (ticker != null && ticker.length() > 0) {
            builder.setTicker(ticker);
        }
        if (when != 0) {
            builder.setWhen(when);
        }
        if (sound != null) {
            builder.setSound(sound);
        }
        if (defaults != 0) {
            builder.setDefaults(defaults);
        }
        //点击自动删除通知
        builder.setAutoCancel(true);
        return builder;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification.Builder getChannelNotification(String title, String content, int icon) {
        Notification.Builder builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID);
        Notification.Builder notificationBuilder = builder
                //设置标题
                .setContentTitle(title)
                //消息内容
                .setContentText(content)
                //设置通知的图标
                .setSmallIcon(icon)
                //让通知左右滑的时候是否可以取消通知
                .setOngoing(ongoing)
                //设置优先级
                .setPriority(priority)
                //是否提示一次.true - 如果Notification已经存在状态栏即使在调用notify函数也不会更新
                .setOnlyAlertOnce(onlyAlertOnce)
                .setAutoCancel(true);
        if (remoteViews != null) {
            //设置自定义view通知栏
            notificationBuilder.setContent(remoteViews);
        }
        if (intent != null) {
            notificationBuilder.setContentIntent(intent);
        }
        if (ticker != null && ticker.length() > 0) {
            //设置状态栏的标题
            notificationBuilder.setTicker(ticker);
        }
        if (when != 0) {
            //设置通知时间，默认为系统发出通知的时间，通常不用设置
            notificationBuilder.setWhen(when);
        }
        if (sound != null) {
            //设置sound
            notificationBuilder.setSound(sound);
        }
        if (defaults != 0) {
            //设置默认的提示音
            notificationBuilder.setDefaults(defaults);
        }
        if (pattern != null) {
            //自定义震动效果
            notificationBuilder.setVibrate(pattern);
        }
        return notificationBuilder;
    }

    /**
     * 让通知左右滑的时候是否可以取消通知
     *
     * @param ongoing 是否可以取消通知
     * @return
     */
    public MyNotificationUtils setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
        return this;
    }

    /**
     * 设置自定义view通知栏布局
     *
     * @param remoteViews view
     * @return
     */
    public MyNotificationUtils setContent(RemoteViews remoteViews) {
        this.remoteViews = remoteViews;
        return this;
    }

    /**
     * 设置内容点击
     *
     * @param intent intent
     * @return
     */
    public MyNotificationUtils setContentIntent(PendingIntent intent) {
        this.intent = intent;
        return this;
    }

    /**
     * 设置状态栏的标题
     *
     * @param ticker 状态栏的标题
     * @return
     */
    public MyNotificationUtils setTicker(String ticker) {
        this.ticker = ticker;
        return this;
    }


    /**
     * 设置优先级
     * 注意：
     * Android 8.0以及上，在 NotificationChannel 的构造函数中指定，总共要五个级别；
     * Android 7.1（API 25）及以下的设备，还得调用NotificationCompat 的 setPriority方法来设置
     *
     * @param priority 优先级，默认是Notification.PRIORITY_DEFAULT
     * @return
     */
    public MyNotificationUtils setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * 是否提示一次.true - 如果Notification已经存在状态栏即使在调用notify函数也不会更新
     *
     * @param onlyAlertOnce 是否只提示一次，默认是false
     * @return
     */
    public MyNotificationUtils setOnlyAlertOnce(boolean onlyAlertOnce) {
        this.onlyAlertOnce = onlyAlertOnce;
        return this;
    }

    /**
     * 设置通知时间，默认为系统发出通知的时间，通常不用设置
     *
     * @param when when
     * @return
     */
    public MyNotificationUtils setWhen(long when) {
        this.when = when;
        return this;
    }

    /**
     * 设置sound
     *
     * @param sound sound
     * @return
     */
    public MyNotificationUtils setSound(Uri sound) {
        this.sound = sound;
        return this;
    }


    /**
     * 设置默认的提示音
     *
     * @param defaults defaults
     * @return
     */
    public MyNotificationUtils setDefaults(int defaults) {
        this.defaults = defaults;
        return this;
    }

    /**
     * 自定义震动效果
     *
     * @param pattern pattern
     * @return
     */
    public MyNotificationUtils setVibrate(long[] pattern) {
        this.pattern = pattern;
        return this;
    }

    /**
     * 设置flag标签
     *
     * @param flags flags
     * @return
     */
    public MyNotificationUtils setFlags(int... flags) {
        this.flags = flags;
        return this;
    }


    /*----------------------------------------*/

    /**
     * 跳转到权限设置界面
     */
    public void openNotificationSetting(Context context) {
        // vivo 点击设置图标>加速白名单>我的app
        //      点击软件管理>软件管理权限>软件>我的app>信任该软件
        Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
        if (appIntent != null) {
            context.startActivity(appIntent);
            return;
        }

        // oppo 点击设置图标>应用权限管理>按应用程序管理>我的app>我信任该应用
        //      点击权限隐私>自启动管理>我的app
        appIntent = context.getPackageManager().getLaunchIntentForPackage("com.oppo.safe");
        if (appIntent != null) {
            context.startActivity(appIntent);
            return;
        }

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }


    /**
     * 判断该app是否打开了通知
     * 可以通过NotificationManagerCompat 中的 areNotificationsEnabled()来判断是否开启通知权限。NotificationManagerCompat 在 android.support.v4.app包中，是API 22.1.0 中加入的。而 areNotificationsEnabled()则是在 API 24.1.0之后加入的。
     * areNotificationsEnabled 只对 API 19 及以上版本有效，低于API 19 会一直返回true
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            boolean areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled();
            return areNotificationsEnabled;
        }

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 生成PendingIntent
     *
     * @param context     跳转前的Activity
     * @param targetClass 跳转目标Activity
     * @return PendingIntent
     */
    public PendingIntent createPendingIntent(Context context, Class<?> targetClass) {
       return createPendingIntent(context, targetClass, null);
    }

    /**
     * 生成PendingIntent，传输参数
     *
     * @param context     跳转前的Activity
     * @param targetClass 跳转目标Activity
     * @param bundle      传输参数Bundle
     * @return PendingIntent
     */
    public PendingIntent createPendingIntent(Context context, Class<?> targetClass, Bundle bundle) {
        Intent intent = new Intent(context, targetClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

}