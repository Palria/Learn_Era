package com.palria.learnera;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.RatingItemRecyclerViewAdapter;
import com.palria.learnera.models.RatingDataModel;

import java.util.ArrayList;

public class AllReviewsFragment extends Fragment {

    RecyclerView recyclerView;
    RatingItemRecyclerViewAdapter ratingItemRecyclerViewAdapter;
    ArrayList<RatingDataModel> ratingDataModels = new ArrayList<>();
    TabLayout tabLayout;
    String userId;
    FrameLayout libraryReviewFrameLayout;
    FrameLayout tutorialReviewFrameLayout;
    FrameLayout userReviewFrameLayout;

    boolean isLibraryReviewFrameLayoutOpened=false;
    boolean isTutorialReviewFrameLayoutOpened=false;
    boolean isUserReviewFrameLayoutOpened=false;


    public AllReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            userId = getArguments().getString(GlobalConfig.USER_ID_KEY,"0");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_all_reviews, container, false);
        initUI(parentView);
//        fetchReviews(new ReviewFetchListener() {
//            @Override
//            public void onSuccess(RatingDataModel ratingDataModel) {
////                displayReviews( authorId, libraryId, tutorialId, dateReviewed, reviewComment, starLevel, isAuthorReview,isLibraryReview, isTutorialReview);;
//
//                ratingDataModels.add(ratingDataModel);
//                ratingItemRecyclerViewAdapter.notifyItemChanged(ratingDataModels.size());
//            }
//
//            @Override
//            public void onFailed(String errorMessage) {
//
//            }
//        });
        createTabLayout();

        return parentView;
    }


    private void initUI(View parentView){
        //use the parentView to find the  Id as in : parentView.findViewById(...);
        tabLayout = parentView.findViewById(R.id.tabLayoutId);
        libraryReviewFrameLayout = parentView.findViewById(R.id.libraryReviewFrameLayoutId);
        tutorialReviewFrameLayout = parentView.findViewById(R.id.tutorialReviewFrameLayoutId);
        userReviewFrameLayout = parentView.findViewById(R.id.userReviewFrameLayoutId);


        ratingItemRecyclerViewAdapter = new RatingItemRecyclerViewAdapter(ratingDataModels,getContext());
        recyclerView = parentView.findViewById(R.id.ratingsRecyclerListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL ,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ratingItemRecyclerViewAdapter);
        Toast.makeText(getContext(), "all reviews init", Toast.LENGTH_SHORT).show();

    }

    private void createTabLayout(){


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition()==0){
                    if(isLibraryReviewFrameLayoutOpened){
                        //Just set the frame layout visibility
                        setFrameLayoutVisibility(libraryReviewFrameLayout);
                    }else {
                        isLibraryReviewFrameLayoutOpened =true;
                        setFrameLayoutVisibility(libraryReviewFrameLayout);

                        LibraryReviewsFragment libraryReviewsFragment = new LibraryReviewsFragment();

                        initFragment(libraryReviewsFragment, libraryReviewFrameLayout);
                    }


                }else if(tab.getPosition()==1)
                {
                    if(isTutorialReviewFrameLayoutOpened){
                        //Just set the frame layout visibility
                        setFrameLayoutVisibility(tutorialReviewFrameLayout);
                    }else {
                        isTutorialReviewFrameLayoutOpened =true;
                        setFrameLayoutVisibility(tutorialReviewFrameLayout);
                        TutorialReviewsFragment tutorialReviewsFragment = new TutorialReviewsFragment();

                        initFragment(tutorialReviewsFragment, tutorialReviewFrameLayout);
                    }
                }else if(tab.getPosition()==2){
                    if(isUserReviewFrameLayoutOpened){
                        //Just set the frame layout visibility
                        setFrameLayoutVisibility(userReviewFrameLayout);
                    }else {
                        isUserReviewFrameLayoutOpened =true;
                        setFrameLayoutVisibility(userReviewFrameLayout);
                        AllUserReviewsFragment allUserReviewsFragment = new AllUserReviewsFragment();

                        initFragment(allUserReviewsFragment, userReviewFrameLayout);
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


        TabLayout.Tab libraryReviewTabItem = tabLayout.newTab();
        libraryReviewTabItem.setText("Library");
        tabLayout.addTab(libraryReviewTabItem,0,true);

        TabLayout.Tab tutorialReviewTabItem = tabLayout.newTab();
        tutorialReviewTabItem.setText("Tutorial");
        tabLayout.addTab(tutorialReviewTabItem,1);

        TabLayout.Tab userReviewTabItem = tabLayout.newTab();
        userReviewTabItem.setText("User");
        tabLayout.addTab(userReviewTabItem,2);
    }


    private void initFragment(Fragment fragment, FrameLayout frameLayout){
        Bundle bundle = new Bundle();
        bundle.putString(GlobalConfig.USER_ID_KEY,userId);
        fragment.setArguments(bundle);
        getChildFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), fragment)
                .commit();


    }

    private void setFrameLayoutVisibility(FrameLayout frameLayoutToSetVisible){
        libraryReviewFrameLayout.setVisibility(View.GONE);
        tutorialReviewFrameLayout.setVisibility(View.GONE);
        userReviewFrameLayout.setVisibility(View.GONE);
        frameLayoutToSetVisible.setVisibility(View.VISIBLE);
    }



//Do not call anymore
    private void fetchReviews(ReviewFetchListener reviewFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId())
                .collection(GlobalConfig.OTHER_REVIEWS_KEY)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        reviewFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            String authorId = ""+ documentSnapshot.get(GlobalConfig.AUTHOR_ID_KEY);
                            String libraryId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_ID_KEY);
                            String tutorialId = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_ID_KEY);
                            String reviewComment = ""+ documentSnapshot.get(GlobalConfig.REVIEW_COMMENT_KEY);
                            long starLevel =  documentSnapshot.get(GlobalConfig.REVIEW_COMMENT_KEY)!=null && documentSnapshot.get(GlobalConfig.REVIEW_COMMENT_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.STAR_LEVEL_KEY) : 0L;

                                String dateReviewed = documentSnapshot.get(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY) != null && documentSnapshot.get(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY) instanceof Timestamp ? documentSnapshot.getTimestamp(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY).toDate().toString() : "Undefined";
                            if(dateReviewed.length()>10){
                                dateReviewed = dateReviewed.substring(0,10);
                            }
                            final String finalDateReviewed = dateReviewed;

                            final boolean isAuthorReview= (documentSnapshot.get(GlobalConfig.IS_AUTHOR_REVIEW_KEY))!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_AUTHOR_REVIEW_KEY)  :false;
                            final boolean isLibraryReview= (documentSnapshot.get(GlobalConfig.IS_LIBRARY_REVIEW_KEY))!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_LIBRARY_REVIEW_KEY)  :false;
                            final boolean isTutorialReview = (documentSnapshot.get(GlobalConfig.IS_TUTORIAL_REVIEW_KEY))!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_TUTORIAL_REVIEW_KEY)  :false;

                            if(isAuthorReview) {
                                GlobalConfig.getFirebaseFirestoreInstance()
                                        .collection(GlobalConfig.ALL_USERS_KEY)
                                        .document(authorId)
                                        .get()
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                reviewFetchListener.onSuccess(new RatingDataModel(GlobalConfig.getCurrentUserId(), "Error", authorId, "user", finalDateReviewed, reviewComment, (int) starLevel, "Error"));

                                            }
                                        })
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String userName = "" + documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                                                String userProfilePhotoDownloadUrl = "" + documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                                                reviewFetchListener.onSuccess(new RatingDataModel(GlobalConfig.getCurrentUserId(), userName, authorId, "user", finalDateReviewed, reviewComment, (int) starLevel, userProfilePhotoDownloadUrl));

                                            }
                                        });
                            }else if(isLibraryReview){
                                GlobalConfig.getFirebaseFirestoreInstance()
                                        .collection(GlobalConfig.ALL_LIBRARY_KEY)
                                        .document(libraryId)
                                        .get()
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                reviewFetchListener.onSuccess(new RatingDataModel(GlobalConfig.getCurrentUserId(), "Error", libraryId, "library", finalDateReviewed, reviewComment, (int) starLevel, "Error"));

                                            }
                                        })
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String libraryName = "" + documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                                                String libraryProfilePhotoDownloadUrl = "" + documentSnapshot.get(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);
                                                reviewFetchListener.onSuccess(new RatingDataModel(GlobalConfig.getCurrentUserId(), libraryName, libraryId, "library", finalDateReviewed, reviewComment, (int) starLevel, libraryProfilePhotoDownloadUrl));

                                            }
                                        });
                            }else if(isTutorialReview){
                                GlobalConfig.getFirebaseFirestoreInstance()
                                        .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                                        .document(tutorialId)
                                        .get()
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                reviewFetchListener.onSuccess(new RatingDataModel(GlobalConfig.getCurrentUserId(), "Error", tutorialId, "tutorial", finalDateReviewed, reviewComment, (int) starLevel, "Error"));

                                            }
                                        })
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String tutorialName = "" + documentSnapshot.get(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY);
                                                String tutorialProfilePhotoDownloadUrl = "" + documentSnapshot.get(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY);
                                                reviewFetchListener.onSuccess(new RatingDataModel(GlobalConfig.getCurrentUserId(), tutorialName, tutorialId, "tutorial", finalDateReviewed, reviewComment, (int) starLevel, tutorialProfilePhotoDownloadUrl));

                                            }
                                        });
                            }
//                            reviewFetchListener.onSuccess( authorId, libraryId, tutorialId, dateReviewed, reviewComment, starLevel, isAuthorReview,isLibraryReview, isTutorialReview);
                        }
                    }
                });
    }

    private void displayReviews(String authorId,String libraryId,String tutorialId,String dateReviewed,String reviewComment,long starLevel,final boolean isAuthorReview,final boolean isLibraryReview,final boolean isTutorialReview){
        //<Uncomment and implement>
        /*
        if(getContext() != null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View reviewView = layoutInflater.inflate(R.layout.all_review_custom_view, null);
            TextView viewTypeIndicatorTextView = reviewView.findViewById(R.id.viewTypeIndicatorTextViewId);
            TextView dateReviewedTextView = reviewView.findViewById(R.id.dateReviewedTextViewId);
            TextView titleTextView = reviewView.findViewById(R.id.titleTextViewId);
            TextView authorNameTextView = reviewView.findViewById(R.id.authorNameTextViewId);
            TextView reviewCommentTextView = reviewView.findViewById(R.id.reviewCommentTextViewId);
            ImageView coverPhotoImageView = reviewView.findViewById(R.id.coverPhotoImageViewId);
            reviewCommentTextView.setText(reviewComment);
            dateReviewedTextView.setText(dateReviewed);




             if(isLibraryReview){
                viewTypeIndicatorTextView.setText("Library");
                GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(authorId)
                        .collection(GlobalConfig.ALL_LIBRARY_KEY)
                        .document(libraryId)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String libraryName =""+ documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                                String libraryCoverPhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);
                                titleTextView.setText(libraryName);
                                Glide.with(getContext())
                                        .load(libraryCoverPhotoDownloadUrl)
                                        .centerCrop()
                                        .into(coverPhotoImageView);
                            }
                        });
            }
            else if(isTutorialReview){
                viewTypeIndicatorTextView.setText("Tutorial");
                GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(authorId)
                        .collection(GlobalConfig.ALL_LIBRARY_KEY)
                        .document(libraryId)
                        .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                        .document(tutorialId)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String tutorialName =""+ documentSnapshot.get(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY);
                                String tutorialCoverPhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY);
                                titleTextView.setText(tutorialName);
                                Glide.with(getContext())
                                        .load(tutorialCoverPhotoDownloadUrl)
                                        .centerCrop()
                                        .into(coverPhotoImageView);
                            }
                        });
            }
            GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(authorId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String authorName =""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                            String authorProfilePhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                            if(isAuthorReview){
                                viewTypeIndicatorTextView.setText("Author");
                                titleTextView.setText(authorName);
                                Glide.with(getContext())
                                        .load(authorProfilePhotoDownloadUrl)
                                        .centerCrop()
                                        .into(coverPhotoImageView);
                            }else{
                                authorNameTextView.setText(authorName);
                            }

                        }
                    });
            containerLinearLayout.addView(reviewView);

        }
        */
    }


    interface ReviewFetchListener{
//        void onSuccess(String authorId,String libraryId,String tutorialId,String dateReviewed,String reviewComment,long starLevel,final boolean isAuthorReview,final boolean isLibraryReview,final boolean isTutorialReview);
        void onSuccess(RatingDataModel ratingDataModel);
         void onFailed(String errorMessage);
    }

}