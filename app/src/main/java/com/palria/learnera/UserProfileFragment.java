package com.palria.learnera;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.models.LibraryDataModel;

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
    ScrollView parentScrollView;
    BottomAppBar bottomAppBar;



    public UserProfileFragment(BottomAppBar b) {
        // Required empty public constructor
        bottomAppBar = b;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getProfile(new ProfileValueLoadListener() {
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
       toggleProgress(true);
       loadCurrentUserProfile();
       fetchAllLibrary(new LibraryFetchListener() {
           @Override
           public void onFailed(String errorMessage) {
               toggleProgress(false);

           }

           @Override
           public void onSuccess(LibraryDataModel libraryDataModel) {
               toggleProgress(false);
               displayLibrary(libraryDataModel);
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
               swipeRefreshLayout.setRefreshing(false);
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

       return parentView;
    }


    private void loadCurrentUserProfile(){
        getProfile(new ProfileValueLoadListener() {
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



            }

            @Override
            public void onFailed(String errorMessage) {
                toggleProgress(false);

            }
        });
    }

    private void initUI(View parentView){
        //use the parentView to find the by Id as in : parentView.findViewById(...);

        parentScrollView = parentView.findViewById(R.id.scrollView);


        //init views
        editProfileButton = parentView.findViewById(R.id.editProfileIcon);
        profileImageView = parentView.findViewById(R.id.imageView1);
        currentDisplayNameView = parentView.findViewById(R.id.current_name);
        currentEmailView = parentView.findViewById(R.id.current_email);
        currentCountryOfResidence = parentView.findViewById(R.id.current_country);

        swipeRefreshLayout = parentView.findViewById(R.id.swiperRefreshLayout);

        alertDialog = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();
    }

    private  void getProfile(ProfileValueLoadListener profileValueLoadListener){
        GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.USER_PROFILE_KEY).document(GlobalConfig.getCurrentUserId())
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        profileValueLoadListener.onFailed(e.getMessage());
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

                        profileValueLoadListener.onSuccess( userDisplayName, userCountryOfResidence, contactEmail, contactPhoneNumber, genderType, userProfilePhotoDownloadUrl, isUserBlocked, isUserProfilePhotoIncluded);

                    }
                });
    }


    private void fetchAllLibrary(LibraryFetchListener libraryFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId())
                .collection(GlobalConfig.ALL_LIBRARY_KEY)
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
                            documentSnapshot.getReference()
                                    .collection(GlobalConfig.LIBRARY_PROFILE_KEY)
                                    .document(libraryId)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {


                                            String libraryName = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                                            String libraryCategory = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_CATEGORY_KEY);
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
                                                    libraryCategory,
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
                                    });
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
            alertDialog.hide();
        }
    }


    interface LibraryFetchListener{
        void onFailed(String errorMessage);
        void onSuccess(LibraryDataModel libraryDataModel);
    }

    interface ProfileValueLoadListener{
        void onSuccess(String userDisplayName,String userCountryOfResidence,String contactEmail,String contactPhoneNumber,String genderType,String userProfilePhotoDownloadUrl,boolean isUserBlocked,boolean isUserProfilePhotoIncluded);
        void onFailed(String errorMessage);
    }
}