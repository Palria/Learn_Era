package com.palria.learnera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

public class HostActivity extends AppCompatActivity {
String FRAGMENT_TYPE = "";
FrameLayout hostFrameLayout;
String userId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        initUI();
        fetchIntentData();
        openIncomingFragment();
    }

    private void initUI(){
        hostFrameLayout = findViewById(R.id.hostFrameLayoutId);
    }

    void fetchIntentData(){
        Intent intent = getIntent();
        FRAGMENT_TYPE = intent.getStringExtra(GlobalConfig.FRAGMENT_TYPE_KEY);
        userId = intent.getStringExtra(GlobalConfig.USER_ID_KEY);
    }

    private void openIncomingFragment(){
        switch(FRAGMENT_TYPE){

            case GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY:
                Bundle bundle = new Bundle();
                bundle.putString(GlobalConfig.USER_ID_KEY,userId);
                openFragment(bundle,new UserProfileFragment());
                break;
        }
    }

    void openFragment(Bundle bundle,Fragment fragment){
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(hostFrameLayout.getId(),fragment)
                .commit();
    }
}