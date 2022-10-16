package com.example.myapplication;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.sql.SqlHelper;



public class URLChecker  extends Service {
    private AlarmManager am;
    private PendingIntent pendingIntent;
    public static SqlHelper sql;
    public static long count;
    private static String CHANNEL_ID = "Cat channel";

    @Override
    public void onCreate() {
        super.onCreate();
        count=0;
    }

    @Override
    public int onStartCommand(Intent intent_, int flags, int startId) {
        /* Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Я слежу за куфаром. Никто не пройдет мимо")
                .setContentText("Хозяин я ищу для тебя квартиру XD")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);*/
        if (sql == null) {
            try {
                sql = new SqlHelper(getBaseContext());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        Log.d("hmm","Start");
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, URLReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT );
        am.cancel(pendingIntent);
        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),300000 , pendingIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),10000 , pendingIntent);

        Log.d("hmm","Start2");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("hmm","onDestroy");
        am.cancel(pendingIntent);
        stopSelf();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
