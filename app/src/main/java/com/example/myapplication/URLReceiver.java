package com.example.myapplication;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.notification.NotificationManager;
import com.example.myapplication.sql.LinksSQL;
import com.example.myapplication.sql.SqlHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.IOException;
import java.net.MalformedURLException;

public class URLReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        CheckURL task = new CheckURL(context);
        task.execute();
    }

    class CheckURL extends AsyncTask<String, String, String>
    {
        private Context context;

        public CheckURL(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params)
        {
            SqlHelper sql = SqlHelper.getInstance();

            if (!isMyServiceRunning(URLChecker.class))
            {
                context.startService(new Intent(context, URLChecker.class));
            }

            for (LinksSQL.Link link : sql.getLinksSQL().selectLinks())
            {
                try
                {
                    Document pod = Jsoup.connect(link.getLink()).ignoreContentType(true).get();
                    Gson gson = new Gson();
                    JsonObject object = gson.fromJson(pod.text(), JsonObject.class);
                    int total = Integer.parseInt(object.get("total").toString());

                    Long oldCount = link.getCount();
                    if (oldCount == total)
                    {
                        continue;
                    }

                    sql.getLinksSQL().updateCount(link.getId(), total);
                    return String.valueOf(oldCount < total);
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return "false";
        }

        private boolean isMyServiceRunning(Class<?> serviceClass)
        {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            {
                if (serviceClass.getName().equals(service.service.getClassName()))
                {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            if (Boolean.parseBoolean(result))
            {
                String title = "Напоминание";
                String description = "Появилось новое предложение";

                NotificationCompat.Builder builder = NotificationManager.createNotification(context, title, description);
                builder.setVibrate(new long[]{300, 300, 300, 300, 300})
                        .setLights(0xFFFF0000, 300, 100)
                        .setPriority(NotificationCompat.PRIORITY_MAX);

                NotificationManager.sendNotification(context, builder.build());
            }
        }
    }
}
