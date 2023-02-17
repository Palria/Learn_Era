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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.models.FolderDataModel;
import com.palria.learnera.widgets.LEBottomSheetDialog;

public class TutorialFolderActivity extends AppCompatActivity {
String authorId = "";
String tutorialId = "";
String folderId = "";
String folderName = "";
boolean isFirstView = true;
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
        folderNameView.setText(folderName);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leBottomSheetDialog.show();
            }
        });
fetchFolderData(new OnFolderFetchListener() {
    @Override
    public void onSuccess(FolderDataModel folderDataModel) {
        folderNameView.setText(folderDataModel.getFolderName());
        dateCreated.setText(folderDataModel.getDateCreated());
        pagesCount.setText(folderDataModel.getNumOfPages()+"");
        GlobalConfig.incrementNumberOfVisitors(authorId,null,tutorialId,folderId,null,false,false,false,true,false,false);

    }

    @Override
    public void onFailed(String errorMessage) {

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
        authorId = intent.getStringExtra(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY);
        tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);
        folderId = intent.getStringExtra(GlobalConfig.FOLDER_ID_KEY);
        folderName = intent.getStringExtra(GlobalConfig.FOLDER_NAME_KEY);
        isFirstView = intent.getBooleanExtra(GlobalConfig.IS_FIRST_VIEW_KEY,true);
    }

    private void fetchFolderData(OnFolderFetchListener onFolderFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                .document(tutorialId)
                .collection(GlobalConfig.ALL_FOLDERS_KEY)
                .document(folderId)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onFolderFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String folderId = documentSnapshot.getId();
                        String folderName  = ""+ documentSnapshot.get(GlobalConfig.FOLDER_NAME_KEY);
                        String dateCreated  = documentSnapshot.get(GlobalConfig.FOLDER_CREATED_DATE_TIME_STAMP_KEY)!=null ?documentSnapshot.getTimestamp(GlobalConfig.FOLDER_CREATED_DATE_TIME_STAMP_KEY).toDate() +""  :"Undefined";
                        if(dateCreated.length()>10){
                            dateCreated = dateCreated.substring(0,10);
                        }
                        long numOfPages  =  documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FOLDER_PAGES_KEY)!=null ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOLDER_PAGES_KEY) : 0L;


                        onFolderFetchListener.onSuccess(new FolderDataModel(folderId,tutorialId,folderName,dateCreated,numOfPages));
                    }
                });
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

    public interface OnFolderFetchListener{
        void onSuccess(FolderDataModel folderDataModel);
        void onFailed(String errorMessage);
    }

}