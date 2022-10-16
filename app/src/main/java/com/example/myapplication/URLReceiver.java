package com.example.myapplication;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.sql.LinksSQL;
import com.example.myapplication.sql.SqlHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class URLReceiver extends BroadcastReceiver {
    private static String CHANNEL_ID = "Cat channel";
    private static final int NOTIFY_ID = 101;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("hmm", "URLReceiver");
        CheckURL task = new CheckURL(context);
        task.execute();
    }

    class CheckURL extends AsyncTask<String, String, String> {
        private Context context;

        public CheckURL(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            SqlHelper sql = URLChecker.sql;
            if (sql == null) {
                try {
                    sql = new SqlHelper(context);
                    URLChecker.sql = sql;
                    if ( !isMyServiceRunning(URLChecker.class)) context.startService(new Intent(context, URLChecker.class));
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            String newFlat = "false";
            for (LinksSQL.Link link : sql.getLinksSQL().selectLinks()) {
                try {
                    Document doc = Jsoup.connect(link.getName()).get();

                    Elements buttons = doc.getElementsByTag("button");
                    Element button = null;
                    for (Element el : buttons){
                        if (el.text().contains("Показать")) button = el;
                    }
                    if (button != null) {
                        String answer = button.text();
                        String dec = (answer.split(" "))[1];
                        dec = dec.substring(1, dec.length() - 1);
                        Log.d("count = ", link.getCount().toString());
                        Log.d("parse count = ", dec);
                        if (link.getCount().compareTo(Long.parseLong(dec)) < 0) {
                            newFlat = "true";
                        }
                        if (!link.getCount().equals(Long.parseLong(dec))) {
                            sql.getLinksSQL().updateCount(link.getId(), Long.parseLong(dec));
                        }
                    }else {
                        Log.d("hmmm","Don't found button");
                    }
                } catch (IOException e) {

                }
            }
            return newFlat;
        }

        private boolean isMyServiceRunning(Class<?> serviceClass) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("hmm", result);
            if (Boolean.parseBoolean(result)) {
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle("Напоминание")
                                .setContentText("Появилось новое предложение")
                                .setVibrate(new long[]{300, 300, 300, 300, 300})
                                .setLights(0xFFFF0000, 300, 100)
                                .setPriority(NotificationCompat.PRIORITY_MAX);
                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(context);
                notificationManager.notify(NOTIFY_ID, builder.build());
            }
        }


    }
}
