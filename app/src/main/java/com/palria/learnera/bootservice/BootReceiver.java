package com.palria.learnera.bootservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.palria.learnera.R;

import java.time.LocalTime;

public class BootReceiver extends BroadcastReceiver {

    public static final String CHANNEL_TEST_ID = "TEST";
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        createNotificationChannel(context); //creates notification channel.
        Toast.makeText(context, "oo good bro", Toast.LENGTH_LONG).show();
        Log.d("", "onReceive: ");
        Intent intent1 = new Intent(context, BootService.class);
        showNotification(context);//show
        context.startService(intent);
    }

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "test";
            String description = "this is test channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_TEST_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showNotification(Context context) {
        LocalTime currentTime = LocalTime.now();
        String greeting="";
        int current_hour = Integer.parseInt(currentTime.toString().substring(0,2));
        if (current_hour < 12)
            greeting = "Good Morning";
        else if (current_hour >= 12 && current_hour <= 17)
            greeting = "Good Afternoon";
        else if (current_hour >= 17 && current_hour <= 24)
            greeting = "Good Evening";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_TEST_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Welcome to Learn Era!")
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon()
                .setOngoing(true)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText("hello {username} " + greeting)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("hello {username} " + greeting))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}