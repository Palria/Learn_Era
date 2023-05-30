package com.palria.learnera.bootservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.security.Provider;

public class BootService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BootReceiver.createNotificationChannel(this);
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startForeground(1,BootReceiver.showNotification(BootService.this));
//            }
//        },10_000);
        //do something in this service
        Log.d("SERVICE DEVICE BOOT DETECTOR", "from service class");
        Toast.makeText(getApplicationContext(), "hello from service", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }
}