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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.models.TutorialDataModel;
import com.palria.learnera.widgets.LEBottomSheetDialog;
import com.palria.learnera.widgets.RatingBottomSheetWidget;

import java.util.ArrayList;

public class TutorialActivity extends AppCompatActivity {
String tutorialId = "";
String authorId = "";
String libraryId = "";

RoundedImageView tutorialCoverImage;
RoundedImageView authorPicture;
TextView authorName;
TextView dateCreated;
TextView tutorialViewCount;
TextView pagesCount;
TextView foldersCount;
TextView tutorialName;
TextView tutorialDescription;

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
                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_VISIT_TUTORIAL_TYPE_KEY, authorId, libraryId, tutorialId, false, false, true, null, null, null, null, false, false, false, new GlobalConfig.ActionCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailed(String errorMessage) {

                    }
                });

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
                        bundle.putString(GlobalConfig.FOLDER_ID_KEY,null);
                        bundle.putBoolean(GlobalConfig.IS_FOLDER_PAGE_KEY,false);
                        allTutorialPageFragment.setArguments(bundle);
//                        initFragment(new AllTutorialPageFragment(), pagesFrameLayout);
                    }
                }else if(tabTitle.equals("RATINGS")){
                    if(isRatingsFragmentOpened){
                        //Just set the frame layout visibility
                        setFrameLayoutVisibility(ratingsFrameLayout);
                    }else {
                        isRatingsFragmentOpened =true;
                        setFrameLayoutVisibility(ratingsFrameLayout);
                        initFragment(new LibraryActivityRatingFragment(), ratingsFrameLayout);
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
                        .setMessage("when you save to bookmark you are abale to view it in your bookmar" +
                                "ks for future.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(TutorialActivity.this, "bookmared", Toast.LENGTH_SHORT).show();
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

                        Intent i = new Intent(TutorialActivity.this, CreateNewTutorialFolderActivity.class);
                        i.putExtra(GlobalConfig.TUTORIAL_ID_KEY,"some-id");
                        //creating new
                        startActivity(i);

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


        ratingBottomSheetWidget= new RatingBottomSheetWidget(this);
        ratingBottomSheetWidget.setRatingPostListener(new RatingBottomSheetWidget.OnRatingPosted(){
            @Override
            public void onPost(int star, String message) {
                Toast.makeText(TutorialActivity.this,star + "-"+ message, Toast.LENGTH_SHORT).show();
                //add the rating to the database with current user.



            }
        }).render();


    }
    private void fetchIntentData(){
        Intent intent = getIntent();
        tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);

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




    interface TutorialFetchListener{
        void onSuccess(TutorialDataModel tutorialDataModel);
        void onFailed(String errorMessage);
    }

}