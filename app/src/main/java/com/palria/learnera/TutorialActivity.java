package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.makeramen.roundedimageview.RoundedImageView;
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

//buttonsl
Button addActionButton;
    Button saveActionButton;
    Button rateActionButton;
    ImageButton backButton;

    RatingBottomSheetWidget ratingBottomSheetWidget;

    LEBottomSheetDialog leBottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        iniUI();
        fetchIntentData();

        fetchTutorial(new TutorialFetchListener() {
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

                GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(tutorialDataModel.getAuthorId())
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
        });

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
                        bundle.putString(GlobalConfig.FOLDER_ID_KEY,"folderId");
                        bundle.putBoolean(GlobalConfig.IS_FOLDER_PAGE_KEY,false);
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
//        tabLayout.getChildAt(tabLayout.getTabAt(0).getPosition()).setSelected(true);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TutorialActivity.this.onBackPressed();
            }
        });

        addActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                leBottomSheetDialog.show();

            }
        });

        rateActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingBottomSheetWidget.show();
            }
        });

        saveActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(TutorialActivity.this)
                        .setTitle("Add this to bookmark?")
                        .setMessage("when you save to bookmark you are able to view it in your bookmark" +
                                "ks for future.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                GlobalConfig.addToBookmark(authorId, libraryId, tutorialId, false, true, new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(TutorialActivity.this, "bookmarked", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
                                        Toast.makeText(TutorialActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

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
        });


        ratingBottomSheetWidget= new RatingBottomSheetWidget(this, authorId, libraryId,  tutorialId,false, false,true);;
        ratingBottomSheetWidget.setRatingPostListener(new RatingBottomSheetWidget.OnRatingPosted(){

            @Override
            public void onPost(int star, String message) {
                Toast.makeText(TutorialActivity.this,star + "-"+ message, Toast.LENGTH_SHORT).show();

            }

            @Override
            public  void onFailed(String errorMessage){
                Toast.makeText(TutorialActivity.this,errorMessage, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(boolean isReviewAuthor,boolean isReviewLibrary,boolean isReviewTutorial){
                Toast.makeText(TutorialActivity.this,"You rated this tutorial", Toast.LENGTH_SHORT).show();

            }
        }).render();

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
        rateActionButton=findViewById(R.id.rateActionButton);
        backButton=findViewById(R.id.backButton);

        tabLayout=findViewById(R.id.tab_layout);


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
                               .setOnSubmit(new BottomSheetFormBuilderWidget.OnSubmitHandler(){
                                   @Override
                                   public void onSubmit(String[] values) {
                                       super.onSubmit(values);

                                       Toast.makeText(TutorialActivity.this, values[0],Toast.LENGTH_SHORT).show();
                                        //values will be returned as array of strings as per input list position
                                       //eg first added input has first value
                                       String folderName = values[0];
                                       if(folderName.trim().equals("")){
//                                           leBottomSheetDialog.setTitle("Folder needs name, must enter name for the folder");

//                                           Toast.makeText(TutorialActivity.this, "Please enter name",Toast.LENGTH_SHORT).show();
                                       }else{

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
                },0)
                .addOptionItem("New page", R.drawable.baseline_pages_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(TutorialActivity.this, CreateNewTutorialPageActivity.class);
                        i.putExtra(GlobalConfig.TUTORIAL_ID_KEY,"some-id");
                        //creating new
                        startActivity(i);
                    }
                },0)
                .render();



    }
    private void fetchIntentData(){
        Intent intent = getIntent();
        tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);
        libraryId = intent.getStringExtra(GlobalConfig.LIBRARY_CONTAINER_ID_KEY);
        isFirstView = intent.getBooleanExtra(GlobalConfig.IS_FIRST_VIEW_KEY,true);

    }

    //


    private void fetchTutorial(TutorialFetchListener tutorialFetchListener){
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
                                                            ));
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
                        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_FOLDER_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryId, tutorialId, folderId, null, null,null, new GlobalConfig.ActionCallback() {
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