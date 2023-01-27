package com.palria.learnera;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.splashscreen.SplashScreen.Companion.*;

public class SplashScrenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        getWindow().setNavigationBarColor(getColor(R.color.teal_700));
        getWindow().setStatusBarColor(getColor(R.color.teal_700));
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_scren);

        // HERE WE ARE TAKING THE REFERENCE OF OUR IMAGE
        // SO THAT WE CAN PERFORM ANIMATION USING THAT IMAGE
          TextView logo_text = findViewById(R.id.logo_text);
         Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        logo_text.startAnimation(slideAnimation);

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashScrenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        }, 3000);



    }
}