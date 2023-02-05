package com.palria.learnera;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.models.LibraryDataModel;
import com.palria.learnera.widgets.LEBottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class UserProfileFragment extends Fragment {

    AlertDialog alertDialog;
    LinearLayout containerLinearLayout;

    //views
    ImageView editProfileButton;
    RoundedImageView profileImageView;
    TextView currentDisplayNameView;
    TextView currentEmailView;
    TextView currentCountryOfResidence;

    //parent swiper layout
    SwipeRefreshLayout swipeRefreshLayout;
    NestedScrollView parentScrollView;
    BottomAppBar bottomAppBar;

    TextView   failureIndicatorTextView;
    Button profileMoreIconButton;
    Button statsButton;

    TextView logButton;

    //learn era bottom sheet dialog
    LEBottomSheetDialog leBottomSheetDialog;

    boolean isUserAuthor = false;

    //replace all catetoareis with categoreis from database
    String[] categories = {"Software Development", "Ui Design", "Web Development", "Machine Learning",
    "Database Design", "Furniture", "Internet", "Communication", "Story", "Drama", "Podcasts"};


    boolean[] selectedCategories;
    ArrayList<Integer> catsList = new ArrayList<>();

    public UserProfileFragment() {
        // Required empty public constructor
    }
    public UserProfileFragment(BottomAppBar b) {
        bottomAppBar = b;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedCategories = new boolean[categories.length];

        getProfile(new OnUserProfileFetchListener() {
            @Override
            public void onSuccess(String userDisplayName, String userCountryOfResidence, String contactEmail, String contactPhoneNumber, String genderType, String userProfilePhotoDownloadUrl, boolean isUserBlocked, boolean isUserProfilePhotoIncluded) {

            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View parentView = inflater.inflate(R.layout.fragment_user_profile, container, false);
       initUI(parentView);
//       toggleProgress(true);
        swipeRefreshLayout.setRefreshing(true);

        loadCurrentUserProfile();
       fetchAllLibrary(new LibraryFetchListener() {
           @Override
           public void onFailed(String errorMessage) {
//               toggleProgress(false);

           }

           @Override
           public void onSuccess(LibraryDataModel libraryDataModel) {
//               toggleProgress(false);
               displayLibrary(libraryDataModel);

               parentScrollView.setVisibility(View.VISIBLE);
               swipeRefreshLayout.setRefreshing(false);

           }
       });

       editProfileButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent i = new Intent(getContext(), EditCurrentUserProfileActivity.class);
               startActivity(i);

           }
       });

       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               loadCurrentUserProfile();
               swipeRefreshLayout.setRefreshing(true);
           }
       });

       parentScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
           float y = 0;
           @Override
           public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

             if(scrollY > 30){
                 bottomAppBar.performHide();
             }else{
                 bottomAppBar.performShow();
             }

           }
       });

        profileMoreIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                leBottomSheetDialog.show();

            }
        });


        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), UserStatsActivity.class);
                startActivity(intent);

            }
        });

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), UserActivityLogActivity.class);
                startActivity(intent);

            }
        });




       return parentView;
    }


    private void loadCurrentUserProfile(){
        getProfile(new OnUserProfileFetchListener() {
            @Override
            public void onSuccess(String userDisplayName, String userCountryOfResidence, String contactEmail, String contactPhoneNumber, String genderType, String userProfilePhotoDownloadUrl, boolean isUserBlocked, boolean isUserProfilePhotoIncluded) {
                toggleProgress(false);

                Glide.with(getContext())
                        .load(userProfilePhotoDownloadUrl)
                        .centerCrop()
                        .into(profileImageView);

                currentEmailView.setText(Html.fromHtml("Contact Email <b>"+contactEmail+"</b> "));
                currentDisplayNameView.setText(userDisplayName);

                currentCountryOfResidence.setText(Html.fromHtml("From <b>"+userCountryOfResidence+"</b> "));


                parentScrollView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailed(String errorMessage) {
//                toggleProgress(false);

                failureIndicatorTextView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private void initUI(View parentView){
        //use the parentView to find the by Id as in : parentView.findViewById(...);

        parentScrollView = parentView.findViewById(R.id.scrollView);

        failureIndicatorTextView = parentView.findViewById(R.id.failureIndicatorTextViewId);
        //init views
        editProfileButton = parentView.findViewById(R.id.editProfileIcon);
        profileImageView = parentView.findViewById(R.id.imageView1);
        currentDisplayNameView = parentView.findViewById(R.id.current_name);
        currentEmailView = parentView.findViewById(R.id.current_email);
        currentCountryOfResidence = parentView.findViewById(R.id.current_country);
        logButton=parentView.findViewById(R.id.logButton);
        statsButton=parentView.findViewById(R.id.statsButton);

        swipeRefreshLayout = parentView.findViewById(R.id.swiperRefreshLayout);
        profileMoreIconButton = parentView.findViewById(R.id.profileMoreIcon);


        alertDialog = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();

        leBottomSheetDialog = new LEBottomSheetDialog(getContext());

        leBottomSheetDialog
                .addOptionItem("My Tutorials", R.drawable.ic_baseline_dynamic_feed_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }, 0)
                .addOptionItem("My Libraries", R.drawable.ic_baseline_library_books_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }, 0);



        if(!isUserAuthor){
            leBottomSheetDialog.addOptionItem("Become An Author", R.drawable.ic_baseline_edit_24, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //show they are going to become an author
                    leBottomSheetDialog.hide();
                    Toast.makeText(getContext(), "author not", Toast.LENGTH_SHORT).show();
                    showPromptToBeAnAuthor();

                }
            },0);
        }
        leBottomSheetDialog.render();

    }

    private void showPromptToBeAnAuthor() {

        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // set title
        builder.setTitle("Choose Prefered Categories for Your Library.");

        // set dialog non cancelable
        builder.setCancelable(false);
        builder.setTitle("Are you sure to become an author in "+getString(R.string.app_name)+" ?")
                .setMessage("When you become an author you are abale to create your own libraryes and tutorials." +
                        "Click Yes to be Author.");



        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //make a user as a author here

                isUserAuthor = !isUserAuthor;

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        builder.show();

    }

    private  void getProfile(OnUserProfileFetchListener onUserProfileFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId())
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onUserProfileFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String userDisplayName =""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                        String userCountryOfResidence =""+ documentSnapshot.get(GlobalConfig.USER_COUNTRY_OF_RESIDENCE_KEY);
                        String contactEmail =""+ documentSnapshot.get(GlobalConfig.USER_CONTACT_EMAIL_ADDRESS_KEY);
                        String contactPhoneNumber =""+ documentSnapshot.get(GlobalConfig.USER_CONTACT_PHONE_NUMBER_KEY);
                        String genderType =""+ documentSnapshot.get(GlobalConfig.USER_GENDER_TYPE_KEY);
                        String userProfilePhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                        boolean isUserBlocked = false;
                        boolean isUserProfilePhotoIncluded = false;
                        if(documentSnapshot.get(GlobalConfig.IS_USER_BLOCKED_KEY) != null){
                            isUserBlocked =documentSnapshot.getBoolean(GlobalConfig.IS_USER_BLOCKED_KEY);

                        }
                        if(documentSnapshot.get(GlobalConfig.IS_USER_PROFILE_PHOTO_INCLUDED_KEY) != null){
                            isUserProfilePhotoIncluded = documentSnapshot.getBoolean(GlobalConfig.IS_USER_PROFILE_PHOTO_INCLUDED_KEY);

                        }

                        onUserProfileFetchListener.onSuccess( userDisplayName, userCountryOfResidence, contactEmail, contactPhoneNumber, genderType, userProfilePhotoDownloadUrl, isUserBlocked, isUserProfilePhotoIncluded);

                    }
                });
    }


    private void fetchAllLibrary(LibraryFetchListener libraryFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_LIBRARY_KEY)
                .whereEqualTo(GlobalConfig.AUTHOR_ID_KEY,GlobalConfig.getCurrentUserId())
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        libraryFetchListener.onFailed(e.getMessage());

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String libraryId = documentSnapshot.getId();
                            long totalNumberOfLibraryView = 0L;
                            if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VIEWS_KEY) != null){
                                totalNumberOfLibraryView =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VIEWS_KEY);
                            }
                            long totalNumberOfLibraryReach = 0L;
                            if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) != null){
                                totalNumberOfLibraryReach =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY);
                            }
                            long totalNumberOfOneStarRate = 0L;
                            if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null){
                                totalNumberOfOneStarRate =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY);
                            }
                            long totalNumberOfTwoStarRate = 0L;
                            if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null){
                                totalNumberOfTwoStarRate =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY);
                            }
                            long totalNumberOfThreeStarRate = 0L;
                            if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null){
                                totalNumberOfThreeStarRate =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY);
                            }
                            long totalNumberOfFourStarRate = 0L;
                            if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null){
                                totalNumberOfFourStarRate =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY);
                            }
                            long totalNumberOfFiveStarRate = 0L;
                            if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null){
                                totalNumberOfFiveStarRate =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY);
                            }
                            long finalTotalNumberOfLibraryView = totalNumberOfLibraryView;
                            long finalTotalNumberOfLibraryReach = totalNumberOfLibraryReach;
                            long finalTotalNumberOfOneStarRate = totalNumberOfOneStarRate;
                            long finalTotalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
                            long finalTotalNumberOfFourStarRate = totalNumberOfFourStarRate;
                            long finalTotalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
                            long finalTotalNumberOfFiveStarRate = totalNumberOfFiveStarRate;

                            String libraryName = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                            ArrayList<String> libraryCategoryArray = (ArrayList<String>) documentSnapshot.get(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY);
                            String dateCreated = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_KEY);
                            String authorUserId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);
                            String libraryCoverPhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);
                            long totalNumberOfTutorials = 0L;
                            if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) != null){
                                totalNumberOfTutorials =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY);
                            }


                            libraryFetchListener.onSuccess(new LibraryDataModel(
                                    libraryName,
                                    libraryId,
                                    libraryCategoryArray,
                                    libraryCoverPhotoDownloadUrl,
                                    dateCreated,
                                    totalNumberOfTutorials,
                                    finalTotalNumberOfLibraryView,
                                    finalTotalNumberOfLibraryReach,
                                    authorUserId,
                                    finalTotalNumberOfOneStarRate,
                                    finalTotalNumberOfTwoStarRate,
                                    finalTotalNumberOfThreeStarRate,
                                    finalTotalNumberOfFourStarRate,
                                    finalTotalNumberOfFiveStarRate
                            ));
                        }
                    }
                });

    }
    private void displayLibrary(LibraryDataModel libraryDataModel){

                 if(getContext() != null) {
    LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    //Uncomment and implement

  /*
    View libraryView = layoutInflater.inflate(R.layout.library_custom_view, null);
    TextView libraryNameTextView = libraryView.findViewById(R.id.libraryNameTextViewId);
    ImageView libraryCoverPhoto = libraryView.findViewById(R.id.libraryCoverPhotoId);

    libraryNameTextView.setText(libraryDataModel.getLibraryName());
    Glide.with(getContext())
            .load(libraryDataModel.getLibraryCoverPhotoDownloadUrl())
            .centerCrop()
            .into(libraryCoverPhoto);
libraryView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(),LibraryActivity.class);
        intent.putExtra(GlobalConfig.LIBRARY_ID_KEY,libraryDataModel.getLibraryId());
        intent.putExtra(GlobalConfig.LIBRARY_AUTHOR_ID_KEY,libraryDataModel.getAuthorUserId());
        startActivity(intent);
    }
});

    containerLinearLayout.addView(libraryView);
    */
}

    }

    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }

    private void becomeAnAuthor(ArrayList<String> categoryTagList, BecomeAuthorListener becomeAuthorListener){
        DocumentReference userProfileDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId());
        HashMap<String,Object> authorDetails = new HashMap<>();
        authorDetails.put(GlobalConfig.AUTHOR_CATEGORY_TAG_ARRAY_KEY,categoryTagList);
        authorDetails.put(GlobalConfig.IS_USER_AUTHOR_KEY,true);
        authorDetails.put(GlobalConfig.AUTHOR_DATE_KEY,GlobalConfig.getDate());
        authorDetails.put(GlobalConfig.AUTHOR_DATE_TIME_STAMP_KEY, FieldValue.serverTimestamp());

        userProfileDocumentReference.update(authorDetails)
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                becomeAuthorListener.onFailed(e.getMessage());
            }
        })
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                becomeAuthorListener.onSuccess();
            }
        });
    }

    interface LibraryFetchListener{
        void onFailed(String errorMessage);
        void onSuccess(LibraryDataModel libraryDataModel);
    }
    interface BecomeAuthorListener{
        void onFailed(String errorMessage);
        void onSuccess();
    }

    interface OnUserProfileFetchListener{
        void onSuccess(String userDisplayName,String userCountryOfResidence,String contactEmail,String contactPhoneNumber,String genderType,String userProfilePhotoDownloadUrl,boolean isUserBlocked,boolean isUserProfilePhotoIncluded);
        void onFailed(String errorMessage);
    }
}