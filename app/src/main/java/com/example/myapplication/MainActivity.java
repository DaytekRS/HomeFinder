package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.notification.NotificationManager;
import com.example.myapplication.sql.SqlHelper;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, URLChecker.class));
        setContentView(R.layout.activity_main);
        SqlHelper.createInstance(getBaseContext());
    }

    public void onSettingsClick(View view)
    {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onClick(View view)
    {
        NotificationManager.sendNotification(this, "Напоминание", "Пора покормить кота");
    }
}
