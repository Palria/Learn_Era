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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.models.FolderDataModel;
import com.palria.learnera.widgets.LEBottomSheetDialog;

public class TutorialFolderActivity extends AppCompatActivity {
String authorId = "";
String libraryId = "";
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
FolderDataModel intentFolderDataModel;
    OnFolderFetchListener onFolderFetchListener;
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
        onFolderFetchListener =  new OnFolderFetchListener() {
            @Override
            public void onSuccess(FolderDataModel folderDataModel) {
                folderNameView.setText(folderDataModel.getFolderName());
                dateCreated.setText(folderDataModel.getDateCreated());
                pagesCount.setText(folderDataModel.getNumOfPages()+"");
                folderViewCount.setText(folderDataModel.getNumOfViews()+"");
                GlobalConfig.incrementNumberOfVisitors(authorId,null,tutorialId,folderId,null,false,false,false,true,false,false);

            }

            @Override
            public void onFailed(String errorMessage) {

            }
        };
        if(isFirstView) {
            fetchFolderData();
        }else{
            onFolderFetchListener.onSuccess(intentFolderDataModel);
        }
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

//        if(authorId.equals(GlobalConfig.getCurrentUserId())) {
//            leBottomSheetDialog.addOptionItem("Edit Folder", R.drawable.ic_baseline_edit_24,
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                        }
//                    }, 0);

            leBottomSheetDialog.addOptionItem("Add Page", R.drawable.ic_baseline_add_circle_24,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(TutorialFolderActivity.this,UploadPageActivity.class);
                            intent.putExtra(GlobalConfig.FOLDER_ID_KEY,folderId);
                            intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
                            intent.putExtra(GlobalConfig.LIBRARY_ID_KEY,libraryId);
                            intent.putExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,false);
                            startActivity(intent);
                        }
                    }, 0);
//        }
        leBottomSheetDialog.addOptionItem("Add to Bookmark", R.drawable.ic_baseline_bookmarks_24,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(TutorialFolderActivity.this, "adding to bookmark", Toast.LENGTH_SHORT).show();

                        GlobalConfig.addToBookmark(authorId, libraryId, tutorialId, folderId,null,false,false, false,true,false,false,  new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(TutorialFolderActivity.this, "bookmarked", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                Toast.makeText(TutorialFolderActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                            }
                        });
                        leBottomSheetDialog.hide();

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
        authorId = intent.getStringExtra(GlobalConfig.AUTHOR_ID_KEY);
        libraryId = intent.getStringExtra(GlobalConfig.LIBRARY_ID_KEY);
        tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);
        folderId = intent.getStringExtra(GlobalConfig.FOLDER_ID_KEY);
        folderName = intent.getStringExtra(GlobalConfig.FOLDER_NAME_KEY);
        isFirstView = intent.getBooleanExtra(GlobalConfig.IS_FIRST_VIEW_KEY,true);
        intentFolderDataModel = (FolderDataModel) intent.getSerializableExtra(GlobalConfig.FOLDER_DATA_MODEL_KEY);
    }

    private void fetchFolderData(){
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
                        String folderName  = ""+ documentSnapshot.get(GlobalConfig.FOLDER_NAME_KEY); String authorId  = ""+ documentSnapshot.get(GlobalConfig.AUTHOR_ID_KEY);
                        String libraryId  = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_ID_KEY);
                        String dateCreated  = documentSnapshot.get(GlobalConfig.FOLDER_CREATED_DATE_TIME_STAMP_KEY)!=null ?documentSnapshot.getTimestamp(GlobalConfig.FOLDER_CREATED_DATE_TIME_STAMP_KEY).toDate() +""  :"Undefined";
                        if(dateCreated.length()>10){
                            dateCreated = dateCreated.substring(0,10);
                        }
                        long numOfPages  =  documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FOLDER_PAGES_KEY)!=null ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOLDER_PAGES_KEY) : 0L;
                        long numOfViews  = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FOLDER_VISITOR_KEY)!=null ?documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOLDER_VISITOR_KEY) : 0L ;

                        onFolderFetchListener.onSuccess(new FolderDataModel(folderId,authorId,libraryId,tutorialId,folderName,dateCreated,numOfPages,numOfViews));
                    }
                });
    }

    private void openAllPageFragment(){
        AllTutorialPageFragment pagesFragment = new AllTutorialPageFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(GlobalConfig.IS_TUTORIAL_PAGE_KEY,false);
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