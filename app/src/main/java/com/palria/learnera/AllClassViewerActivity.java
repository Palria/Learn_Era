package com.palria.learnera;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.chip.Chip;

public class AllClassViewerActivity extends AppCompatActivity {
Chip openChip;
Chip startedChip;
Chip closedChip;
    Toolbar toolbar;

    boolean isOpenClassFragmentOpen = false;
    boolean isStartedClassFragmentOpen = false;
    boolean isClosedFragmentOpen = false;
    FrameLayout openClassFrameLayout;
    FrameLayout startedClassFrameLayout;
    FrameLayout closedClassFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_class_viewer);
        initUI();
        setSupportActionBar(toolbar);
        toolbar.setTitle("All Classes");

        manageChipSelection();
    }

    void initUI(){
        toolbar = findViewById(R.id.topBar);
        openChip = findViewById(R.id.openChipId);
        startedChip = findViewById(R.id.startedChipId);
        closedChip = findViewById(R.id.closedChipId);
        openClassFrameLayout = findViewById(R.id.openClassFragment);
        startedClassFrameLayout = findViewById(R.id.startedClassFragment);
        closedClassFrameLayout = findViewById(R.id.closedClassFragment);
    }



    void manageChipSelection(){
        openChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   if (isOpenClassFragmentOpen) {
                    //Just set the frame layout visibility
                    setFrameLayoutVisibility(openClassFrameLayout);

                }
                   else {
                    isOpenClassFragmentOpen = true;

                    setFrameLayoutVisibility(openClassFrameLayout);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(GlobalConfig.IS_OPEN_STARTED_CLASS_KEY, false);
                    AllClassFragment allClassFragment = new AllClassFragment();
                    allClassFragment.setArguments(bundle);
                    initFragment(allClassFragment, openClassFrameLayout);
                }
            }

            }
        });
        openChip.setChecked(true);
       startedChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   if (isStartedClassFragmentOpen) {
                    //Just set the frame layout visibility
                    setFrameLayoutVisibility(startedClassFrameLayout);

                }
                   else {
                    isStartedClassFragmentOpen = true;

                    setFrameLayoutVisibility(startedClassFrameLayout);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(GlobalConfig.IS_OPEN_STARTED_CLASS_KEY, true);
                    AllClassFragment allClassFragment = new AllClassFragment();
                    allClassFragment.setArguments(bundle);
                    initFragment(allClassFragment, startedClassFrameLayout);
                }
            }

            }
        });
       closedChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   if (isClosedFragmentOpen) {
                    //Just set the frame layout visibility
                    setFrameLayoutVisibility(closedClassFrameLayout);

                }
                   else {
                    isClosedFragmentOpen = true;

                    setFrameLayoutVisibility(closedClassFrameLayout);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(GlobalConfig.IS_OPEN_CLOSED_CLASS_KEY, true);
                    AllClassFragment allClassFragment = new AllClassFragment();
                    allClassFragment.setArguments(bundle);
                    initFragment(allClassFragment, closedClassFrameLayout);
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
        openClassFrameLayout.setVisibility(View.GONE);
        startedClassFrameLayout.setVisibility(View.GONE);
        closedClassFrameLayout.setVisibility(View.GONE);
        frameLayoutToSetVisible.setVisibility(View.VISIBLE);
    }

}