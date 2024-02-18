package com.palria.learnera;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.chip.Chip;

public class AllQuizViewerActivity extends AppCompatActivity {
Chip openChip;
Chip startedChip;
Chip completedChip;
    Toolbar toolbar;

    boolean isOpenQuizFragmentOpen = false;
    boolean isStartedQuizFragmentOpen = false;
    boolean isCompletedFragmentOpen = false;
    FrameLayout openQuizFrameLayout;
    FrameLayout startedQuizFrameLayout;
    FrameLayout completedQuizFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_quiz_viewer);
        initUI();
        setSupportActionBar(toolbar);
        toolbar.setTitle("All Quizzes");

        manageChipSelection();
    }

    void initUI(){
        toolbar = findViewById(R.id.topBar);
        openChip = findViewById(R.id.openChipId);
        startedChip = findViewById(R.id.startedChipId);
        completedChip = findViewById(R.id.completedChipId);
        openQuizFrameLayout = findViewById(R.id.openQuizFragment);
        startedQuizFrameLayout = findViewById(R.id.startedQuizFragment);
        completedQuizFrameLayout = findViewById(R.id.completedQuizFragment);
    }



    void manageChipSelection(){
        openChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   if (isOpenQuizFragmentOpen) {
                    //Just set the frame layout visibility
                    setFrameLayoutVisibility(openQuizFrameLayout);

                }
                   else {
                    isOpenQuizFragmentOpen = true;

                    setFrameLayoutVisibility(openQuizFrameLayout);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(GlobalConfig.IS_OPEN_STARTED_QUIZ_KEY, false);
                    AllQuizFragment allQuizFragment = new AllQuizFragment();
                    allQuizFragment.setArguments(bundle);
                    initFragment(allQuizFragment, openQuizFrameLayout);
                }
            }

            }
        });
        openChip.setChecked(true);
       startedChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   if (isStartedQuizFragmentOpen) {
                    //Just set the frame layout visibility
                    setFrameLayoutVisibility(startedQuizFrameLayout);

                }
                   else {
                    isStartedQuizFragmentOpen = true;

                    setFrameLayoutVisibility(startedQuizFrameLayout);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(GlobalConfig.IS_OPEN_STARTED_QUIZ_KEY, true);
                    AllQuizFragment allQuizFragment = new AllQuizFragment();
                    allQuizFragment.setArguments(bundle);
                    initFragment(allQuizFragment, startedQuizFrameLayout);
                }
            }

            }
        });
       completedChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   if (isCompletedFragmentOpen) {
                    //Just set the frame layout visibility
                    setFrameLayoutVisibility(completedQuizFrameLayout);

                }
                   else {
                    isCompletedFragmentOpen = true;

                    setFrameLayoutVisibility(completedQuizFrameLayout);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(GlobalConfig.IS_OPEN_COMPLETED_QUIZ_KEY, true);
                    AllQuizFragment allQuizFragment = new AllQuizFragment();
                    allQuizFragment.setArguments(bundle);
                    initFragment(allQuizFragment, completedQuizFrameLayout);
                }
            }

            }
        });

    }


    private void initFragment(Fragment fragment, FrameLayout frameLayout){
        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            fragmentManager.beginTransaction()
                    .replace(frameLayout.getId(), fragment)
                    .commit();
        }catch(Exception e){}

    }

    private void setFrameLayoutVisibility(FrameLayout frameLayoutToSetVisible){
        openQuizFrameLayout.setVisibility(View.GONE);
        startedQuizFrameLayout.setVisibility(View.GONE);
        completedQuizFrameLayout.setVisibility(View.GONE);
        frameLayoutToSetVisible.setVisibility(View.VISIBLE);
    }

}