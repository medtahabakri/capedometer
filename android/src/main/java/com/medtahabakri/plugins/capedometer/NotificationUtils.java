
package com.medtahabakri.plugins.capedometer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationUtils {
    private static final String CHANNEL_ID = "step_counter_channel";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "Step Counter Background",
                NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public static Notification getNotification(Context context) {
        return new NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Step Counter Running")
            .setContentText("Tracking steps in the background")
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .build();
    }
}
