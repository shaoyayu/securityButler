package icu.shaoyayu.android.security.butler.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * @author shaoyayu
 * 通知栏管理工具
 */
public class NotificationUtil {

    private static final String TAG = "NotificationUtil";

    /**
     * 条转通知管理类，
     * @param activity
     * @param intent
     * @param idp
     * @param icon
     * @param title
     * @param text
     */
    public static void showNotification(Context activity, Intent intent, int idp, int icon, String title, String text) {
        if (intent.resolveActivity(activity.getPackageManager()) == null){
            Log.w(TAG,"intent.resolveActivity(activity.getPackageManager()) == null");
            return;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(activity,0,intent,0);
        NotificationManager manager = (NotificationManager)activity.getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= 26) {
            //当sdk版本大于26
            String id = "channel_1";
            String description = "143";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(activity, id)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            manager.notify(idp, notification);
        } else {
            Notification notification = new NotificationCompat.Builder(activity)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(icon)
                    .build();
            manager.notify(idp,notification);
        }
    }

    /**
     * 显示提示但是没有跳转
     * @param activity
     * @param idp
     * @param icon
     * @param title
     * @param text
     */
    public static void showNotification(Context activity, int idp, int icon, String title, String text) {
        NotificationManager manager = (NotificationManager)activity.getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= 26) {
            //当sdk版本大于26
            String id = "channel_1";
            String description = "143";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(activity, id)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true)
                    .build();
            manager.notify(idp, notification);
        } else {
            Notification notification = new NotificationCompat.Builder(activity)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(icon)
                    .build();
            manager.notify(idp,notification);
        }
    }

}
