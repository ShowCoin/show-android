package one.show.live.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;

import one.show.live.R;
import one.show.live.util.DeviceUtils;

/**
 */
public class NotificationUtils {

    public static Notification getNotification(Context context,
                                                CharSequence message, PendingIntent pendIntent) {
        return getNotification(context, R.mipmap.ic_launcher,message,pendIntent);
    }


    public static Notification getNotification(Context context,int icon,
                                               CharSequence message, PendingIntent pendIntent) {
        return getNotification(context,icon,context.getString(R.string.app_name),message,pendIntent);
    }

    public static Notification getNotification(Context context,int icon,CharSequence title,
                                               CharSequence message, PendingIntent pendIntent) {
        return getNotification(context,icon,title,message,message,pendIntent,true,null);
    }



    public static Notification getNotification(Context context, int icon, CharSequence title, CharSequence ticker,
                                               CharSequence message, PendingIntent pendIntent, boolean autoCancel, RemoteViews view) {
        Notification.Builder builder = new Notification.Builder(context).setTicker(ticker)
                .setSmallIcon(icon)
                .setAutoCancel(autoCancel)
                .setContentIntent(pendIntent)
                .setContentTitle(title)
                .setContentText(message);
        if(view !=null){
            builder.setContent(view);
        }

        if (DeviceUtils.hasJellyBean()) {
            return builder.build();
        } else {
            return builder.getNotification();
        }
    }

}
