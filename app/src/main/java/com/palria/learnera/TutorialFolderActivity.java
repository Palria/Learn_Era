package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.palria.learnera.widgets.LEBottomSheetDialog;

public class TutorialFolderActivity extends AppCompatActivity {
String tutorialId = "";
String folderId = "";
String folderName = "";

FrameLayout pagesFrameLayout;
TextView folderNameView;
TextView dateCreated;
TextView authorName;
TextView libraryName;
TextView folderViewCount;
TextView pagesCount;
LEBottomSheetDialog leBottomSheetDialog;
ExtendedFloatingActionButton floatingActionButton;
MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_folder);
        fetchIntentData();
        iniUI();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leBottomSheetDialog.show();
            }
        });

        openAllPageFragment();
    }

    private void iniUI(){
        toolbar=findViewById(R.id.topBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Folder");

        pagesFrameLayout = findViewById(R.id.pagesFrameLayoutId);
         folderNameView = findViewById(R.id.folderName);
         dateCreated= findViewById(R.id.dateCreated);
         authorName=findViewById(R.id.authorName);
         libraryName=findViewById(R.id.libraryName);
         folderViewCount=findViewById(R.id.viewCount);
         pagesCount=findViewById(R.id.pagesCount);

         floatingActionButton=findViewById(R.id.fab);

         leBottomSheetDialog=new LEBottomSheetDialog(this);
         //if author id equal current logged in user

         leBottomSheetDialog.addOptionItem("Edit Folder", R.drawable.ic_baseline_edit_24,
                 new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {

                     }
                 },0);

         leBottomSheetDialog.addOptionItem("Add Page", R.drawable.ic_baseline_add_circle_24,
                 new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {

                     }
                 }, 0);

        leBottomSheetDialog.addOptionItem("Add to Bookmark", R.drawable.ic_baseline_bookmarks_24,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }, 0);

        leBottomSheetDialog.render();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
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