package com.palria.learnera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class PaginationActivity extends AppCompatActivity {

    boolean isTutorialPage = true;

    String authorId = "";
    String libraryId = "";
    String tutorialId = "";
    String folderId = "";
    String pageId = "";
    FrameLayout pagesFrameLayout;
    ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagination);
        initUI();
        fetchIntentData();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaginationActivity.super.onBackPressed();
            }
        });
openAllPageFragment();
    }

    void initUI(){
        pagesFrameLayout = findViewById(R.id.pagesFrameLayout);
        backButton = findViewById(R.id.backButton);
    }
    void fetchIntentData(){
        Intent intent = getIntent();

        isTutorialPage = intent.getBooleanExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,true);
        tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);
        authorId = intent.getStringExtra(GlobalConfig.AUTHOR_ID_KEY);
        folderId = intent.getStringExtra(GlobalConfig.FOLDER_ID_KEY);
        pageId = intent.getStringExtra(GlobalConfig.PAGE_ID_KEY);

    }
    private void openAllPageFragment(){
        AllTutorialPageFragment pagesFragment = new AllTutorialPageFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(GlobalConfig.IS_TUTORIAL_PAGE_KEY,isTutorialPage);
        bundle.putBoolean(GlobalConfig.IS_PAGINATION_KEY,true);
        bundle.putString(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
        bundle.putString(GlobalConfig.FOLDER_ID_KEY,folderId);
        pagesFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(pagesFrameLayout.getId(),pagesFragment)
                .commit();
    }
}