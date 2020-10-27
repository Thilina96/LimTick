package com.proj.limtick;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class LayoutActivity extends AppCompatActivity {
    Button notifybtn;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);


        notifybtn=(Button) findViewById(R.id.button_noti);
        notifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                PendingIntent pIntent = PendingIntent.getActivity(LayoutActivity.this,0,intent,0);
                Notification noti = new Notification.Builder(LayoutActivity.this)
                        .setTicker("Ticker Title")
                        .setContentTitle("Content Title")
                        .setContentText("Bus has Arrived at the station")


                        .setSmallIcon(R.mipmap.ic_launcher)
                        .addAction(R.drawable.logout,"Available",pIntent)
                        .addAction(R.drawable.logout,"NotAvailable",pIntent)
                       // .setTimeoutAfter (1000)
                        .setContentIntent(pIntent).getNotification();





                noti.flags = Notification.FLAG_AUTO_CANCEL;
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    String channelId = "Your_channel_id";
                    NotificationChannel channel = new NotificationChannel(
                            channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_HIGH);
                    NotificationManager notificationManager =
                            getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }
                nm.notify(0,noti);
            }
        });
    }
}