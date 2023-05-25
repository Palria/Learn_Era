package com.palria.learnera;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.adapters.HomeBooksRecyclerListViewAdapter;
import com.palria.learnera.adapters.PopularTutorialsListViewAdapter;
import com.palria.learnera.models.LibraryDataModel;
import com.palria.learnera.models.TutorialDataModel;
import com.palria.learnera.widgets.LEBottomSheetDialog;
import com.palria.learnera.widgets.RatingBottomSheetWidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class UserProfileFragment extends Fragment {

    AlertDialog alertDialog;
    AlertDialog createLibraryDialog;
    LinearLayout containerLinearLayout;

    //views
    ImageView editProfileButton;
    RoundedImageView profileImageView;
    TextView currentDisplayNameView;
    TextView currentEmailView;
    TextView currentCountryOfResidence;
    TextView joined_dateTextView;

    TextView numOfLibraryTextView;
    TextView numOfTutorialsTextView;
    TextView numOfRatingsTextView;

    LinearLayout numOfLibraryTutorialRatingsLinearLayout;

    RatingBottomSheetWidget ratingBottomSheetWidget;
    //parent swiper layout
    SwipeRefreshLayout swipeRefreshLayout;
    NestedScrollView parentScrollView;
    BottomAppBar bottomAppBar;

    TextView   failureIndicatorTextView;
    ImageButton profileMoreIconButton;
    Button statsButton;
    String authorId = "";
    TextView logButton;
    ImageView bookmarkedIcon, ratedIcon;
    //learn era bottom sheet dialog
    LEBottomSheetDialog leBottomSheetDialog;

    RecyclerView recentLibraryRcv;
    ArrayList<LibraryDataModel> libraryArrayList = new ArrayList<>();
    HomeBooksRecyclerListViewAdapter libraryRcvAdapter;

    RecyclerView tutorialsRcv;
    ArrayList<TutorialDataModel> tutorialsArrayList=new ArrayList<>();
    PopularTutorialsListViewAdapter tutorialsRcvAdapter;

    boolean isUserAuthor = false;
    boolean isFirstLoad = true;

    //shimmer layout loading preloading effect container.
    ShimmerFrameLayout shimmerLayout;

    //replace all categories with categories from database
//    String[] categories = {"Software Development", "Ui Design", "Web Development", "Machine Learning",
//    "Database Design", "Furniture", "Internet", "Communication", "Story", "Drama", "Podcasts","Java","Android Dev","Python","Data Learning","OOPs Concept","Artificial Intelligence"};
    String[] categories = new String[1];

    boolean[] checkedCategories;
    ArrayList<Integer> catsList = new ArrayList<>();
    ArrayList<String> selectedCategories = new ArrayList<>();

    public UserProfileFragment() {
        // Required empty public constructor
    }
    public UserProfileFragment(BottomAppBar b) {
        bottomAppBar = b;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            authorId = getArguments().getString(GlobalConfig.USER_ID_KEY);
        }
        if((GlobalConfig.getBlockedItemsList().contains(authorId+"")) && !GlobalConfig.getCurrentUserId().equals(authorId+"")) {
        if(getActivity()!=null){
            getActivity().onBackPressed();
        }
        }

//        getProfile(new OnUserProfileFetchListener() {
//            @Override
//            public void onSuccess(String userDisplayName, String userCountryOfResidence, String contactEmail, String contactPhoneNumber, String genderType, String userProfilePhotoDownloadUrl, boolean isUserBlocked, boolean isUserProfilePhotoIncluded, boolean isUserAnAuthor) {
//
//            }
//
//            @Override
//            public void onFailed(String errorMessage) {
//
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View parentView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        if((GlobalConfig.getCurrentUserId().equals(authorId+"")) || !(GlobalConfig.getBlockedItemsList().contains(authorId+""))) {
            initUI(parentView);
            categories =  new String[GlobalConfig.getCategoryList(getContext()).size()];

                    for(int i=0; i<GlobalConfig.getCategoryList(getContext()).size();i++){
                        categories[i] = GlobalConfig.getCategoryList(getContext()).get(i);
                    }
            loadCurrentUserProfile();

            fetchAllLibrary(new LibraryFetchListener() {
                @Override
                public void onFailed(String errorMessage) {
//               toggleProgress(false);
//                    swipeRefreshLayout.setRefreshing(false);
//                    Toast.makeText(getContext(), "failed to fetch library.", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onSuccess(LibraryDataModel libraryDataModel) {
//               toggleProgress(false);
                    swipeRefreshLayout.setRefreshing(false);
                    // displayLibrary(libraryDataModel);
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);
                    parentScrollView.setVisibility(View.VISIBLE);
//               libraryArrayList.add(new LibraryDataModel(libraryDataModel.getLibraryName(),libraryDataModel.getLibraryId(),libraryDataModel.getLibraryCategoryArrayList(),libraryDataModel.getLibraryCoverPhotoDownloadUrl(),libraryDataModel.getLibraryDescription(),libraryDataModel.getDateCreated(),libraryDataModel.getTotalNumberOfTutorials(),libraryDataModel.getTotalNumberOfLibraryViews(),libraryDataModel.getTotalNumberOfLibraryReach(),libraryDataModel.getAuthorUserId(),libraryDataModel.getTotalNumberOfOneStarRate(),libraryDataModel.getTotalNumberOfTwoStarRate(),libraryDataModel.getTotalNumberOfThreeStarRate(),libraryDataModel.getTotalNumberOfFourStarRate(),libraryDataModel.getTotalNumberOfFiveStarRate()));
                    if(libraryDataModel.isPublic() || (GlobalConfig.getCurrentUserId().equals(libraryDataModel.getAuthorUserId()+""))) {
                        libraryArrayList.add(libraryDataModel);
                        libraryRcvAdapter.notifyItemChanged(libraryArrayList.size());
                    }
//

                }
            });

            fetchTutorial(new TutorialFetchListener() {
                @Override
                public void onSuccess(TutorialDataModel tutorialDataModel) {

                    swipeRefreshLayout.setRefreshing(false);
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);

                    parentScrollView.setVisibility(View.VISIBLE);
                    if(tutorialDataModel.isPublic() || (GlobalConfig.getCurrentUserId().equals(tutorialDataModel.getAuthorId()+""))) {

                        tutorialsArrayList.add(tutorialDataModel);
                        tutorialsRcvAdapter.notifyItemChanged(tutorialsArrayList.size());
                    }
                }

                @Override
                public void onFailed(String errorMessage) {
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

                    if (bottomAppBar != null) {
                        if (oldScrollY > scrollY) {
                            bottomAppBar.performShow();

                        } else {
                            bottomAppBar.performHide();

                        }
                    }

                    //
//             if(scrollY > 30){
//                 if(bottomAppBar!=null) {
//                     bottomAppBar.performHide();
//                 }
//             }else{
//                 if(bottomAppBar!=null) {
//                     bottomAppBar.performShow();
//                 }
//             }

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
                    intent.putExtra(GlobalConfig.USER_ID_KEY, authorId);
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


            DocumentReference bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(GlobalConfig.getCurrentUserId())
                    .collection(GlobalConfig.BOOK_MARKS_KEY).document(authorId);
            GlobalConfig.checkIfDocumentExists(bookMarkOwnerReference, new GlobalConfig.OnDocumentExistStatusCallback() {
                @Override
                public void onExist(DocumentSnapshot documentSnapshot) {
                    bookmarkedIcon.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNotExist() {

                }

                @Override
                public void onFailed(@NonNull String errorMessage) {

                }
            });

            DocumentReference authorReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(authorId).collection(GlobalConfig.REVIEWS_KEY)
                    .document(GlobalConfig.getCurrentUserId());
            GlobalConfig.checkIfDocumentExists(authorReviewDocumentReference, new GlobalConfig.OnDocumentExistStatusCallback() {
                @Override
                public void onExist(DocumentSnapshot documentSnapshot) {
                    ratedIcon.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNotExist() {

                }

                @Override
                public void onFailed(@NonNull String errorMessage) {

                }
            });


            ratingBottomSheetWidget = new RatingBottomSheetWidget(getContext(), authorId, null, null, true, false, false) {

            };
            ratingBottomSheetWidget.setRatingPostListener(new RatingBottomSheetWidget.OnRatingPosted() {

                @Override
                public void onPost(int star, String message) {
                    super.onPost(star, message);
//                Toast.makeText(LibraryActivity.this,star + "-"+ message, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailed(String errorMessage) {
                    super.onFailed(errorMessage);

//                Toast.makeText(LibraryActivity.this,"failed", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onSuccess(boolean isReviewAuthor, boolean isReviewLibrary, boolean isReviewTutorial) {
                    super.onSuccess(isReviewAuthor, isReviewLibrary, isReviewTutorial);
//                Toast.makeText(LibraryActivity.this,"You rated this library", Toast.LENGTH_SHORT).show();
                    ratedIcon.setVisibility(View.VISIBLE);

                }
            });
        } else{

            Toast.makeText(getContext(), "Author Blocked! Unblock to explore the Author", Toast.LENGTH_SHORT).show();

            if(getActivity()!=null){
                getActivity().onBackPressed();
            }
            }
       return parentView;
    }


    private void loadCurrentUserProfile(){
        getProfile(new OnUserProfileFetchListener() {
            @Override
            public void onSuccess(String userDisplayName, String userCountryOfResidence, String contactEmail, String contactPhoneNumber, String genderType, String userProfilePhotoDownloadUrl, String joined_date,String numOfLibraryCreated,String numOfTutorialCreated,String numOfRatings, boolean isUserBlocked, boolean isUserProfilePhotoIncluded, boolean isUserAnAuthor) {
                swipeRefreshLayout.setRefreshing(false);
                isUserAuthor = isUserAnAuthor;
                try {
                    Glide.with(getContext())
                            .load(userProfilePhotoDownloadUrl)
                            .centerCrop()
                            .into(profileImageView);
                }catch(Exception e){}

                currentEmailView.setText(Html.fromHtml("Contact Email <b>"+contactEmail+"</b> "));
                currentDisplayNameView.setText(userDisplayName);
                currentCountryOfResidence.setText(Html.fromHtml("From <b>"+userCountryOfResidence+"</b> "));
                joined_dateTextView.setText(Html.fromHtml("Joined <b>"+joined_date+"</b> "));

                numOfLibraryTextView.setText(numOfLibraryCreated);
                numOfTutorialsTextView.setText(numOfTutorialCreated);
                numOfRatingsTextView.setText(numOfRatings);

                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);

                parentScrollView.setVisibility(View.VISIBLE);
//                Toast.makeText(getContext(), "Libraries loaded.", Toast.LENGTH_SHORT).show();

                if(!isUserAuthor && authorId.equals(GlobalConfig.getCurrentUserId())){
//                if(authorId.equals(GlobalConfig.getCurrentUserId())) {
                 if(isFirstLoad) {
                     leBottomSheetDialog.addOptionItem("Become An Author", R.drawable.ic_baseline_edit_24, new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             //show they are going to become an author
                             leBottomSheetDialog.hide();

                             showPromptToBeAnAuthor();

                         }
                     }, 0);
                     leBottomSheetDialog.render();
                     isFirstLoad = false;
                 }
                }else{
                    leBottomSheetDialog.render();
                    numOfLibraryTutorialRatingsLinearLayout.setVisibility(View.GONE);

                }

                if(authorId.equals(GlobalConfig.getCurrentUserId())){
                    //he is the owner of this profile

                }else{
                    //he is not the owner of this profile
                    editProfileButton.setVisibility(View.GONE);
                    logButton.setVisibility(View.INVISIBLE);
                    GlobalConfig.incrementNumberOfVisitors(authorId,null,null,null,null,true,false,false,false,false,false);
                }
            }

            @Override
            public void onFailed(String errorMessage) {
//                toggleProgress(false);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Failed to retrieve profile data.", Toast.LENGTH_SHORT).show();
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
            joined_dateTextView = parentView.findViewById(R.id.joined_date);
            logButton = parentView.findViewById(R.id.logButton);
            statsButton = parentView.findViewById(R.id.statsButton);
            ratedIcon = parentView.findViewById(R.id.ratedIcon);
            bookmarkedIcon = parentView.findViewById(R.id.bookmarkedIcon);


            numOfLibraryTutorialRatingsLinearLayout = parentView.findViewById(R.id.numOfLibraryTutorialRatingsLinearLayoutId);


            numOfLibraryTextView = parentView.findViewById(R.id.numOfLibraryCreatedTextView);
            numOfTutorialsTextView = parentView.findViewById(R.id.numOfTutorialCreatedTextView);
            numOfRatingsTextView = parentView.findViewById(R.id.numOfRatingsCreatedTextView);

            swipeRefreshLayout = parentView.findViewById(R.id.swiperRefreshLayout);
            profileMoreIconButton = parentView.findViewById(R.id.profileMoreIcon);
            shimmerLayout = parentView.findViewById(R.id.shimmerLayout);

            recentLibraryRcv = parentView.findViewById(R.id.recentLibraryRcv);
            tutorialsRcv = parentView.findViewById(R.id.tutorialsRcv);

            categories = getResources().getStringArray(R.array.category);
            checkedCategories = new boolean[categories.length];

            alertDialog = new AlertDialog.Builder(getContext())
                    .setCancelable(false)
                    .setView(getLayoutInflater().inflate(R.layout.default_loading_layout, null))
                    .create();

            createLibraryDialog = new AlertDialog.Builder(getContext())
                    .setCancelable(false)
                    .setTitle("Congrats! You are now an author, You are  privileged to create and own a library")
                    .setMessage("Create your first library")
                    .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getContext(), CreateNewLibraryActivity.class);
                            //creating new

                            intent.putExtra(GlobalConfig.IS_CREATE_NEW_LIBRARY_KEY, true);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton("Not yet", null)
                    .create();

            leBottomSheetDialog = new LEBottomSheetDialog(getContext());

            String libraryLeOptionName = "";
            String tutorialLeOptionName = "";

            if (authorId.equals(GlobalConfig.getCurrentUserId() + "")) {
                libraryLeOptionName = "My Libraries";
                tutorialLeOptionName = "My Tutorials";
            } else {
                libraryLeOptionName = "Libraries";
                tutorialLeOptionName = "Tutorials";

            }

            leBottomSheetDialog
                    .addOptionItem(tutorialLeOptionName, R.drawable.ic_baseline_dynamic_feed_24, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            leBottomSheetDialog.hide();
                            startActivity(GlobalConfig.getHostActivityIntent(getContext(), null, GlobalConfig.TUTORIAL_FRAGMENT_TYPE_KEY, authorId));

                        }
                    }, 0)
                    .addOptionItem(libraryLeOptionName, R.drawable.ic_baseline_library_books_24, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            leBottomSheetDialog.hide();
                            startActivity(GlobalConfig.getHostActivityIntent(getContext(), null, GlobalConfig.LIBRARY_FRAGMENT_TYPE_KEY, authorId));

                        }
                    }, 0)
                    .addOptionItem("Bookmark", R.drawable.ic_baseline_bookmarks_24, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            leBottomSheetDialog.hide();
                            Snackbar saveSnackBar = GlobalConfig.createSnackBar(getContext(), editProfileButton, "Initializing bookmark please wait...", Snackbar.LENGTH_INDEFINITE);
                            //CHECK IF THE CURRENT USER HAS ALREADY SAVED THIS USER, IF SO DO STH
                            DocumentReference bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                                    .collection(GlobalConfig.ALL_USERS_KEY)
                                    .document(GlobalConfig.getCurrentUserId())
                                    .collection(GlobalConfig.BOOK_MARKS_KEY).document(authorId);
                            GlobalConfig.checkIfDocumentExists(bookMarkOwnerReference, new GlobalConfig.OnDocumentExistStatusCallback() {
                                @Override
                                public void onExist(DocumentSnapshot documentSnapshot) {
                                    saveSnackBar.dismiss();

                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Remove this user from bookmark?")
                                            .setMessage("You have already added this user to your bookmarks")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Snackbar snackBar = GlobalConfig.createSnackBar(getContext(), editProfileButton, "Removing from bookmark...", Snackbar.LENGTH_INDEFINITE);

                                                    GlobalConfig.removeBookmark(authorId, null, null, null, null, GlobalConfig.AUTHOR_TYPE_KEY, new GlobalConfig.ActionCallback() {
                                                        @Override
                                                        public void onSuccess() {
//                                                        Toast.makeText(getContext(), "bookmark removed", Toast.LENGTH_SHORT).show();
                                                            snackBar.dismiss();
                                                            GlobalConfig.createSnackBar(getContext(), editProfileButton, "Bookmark removed!", Snackbar.LENGTH_SHORT);
                                                            bookmarkedIcon.setVisibility(View.GONE);

                                                        }

                                                        @Override
                                                        public void onFailed(String errorMessage) {
//                                                        Toast.makeText(LibraryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                                            snackBar.dismiss();
                                                            GlobalConfig.createSnackBar(getContext(), editProfileButton, "Failed to remove from bookmark please try again!", Snackbar.LENGTH_SHORT);
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(getContext(), "undo remove bookmark.", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .show();
                                }

                                @Override
                                public void onNotExist() {
                                    saveSnackBar.dismiss();

                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Add this to bookmark?")
                                            .setMessage("when you save to bookmark you are able to view it in your bookmarked")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Snackbar snackBar = GlobalConfig.createSnackBar(getContext(), editProfileButton, "Adding to bookmark...", Snackbar.LENGTH_INDEFINITE);

                                                    GlobalConfig.addToBookmark(authorId, null, null, null, null, GlobalConfig.AUTHOR_TYPE_KEY, new GlobalConfig.ActionCallback() {
                                                        @Override
                                                        public void onSuccess() {
//                                                Toast.makeText(LibraryActivity.this, "bookmark added", Toast.LENGTH_SHORT).show();
                                                            snackBar.dismiss();
                                                            GlobalConfig.createSnackBar(getContext(), editProfileButton, "Bookmark added!", Snackbar.LENGTH_SHORT);
                                                            bookmarkedIcon.setVisibility(View.VISIBLE);
                                                        }

                                                        @Override
                                                        public void onFailed(String errorMessage) {
                                                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                                            snackBar.dismiss();
                                                            GlobalConfig.createSnackBar(getContext(), editProfileButton, "Failed to add to bookmark please try again!", Snackbar.LENGTH_SHORT);
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(getContext(), "cancelled bookmark.", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .show();
                                }

                                @Override
                                public void onFailed(@NonNull String errorMessage) {
                                    saveSnackBar.dismiss();
                                    GlobalConfig.createSnackBar(getContext(), editProfileButton, "Failed to initialize bookmark please try again", Snackbar.LENGTH_SHORT);

                                }
                            });


                        }
                    }, 0)
                    .addOptionItem("Rate", R.drawable.ic_baseline_stars_24, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            leBottomSheetDialog.hide();

                            DocumentReference authorReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                                    .collection(GlobalConfig.ALL_USERS_KEY)
                                    .document(authorId).collection(GlobalConfig.REVIEWS_KEY)
                                    .document(GlobalConfig.getCurrentUserId());
                            Snackbar snackbar = GlobalConfig.createSnackBar(getContext(), editProfileButton, "Initializing rating details...", Snackbar.LENGTH_INDEFINITE);
                            //Check if he has already rated this user, else if not rated then rate but if rated edit the rating
                            GlobalConfig.checkIfDocumentExists(authorReviewDocumentReference, new GlobalConfig.OnDocumentExistStatusCallback() {
                                @Override
                                public void onExist(DocumentSnapshot snapshot) {

                                    snackbar.dismiss();
                                    Log.e("tag", snapshot.toString());
                                    String message = snapshot.getString(GlobalConfig.REVIEW_COMMENT_KEY);
                                    Double starLevel = snapshot.getDouble(GlobalConfig.STAR_LEVEL_KEY);
                                    Integer star = Integer.parseInt(String.valueOf(starLevel).substring(0,1));

                                    new AlertDialog.Builder(getContext())
                                            .setTitle("User already reviewed!")
                                            .setMessage("Chose what to do with the already reviewed user:")
                                            .setCancelable(true)
                                            .setPositiveButton("Edit review", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    ratingBottomSheetWidget
                                                            .setMessage(message)
                                                            .setRating(star)
                                                            .render(editProfileButton, true).show();

                                                }
                                            })
                                            .setNegativeButton("Delete review", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Snackbar deleteReviewSnackBar = GlobalConfig.createSnackBar(getContext(), editProfileButton, "Deleting review...", Snackbar.LENGTH_INDEFINITE);

                                                    GlobalConfig.removeAuthorReview(authorId, new GlobalConfig.ActionCallback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_DELETE_AUTHOR_REVIEW_TYPE_KEY, authorId, null, null, null, null, GlobalConfig.getCurrentUserId(), new GlobalConfig.ActionCallback() {
                                                                @Override
                                                                public void onSuccess() {
                                                                    deleteReviewSnackBar.dismiss();
                                                                    GlobalConfig.createSnackBar(getContext(), editProfileButton, "Review Deleted!", Snackbar.LENGTH_SHORT);
                                                                    ratedIcon.setVisibility(View.GONE);

                                                                }

                                                                @Override
                                                                public void onFailed(String errorMessage) {
                                                                    deleteReviewSnackBar.dismiss();
                                                                    GlobalConfig.createSnackBar(getContext(), editProfileButton, "Review Deleted!", Snackbar.LENGTH_SHORT);
                                                                    ratedIcon.setVisibility(View.GONE);


                                                                }
                                                            });

                                                        }

                                                        @Override
                                                        public void onFailed(String errorMessage) {
                                                            deleteReviewSnackBar.dismiss();
                                                            GlobalConfig.createSnackBar(getContext(), editProfileButton, "Review failed to delete!", Snackbar.LENGTH_SHORT);

                                                        }
                                                    });
                                                }
                                            })
                                            .show();
                                }

                                @Override
                                public void onNotExist() {

                                    snackbar.dismiss();

                                    ratingBottomSheetWidget.render(editProfileButton, false).show();

                                }

                                @Override
                                public void onFailed(@NonNull String errorMessage) {

                                    snackbar.dismiss();
                                    GlobalConfig.createSnackBar(getContext(), editProfileButton, "Failed to initialize rating", Snackbar.LENGTH_SHORT);


                                }
                            });


                        }
                    }, 0);

            if (!authorId.equals(GlobalConfig.getCurrentUserId() + "")) {

                leBottomSheetDialog.addOptionItem("Block User", R.drawable.ic_baseline_error_outline_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        leBottomSheetDialog.hide();

                        Toast.makeText(getContext(), "Blocking", Toast.LENGTH_SHORT).show();
                        GlobalConfig.block(GlobalConfig.ACTIVITY_LOG_USER_BLOCK_USER_TYPE_KEY, authorId, null, null, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });
                        UserProfileFragment.this.getActivity().onBackPressed();

                    }
                }, 0);
                leBottomSheetDialog.addOptionItem("Report User", R.drawable.ic_baseline_error_outline_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        leBottomSheetDialog.hide();
                        Toast.makeText(getContext(), "reporting", Toast.LENGTH_SHORT).show();
                        GlobalConfig.report(GlobalConfig.ACTIVITY_LOG_USER_REPORT_USER_TYPE_KEY, authorId, null, null, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });
                        UserProfileFragment.this.getActivity().onBackPressed();

                    }

                }, 0);

            }


            //init recycler list view here
        /*
        libraryArrayList.add(new LibraryDataModel(
                "Deploying the constructor for free.",
                "lasdjf",
                null,
                "https://api.lorem.space/image/album?w=150&h=150&hash=5115",
                "",
                "",
                0l,
                0l,
                1,
                "jvjhgjyuikjkj",
                0l,
                0l,
                0l,
                0l,
                0));

        libraryArrayList.add(new LibraryDataModel(
                "Cracking the Hash with hashcat",
                "lasdjf",
                null,
                "https://api.lorem.space/image/album?w=150&h=150&hash=410115",
                "",
                "",
                0l,
                0l,
                1,
                "jvjhgjyuikjkj",
                0l,
                0l,
                0l,
                0l,
                0));

        libraryArrayList.add(new LibraryDataModel(
                "Design Principle in Short ",
                "lasdjf",
                null,
                "https://api.lorem.space/image/album?w=150&h=150&hash=12500",
                "",
                "",
                0l,
                0l,
                1,
                "jvjhgjyuikjkj",
                0l,
                0l,
                0l,
                0l,
                0));

*/

            libraryRcvAdapter = new HomeBooksRecyclerListViewAdapter(libraryArrayList, getContext());
            recentLibraryRcv.setHasFixedSize(false);
            recentLibraryRcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recentLibraryRcv.setAdapter(libraryRcvAdapter);
/*
              tutorialsArrayList.add(
                new TutorialDataModel("How to connect to mysql database for free. in 2012 for Users to get it.",
                        "category",
                        "description",
                        "__id__02151",
                        "1 days ago",
                        2l,
                        1l,
                        0l,
                        0l,
                        "Kamaensi",
                        "",
                        "https://api.lorem.space/image/movie?w=400&h=220&hash=45110",
                        0l,
                        0l,
                        0l,
                        0l,
                        0l));

        tutorialsArrayList.add(
                new TutorialDataModel("The protest was organised against the Kathmandu Metropolitan City mayor’s recent move to demolish a part of private property in Sankhamul",
                        "Category",
                        "description",
                        "Jeevan",
                        "32 mins ago",
                        2l,
                        1l,
                        0l,
                        0l,
                        "Jeevan",
                        "",
                        "https://api.lorem.space/image/movie?w=400&h=220&hash=123",
                        0l,
                        0l,
                        0l,
                        0l,
                        0l));

        tutorialsArrayList.add(
                new TutorialDataModel("According to him, the metropolis demolished the house compound without any letter or notice.",
                        "Category",
                        "description",
                        "Palria",
                        "32 mins ago",
                        2l,
                        1l,
                        0l,
                        0l,
                        "Palria",
                        "",
                        "https://api.lorem.space/image/movie?w=450&h=200&hash=334",
                        0l,
                        0l,
                        0l,
                        0l,
                        0l));

        tutorialsArrayList.add(
                new TutorialDataModel("According to him, the metropolis demolished the house compound without any letter or notice.",
                        "Category",
                        "description",
                        "Palria",
                        "32 mins ago",
                        2l,
                        1l,
                        0l,
                        0l,
                        "Palria",
                        "",
                        "https://api.lorem.space/image/movie?w=450&h=200&hash=asdfadsew",
                        0l,
                        0l,
                        0l,
                        0l,
                        0l));

        tutorialsArrayList.add(
                new TutorialDataModel("According to him, the metropolis demolished the house compound without any letter or notice.",
                        "Category",
                        "description",
                        "Palria",
                        "32 mins ago",
                        2l,
                        1l,
                        0l,
                        0l,
                        "Palria",
                        "",
                        "https://api.lorem.space/image/movie?w=450&h=200&hash=sdfsad",
                        0l,
                        0l,
                        0l,
                        0l,
                        0l));

        tutorialsArrayList.add(
                new TutorialDataModel("According to him, the metropolis demolished the house compound without any letter or notice.",
                        "Category",
                        "description",
                        "Palria",
                        "32 mins ago",
                        2l,
                        1l,
                        0l,
                        0l,
                        "Palria",
                        "",
                        "https://api.lorem.space/image/movie?w=450&h=200&hash=sdfs33423",
                        0l,
                        0l,
                        0l,
                        0l,
                        0l));
*/

            tutorialsRcvAdapter = new PopularTutorialsListViewAdapter(tutorialsArrayList, getContext());
            tutorialsRcv.setHasFixedSize(false);
            tutorialsRcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            tutorialsRcv.setAdapter(tutorialsRcvAdapter);


    }

    private void showPromptToBeAnAuthor() {

        // Initialize alert dialog
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getContext());

        // set title
        builder.setTitle("Choose Preferred Categories for Your Library.");

        // set dialog non cancelable
        builder.setCancelable(false);
        builder.setTitle("Become an author in "+getString(R.string.app_name)+" ?")
                .setMessage("When you become an author you are able to create your own libraries and tutorials." +
                        "Click Yes to be an Author.");



        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //make a user as an author here
                // Initialize alert dialog
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

                // set title
                builder.setTitle("Select Categories where you will add Libraries.");

                // set dialog non cancelable
                builder.setCancelable(false);
//
//                String[] arr = new String[libraryCategoryArrayList.size()];
//                for(int i=0; i<arr.length;i++){
//                    arr[i]=libraryCategoryArrayList.get(i);
//                }

                builder.setMultiChoiceItems(categories,checkedCategories , new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                        if (isChecked) {
                            catsList.add(i);
                            Collections.sort(catsList);
                        } else {
                            catsList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("Become author", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < catsList.size(); j++) {
                            // concat array value
                            selectedCategories.add(categories[catsList.get(j)]);
                        }
                        dialogInterface.cancel();
                        toggleProgress(true);
                        becomeAnAuthor(selectedCategories, new BecomeAuthorListener() {
                            @Override
                            public void onFailed(String errorMessage) {
                                toggleProgress(false);
                                GlobalHelpers.showAlertMessage("error",getContext(), "Oops! Failed to make you author", errorMessage +", Please try again! ");

                            }

                            @Override
                            public void onSuccess() {
                                toggleProgress(false);
                                GlobalHelpers.showAlertMessage("success",getContext(), "Congrats! You are now an author", "You are now privileged to create and own a library");

                            }
                        });
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                // show dialog
                builder.show();

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
        GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(authorId)
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
                        String joined_date = documentSnapshot.get(GlobalConfig.USER_PROFILE_DATE_CREATED_TIME_STAMP_KEY)!=null ? documentSnapshot.getTimestamp(GlobalConfig.USER_PROFILE_DATE_CREATED_TIME_STAMP_KEY).toDate()+"" :"Undefined";
                        if(joined_date.length()>10){
                            joined_date = joined_date.substring(0,10);
                        }
                        long numOfLibraryCreated = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY):0L;
                        long numOfTutorialCreated = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY):0L;
                        long numOfRatings = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_AUTHOR_REVIEWS_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_AUTHOR_REVIEWS_KEY):0L;


                        boolean isUserAnAuthor = false;
                        boolean isUserBlocked = false;
                        boolean isUserProfilePhotoIncluded = false;
                        if(documentSnapshot.get(GlobalConfig.IS_USER_AUTHOR_KEY) != null){
                            isUserAnAuthor =documentSnapshot.getBoolean(GlobalConfig.IS_USER_AUTHOR_KEY);

                        }
                        if(documentSnapshot.get(GlobalConfig.IS_USER_BLOCKED_KEY) != null){
                            isUserBlocked =documentSnapshot.getBoolean(GlobalConfig.IS_USER_BLOCKED_KEY);

                        }
                        if(documentSnapshot.get(GlobalConfig.IS_USER_PROFILE_PHOTO_INCLUDED_KEY) != null){
                            isUserProfilePhotoIncluded = documentSnapshot.getBoolean(GlobalConfig.IS_USER_PROFILE_PHOTO_INCLUDED_KEY);

                        }

                        onUserProfileFetchListener.onSuccess( userDisplayName, userCountryOfResidence, contactEmail, contactPhoneNumber, genderType, userProfilePhotoDownloadUrl,joined_date,""+ numOfLibraryCreated,""+ numOfTutorialCreated,""+ numOfRatings, isUserBlocked, isUserProfilePhotoIncluded,isUserAnAuthor);
                        }
                });
    }


    private void fetchAllLibrary(LibraryFetchListener libraryFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_LIBRARY_KEY)
                .whereEqualTo(GlobalConfig.LIBRARY_AUTHOR_ID_KEY,authorId)
                .limit(10L)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        libraryFetchListener.onFailed(e.getMessage());
                        Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                            String libraryId = documentSnapshot.getId();
                            boolean isPublic =  documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY)!=null?  documentSnapshot.getBoolean(GlobalConfig.IS_PUBLIC_KEY)  :true;

//                            Toast.makeText(getContext(), libraryId,Toast.LENGTH_SHORT).show();
                            long totalNumberOfLibraryView = 0L;
                            if(documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VIEWS_KEY) != null){
                                totalNumberOfLibraryView =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VIEWS_KEY);
                            }
                            long totalNumberOfLibraryReach = 0L;
                            if(documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) != null){
                                totalNumberOfLibraryReach =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY);
                            }
                            long totalNumberOfOneStarRate = 0L;
                            if(documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null){
                                totalNumberOfOneStarRate =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY);
                            }
                            long totalNumberOfTwoStarRate = 0L;
                            if(documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null){
                                totalNumberOfTwoStarRate =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY);
                            }
                            long totalNumberOfThreeStarRate = 0L;
                            if(documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null){
                                totalNumberOfThreeStarRate =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY);
                            }
                            long totalNumberOfFourStarRate = 0L;
                            if(documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null){
                                totalNumberOfFourStarRate =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY);
                            }
                            long totalNumberOfFiveStarRate = 0L;
                            if(documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null){
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
                            String libraryDescription = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DESCRIPTION_KEY);
                            String dateCreated =documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY)!=null && documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY) instanceof Timestamp ? ""+ documentSnapshot.getTimestamp(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY).toDate() :"Undefined";
                            if(dateCreated.length()>10){
                                dateCreated = dateCreated.substring(0,10);
                            }
                            String authorUserId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);
                            String libraryCoverPhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);
                            long totalNumberOfTutorials = 0L;
                            if(documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) != null){
                                totalNumberOfTutorials =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY);
                            }


                            libraryFetchListener.onSuccess(new LibraryDataModel(
                                    isPublic,
                                    libraryName,
                                    libraryId,
                                    libraryCategoryArray,
                                    libraryCoverPhotoDownloadUrl,
                                    libraryDescription,
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
//                                    documentSnapshot
                            ));
                        }
                    }
                });

    }

    private void fetchTutorial(TutorialFetchListener tutorialFetchListener){
        Query tutorialQuery = null;

            tutorialQuery =   GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).whereEqualTo(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY, authorId).limit(10L);
        tutorialQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tutorialFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String authorId =""+ documentSnapshot.get(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY);
                            boolean isPublic =  documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY)!=null?  documentSnapshot.getBoolean(GlobalConfig.IS_PUBLIC_KEY)  :true;

                            String libraryId =""+ documentSnapshot.get(GlobalConfig.LIBRARY_CONTAINER_ID_KEY);
                            String tutorialId =""+ documentSnapshot.get(GlobalConfig.TUTORIAL_ID_KEY);
                            String tutorialName = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY);
                            String tutorialCategory = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_CATEGORY_KEY);
                            String tutorialDescription = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_DESCRIPTION_KEY);

                            String dateCreated =  documentSnapshot.get(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY)!=null &&  documentSnapshot.get(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY) instanceof Timestamp ?  documentSnapshot.getTimestamp(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY).toDate().toString() : "Undefined" ;
                            if(dateCreated.length()>10){
                                dateCreated = dateCreated.substring(0,10);
                            }
                            long  totalNumberOfFolders = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY)!=null ?documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY) :0L;
                            long totalNumberOfPages =  documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_PAGES_CREATED_KEY)!=null ?documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_PAGES_CREATED_KEY) :0L;
                            String tutorialCoverPhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY);


                            long totalNumberOfTutorialVisitor = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY) : 0L;
                            long totalNumberOfTutorialReach = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REACH_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REACH_KEY) : 0L;
                            long totalNumberOfOneStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) : 0L;
                            long totalNumberOfTwoStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) : 0L;
                            long totalNumberOfThreeStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) : 0L;
                            long totalNumberOfFourStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) : 0L;
                            long totalNumberOfFiveStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) : 0L;



                            tutorialFetchListener.onSuccess(new TutorialDataModel(
                                    isPublic,
                                    tutorialName,
                                    tutorialCategory,
                                    tutorialDescription,
                                    tutorialId,
                                    dateCreated,
                                    totalNumberOfPages,
                                    totalNumberOfFolders,
                                    totalNumberOfTutorialVisitor,
                                    totalNumberOfTutorialReach,
                                    authorId,
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
//        authorDetails.put(GlobalConfig.AUTHOR_DATE_KEY,GlobalConfig.getDate());
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
    interface TutorialFetchListener{
        void onFailed(String errorMessage);
        void onSuccess(TutorialDataModel tutorialDataModel);
    }
    interface BecomeAuthorListener{
        void onFailed(String errorMessage);
        void onSuccess();
    }

    interface OnUserProfileFetchListener{
        void onSuccess(String userDisplayName,String userCountryOfResidence,String contactEmail,String contactPhoneNumber,String genderType,String userProfilePhotoDownloadUrl,String joined_date,String numOfLibraryCreated,String numOfTutorialCreated,String numberOfRatings,boolean isUserBlocked,boolean isUserProfilePhotoIncluded, boolean isUserAnAuthor);
        void onFailed(String errorMessage);
    }
}

