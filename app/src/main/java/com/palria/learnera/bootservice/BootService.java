package com.palria.learnera.bootservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.security.Provider;

public class BootService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //do something in this service
        Log.d("s", "from service class");
        Toast.makeText(getApplicationContext(), "hello from service", Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }
}