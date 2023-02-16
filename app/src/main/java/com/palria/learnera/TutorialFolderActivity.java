package com.palria.learnera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

public class TutorialFolderActivity extends AppCompatActivity {
String tutorialId = "";
String folderId = "";
String folderName = "";

FrameLayout pagesFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_folder);
        fetchIntentData();
        iniUI();
        openAllPageFragment();
    }

    private void iniUI(){
        pagesFrameLayout = findViewById(R.id.pagesFrameLayoutId);
    }
    private void fetchIntentData(){
        Intent intent = getIntent();
        tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);
        folderId = intent.getStringExtra(GlobalConfig.FOLDER_ID_KEY);
        folderName = intent.getStringExtra(GlobalConfig.FOLDER_NAME_KEY);
    }

    private void openAllPageFragment(){
        AllTutorialPageFragment pagesFragment = new AllTutorialPageFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(GlobalConfig.IS_FOLDER_PAGE_KEY,true);
        bundle.putString(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
        bundle.putString(GlobalConfig.FOLDER_ID_KEY,folderId);
        pagesFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(pagesFrameLayout.getId(),pagesFragment)
                .commit();
    }

}