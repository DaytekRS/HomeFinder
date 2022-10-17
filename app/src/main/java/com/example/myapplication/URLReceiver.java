package com.example.myapplication;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.sql.LinksSQL;
import com.example.myapplication.sql.SqlHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.IOException;
import java.net.MalformedURLException;

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
            Boolean newFlat = false;
            for (LinksSQL.Link link : sql.getLinksSQL().selectLinks()) {
                try {
                    //https://cre-api-v2.kufar.by/items-search/v1/engine/v1/search/map?cat=1010&gtsy=country-belarus~province-vitebskaja_oblast~locality-novopolock&rnt=1&size=5000&typ=let
                    //https://cre-api-v2.kufar.by/items-search/v1/engine/v1/search/map?cat=1010&gtsy=country-belarus~province-vitebskaja_oblast~area-polockij_rajon~locality-polock&rnt=1&size=5000&typ=let
                    Document pod = Jsoup.connect("http://cre-api-v2.kufar.by/items-search/v1/engine/v1/search/rendered-paginated?cat=1010&cur=USD&gtsy=&lang=ru&rnt=1&size=30&typ=let").ignoreContentType(true).get();
                    Gson gson = new Gson();
                    JsonObject object = gson.fromJson(pod.text(), JsonObject.class);
                    int total = Integer.parseInt(object.get("total").toString());

                    newFlat = link.getCount() < total;
                    if (link.getCount() > total || newFlat) {
                        sql.getLinksSQL().updateCount(link.getId(), total);

                        if (newFlat) {
                            break;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return newFlat.toString();
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
            Log.d("New house find:", result);
            if (Boolean.parseBoolean(result)) {
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle("Напоминание")
                                .setContentText("Появилось новое предложение")
                                .setVibrate(new long[]{300, 300, 300, 300, 300})
                                .setLights(0xFFFF0000, 300, 100)
                                .setPriority(NotificationCompat.PRIORITY_MAX);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel", NotificationManager.IMPORTANCE_HIGH);
                    channel.setDescription("Home Finder");
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify(NOTIFY_ID, builder.build());
            }
        }


    }
}
