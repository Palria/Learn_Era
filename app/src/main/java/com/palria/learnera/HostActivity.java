package com.palria.learnera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

public class HostActivity extends AppCompatActivity {
    Intent intent;
    String FRAGMENT_TYPE = "";
FrameLayout hostFrameLayout;
String userId = "";

TextView headerTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        initUI();
        fetchIntentData();
        openIncomingFragment();
    }

    private void initUI(){
        headerTextView = findViewById(R.id.headerTextViewId);
        hostFrameLayout = findViewById(R.id.hostFrameLayoutId);
    }

    void fetchIntentData(){
         intent = getIntent();
        FRAGMENT_TYPE = intent.getStringExtra(GlobalConfig.FRAGMENT_TYPE_KEY);
        userId = intent.getStringExtra(GlobalConfig.USER_ID_KEY);


    }

    private void openIncomingFragment(){
        Bundle bundle = new Bundle();

        switch(FRAGMENT_TYPE){
            case GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY:
                 bundle = new Bundle();
                bundle.putString(GlobalConfig.USER_ID_KEY,userId);
                initFragment(bundle,new UserProfileFragment(null));
                break;
            case GlobalConfig.LIBRARY_FRAGMENT_TYPE_KEY:
                if(userId.equals(GlobalConfig.getCurrentUserId())) {
                    headerTextView.setText("My Libraries");
                }else{
                    headerTextView.setText("Libraries");
                }
                 bundle = new Bundle();
                bundle.putString(GlobalConfig.LIBRARY_AUTHOR_ID_KEY,userId);
                bundle.putString(AllLibraryFragment.OPEN_TYPE_KEY,AllLibraryFragment.OPEN_TYPE_USER_LIBRARY);
                initFragment(bundle,new AllLibraryFragment());
                break;
            case GlobalConfig.TUTORIAL_FRAGMENT_TYPE_KEY:
                if(userId.equals(GlobalConfig.getCurrentUserId())) {
                    headerTextView.setText("My Tutorials");
                }else{
                    headerTextView.setText("Tutorials");
                }
                bundle = new Bundle();
                bundle.putString(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY,userId);
                bundle.putString(AllTutorialFragment.OPEN_TYPE_KEY,AllTutorialFragment.OPEN_TYPE_USER_TUTORIAL);
                initFragment(bundle,new AllTutorialFragment());
                break;
        }
    }

    void initFragment(Bundle bundle,Fragment fragment){
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(hostFrameLayout.getId(),fragment)
                .commit();
    }
}