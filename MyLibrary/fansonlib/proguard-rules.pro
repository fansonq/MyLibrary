# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\fanson\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep com.dou361.ijkplayer.** {
*;
}


#Universal Image Loader
-keep class com.nostra13.universalimageloader.** { *; }
-keepattributes Signature

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep  class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
    }
-keep  class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
-keepnames  class * implements com.bumptech.glide.module.GlideModule

-dontwarn jp.co.cyberagent.android.gpuimage.**
-keep class jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter{*;}
-keep class jp.co.cyberagent.android.gpuimage.GPUImage{*;}
-keep class jp.co.cyberagent.android.gpuimage.GPUImageKuwaharaFilter{*;}
-keep class jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter{*;}



#okhttp
-dontwarn com.squareup.okhttp.
-keep class com.squareup.okhttp.{*;}
-keep class com.zhy.http.okhttp.{*;}
-keep interface com.squareup.okhttp.{ *;}
-dontwarn okio.*
-keep class com.google.gson.{*;}
-keep class com.google.gson.JsonObject{ *;}
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**

-optimizationpasses 5 # 指定代码的压缩级别
-dontusemixedcaseclassnames # 是否使用大小写混合
-skipnonpubliclibraryclasses #跳过(不混淆) jars中的 非public classes
-dontpreverify # 混淆时是否做预校验
-verbose # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/* # 混淆时所采用的算法

#四大组件
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application {*;}
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

#adapter
-keep public class * extends android.widget.BaseAdapter {*;}

#自定义View
-keep public class * extends android.view.View {*;}

#Universal Image Loader
-keep class com.nostra13.universalimageloader.** { *; }
-keepattributes Signature

#android-async-http
-dontwarn com.loopj.android.http.**
-keep class com.loopj.android.http.** { *; }
-keep class cz.msebera.** { *; }

#CircleImageView
-keep class de.hdodenhof.circleimageview.** { *; }

#Gson
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.** { *;}
#这句非常重要，主要是滤掉 com.bgb.scan.model包下的所有.class文件不进行混淆编译
-keep class com.example.fansonlib.bean.** {*;}

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep  class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
    }
-keep  class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
-keepnames public class * implements com.bumptech.glide.module.GlideModule

#rxjava
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}
-keepclassmembers class rx.internal.util.unsafe.** {
    long producerIndex;
    long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontwarn rx.internal.util.unsafe.**

#retrofit2.X
-dontwarn retrofit.
-keep class retrofit.{*;}
-keepattributes Signature
-keepattributes Exceptions
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8

#okhttp
-dontwarn com.squareup.okhttp.
-keep class com.squareup.okhttp.{*;}
-keep class com.zhy.http.okhttp.{*;}
-keep interface com.squareup.okhttp.{ *;}
-dontwarn okio.*
-keep class com.google.gson.{*;}
-keep class com.google.gson.JsonObject{ *;}
# OkHttp3
-dontwarn okhttp3.**
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**


-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

-dontwarn com.viewpagerindicator.**

# -- Android Annotations --
-dontwarn org.springframework.**

#Material
-keep class me.drakeet.materialdialog.** { *; }

#EventBus
-keepclassmembers class ** {
    public void onEvent*(**);
}

#DBFlow
-keep class com.raizlabs.android.dbflow.**{*;}
-keep class com.raizlabs.dbflow.android.sqlcipher.**{*;}

#腾讯云
-keep class com.tencent.**{*;}
-dontwarn com.tencent.**
-keep class tencent.**{*;}
-dontwarn tencent.**
-keep class qalsdk.**{*;}
-dontwarn qalsdk.**
#小米
#这里com.tencent.imsdk.MiPushMessageReceiver改成app中定义的完整类名
-keep class com.yinghai.loopRider.MiPushMessageReceiver.** {*;}
#可以防止一个误报的 warning 导致无法成功编译，如果编译使用的 Android 版本是 23。
-dontwarn com.xiaomi.push.**
#华为
-keep class com.huawei.android.pushagent.**{*;}
-keep class com.huawei.android.pushselfshow.**{*;}
-keep class com.huawei.android.microkernel.**{*;}
-keep class com.baidu.mapapi.**{*;}

#umeng
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class [com.yinghai.loopRider].R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#3D 地图 V5.0.0之前：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.amap.mapcore.*{*;}
-keep   class com.amap.api.trace.**{*;}
#3D 地图 V5.0.0之后：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}
#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
#搜索
-keep   class com.amap.api.services.**{*;}
#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

#讯飞语音
-keep class com.iflytek.**{*;}

# LeakCanary
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }


# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

 # 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

 # 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

 # 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

 # 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


#引用v4包
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

#引用v7包
# Keep the support library
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }


#xUtils(保持注解，及使用注解的Activity不被混淆，不然会影响Activity中你使用注解相关的代码无法使用)
-keep class * extends java.lang.annotation.Annotation {*;}
-keep class com.otb.designerassist.activity.** {*;}

 #保护引用的第三方jar包不被混淆
-dontwarn Java.awt.**
-dontwarn java.beans.Beans
-keep class org.apache.harmony.awt.** {*;}
-keep class org.apache.harmony.misc.** {*;}


-keepattributes Signature  #过滤泛型（不写可能会出现类型转换错误，一般情况把这个加上就是了）

-keepattributes *Annotation*  #假如项目中有用到注解，应加入这行配置

-keep class **.R$* { *; }  #保持R文件不被混淆，否则，你的反射是获取不到资源id的

-keepclassmembers class * extends android.webkit.WebChromeClient {  #如果你的项目中用到了webview的复杂操作 ，最好加入
     public void *(android.webkit.WebView,java.lang.String);
}

# 源文件和行号的信息不混淆
-keepattributes SourceFile,LineNumberTable

-renamesourcefileattribute SourceFile

-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**


-keep class org.apache.**
-dontwarn org.apache.**
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**

-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.android.volley.toolbox.**



-ignorewarnings
