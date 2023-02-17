package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.models.*;
import com.palria.learnera.widgets.LEBottomSheetDialog;
import com.palria.learnera.widgets.RatingBarWidget;
import com.palria.learnera.widgets.RatingBottomSheetWidget;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.glide.transformations.BitmapTransformation;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class LibraryActivity extends AppCompatActivity implements Serializable {

String libraryId;
String authorId;
String authorName;
String authorProfilePhotoDownloadUrl;
AlertDialog alertDialog;
FrameLayout tutorialsFrameLayout;
FrameLayout ratingsFrameLayout;
FrameLayout booksFrameLayout;
ArrayList<String> libraryCategoryList = new ArrayList<>();
TabLayout tabLayout;
ImageView libraryCoverImage;
ImageView authorPicture;
TextView libraryName;
TextView libraryDescription;
TextView authorNameView;
TextView libraryViewCount;
TextView tutorialsCount;
Button addActionButton;
Button saveActionButton;
Button rateActionButton;

ImageButton backButton;

TextView dateCreated;
ChipGroup categoriesChipGroup;

boolean isRatingFragmentOpen=false;
boolean isTutorialsFragmentOpen=false;
boolean isBooksFragmentOpen=false;

LEBottomSheetDialog leBottomSheetDialog;

    RatingBottomSheetWidget ratingBottomSheetWidget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        initUI();
        fetchIntentData();
        toggleProgress(true);
if(!authorId.equals(GlobalConfig.getCurrentUserId())){
    //this user is not the owner of this library, so hide some widgets to limit access
    addActionButton.setVisibility(View.GONE);
}
        fetchLibraryProfile(new LibraryProfileFetchListener() {
            @Override
            public void onFailed(String errorMessage) {
                toggleProgress(false);

            }

            @Override
            public void onSuccess(LibraryDataModel libraryDataModel) {
            //use this libraryDataModel object to access the public methods.

                libraryCategoryList = libraryDataModel.getLibraryCategoryArrayList();
                libraryDescription.setText(libraryDataModel.getLibraryDescription());
                libraryViewCount.setText(libraryDataModel.getTotalNumberOfLibraryViews()+"");
                tutorialsCount.setText(libraryDataModel.getTotalNumberOfTutorials()+"");

                toggleProgress(false);
                //set library name and image
                libraryName.setText(libraryDataModel.getLibraryName());
                Glide.with(LibraryActivity.this)
                        .load(libraryDataModel.getLibraryCoverPhotoDownloadUrl())
                        .placeholder(R.drawable.book_cover)
                       // .apply(RequestOptions.bitmapTransform(new BlurTransformation(10, 3)))
                        .into(libraryCoverImage);

                //set library cetegories.
                String categories = "N/A";
                ArrayList<String> cats = libraryDataModel.getLibraryCategoryArrayList();
                if(cats!=null) {
                    categories = "";
                    int j=0;
                    for (String cat : cats) {
                        if(j==cats.size()-1){
                            categories += cat;
                        }else{
                            categories += cat+", ";
                        }
                        j++;
                    }
                }

initCategoriesChip(categories);
                dateCreated.setText(libraryDataModel.getDateCreated());

                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_VISIT_LIBRARY_TYPE_KEY, authorId, libraryId, null,  null, null, null, null,  new GlobalConfig.ActionCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailed(String errorMessage) {
                    }
                });


            }
        });

        getAuthorProfile(new AuthorProfileFetchListener() {
            @Override
            public void onFailed(String errorMessage) {
                toggleProgress(false);

            }

            @Override
            public void onSuccess(String authorName, String authorProfilePhotoDownloadUrl) {
                toggleProgress(false);
                authorNameView.setText(authorName);
                Glide.with(LibraryActivity.this)
                        .load(authorProfilePhotoDownloadUrl)
                        .placeholder(R.drawable.default_profile)
                        .centerCrop()
                        .into(authorPicture);


            }
        });

        //tab layout selected goes here .
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               String tabTitle=tab.getText().toString().trim().toUpperCase();
               if(tabTitle.equals("TUTORIALS")){
                   if(isTutorialsFragmentOpen){
                       //Just set the frame layout visibility
                       setFrameLayoutVisibility(tutorialsFrameLayout);
                   }else {
                       isTutorialsFragmentOpen =true;
                       setFrameLayoutVisibility(tutorialsFrameLayout);
                       initFragment(new AllTutorialFragment(), tutorialsFrameLayout);
                   }


               }else if(tabTitle.equals("BOOKS"))
               {
                   if(isBooksFragmentOpen){
                       //Just set the frame layout visibility
                       setFrameLayoutVisibility(booksFrameLayout);
                   }else {
                       isBooksFragmentOpen =true;
                       setFrameLayoutVisibility(booksFrameLayout);
                       initFragment(new LibraryActivityRatingFragment(), booksFrameLayout);
                   }
               }else if(tabTitle.equals("RATINGS")){
                   if(isRatingFragmentOpen){
                       //Just set the frame layout visibility
                       setFrameLayoutVisibility(ratingsFrameLayout);
                   }else {
                       isRatingFragmentOpen =true;
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
                new AlertDialog.Builder(LibraryActivity.this)
                        .setTitle("Add this to bookmark?")
                        .setMessage("when you save to bookmark you are able to view it in your bookmarked" +
                                "ks for future.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                GlobalConfig.addToBookmark(authorId, libraryId, null, true, false, new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(LibraryActivity.this, "bookmarked", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
                                        Toast.makeText(LibraryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(LibraryActivity.this, "cancelled bookmark.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LibraryActivity.this.onBackPressed();
            }
        });

        openAllTutorialFragment();


        ratingBottomSheetWidget= new RatingBottomSheetWidget(this, authorId, libraryId,  null,false, true,false);
        ratingBottomSheetWidget.setRatingPostListener(new RatingBottomSheetWidget.OnRatingPosted(){

            @Override
            public void onPost(int star, String message) {
                Toast.makeText(LibraryActivity.this,star + "-"+ message, Toast.LENGTH_SHORT).show();

            }

            @Override
            public  void onFailed(String errorMessage){
                Toast.makeText(LibraryActivity.this,"failed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(boolean isReviewAuthor,boolean isReviewLibrary,boolean isReviewTutorial){
                Toast.makeText(LibraryActivity.this,"You rated this library", Toast.LENGTH_SHORT).show();

            }
        }).render();

    }

    private void initFragment(Fragment fragment, FrameLayout frameLayout){
Bundle bundle = new Bundle();
bundle.putString(GlobalConfig.LIBRARY_ID_KEY,libraryId);
fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), fragment)
                .commit();


    }

    private void setFrameLayoutVisibility(FrameLayout frameLayoutToSetVisible){
        tutorialsFrameLayout.setVisibility(View.GONE);
        ratingsFrameLayout.setVisibility(View.GONE);
        booksFrameLayout.setVisibility(View.GONE);
        frameLayoutToSetVisible.setVisibility(View.VISIBLE);
    }

    private void initUI(){


        tutorialsFrameLayout=findViewById(R.id.tutorialsFrameLayout);
        booksFrameLayout=findViewById(R.id.booksFrameLayout);
        ratingsFrameLayout=findViewById(R.id.booksFrameLayout);

        tabLayout=findViewById(R.id.tab_layout);
        libraryCoverImage=findViewById(R.id.libraryCoverImage);
        authorPicture=findViewById(R.id.authorPicture);
        libraryName=findViewById(R.id.libraryName);
        libraryDescription=findViewById(R.id.libraryDescription);
        authorNameView=findViewById(R.id.authorName);
        libraryViewCount=findViewById(R.id.libraryViewCount);
        tutorialsCount=findViewById(R.id.tutorialsCount);
        addActionButton=findViewById(R.id.addActionButton);
        saveActionButton=findViewById(R.id.saveActionButton);
        rateActionButton=findViewById(R.id.rateActionButton);

        backButton=findViewById(R.id.backButton);
        dateCreated=findViewById(R.id.dateCreated);
        categoriesChipGroup=findViewById(R.id.categoriesChipGroup);

        alertDialog = new AlertDialog.Builder(LibraryActivity.this)
            .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
            .create();

        //set tab layout here .
//        tabLayout.addTab( tabLayout.newTab(),0,true);
//        tabLayout.addTab( tabLayout.newTab(),1);
////        tabLayout.addTab( tabLayout.newTab(),2);
//        tabLayout.getTabAt(0).setText("Tutorials");
////        tabLayout.getTabAt(1).setText("Books");
//        tabLayout.getTabAt(1).setText("Ratings");

        leBottomSheetDialog = new LEBottomSheetDialog(this)
                .addOptionItem("New Tutorial", R.drawable.ic_baseline_add_circle_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        leBottomSheetDialog.hide();

                        Intent i = new Intent(LibraryActivity.this, CreateNewTutorialActivity.class);
                        //creating new

                        ArrayList<String> catArrayKeys=new ArrayList<>();
                        catArrayKeys.add("Java and Design");

                        i.putExtra(GlobalConfig.IS_CREATE_NEW_TUTORIAL_KEY,true);
                        i.putExtra(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY, libraryCategoryList);
                        i.putExtra(GlobalConfig.LIBRARY_CONTAINER_ID_KEY,libraryId);
                        i.putExtra(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY, libraryName.getText().toString());
                        startActivity(i);

                    }
                },0)
//                .addOptionItem("New Book", R.drawable.ic_baseline_library_books_24, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                },0)
                .render();


}



    private void toggleProgress(boolean show) {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }


    private void initCategoriesChip(String categories){

        categoriesChipGroup.removeAllViews();
        String[] cats = categories.split(",");
        for(String cat : cats){
            Chip chip = new Chip(this);
            chip.setText(cat);
            categoriesChipGroup.addView(chip);
        }

    }


    private void fetchIntentData() {
        Intent intent = getIntent();
        libraryId = intent.getStringExtra(GlobalConfig.LIBRARY_ID_KEY);
        authorId = intent.getStringExtra(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);

    }

    private void fetchLibraryProfile(LibraryProfileFetchListener libraryProfileFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_LIBRARY_KEY)
                .document(libraryId)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(LibraryActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot){
                String libraryId = documentSnapshot.getId();
                long totalNumberOfLibraryVisitor = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY) : 0L;
                long totalNumberOfLibraryReach = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) : 0L;
                long totalNumberOfOneStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) : 0L;
                long totalNumberOfTwoStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) : 0L;
                long totalNumberOfThreeStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) : 0L;
                long totalNumberOfFourStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) : 0L;
                long totalNumberOfFiveStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) : 0L;
                long totalNumberOfTutorials = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) : 0L;


                String libraryName = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                ArrayList<String> libraryCategoryArray = (ArrayList<String>) documentSnapshot.get(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY);
                String libraryDescription = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DESCRIPTION_KEY);

                String dateCreated =  documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY)!=null ? documentSnapshot.getTimestamp(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY).toDate()+"": "Undefined";
                if(dateCreated.length()>10){
                    dateCreated = dateCreated.substring(0,10);
                }
                String authorUserId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);
                String libraryCoverPhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);



                libraryProfileFetchListener.onSuccess(new LibraryDataModel(
                        libraryName,
                        libraryId,
                        libraryCategoryArray,
                        libraryCoverPhotoDownloadUrl,
                        libraryDescription,
                        dateCreated,
                        totalNumberOfTutorials,
                        totalNumberOfLibraryVisitor,
                        totalNumberOfLibraryReach,
                        authorUserId,
                        totalNumberOfOneStarRate,
                        totalNumberOfTwoStarRate,
                        totalNumberOfThreeStarRate,
                        totalNumberOfFourStarRate,
                        totalNumberOfFiveStarRate
                ));
            }

        });
    }

    private void getAuthorProfile(AuthorProfileFetchListener authorProfileFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(authorId)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        authorProfileFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                    authorName =""+  documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);

                    authorProfilePhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
//USE THIS authorProfilePhotoDownloadUrl TO LOAD THE AUTHOR'S COVER PHOTO INTO IMAGE VIEW
//                        Picasso.get().load(authorProfilePhotoDownloadUrl).into(/*put image view object here*/);
                        authorProfileFetchListener.onSuccess(authorName,authorProfilePhotoDownloadUrl);
                    }
                });

    }

    private void openAllTutorialFragment(){
        isTutorialsFragmentOpen=true;
        AllTutorialFragment tutorialsFragment = new AllTutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(GlobalConfig.IS_FROM_LIBRARY_ACTIVITY_CONTEXT_KEY,true);
        bundle.putString(GlobalConfig.LIBRARY_CONTAINER_ID_KEY,libraryId);
        tutorialsFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(tutorialsFrameLayout.getId(),tutorialsFragment)
                .commit();
    }


    interface LibraryProfileFetchListener{
        void onFailed(String errorMessage);
        void onSuccess(LibraryDataModel libraryDataModel);
    }
    interface AuthorProfileFetchListener{
        void onFailed(String errorMessage);
        void onSuccess(String authorName,String authorProfilePhotoDownloadUrl);
    }

}