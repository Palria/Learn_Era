package com.palria.learnera.bootservice;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.security.Provider;

public class BootService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Toast.makeText(getApplicationContext(), "job started ", Toast.LENGTH_LONG).show();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Toast.makeText(getApplicationContext(), "job stopped ", Toast.LENGTH_LONG).show();

        return false;
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