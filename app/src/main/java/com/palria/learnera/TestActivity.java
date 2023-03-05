package com.palria.learnera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RemoteViews;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        // Create a notification channel (required for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("my_channel_id", "My Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

// Create a custom layout for the notification
        @SuppressLint("RemoteViewLayout")
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_layout);
        //set image cover icon here
        notificationLayout.setImageViewResource(R.id.icon, R.drawable.book_cover2);
        // Create an explicit intent for launching the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        // Create a PendingIntent from the intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        int notificationId = (int) System.currentTimeMillis(); // generate a unique ID
        NotificationCompat.Builder builder = new NotificationCompat.Builder(TestActivity.this, "my_channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_24) //notification icon
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCustomBigContentView(notificationLayout)
                .setCustomHeadsUpContentView(notificationLayout)
                .setContentIntent(pendingIntent) //intent to start when click notification
                .setOnlyAlertOnce(true) //only show once not when updating progress
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Expanded content"))
                .setOngoing(true) //set ongoing with is not cancelable by user
                .setAutoCancel(false); //auto cancel false

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());

        // Create a Handler to update the notification loader and percent every second
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            //uplod percentage count .
            int percentageCompleted = 0;
            @Override
            public void run() {
               if(percentageCompleted==100){
                   handler.removeCallbacks(this);
                   //cancel all or single notification
                   notificationManager.cancelAll();
                   showCompletedNotification("kldsjf9asdf908as09df9s");
               }else{
                   // Update the  with the current time
                   percentageCompleted++;

                   notificationLayout.setProgressBar(R.id.progress_bar,100,percentageCompleted,false);
                   notificationLayout.setTextViewText(R.id.percent,percentageCompleted+"% completed");
                   // Update the notification
                   NotificationManagerCompat.from(getApplicationContext()).notify(notificationId, builder.build());
                   // Schedule the next update in 1 second
                   handler.postDelayed(this, 100);//make it 1000
               }
            }
        };
// Start the periodic updates
        handler.postDelayed(runnable, 100);//make it 1000


    }

    public void showCompletedNotification(String pageId){
        @SuppressLint("RemoteViewLayout")
                //customize notification
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_layout);
        notificationLayout.setTextViewText(R.id.title, "Upload Completed");
        notificationLayout.setProgressBar(R.id.progress_bar,100,100,false);
        notificationLayout.setTextViewText(R.id.percent, "100% completed");
        notificationLayout.setImageViewResource(R.id.icon,R.drawable.book_cover2);


        //intent where to redirect the user
        Intent intent = new Intent(TestActivity.this, PageActivity.class);
        intent.putExtra("PAGE_ID",pageId);

        // Create a PendingIntent from the intent
        PendingIntent pendingIntent = PendingIntent.getActivity(TestActivity.this, 0, intent, 0);

        int notificationId = (int) System.currentTimeMillis(); // generate a unique ID
        NotificationCompat.Builder builder = new NotificationCompat.Builder(TestActivity.this, "my_channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_24) //notification icon
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCustomBigContentView(notificationLayout)
                .setCustomHeadsUpContentView(notificationLayout)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Expanded content"))
                .setAutoCancel(false);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(TestActivity.this);
        notificationManager.notify(notificationId, builder.build());

    }
}