package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.palria.learnera.models.FolderDataModel;
import com.palria.learnera.widgets.BottomSheetFormBuilderWidget;
import com.palria.learnera.widgets.LEBottomSheetDialog;

import java.util.HashMap;

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
Button editFolderActionButton;
AlertDialog alertDialog;
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
        initUI();
        if(!(GlobalConfig.getBlockedItemsList().contains(authorId+"")) &&!(GlobalConfig.getBlockedItemsList().contains(libraryId+"")) && !(GlobalConfig.getBlockedItemsList().contains(tutorialId+"")))  {
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

    }else{

        Toast.makeText(this, "Folder Blocked! Unblock to explore the Folder", Toast.LENGTH_SHORT).show();

        super.onBackPressed();
    }
    }

    private void initUI(){
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
        alertDialog = new AlertDialog.Builder(TutorialFolderActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();

         leBottomSheetDialog=new LEBottomSheetDialog(this);
         //if author id equal current logged in user
//        Snackbar saveSnackBar = GlobalConfig.createSnackBar(getApplicationContext(),pagesFrameLayout,authorId +" id ", Snackbar.LENGTH_INDEFINITE);
        if(GlobalConfig.getCurrentUserId().equals(authorId+"")){
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
                            leBottomSheetDialog.hide();
                            Intent intent = new Intent(TutorialFolderActivity.this,UploadPageActivity.class);
                            intent.putExtra(GlobalConfig.FOLDER_ID_KEY,folderId);
                            intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
                            intent.putExtra(GlobalConfig.LIBRARY_ID_KEY,libraryId);
                            intent.putExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,false);
                            startActivity(intent);
                        }
                    }, 0);
      leBottomSheetDialog.addOptionItem("Edit folder", R.drawable.ic_baseline_edit_24,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            leBottomSheetDialog.hide();

                            new BottomSheetFormBuilderWidget(TutorialFolderActivity.this)
                                    .setTitle("Edit your folder")
                                    .setPositiveTitle("Edit")
                                    .addInputField(new BottomSheetFormBuilderWidget.EditTextInput(TutorialFolderActivity.this)
                                            .setHint(folderNameView.getText()+"")
                                            .autoFocus())
                                    .setOnSubmit(new BottomSheetFormBuilderWidget.OnSubmitHandler() {
                                        @Override
                                        public void onSubmit(String[] values) {
                                            super.onSubmit(values);

                                            Toast.makeText(TutorialFolderActivity.this, values[0], Toast.LENGTH_SHORT).show();
                                            //values will be returned as array of strings as per input list position
                                            //eg first added input has first value
                                            String folderName = values[0];
                                            if (folderName.trim().equals("")) {
//                                           leBottomSheetDialog.setTitle("Folder needs name, must enter name for the folder");

//                                           Toast.makeText(TutorialActivity.this, "Please enter name",Toast.LENGTH_SHORT).show();
                                            } else {
                                                editFolder(folderName);
//                                           leBottomSheetDialog.setTitle("Creating "+values[0]+" folder in progress...");
//                                           leBottomSheetDialog.hide();
                                            }
                                            //create folder process here
                                        }
                                    })
                                    .render()
                                    .show();
                        }
                    }, 0);
            leBottomSheetDialog.addOptionItem("Delete Folder", R.drawable.ic_baseline_error_outline_24, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(TutorialFolderActivity.this)
                            .setCancelable(true)
                            .setTitle("Delete Your Folder!")
                            .setMessage("Action cannot be undone, are you sure you want to delete your Folder?")
                            .setPositiveButton("Yes,delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    toggleProgress(true);
                                    Toast.makeText(getApplicationContext(), "Deleting", Toast.LENGTH_SHORT).show();

                                    leBottomSheetDialog.hide();
                                    GlobalConfig.deleteTutorial(libraryId, tutorialId, new GlobalConfig.ActionCallback() {
                                        @Override
                                        public void onSuccess() {
                                            toggleProgress(false);
                                            Toast.makeText(getApplicationContext(), "Delete Folder success", Toast.LENGTH_SHORT).show();

                                            TutorialFolderActivity.super.onBackPressed();
                                        }

                                        @Override
                                        public void onFailed(String errorMessage) {
                                            toggleProgress(false);
                                            GlobalHelpers.showAlertMessage("error",getApplicationContext(), "Unable to delete Folder",errorMessage);
                                            Toast.makeText(getApplicationContext(), "Unable to deleted Folder!  please try again", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    TutorialFolderActivity.super.onBackPressed();

                                }
                            })
                            .setNegativeButton("No", null)
                            .create().show();


                }
            }, 0);

        }

        leBottomSheetDialog.addOptionItem("Bookmark", R.drawable.ic_baseline_bookmarks_24,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar saveSnackBar = GlobalConfig.createSnackBar(getApplicationContext(),pagesFrameLayout,"Fetching bookmark details...", Snackbar.LENGTH_INDEFINITE);



                        //CHECK IF THE CURRENT USER HAS ALREADY SAVED THIS FOLDER, IF SO DO STH
                        DocumentReference bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                                .collection(GlobalConfig.ALL_USERS_KEY)
                                .document(GlobalConfig.getCurrentUserId())
                                .collection(GlobalConfig.BOOK_MARKS_KEY).document(folderId);
                        GlobalConfig.checkIfDocumentExists(bookMarkOwnerReference, new GlobalConfig.OnDocumentExistStatusCallback() {
                            @Override
                            public void onExist() {
                                saveSnackBar.dismiss();

                                new AlertDialog.Builder(TutorialFolderActivity.this)
                                        .setTitle("Remove this  bookmark?")
                                        .setMessage("You have already added this folder to your bookmarks")
                                        .setCancelable(false)
                                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Snackbar snackBar = GlobalConfig.createSnackBar(getApplicationContext(),pagesFrameLayout,"Removing from bookmark...", Snackbar.LENGTH_INDEFINITE);

                                                GlobalConfig.removeBookmark(authorId, libraryId, tutorialId, folderId,null,GlobalConfig.FOLDER_TYPE_KEY, new GlobalConfig.ActionCallback() {
                                                    @Override
                                                    public void onSuccess() {
//                                                Toast.makeText(TutorialActivity.this, "bookmark removed", Toast.LENGTH_SHORT).show();
                                                        snackBar.dismiss();
                                                        GlobalConfig.createSnackBar(getApplicationContext(),pagesFrameLayout,"Bookmark removed!", Snackbar.LENGTH_SHORT);
                                                    }

                                                    @Override
                                                    public void onFailed(String errorMessage) {
//                                                Toast.makeText(TutorialActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                                        snackBar.dismiss();
                                                        GlobalConfig.createSnackBar(getApplicationContext(),pagesFrameLayout,"Failed to remove from bookmark please try again!", Snackbar.LENGTH_SHORT);
                                                    }
                                                });
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Toast.makeText(TutorialFolderActivity.this, "undo remove bookmark.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .show();
                            }

                            @Override
                            public void onNotExist() {
                                saveSnackBar.dismiss();

                                new AlertDialog.Builder(TutorialFolderActivity.this)
                                        .setTitle("Add this to bookmark?")
                                        .setMessage("when you save to bookmark you are able to view it in your bookmarked tab")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Snackbar snackBar = GlobalConfig.createSnackBar(getApplicationContext(),pagesFrameLayout,"Adding to bookmark...", Snackbar.LENGTH_INDEFINITE);

                                                GlobalConfig.addToBookmark(authorId, libraryId, tutorialId, folderId,null,GlobalConfig.FOLDER_TYPE_KEY, new GlobalConfig.ActionCallback() {
                                                    @Override
                                                    public void onSuccess() {
//                                                Toast.makeText(LibraryActivity.this, "bookmark added", Toast.LENGTH_SHORT).show();
                                                        snackBar.dismiss();
                                                        GlobalConfig.createSnackBar(getApplicationContext(),pagesFrameLayout,"Bookmark added!", Snackbar.LENGTH_SHORT);

                                                    }

                                                    @Override
                                                    public void onFailed(String errorMessage) {
//                                                Toast.makeText(TutorialActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                                        snackBar.dismiss();
                                                        GlobalConfig.createSnackBar(getApplicationContext(),pagesFrameLayout,"Failed to add to bookmark please try again!", Snackbar.LENGTH_SHORT);
                                                    }
                                                });
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Toast.makeText(TutorialFolderActivity.this, "cancelled bookmark.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .show();
                            }

                            @Override
                            public void onFailed(@NonNull String errorMessage) {
                                saveSnackBar.dismiss();
                                GlobalConfig.createSnackBar(getApplicationContext(),pagesFrameLayout,"Failed to Fetch bookmark details please try again", Snackbar.LENGTH_SHORT);

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

    private void toggleProgress(boolean show) {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
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
    private void editFolder(final String folderName) {
        Snackbar editSnackBar = GlobalConfig.createSnackBar(getApplicationContext(),pagesFrameLayout,"Editing folder, please wait...", Snackbar.LENGTH_INDEFINITE);

        String folderId = GlobalConfig.getRandomString(50);
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
        DocumentReference documentReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                .document(tutorialId)
                .collection(GlobalConfig.ALL_FOLDERS_KEY)
                .document(folderId);

        HashMap<String, Object> folderDetails = new HashMap<>();
        folderDetails.put(GlobalConfig.FOLDER_NAME_KEY, folderName);
        writeBatch.update(documentReference,folderDetails);

        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        editSnackBar.dismiss();
                        GlobalConfig.createSnackBar(getApplicationContext(),pagesFrameLayout,"Failed to edit your folder", Snackbar.LENGTH_SHORT);

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_EDIT_TUTORIAL_FOLDER_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryId, tutorialId, folderId, null, null, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {
                                editSnackBar.dismiss();
                                GlobalConfig.createSnackBar(getApplicationContext(),pagesFrameLayout,"Folder successfully edited", Snackbar.LENGTH_SHORT);

                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                editSnackBar.dismiss();
                                GlobalConfig.createSnackBar(getApplicationContext(),pagesFrameLayout,"Folder successfully edited", Snackbar.LENGTH_SHORT);

                            }
                        });

                    }
                });
    }
    public interface OnFolderFetchListener{
        void onSuccess(FolderDataModel folderDataModel);
        void onFailed(String errorMessage);
    }

}