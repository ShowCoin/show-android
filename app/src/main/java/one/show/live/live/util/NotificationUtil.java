package one.show.live.live.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import java.lang.reflect.Field;
import one.show.live.R;
import one.show.live.home.ui.HomePageActivity;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public class NotificationUtil {

  private Context context;

  public static NotificationUtil getInstance(Context context) {
    NotificationUtil notificationUtil = new NotificationUtil(context);
    return notificationUtil;
  }

  public NotificationUtil(Context context) {
    this.context = context;
  }

  public void sendBadgeNumber(int num) {
    String number = "35";
    if (TextUtils.isEmpty(number)) {
      number = "0";
    } else {
      int numInt = Integer.valueOf(number);
      number = String.valueOf(Math.max(0, Math.min(numInt, 99)));
    }

    if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
      sendToXiaoMi(number);
    } else if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
      sendToSony(number);
    } else if (Build.MANUFACTURER.toLowerCase().contains("sony")) {
      sendToSamsumg(number);
    }
  }

  private void sendToXiaoMi(String number) {
    NotificationManager nm =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    Notification notification = null;
    boolean isMiUIV6 = true;
    try {
      NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
      builder.setContentTitle("您有" + number + "未读消息");
      builder.setTicker("您有" + number + "未读消息");
      builder.setAutoCancel(true);
      builder.setSmallIcon(R.mipmap.app_icon);
      builder.setDefaults(Notification.DEFAULT_LIGHTS);
      notification = builder.build();
      Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
      Object miuiNotification = miuiNotificationClass.newInstance();
      Field field = miuiNotification.getClass().getDeclaredField("messageCount");
      field.setAccessible(true);
      field.set(miuiNotification, number);// 设置信息数
      field = notification.getClass().getField("extraNotification");
      field.setAccessible(true);
      field.set(notification, miuiNotification);
    } catch (Exception e) {
      e.printStackTrace();
      //miui 6之前的版本
      isMiUIV6 = false;
      Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
      localIntent.putExtra("android.intent.extra.update_application_component_name",
          context.getPackageName() + "/" + HomePageActivity.class);
      localIntent.putExtra("android.intent.extra.update_application_message_text", number);
      context.sendBroadcast(localIntent);
    } finally {
      if (notification != null && isMiUIV6) {
        //miui6以上版本需要使用通知发送
        nm.notify(101010, notification);
      }
    }
  }

  private void sendToSony(String number) {
    boolean isShow = true;
    if ("0".equals(number)) {
      isShow = false;
    }
    Intent localIntent = new Intent();
    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);//是否显示
    localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",
        HomePageActivity.class);//启动页
    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", number);//数字
    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME",
        context.getPackageName());//包名
    context.sendBroadcast(localIntent);
  }

  private void sendToSamsumg(String number) {
    Intent localIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
    localIntent.putExtra("badge_count", number);//数字
    localIntent.putExtra("badge_count_package_name", context.getPackageName());//包名
    localIntent.putExtra("badge_count_class_name", HomePageActivity.class); //启动页
    context.sendBroadcast(localIntent);
  }
}
