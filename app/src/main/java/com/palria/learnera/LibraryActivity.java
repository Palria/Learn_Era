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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
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


boolean isFirstView = true;
String libraryId;
String authorId;
String authorName;
String authorProfilePhotoDownloadUrl;
AlertDialog alertDialog;
FrameLayout mainLayout;
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
int[] ratings = {0,0,0,0,0};
ImageButton backButton;
ImageButton editLibraryActionButton;

TextView dateCreated;
ChipGroup categoriesChipGroup;

boolean isRatingFragmentOpen=false;
boolean isTutorialsFragmentOpen=false;
boolean isBooksFragmentOpen=false;

LEBottomSheetDialog leBottomSheetDialog;

    RatingBottomSheetWidget ratingBottomSheetWidget;
DocumentSnapshot intentLibraryDocumentSnapshot;
LibraryDataModel intentLibraryDataModel;
    LibraryProfileFetchListener libraryProfileFetchListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        fetchIntentData();
        initUI();

        toggleProgress(true);
if(!authorId.equals(GlobalConfig.getCurrentUserId())){
    //this user is not the owner of this library, so hide some widgets to limit access
    addActionButton.setVisibility(View.GONE);
}
        libraryProfileFetchListener = new LibraryProfileFetchListener() {
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

                ratings[0] = Integer.parseInt(Long.toString(libraryDataModel.getTotalNumberOfOneStarRate()));
                ratings[1] = Integer.parseInt(Long.toString(libraryDataModel.getTotalNumberOfTwoStarRate()));
                ratings[2] = Integer.parseInt(Long.toString(libraryDataModel.getTotalNumberOfThreeStarRate()));
                ratings[3] = Integer.parseInt(Long.toString(libraryDataModel.getTotalNumberOfFourStarRate()));
                ratings[4] = Integer.parseInt(Long.toString(libraryDataModel.getTotalNumberOfFiveStarRate()));


                dateCreated.setText(libraryDataModel.getDateCreated());

//                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_VISIT_LIBRARY_TYPE_KEY, authorId, libraryId, null,  null, null, null, null,  new GlobalConfig.ActionCallback() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onFailed(String errorMessage) {
//                    }
//                });

                GlobalConfig.incrementNumberOfVisitors(authorId,libraryId,null,null,null,false,true,false,false,false,false);


            }
        };
if(isFirstView) {
    fetchLibraryProfile();
}else{
    libraryProfileFetchListener.onSuccess(intentLibraryDataModel);
}

        getAuthorProfile(new AuthorProfileFetchListener() {
            @Override
            public void onFailed(String errorMessage) {
                toggleProgress(false);

            }

            @Override
            public void onSuccess(String authorName, String authorProfilePhotoDownloadUrl) {
                toggleProgress(false);
                authorNameView.setText(authorName);
                try {
                    Glide.with(LibraryActivity.this)
                            .load(authorProfilePhotoDownloadUrl)
                            .placeholder(R.drawable.default_profile)
                            .centerCrop()
                            .into(authorPicture);
                }catch(Exception e){}

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
                       LibraryActivityRatingFragment libraryActivityRatingFragment = new LibraryActivityRatingFragment();
                       Bundle bundle = new Bundle();
                       bundle.putBoolean(GlobalConfig.IS_LIBRARY_REVIEW_KEY,true);
                       bundle.putString(GlobalConfig.LIBRARY_ID_KEY,libraryId);
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

        addActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             leBottomSheetDialog.show();

            }
        });
        authorPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(GlobalConfig.getHostActivityIntent(getApplicationContext(),null,GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY,authorId));

            }
        });
        authorNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(GlobalConfig.getHostActivityIntent(getApplicationContext(),null,GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY,authorId));

            }
        });

        rateActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateActionButton.setEnabled(false);
                DocumentReference authorReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_LIBRARY_KEY)
                        .document(libraryId).collection(GlobalConfig.REVIEWS_KEY)
                        .document(GlobalConfig.getCurrentUserId());
                Snackbar snackbar = GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Initializing rating details...", Snackbar.LENGTH_INDEFINITE);
                //Check if he has already rated this library, else if not rated then rate but if rated edit the rating
                GlobalConfig.checkIfDocumentExists(authorReviewDocumentReference, new GlobalConfig.OnDocumentExistStatusCallback() {
                    @Override
                    public void onExist() {
                        rateActionButton.setEnabled(true);

                        snackbar.dismiss();

                        new AlertDialog.Builder(LibraryActivity.this)
                                .setTitle("Library already reviewed!")
                                .setMessage("Chose what to do with the already reviewed library:")
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

                                        GlobalConfig.removeLibraryReview(libraryId, new GlobalConfig.ActionCallback() {
                                            @Override
                                            public void onSuccess() {
                                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_DELETE_LIBRARY_REVIEW_TYPE_KEY, authorId, null, null, null, null, GlobalConfig.getCurrentUserId(), new GlobalConfig.ActionCallback() {
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
                        rateActionButton.setEnabled(true);

                        snackbar.dismiss();
                        GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Failed to initialize rating", Snackbar.LENGTH_SHORT);


                    }
                });
            }
        });

        saveActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar saveSnackBar = GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Initializing bookmark please wait...", Snackbar.LENGTH_INDEFINITE);
                saveActionButton.setEnabled(false);
                //CHECK IF THE CURRENT USER HAS ALREADY SAVED THIS LIBRARY, IF SO DO STH
                DocumentReference bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(libraryId);
                GlobalConfig.checkIfDocumentExists(bookMarkOwnerReference, new GlobalConfig.OnDocumentExistStatusCallback() {
                    @Override
                    public void onExist() {
                        saveSnackBar.dismiss();
                        saveActionButton.setEnabled(true);

                        new AlertDialog.Builder(LibraryActivity.this)
                                .setTitle("Remove this  bookmark?")
                                .setMessage("You have already added this library to your bookmarks")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Snackbar snackBar = GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Removing from bookmark...", Snackbar.LENGTH_INDEFINITE);

                                        GlobalConfig.removeBookmark(authorId, libraryId, null, null,null,GlobalConfig.LIBRARY_TYPE_KEY, new GlobalConfig.ActionCallback() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(LibraryActivity.this, "bookmark removed", Toast.LENGTH_SHORT).show();
                                                snackBar.dismiss();
                                                GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Bookmark removed!", Snackbar.LENGTH_SHORT);
                                                saveActionButton.setText(R.string.save);
                                            }

                                            @Override
                                            public void onFailed(String errorMessage) {
                                                Toast.makeText(LibraryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                                snackBar.dismiss();
                                                GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Failed to remove from bookmark please try again!", Snackbar.LENGTH_SHORT);
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(LibraryActivity.this, "undo remove bookmark.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onNotExist() {
                        saveSnackBar.dismiss();
                        saveActionButton.setEnabled(true);

                        new AlertDialog.Builder(LibraryActivity.this)
                                .setTitle("Add this to bookmark?")
                                .setMessage("when you save to bookmark you are able to view it in your bookmarked")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Snackbar snackBar = GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Adding to bookmark...", Snackbar.LENGTH_INDEFINITE);

                                        GlobalConfig.addToBookmark(authorId, libraryId, null, null,null,GlobalConfig.LIBRARY_TYPE_KEY, new GlobalConfig.ActionCallback() {
                                            @Override
                                            public void onSuccess() {
//                                                Toast.makeText(LibraryActivity.this, "bookmark added", Toast.LENGTH_SHORT).show();
                                                snackBar.dismiss();
                                                GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Bookmark added!", Snackbar.LENGTH_SHORT);
                                                saveActionButton.setText(R.string.un_save);

                                            }

                                            @Override
                                            public void onFailed(String errorMessage) {
                                                Toast.makeText(LibraryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                                snackBar.dismiss();
                                                GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Failed to add to bookmark please try again!", Snackbar.LENGTH_SHORT);
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

                    @Override
                    public void onFailed(@NonNull String errorMessage) {
                        saveSnackBar.dismiss();
                        GlobalConfig.createSnackBar(getApplicationContext(),mainLayout,"Failed to Fetch bookmark details please try again", Snackbar.LENGTH_SHORT);
                        saveActionButton.setEnabled(true);

                    }
                });


            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LibraryActivity.this.onBackPressed();
            }
        });
        editLibraryActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CreateNewLibraryActivity.class);
                intent.putExtra(GlobalConfig.IS_CREATE_NEW_LIBRARY_KEY,false);
                intent.putExtra(GlobalConfig.LIBRARY_ID_KEY,libraryId);
                startActivity(intent);


            }
        });

        openAllTutorialFragment();

        //CHECK IF THE CURRENT USER HAS ALREADY SAVED THIS LIBRARY, IF SO DO STH
        DocumentReference bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId())
                .collection(GlobalConfig.BOOK_MARKS_KEY).document(libraryId);
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


        //CHECK IF THE CURRENT USER HAS ALREADY RATED THIS LIBRARY, IF SO DO STH
        DocumentReference authorReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_LIBRARY_KEY)
                .document(libraryId).collection(GlobalConfig.REVIEWS_KEY)
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

        ratingBottomSheetWidget= new RatingBottomSheetWidget(this, authorId, libraryId,  null,false, true,false){

        };
        ratingBottomSheetWidget.setRatingPostListener(new RatingBottomSheetWidget.OnRatingPosted(){

            @Override
            public void onPost(int star, String message) {
                super.onPost(star,message);
//                Toast.makeText(LibraryActivity.this,star + "-"+ message, Toast.LENGTH_SHORT).show();

            }

            @Override
            public  void onFailed(String errorMessage){
                super.onFailed(errorMessage);

//                Toast.makeText(LibraryActivity.this,"failed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(boolean isReviewAuthor,boolean isReviewLibrary,boolean isReviewTutorial){
                super.onSuccess(isReviewAuthor,isReviewLibrary,isReviewTutorial);
//                Toast.makeText(LibraryActivity.this,"You rated this library", Toast.LENGTH_SHORT).show();

                rateActionButton.setTextColor(getResources().getColor(R.color.teal_700,getTheme()));
                rateActionButton.setText("Rated");

            }
        });

    }

    private void initFragment(Fragment fragment, FrameLayout frameLayout){
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


        mainLayout=findViewById(R.id.mainLayout);
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
        editLibraryActionButton=findViewById(R.id.editLibraryActionButtonId);
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
        if(!authorId.equals(GlobalConfig.getCurrentUserId())) {
            addActionButton.setVisibility(View.GONE);
            editLibraryActionButton.setVisibility(View.GONE);
        }


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
        isFirstView = intent.getBooleanExtra(GlobalConfig.IS_FIRST_VIEW_KEY,true);
if(!isFirstView) {
    intentLibraryDataModel = (LibraryDataModel) intent.getSerializableExtra(GlobalConfig.LIBRARY_DATA_MODEL_KEY);
    intentLibraryDocumentSnapshot = intentLibraryDataModel.getLibraryDocumentSnapshot();
}

    }

    private void fetchLibraryProfile(){
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
//                        documentSnapshot
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