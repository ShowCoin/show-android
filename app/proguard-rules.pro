# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/apple/Library/Android/sdk/tools/proguard/proguard-android.txt
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
#*******************************混淆参数配置----------------------------------start

#日志数据存放位置。路径可以自行更换
-dump build/outputs/mapping/class_files.txt
-printseeds build/outputs/mapping/seeds.txt
-printusage build/outputs/mapping/unused.txt
-printmapping build/outputs/mapping/mapping.txt


-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-ignorewarnings
#-dontoptimize
-repackageclasses ''
-verbose
-allowaccessmodification
#--------------------------混淆参数配置**********************************************end

#----------------注解------------------#
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation { *; }

#<=============高德地图  start ================>

-keep class com.amap.api.location.**{*;}

-keep class com.amap.api.fence.**{*;}

-keep class com.autonavi.aps.amapapi.model.**{*;}
#<=============高德地图  end ================>

#================ Android通用混淆配置================start

-keepattributes Exceptions,InnerClasses,Signature
-keepattributes SourceFile,LineNumberTable

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#必须keep 这些继承的class,出现在xml中,都需要keep class
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.opengl.** {*;}
-keep public class * extends com.umeng.**


-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclasseswithmembers public class * {
public static void main(java.lang.String[]);
}

-dontwarn android.net.http.**
-keep class android.net.http.** {*;}

-dontwarn android.support.**
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.FragmentActivity

# 如果有使用Annotation,通过这个来keep
-keepattributes *Annotation*


-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService



# native method
-keepclasseswithmembernames class * {
    native  <methods>;
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
    *** get*();
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}


# 如果你在xml中定义方法:android:onClick="onBtnClicked"
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}


# 枚举类型keep
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);

}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}


-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# 第三方jar有R文件,需要keep
-keep class **.R$* {
 *;
}

#google service
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

#================ Android通用混淆配置 ================ end

#**********************  融云混淆配置    start********************

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
 public *;
}
-keepattributes Exceptions,InnerClasses

-keepattributes Signature

-keepattributes *Annotation*

-keepattributes EnclosingMethod

-keep class com.google.gson.examples.android.model.** { *; }

-keep class **$Properties

-dontwarn org.eclipse.jdt.annotation.**


-keep class io.rong.common.** {*;}

-keep class io.rong.eventbus.** {*;}
-keep class io.rong.eventbus.util.** {*;}

-keep class io.rong.imageloader.cache.disc.** {*;}
-keep class io.rong.imageloader.cache.disc.impl.** {*;}
-keep class io.rong.imageloader.cache.disc.impl.ext.** {*;}
-keep class io.rong.imageloader.cache.disc.naming.** {*;}
-keep class io.rong.imageloader.cache.memory.** {*;}
-keep class io.rong.imageloader.cache.memory.impl.** {*;}
-keep class io.rong.imageloader.core.** {*;}
-keep class io.rong.imageloader.core.assist.** {*;}
-keep class io.rong.imageloader.core.assist.deque.** {*;}
-keep class io.rong.imageloader.core.decode.** {*;}
-keep class io.rong.imageloader.core.display.** {*;}
-keep class io.rong.imageloader.core.download.** {*;}
-keep class io.rong.imageloader.core.imageaware.** {*;}
-keep class io.rong.imageloader.core.listener.** {*;}
-keep class io.rong.imageloader.core.process.** {*;}
-keep class io.rong.imageloader.utils.** {*;}

-keep class io.rong.imkit.** {*;}
-keep class io.rong.imkit.activity.** {*;}
-keep class io.rong.imkit.cache.** {*;}
-keep class io.rong.imkit.common.** {*;}
-keep class io.rong.imkit.fragment.** {*;}
-keep class io.rong.imkit.model.** {*;}
-keep class io.rong.imkit.notification.** {*;}
-keep class io.rong.imkit.tools.** {*;}
-keep class io.rong.imkit.userInfoCache.** {*;}
-keep class io.rong.imkit.utils.** {*;}
-keep class io.rong.imkit.utils.widget.** {*;}
-keep class io.rong.imkit.utils.widget.adapter.** {*;}
-keep class io.rong.imkit.utils.widget.provider.** {*;}


-keep class io.rong.imlib.** {*;}
-keep class io.rong.imlib.common.** {*;}
-keep class io.rong.imlib.filetransfer.** {*;}
-keep class io.rong.imlib.ipc.** {*;}
-keep class io.rong.imlib.location.** {*;}
-keep class io.rong.imlib.location.message.** {*;}
-keep class io.rong.imlib.model.** {*;}
-keep class io.rong.imlib.navigation.** {*;}
-keep class io.rong.imlib.stateMachine.** {*;}
-keep class io.rong.imlib.statistics.** {*;}
-keep class io.rong.imlib.TypingMessage.** {*;}

-keep class io.rong.message.** {*;}
-keep class io.rong.message.utils.** {*;}


-keep class io.rong.photoview.** {*;}
-keep class io.rong.photoview.gestures.** {*;}
-keep class io.rong.photoview.log.** {*;}
-keep class io.rong.photoview.scrollerproxy.** {*;}

-keep class io.rong.push.** {*;}
-keep class io.rong.push.common.** {*;}
-keep class io.rong.push.common.stateMachine.** {*;}
-keep class io.rong.push.core.** {*;}
-keep class io.rong.push.notification.** {*;}
-keep class io.rong.push.platform.** {*;}

-keep class * implements io.rong.imlib.model.MessageContent{*;}


#**********************  融云混淆配置    end********************

#******************************************忽略所有三方jar-------------------------------------------start

#如果不是gradle打包，需要把lib中的jar包一个个引用进来

#-libraryjars libs/alipaySdk-20151215.jar
#-libraryjars libs/ijkplayer_exo.jar
#-libraryjars libs/ijkplayer_media.jar
#-libraryjars libs/libammsdk.jar
#-libraryjars libs/mta-sdk-1.6.2.jar
#-libraryjars libs/open_sdk_r5756_lite.jar
#-libraryjars libs/weiboSDKCore_3.1.4.jar


#微信
-keep class com.tencent.mm.sdk.** {*;}
#腾讯
-keep class com.tencent.** {*;}

#百度地图
-keep class vi.com.gdi.bgl.android.**{*;}
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

#支付宝
-keep class com.alipay.** {*;}
-keep class com.ta.utdid2.** {*;}
-keep class com.ut.device.** {*;}
-keep class org.json.alipay.** {*;}

#支付宝
#-keep class com.alipay.android.app.IAlixPay{*;}
#-keep class com.alipay.android.app.IAlixPay$Stub{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
#-keep class com.alipay.sdk.app.PayTask{ public *;}
#-keep class com.alipay.sdk.app.AuthTask{ public *;}
#新浪
-keep class com.sina.** {*;}
-keep class com.sina.weibo.sdk.component.WeiboSdkBrowser{*;}
-dontwarn com.weibo.sdk.android.WeiboDialog
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient
-keep public class android.net.http.SslError{*;}
-keep public class android.webkit.WebView{*;}
-keep public class android.webkit.WebViewClient{*;}
-keep public class android.webkit.WebChromeClient{*;}
-keep public interface android.webkit.WebChromeClient$CustomViewCallback {*;}
-keep public interface android.webkit.ValueCallback {*;}
-keep class * implements android.webkit.WebChromeClient {*;}
#友盟
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class [tv.beke].R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#极光
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}


#eventbus
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


#--------- fresco  混淆配置  start-------------------------------#


# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**


#--------- fresco  混淆配置  end-------------------------------#




  # ProGuard configurations for Bugtags
   -keepattributes LineNumberTable,SourceFile

   -keep class com.bugtags.library.** {*;}
   -dontwarn com.bugtags.library.**
   -keep class io.bugtags.** {*;}
   -dontwarn io.bugtags.**
   -dontwarn org.apache.http.**
   -dontwarn android.net.http.AndroidHttpClient

   # End Bugtags


   # ProGuard configurations for TalkingData

  -dontwarn com.tendcloud.tenddata.**
  -dontwarn com.tendcloud.**

  -keep class com.talkingdata.sdk.** {*;}
  -keep class com.tendcloud.** {*;}
  -keep public class com.tendcloud.tenddata.** { public protected *;}



  -dontwarn com.tendcloud.tenddata.**
  -keep class com.tendcloud.** {*;}
  -keep public class com.tendcloud.tenddata.** { public protected *;}
  -keepclassmembers class com.tendcloud.tenddata.**{
  public void *(***);
  }
  -keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
  -keep class com.apptalkingdata.** {*;}
  -keep class dice.** {*; }
  -dontwarn dice.**

  # End TalkingData

  # 阿里云存储-------------start
  -keep class com.alibaba.sdk.android.oss.** { *; }
  -dontwarn org.apache.commons.codec.binary.**
  # 阿里云存储-------------end


#******************************************忽略所有三方jar-------------------------------------------end

#***********************************************自定义配置-------------------------------------------start

#json实体类不能混淆，需要序列化本地的类不能混淆，ormlite数据库映射model不能混淆


-keep class one.show.live.po.** {*;}
-keep  class one.show.live.po.**$*{*;}
-keepclassmembers class one.show.live.po.** {*;}
-keepclassmembers class one.show.live.po.**$* {*;}
-keepnames class one.show.live.po.** {
    public <fields>;
    public <methods>;
}
-keepnames class one.show.live.po.**$* {
    public <fields>;
    public <methods>;
}

-keep class one.show.live.po.** {*;}
-keep  class one.show.live.po.**$*{*;}
-keepclassmembers class one.show.live.po.** {*;}
-keepclassmembers class one.show.live.po.**$* {*;}
-keepnames class one.show.live.po.** {
    public <fields>;
    public <methods>;
}
-keepnames class one.show.live.po.**$* {
    public <fields>;
    public <methods>;
}

-keep class one.show.live.po.eventbus.** {*;}
-keepclassmembers class one.show.live.po.eventbus.** {*;}
-keepnames class one.show.live.po.eventbus.** {
    public <fields>;
    public <methods>;
}
#***********************************************po or JavaBen  End-------------------------------------------end

#***********************************************ijkplayer-------------------------------------------end
-keep class tv.danmaku.ijk.media.player.** {*; }
-keep class tv.danmaku.ijk.media.player.IjkMediaPlayer{
*;
}
-keep class tv.danmaku.ijk.media.player.ffmpeg.FFmpegApi{
*;
}
#***********************************************ijkplayer end-------------------------------------------end


-keep class one.show.live.MediaObject {*;}
-keep  class one.show.live.MediaObject$MediaPart {*;}
-keepclassmembers class one.show.live.camera.base.MediaObject {*;}
-keepclassmembers class one.show.live.camera.base.MediaObject$MediaPart{*;}
-keepnames class one.show.live.camera.base.MediaObject {
    public <fields>;
    public <methods>;
}
-keepnames class one.show.live.camera.base.MediaObject$MediaPart {
    public <fields>;
    public <methods>;
}


#底层通过jni调用的上层类，需要通过类名查找加载的，不混淆
-dontwarn com.me.relex.**
-keep class com.me.relex.** { *; }

-dontwarn com.vlee78.android.media.**
-keep class com.vlee78.android.media.** { *; }

-keep class wseemann.media.**{*; }


# greeddao
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class one.show.live.greendao.** {*;}
-keep  class one.show.live.greendao.**$*{*;}


#--------------- BEGIN: 返回到页面的自定义Java对象防混淆 ----------
-keepclassmembers class one.show.live.personal.ui.OneWebViewActivity$*{ *; }
#--------------- END ----------

#--------------- BEGIN: 注入到页面的接口类防混淆 ----------
-keepclassmembers class one.show.live.personal.ui.OneWebViewActivity{ *; }
#--------------- END ----------

-keep class com.douyaim.effect.face**{*;}
-keep class com.douyaim.effect.imp**{*;}
-keep class com.douyaim.effect.model**{*;}
-keep class com.douyaim.argame.effect**{*;}
-keep class com.douyaim.argame.presenter**{*;}
-keep class com.hulu.sdk.**{*;}
-keep class com.hulu.sdk.**{public <init>();}
-keep class com.douyaim.argame.**{*;}
-keep class com.douyaim.qsapp.jni.**{*;}
-keep class one.show.live.recorder.newrecord.bean.**{*;}
-keep class com.newborntown.android.solo.video.ffmpeg.**{*;}


#底层通过jni调用的上层类，需要通过类名查找加载的，不混淆
-keep class * implements com.coremedia.iso.boxes.Box {* ; }
-dontwarn com.coremedia.iso.boxes.*
-dontwarn com.googlecode.mp4parser.authoring.tracks.mjpeg.**
-dontwarn com.googlecode.mp4parser.authoring.tracks.ttml.**

#Rxjava RxAndroid
 -dontwarn rx.*
 -dontwarn sun.misc.**

 -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
 }

 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode producerNode;
 }

 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode consumerNode;
 }

 -keep class com.lsjwzh.widget.recyclerviewpager.**
 -dontwarn com.lsjwzh.widget.recyclerviewpager.**
