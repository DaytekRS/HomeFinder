package com.example.myapplication.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class NotificationManager {
    public static final String CHANNEL_ID = "Home Finder";

    public static final String FOREGROUND_CHANNEL_ID = "Foreground Home Finder";

    private static int NOTIFY_ID = 101;

    private static NotificationChannel NOTIFICATION_CHANNEL;

    private static NotificationChannel FOREGROUND_NOTIFICATION_CHANNEL;

    public static NotificationCompat.Builder createNotification(Context context, String title, String text)
    {
        return createNotification(context, title, text, null);
    }

    public static NotificationCompat.Builder createNotification(Context context, String title, String text, PendingIntent intent)
    {
        String channelId = intent != null ? FOREGROUND_CHANNEL_ID : CHANNEL_ID;
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        if (intent != null)
        {
            notificationBuilder.setContentIntent(intent);
        }
        else
        {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        return notificationBuilder;
    }

    public static void sendForegroundNotification(Service context, String title, String text)
    {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            if (FOREGROUND_NOTIFICATION_CHANNEL == null)
            {
                android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                FOREGROUND_NOTIFICATION_CHANNEL = new NotificationChannel(FOREGROUND_CHANNEL_ID, "Foreground channel", android.app.NotificationManager.IMPORTANCE_DEFAULT);
                FOREGROUND_NOTIFICATION_CHANNEL.setDescription("Home Finder");
                notificationManager.createNotificationChannel(FOREGROUND_NOTIFICATION_CHANNEL);
            }

            context.startForegroundService(notificationIntent);
        }
        context.startForeground(1, createNotification(context, title, text, pendingIntent).build());
    }

    public static void sendNotification(Context context, String title, String text)
    {
        sendNotification(context, createNotification(context, title, text).build());
    }

    public static void sendNotification(Context context, Notification notification)
    {
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            if (NOTIFICATION_CHANNEL == null)
            {
                NOTIFICATION_CHANNEL = new NotificationChannel(CHANNEL_ID, "Channel", android.app.NotificationManager.IMPORTANCE_HIGH);
                NOTIFICATION_CHANNEL.setDescription("Home Finder");
                notificationManager.createNotificationChannel(NOTIFICATION_CHANNEL);
            }
        }
        notificationManager.notify(NOTIFY_ID++, notification);
    }
}
