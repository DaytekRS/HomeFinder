package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.example.myapplication.notification.NotificationManager;

public class URLChecker extends Service
{
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    public int onStartCommand(Intent intent_, int flags, int startId)
    {
        String title = "Я слежу за куфаром. Никто не пройдет мимо";
        String text = "Хозяин я ищу для тебя квартиру XD";
        NotificationManager.sendForegroundNotification(this, title, text);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, URLReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT );
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        alarmManager.cancel(pendingIntent);
        stopSelf();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
