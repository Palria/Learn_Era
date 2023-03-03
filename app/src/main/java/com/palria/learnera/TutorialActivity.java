package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.models.LibraryDataModel;
import com.palria.learnera.models.TutorialDataModel;
import com.palria.learnera.widgets.BottomSheetFormBuilderWidget;
import com.palria.learnera.widgets.LEBottomSheetDialog;
import com.palria.learnera.widgets.RatingBottomSheetWidget;

import java.util.ArrayList;
import java.util.HashMap;

public class TutorialActivity extends AppCompatActivity {
String tutorialId = "";
String authorId = "";
String libraryId = "";
boolean isFirstView = true;
RoundedImageView tutorialCoverImage;
RoundedImageView authorPicture;
TextView authorName;
TextView dateCreated;
TextView tutorialViewCount;
TextView pagesCount;
TextView foldersCount;
TextView tutorialName;
TextView tutorialDescription;
    int[] ratings = {0,0,0,0,0};

//get frame layouts
    FrameLayout foldersFrameLayout;
    FrameLayout pagesFrameLayout;
    FrameLayout ratingsFrameLayout;

    //tab layout
    TabLayout tabLayout;

    //boolean fragment open stats
    boolean isFoldersFragmentOpened=false;
    boolean isPagesFragmentOpened=false;
    boolean isRatingsFragmentOpened=false;

//buttons
Button addActionButton;
    Button saveActionButton;
    ImageButton editTutorialActionButton;
    Button rateActionButton;
    ImageButton backButton;

    RatingBottomSheetWidget ratingBottomSheetWidget;
    FrameLayout mainLayout;

    LEBottomSheetDialog leBottomSheetDialog;
    TutorialFetchListener tutorialFetchListener;
    DocumentSnapshot intentTutorialDocumentSnapshot;
    TutorialDataModel intentTutorialDataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        fetchIntentData();
        iniUI();
        fetchAuthorProfile();
        tutorialFetchListener = new TutorialFetchListener() {
            @Override
            public void onSuccess(TutorialDataModel tutorialDataModel) {
//                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_VISIT_TUTORIAL_TYPE_KEY, authorId, libraryId, tutorialId,  null, null, null, null, new GlobalConfig.ActionCallback() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onFailed(String errorMessage) {
//
//                    }
//                });


                Glide.with(getApplicationContext())
                        .load(tutorialDataModel.getTutorialCoverPhotoDownloadUrl())
                        .into(tutorialCoverImage);
                dateCreated.setText(tutorialDataModel.getDateCreated());
                tutorialViewCount.setText(tutorialDataModel.getTotalNumberOfTutorialViews()+"");
                pagesCount.setText(tutorialDataModel.getTotalNumberOfPages()+"");
                foldersCount.setText(tutorialDataModel.getTotalNumberOfFolders()+"");
                tutorialName.setText(tutorialDataModel.getTutorialName()+"");
                tutorialDescription.setText(tutorialDataModel.getTutorialDescription()+"");


                ratings[0] = (int) tutorialDataModel.getTotalNumberOfOneStarRate();
                ratings[1] = (int) tutorialDataModel.getTotalNumberOfTwoStarRate();
                ratings[2] = (int) tutorialDataModel.getTotalNumberOfThreeStarRate();
                ratings[3] = (int) tutorialDataModel.getTotalNumberOfFourStarRate();
                ratings[4] = (int) tutorialDataModel.getTotalNumberOfFiveStarRate();

                GlobalConfig.incrementNumberOfVisitors(authorId,libraryId,tutorialId,null,null,false,false,true,false,false,false);

            }

            @Override
            public void onFailed(String errorMessage) {

            }
        };
        if(isFirstView) {
            fetchTutorial();
        }else{
            tutorialFetchListener.onSuccess(intentTutorialDataModel);
        }
createTabLayout();
//        tabLayout.getChildAt(tabLayout.getTabAt(0).getPosition()).setSelected(true);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TutorialActivity.this.onBackPressed();
            }
        });

        if(authorId.equals(GlobalConfig.getCurrentUserId())) {
            addActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    leBottomSheetDialog.show();

                }
            });
        }

        rateActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateActionButton.setEnabled(false);

                DocumentReference authorReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                        .document(tutorialId).collection(GlobalConfig.REVIEWS_KEY)
                        .document(GlobalConfig.getCurrentUserId());
                Snackbar snackbar = GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Initializing rating details...", Snackbar.LENGTH_INDEFINITE);
                //Check if he has already rated this tutorial, else if not rated then rate but if rated edit the rating
                GlobalConfig.checkIfDocumentExists(authorReviewDocumentReference, new GlobalConfig.OnDocumentExistStatusCallback() {
                    @Override
                    public void onExist() {
                        rateActionButton.setEnabled(true);
                        snackbar.dismiss();

                        ratingBottomSheetWidget.render(mainLayout,true).show();


                        new AlertDialog.Builder(TutorialActivity.this)
                                .setTitle("Tutorial already reviewed!")
                                .setMessage("Chose what to do with the already reviewed tutorial:")
                                .setCancelable(true)
                                .setPositiveButton("Edit review", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        ratingBottomSheetWidget.render(mainLayout,true).show();

                                    }
                                })
                                .setNegativeButton("Delete review", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Snackbar deleteReviewSnackBar = GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Deleting review...", Snackbar.LENGTH_INDEFINITE);

                                        GlobalConfig.removeTutorialReview(tutorialId, new GlobalConfig.ActionCallback() {
                                            @Override
                                            public void onSuccess() {
                                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_DELETE_TUTORIAL_REVIEW_TYPE_KEY, authorId, null, null, null, null, GlobalConfig.getCurrentUserId(), new GlobalConfig.ActionCallback() {
                                                    @Override
                                                    public void onSuccess() {
                                                        deleteReviewSnackBar.dismiss();
                                                        GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Review Deleted!", Snackbar.LENGTH_SHORT);

                                                        rateActionButton.setTextColor(getResources().getColor(R.color.black_overlay,getTheme()));
                                                        rateActionButton.setText("Rate");

                                                    }

                                                    @Override
                                                    public void onFailed(String errorMessage) {
                                                        deleteReviewSnackBar.dismiss();
                                                        GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Review Deleted!", Snackbar.LENGTH_SHORT);

                                                        rateActionButton.setTextColor(getResources().getColor(R.color.black_overlay,getTheme()));
                                                        rateActionButton.setText("Rate");


                                                    }
                                                });

                                            }

                                            @Override
                                            public void onFailed(String errorMessage) {
                                                deleteReviewSnackBar.dismiss();
                                                GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Review failed to delete!", Snackbar.LENGTH_SHORT);

                                            }
                                        });
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onNotExist() {
                        rateActionButton.setEnabled(true);
                        snackbar.dismiss();

                        ratingBottomSheetWidget.render(mainLayout,false).show();

                    }

                    @Override
                    public void onFailed(@NonNull String errorMessage) {

                        snackbar.dismiss();
                        Snackbar snackbar = GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Failed to initialize rating", Snackbar.LENGTH_SHORT);

                    }
                });
            }
        });

        editTutorialActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(getApplicationContext(),CreateNewTutorialActivity.class);
              intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
              intent.putExtra(GlobalConfig.IS_CREATE_NEW_TUTORIAL_KEY,false);
              intent.putExtra(GlobalConfig.LIBRARY_CONTAINER_ID_KEY,libraryId);
              startActivity(intent);

            }
        });
        saveActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar saveSnackBar = GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Fetching bookmark details...", Snackbar.LENGTH_INDEFINITE);
                saveActionButton.setEnabled(false);

                //CHECK IF THE CURRENT USER HAS ALREADY SAVED THIS TUTORIAL, IF SO DO STH
                DocumentReference bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(tutorialId);
                GlobalConfig.checkIfDocumentExists(bookMarkOwnerReference, new GlobalConfig.OnDocumentExistStatusCallback() {
                    @Override
                    public void onExist() {
                        saveSnackBar.dismiss();
                        saveActionButton.setEnabled(true);

                        new AlertDialog.Builder(TutorialActivity.this)
                                .setTitle("Remove this  bookmark?")
                                .setMessage("You have already added this tutorial to your bookmarks")
                                .setCancelable(false)
                                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Snackbar snackBar = GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Removing from bookmark...", Snackbar.LENGTH_INDEFINITE);

                                        GlobalConfig.removeBookmark(authorId, libraryId, tutorialId, null,null,GlobalConfig.TUTORIAL_TYPE_KEY, new GlobalConfig.ActionCallback() {
                                            @Override
                                            public void onSuccess() {
//                                                Toast.makeText(TutorialActivity.this, "bookmark removed", Toast.LENGTH_SHORT).show();
                                                snackBar.dismiss();
                                                GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Bookmark removed!", Snackbar.LENGTH_SHORT);
                                                saveActionButton.setText(R.string.save);
                                                saveActionButton.setTextColor(getResources().getColor(R.color.black_overlay,getTheme()));

                                            }

                                            @Override
                                            public void onFailed(String errorMessage) {
//                                                Toast.makeText(TutorialActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                                snackBar.dismiss();
                                                GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Failed to remove from bookmark please try again!", Snackbar.LENGTH_SHORT);
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(TutorialActivity.this, "undo remove bookmark.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onNotExist() {
                        saveSnackBar.dismiss();
                        saveActionButton.setEnabled(true);

                        new AlertDialog.Builder(TutorialActivity.this)
                                .setTitle("Add this to bookmark?")
                                .setMessage("when you save to bookmark you are able to view it in your bookmarked tab")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Snackbar snackBar = GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Adding to bookmark...", Snackbar.LENGTH_INDEFINITE);

                                        GlobalConfig.addToBookmark(authorId, libraryId, tutorialId, null,null,GlobalConfig.TUTORIAL_TYPE_KEY, new GlobalConfig.ActionCallback() {
                                            @Override
                                            public void onSuccess() {
//                                                Toast.makeText(LibraryActivity.this, "bookmark added", Toast.LENGTH_SHORT).show();
                                                snackBar.dismiss();
                                                GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Bookmark added!", Snackbar.LENGTH_SHORT);
                                                saveActionButton.setText(R.string.un_save);
                                                saveActionButton.setTextColor(getResources().getColor(R.color.teal_700,getTheme()));


                                            }

                                            @Override
                                            public void onFailed(String errorMessage) {
//                                                Toast.makeText(TutorialActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                                snackBar.dismiss();
                                                GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Failed to add to bookmark please try again!", Snackbar.LENGTH_SHORT);
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(TutorialActivity.this, "cancelled bookmark.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onFailed(@NonNull String errorMessage) {
                        saveSnackBar.dismiss();
                        GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Failed to Fetch bookmark details please try again", Snackbar.LENGTH_SHORT);
                        saveActionButton.setEnabled(true);

                    }
                });


            }
        });

        authorPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(GlobalConfig.getHostActivityIntent(getApplicationContext(),null,GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY,authorId));

            }
        });
        authorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(GlobalConfig.getHostActivityIntent(getApplicationContext(),null,GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY,authorId));

            }
        });

        DocumentReference bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId())
                .collection(GlobalConfig.BOOK_MARKS_KEY).document(tutorialId);
        GlobalConfig.checkIfDocumentExists(bookMarkOwnerReference, new GlobalConfig.OnDocumentExistStatusCallback() {
            @Override
            public void onExist() {
                saveActionButton.setTextColor(getResources().getColor(R.color.teal_700,getTheme()));
                saveActionButton.setText(R.string.un_save);

            }

            @Override
            public void onNotExist() {

            }

            @Override
            public void onFailed(@NonNull String errorMessage) {

            }
        });

        DocumentReference authorReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                .document(tutorialId).collection(GlobalConfig.REVIEWS_KEY)
                .document(GlobalConfig.getCurrentUserId());
         GlobalConfig.checkIfDocumentExists(authorReviewDocumentReference, new GlobalConfig.OnDocumentExistStatusCallback() {
            @Override
            public void onExist() {
                rateActionButton.setTextColor(getResources().getColor(R.color.teal_700,getTheme()));
                rateActionButton.setText("Rated");

            }

            @Override
            public void onNotExist() {

            }

            @Override
            public void onFailed(@NonNull String errorMessage) {

            }
        });

        ratingBottomSheetWidget= new RatingBottomSheetWidget(this, authorId, libraryId,  tutorialId,false, false,true);;
        ratingBottomSheetWidget.setRatingPostListener(new RatingBottomSheetWidget.OnRatingPosted(){

            @Override
            public void onPost(int star, String message) {
                super.onPost(star,message);

//                Toast.makeText(TutorialActivity.this,star + "-"+ message, Toast.LENGTH_SHORT).show();

            }

            @Override
            public  void onFailed(String errorMessage){
                super.onFailed(errorMessage);
//                Toast.makeText(TutorialActivity.this,errorMessage, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(boolean isReviewAuthor,boolean isReviewLibrary,boolean isReviewTutorial){
                super.onSuccess(isReviewAuthor,isReviewLibrary,isReviewTutorial);

                rateActionButton.setTextColor(getResources().getColor(R.color.teal_700,getTheme()));
                rateActionButton.setText("Rated");
//                Toast.makeText(TutorialActivity.this,"You rated this tutorial", Toast.LENGTH_SHORT).show();

            }
        });

//        openFoldersFragment();
    }

    private void initFragment(Fragment fragment, FrameLayout frameLayout){
//        Bundle bundle = new Bundle();
//        bundle.putString(GlobalConfig.LIBRARY_ID_KEY,libraryId);
//        bundle.putString(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
//        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), fragment)
                .commit();


    }

    private void setFrameLayoutVisibility(FrameLayout frameLayoutToSetVisible){
        foldersFrameLayout.setVisibility(View.GONE);
        ratingsFrameLayout.setVisibility(View.GONE);
        pagesFrameLayout.setVisibility(View.GONE);
        frameLayoutToSetVisible.setVisibility(View.VISIBLE);
    }

//    private void openFoldersFragment(){
//        isFoldersFragmentOpened=true;
//        Fragment foldersFragment = new FoldersFragment();
//        Bundle bundle = new Bundle();
////        bundle.putBoolean(GlobalConfig.IS_FROM_LIBRARY_ACTIVITY_CONTEXT_KEY,true);
////        bundle.putString(GlobalConfig.LIBRARY_CONTAINER_ID_KEY,libraryId);
//        foldersFragment.setArguments(bundle);
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(foldersFrameLayout.getId(),foldersFragment)
//                .commit();
//    }

    private void iniUI(){
        mainLayout = findViewById(R.id.mainLayout);
        tutorialCoverImage = findViewById(R.id.tutorialCoverImage);
        authorPicture = findViewById(R.id.authorPicture);
        authorName = findViewById(R.id.authorName);
        dateCreated = findViewById(R.id.dateCreated);
        tutorialViewCount = findViewById(R.id.tutorialViewCount);
        pagesCount = findViewById(R.id.pagesCount);
        foldersCount = findViewById(R.id.foldersCount);
        tutorialName = findViewById(R.id.tutorialName);
        tutorialDescription = findViewById(R.id.tutorialDescription);
        foldersFrameLayout=findViewById(R.id.foldersFrameLayout);
        pagesFrameLayout=findViewById(R.id.pagesFrameLayout);
        ratingsFrameLayout=findViewById(R.id.ratingsFrameLayout);
        addActionButton=findViewById(R.id.addActionButton);
        saveActionButton=findViewById(R.id.saveActionButton);
        editTutorialActionButton=findViewById(R.id.editTutorialActionButtonId);
        rateActionButton=findViewById(R.id.rateActionButton);
        backButton=findViewById(R.id.backButton);

        tabLayout=findViewById(R.id.tab_layout);
//       GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,authorId, Snackbar.LENGTH_INDEFINITE);

if(authorId.equals(GlobalConfig.getCurrentUserId())) {
    leBottomSheetDialog = new LEBottomSheetDialog(this)
            .addOptionItem("New Folder", R.drawable.ic_baseline_add_circle_24, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    leBottomSheetDialog.hide();

                    new BottomSheetFormBuilderWidget(TutorialActivity.this)
                            .setTitle("Folder is used to organize your pages within a tutorial")
                            .setPositiveTitle("Create")
                            .addInputField(new BottomSheetFormBuilderWidget.EditTextInput(TutorialActivity.this)
                                    .setHint("Enter folder name")
                                    .autoFocus())
                            .setOnSubmit(new BottomSheetFormBuilderWidget.OnSubmitHandler() {
                                @Override
                                public void onSubmit(String[] values) {
                                    super.onSubmit(values);

                                    Toast.makeText(TutorialActivity.this, values[0], Toast.LENGTH_SHORT).show();
                                    //values will be returned as array of strings as per input list position
                                    //eg first added input has first value
                                    String folderName = values[0];
                                    if (folderName.trim().equals("")) {
//                                           leBottomSheetDialog.setTitle("Folder needs name, must enter name for the folder");

//                                           Toast.makeText(TutorialActivity.this, "Please enter name",Toast.LENGTH_SHORT).show();
                                    } else {

                                        createNewFolder(values[0]);
//                                           leBottomSheetDialog.setTitle("Creating "+values[0]+" folder in progress...");
//                                           leBottomSheetDialog.hide();
                                    }
                                    //create folder process here
                                }
                            })
                            .render()
                            .show();

                }
            }, 0)
            .addOptionItem("New page", R.drawable.baseline_pages_24, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                        Intent i = new Intent(TutorialActivity.this, CreateNewTutorialPageActivity.class);
//                        i.putExtra(GlobalConfig.TUTORIAL_ID_KEY,"some-id");
//                        //creating new

                    Intent intent = new Intent(TutorialActivity.this, UploadPageActivity.class);
                    intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY, tutorialId);
                    intent.putExtra(GlobalConfig.LIBRARY_ID_KEY, libraryId);
                    intent.putExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY, true);
                    startActivity(intent);
                }
            }, 0)
            .render();
}else{
    addActionButton.setVisibility(View.GONE);
    editTutorialActionButton.setVisibility(View.GONE);
}



    }
    private void fetchIntentData(){
        Intent intent = getIntent();
        tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);
        authorId = intent.getStringExtra(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY);
        libraryId = intent.getStringExtra(GlobalConfig.LIBRARY_CONTAINER_ID_KEY);
        isFirstView = intent.getBooleanExtra(GlobalConfig.IS_FIRST_VIEW_KEY,true);

if(!isFirstView) {
    intentTutorialDataModel = (TutorialDataModel) intent.getSerializableExtra(GlobalConfig.TUTORIAL_DATA_MODEL_KEY);
    intentTutorialDocumentSnapshot = intentTutorialDataModel.getTutorialDocumentSnapshot();
}
    }

    //

    public void createTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabTitle=tab.getText().toString().trim().toUpperCase();
                if(tabTitle.equals("FOLDERS")){
                    if(isFoldersFragmentOpened){
                        //Just set the frame layout visibility
                        setFrameLayoutVisibility(foldersFrameLayout);
                    }else {
                        isFoldersFragmentOpened =true;
                        setFrameLayoutVisibility(foldersFrameLayout);

                        FoldersFragment foldersFragment = new FoldersFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
                        foldersFragment.setArguments(bundle);
                        initFragment(foldersFragment, foldersFrameLayout);
                    }


                }else if(tabTitle.equals("PAGES"))
                {
                    if(isPagesFragmentOpened){
                        //Just set the frame layout visibility
                        setFrameLayoutVisibility(pagesFrameLayout);
                    }else {
                        isPagesFragmentOpened =true;
                        setFrameLayoutVisibility(pagesFrameLayout);
                        AllTutorialPageFragment allTutorialPageFragment = new AllTutorialPageFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
//                        bundle.putString(GlobalConfig.FOLDER_ID_KEY,"folderId");
                        bundle.putBoolean(GlobalConfig.IS_TUTORIAL_PAGE_KEY,true);
                        allTutorialPageFragment.setArguments(bundle);
                        initFragment(allTutorialPageFragment, pagesFrameLayout);
                    }
                }else if(tabTitle.equals("RATINGS")){
                    if(isRatingsFragmentOpened){
                        //Just set the frame layout visibility
                        setFrameLayoutVisibility(ratingsFrameLayout);
                    }else {
                        isRatingsFragmentOpened =true;
                        setFrameLayoutVisibility(ratingsFrameLayout);
                        LibraryActivityRatingFragment libraryActivityRatingFragment = new LibraryActivityRatingFragment();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(GlobalConfig.IS_LIBRARY_REVIEW_KEY,false);
                        bundle.putString(GlobalConfig.LIBRARY_ID_KEY,libraryId);
                        bundle.putString(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
                        bundle.putIntArray(GlobalConfig.STAR_RATING_ARRAY_KEY,ratings);
                        libraryActivityRatingFragment.setArguments(bundle);
                        initFragment(libraryActivityRatingFragment, ratingsFrameLayout);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        TabLayout.Tab foldersTabItem = tabLayout.newTab();
        foldersTabItem.setText("Folders");
        tabLayout.addTab(foldersTabItem, 0, true);

        TabLayout.Tab pagesTabItem = tabLayout.newTab();
        pagesTabItem.setText("Pages");
        tabLayout.addTab(pagesTabItem, 1);

        TabLayout.Tab ratingsTabItem = tabLayout.newTab();
        ratingsTabItem.setText("Ratings");
        tabLayout.addTab(ratingsTabItem, 2);
    }
    private void fetchTutorial(){
       GlobalConfig.getFirebaseFirestoreInstance()
               .collection(GlobalConfig.ALL_TUTORIAL_KEY)
               .document(tutorialId)
               .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tutorialFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot){

//                        if(1>0)return;
                            String libraryId =""+ documentSnapshot.get(GlobalConfig.LIBRARY_CONTAINER_ID_KEY);
                            String tutorialId =""+ documentSnapshot.get(GlobalConfig.TUTORIAL_ID_KEY);

                            long totalNumberOfTutorialVisitor = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY) : 0L;
                            long totalNumberOfTutorialReach = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REACH_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REACH_KEY) : 0L;
                            long totalNumberOfOneStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) : 0L;
                            long totalNumberOfTwoStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) : 0L;
                            long totalNumberOfThreeStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) : 0L;
                            long totalNumberOfFourStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) : 0L;
                            long totalNumberOfFiveStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) : 0L;


                            String tutorialName = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY);
                            String tutorialCategory = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_CATEGORY_KEY);
                            String tutorialDescription = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_DESCRIPTION_KEY);

                            String dateCreated =  documentSnapshot.get(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY)!=null ? documentSnapshot.getTimestamp(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY).toDate()+"": "Undefined";
                            if(dateCreated.length()>10){
                                dateCreated = dateCreated.substring(0,10);
                            }
                            String authorUserId = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY);
                            long totalNumberOfPages = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_PAGES_CREATED_KEY)!=null ?documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_PAGES_CREATED_KEY) :0L;
                            long totalNumberOfFolders =  documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY)!=null ?documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY) :0L;
                            String tutorialCoverPhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY);



                            tutorialFetchListener.onSuccess(new TutorialDataModel(
                                                            tutorialName,
                                                            tutorialCategory,
                                    tutorialDescription,
                                                            tutorialId,
                                                            dateCreated,
                                                            totalNumberOfPages,
                                                            totalNumberOfFolders,
                                                            totalNumberOfTutorialVisitor,
                                                            totalNumberOfTutorialReach,
                                                            authorUserId,
                                                            libraryId,
                                                            tutorialCoverPhotoDownloadUrl,
                                                            totalNumberOfOneStarRate,
                                                            totalNumberOfTwoStarRate,
                                                            totalNumberOfThreeStarRate,
                                                            totalNumberOfFourStarRate,
                                                            totalNumberOfFiveStarRate
//                                    documentSnapshot
                                                            ));
                                }
                            });


    }

    void fetchAuthorProfile(){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(authorId)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String authorName1 = ""+documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                        String authorProfilePhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                        Glide.with(getApplicationContext())
                                .load(authorProfilePhotoDownloadUrl)
                                .into(authorPicture);
                        authorName.setText(authorName1);
                    }
                });
    }

    private void createNewFolder(final String folderName){
        String folderId = GlobalConfig.getRandomString(50);
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
        DocumentReference documentReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                .document(tutorialId)
                .collection(GlobalConfig.ALL_FOLDERS_KEY)
                .document(folderId);

        HashMap<String,Object> folderDetails = new HashMap<>();
        folderDetails.put(GlobalConfig.FOLDER_NAME_KEY,folderName );
        folderDetails.put(GlobalConfig.FOLDER_ID_KEY,folderId );
        folderDetails.put(GlobalConfig.AUTHOR_ID_KEY,authorId );
        folderDetails.put(GlobalConfig.LIBRARY_ID_KEY,libraryId );
        folderDetails.put(GlobalConfig.TUTORIAL_ID_KEY,tutorialId );
        folderDetails.put(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY,authorId);
        folderDetails.put(GlobalConfig.FOLDER_CREATED_DATE_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        writeBatch.set(documentReference,folderDetails, SetOptions.merge());

         DocumentReference tutorialDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                .document(tutorialId);
        HashMap<String,Object> tutorialDetails = new HashMap<>();
        tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY,FieldValue.increment(1L) );
        tutorialDetails.put(GlobalConfig.FOLDER_CREATED_DATE_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        writeBatch.set(tutorialDocumentReference,tutorialDetails, SetOptions.merge());


        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        GlobalHelpers.showAlertMessage("error",
                                TutorialActivity.this,
                                "Folder Creation failed.",
                                "Your "+folderName+" folder failed to create with error: "+e.getMessage());

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_FOLDER_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryId, tutorialId, folderId, null, null, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {
////
//                                                GlobalHelpers.showAlertMessage("success",
//                                                        TutorialActivity.this,
//                                                        "Folder Created Successfully.",
//                                                        "You have successfully created folder,thanks and go ahead and contribute to Learn Era ");
                            Intent intent  = new Intent(TutorialActivity.this,TutorialFolderActivity.class);
                                intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
                                intent.putExtra(GlobalConfig.FOLDER_ID_KEY,folderId);
                                intent.putExtra(GlobalConfig.FOLDER_NAME_KEY,folderName);
                                intent.putExtra(GlobalConfig.IS_FIRST_VIEW_KEY,true);
                                startActivity(intent);
                                leBottomSheetDialog.hide();
                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                //GlobalHelpers.showAlertMessage("success",
//                                    TutorialActivity.this,
//                                    "Folder Created Successfully.",
//                                    "You have successfully created folder,thanks and go ahead and contribute to Learn Era ");

                                Intent intent  = new Intent(TutorialActivity.this,TutorialFolderActivity.class);
                                intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
                                intent.putExtra(GlobalConfig.FOLDER_ID_KEY,folderId);
                                intent.putExtra(GlobalConfig.FOLDER_NAME_KEY,folderName);
                                intent.putExtra(GlobalConfig.IS_FIRST_VIEW_KEY,true);
                                startActivity(intent);
                                leBottomSheetDialog.hide();

                            }
                        });

                    }
                });
    }

    interface TutorialFetchListener{
        void onSuccess(TutorialDataModel tutorialDataModel);
        void onFailed(String errorMessage);
    }

}