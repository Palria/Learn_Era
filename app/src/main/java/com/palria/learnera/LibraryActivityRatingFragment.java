package com.palria.learnera;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.DocumentSet;
import com.palria.learnera.adapters.RatingItemRecyclerViewAdapter;
import com.palria.learnera.models.RatingDataModel;
import com.palria.learnera.widgets.RatingBarWidget;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibraryActivityRatingFragment#} factory method to
 * create an instance of this fragment.
 */

public class LibraryActivityRatingFragment extends Fragment {

    LinearLayout ratingBarContainer;
    RecyclerView ratingsRecyclerListView;
    String libraryId = "";
    String tutorialId = "";
    boolean isLibraryReview = true;
    ArrayList<RatingDataModel> ratingDataModels;
    RatingItemRecyclerViewAdapter ratingItemRecyclerViewAdapter;
    int[] ratings ={0,0,0,0,0};
    public LibraryActivityRatingFragment() {
        // Required empty public constructor
    }

OnReviewFetchListener onReviewFetchListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
if(getArguments() != null){
    libraryId = getArguments().getString(GlobalConfig.LIBRARY_ID_KEY);
    tutorialId = getArguments().getString(GlobalConfig.TUTORIAL_ID_KEY);
    isLibraryReview = getArguments().getBoolean(GlobalConfig.IS_LIBRARY_REVIEW_KEY);
    ratings = getArguments().getIntArray(GlobalConfig.STAR_RATING_ARRAY_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_library_activity_rating, container, false);
        initView(view);

onReviewFetchListener = new OnReviewFetchListener() {
    @Override
    public void onSuccess(RatingDataModel ratingDataModel) {
     ratingDataModels.add(ratingDataModel);
     ratingItemRecyclerViewAdapter.notifyItemChanged(ratingDataModels.size());

        //int[] ratings = {40,12,155,1,15};

        RatingBarWidget ratingBarWidget = new RatingBarWidget();
        if(getContext()!=null) {

            ratingBarWidget.setContainer(ratingBarContainer)
                    .setContext(getContext())
                    .setMax_value(5)
                    .setRatings(ratings)
                    .setText_color(R.color.teal_700)
                    .render();
        }
    }

    @Override
    public void onFailed(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
};

getReviews();

        return view;
    }

    private void initView(View parentView) {

        ratingBarContainer=parentView.findViewById(R.id.ratingBarContainer);
        ratingsRecyclerListView=parentView.findViewById(R.id.ratingsRecyclerListView);

         ratingDataModels = new ArrayList<>();

//        ratingDataModels.add(new RatingDataModel("lasdjf","Karmalu Sherpa",
//                "123",
//                "library",
//                "Sat 01",
//                "Hello i love this app so much dude.",
//                4,
//                "https://api.lorem.space/image/face?w=150&h=150"));
//
//        ratingDataModels.add(new RatingDataModel("lasdjf","Hira Kamu",
//                "123",
//                "library",
//                "June 01",
//                "Wow perficet.",
//                4,
//                "https://api.lorem.space/image/face?w=150&h=150"));
//
//        ratingDataModels.add(new RatingDataModel("lasdjf","Nima Shrestha",
//                "123",
//                "library",
//                "Feb 04",
//                "Everything is good but have some bugs in this tutorial. please fix it fast now .",
//                4,
//                "https://api.lorem.space/image/face?w=150&h=150"));


        ratingItemRecyclerViewAdapter = new RatingItemRecyclerViewAdapter(ratingDataModels,getContext());


        ratingsRecyclerListView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
//        ratingsRecyclerListView.setHasFixedSize(true);
        ratingsRecyclerListView.setAdapter(ratingItemRecyclerViewAdapter);

        //load rating bar

    }

    void getReviews(){
      Query reviewQuery = null;
      if(isLibraryReview) {
          reviewQuery = GlobalConfig.getFirebaseFirestoreInstance()
                  .collection(GlobalConfig.ALL_LIBRARY_KEY)
                  .document(libraryId)
                  .collection(GlobalConfig.REVIEWS_KEY);
      }else{
          reviewQuery = GlobalConfig.getFirebaseFirestoreInstance()
                  .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                  .document(tutorialId)
                  .collection(GlobalConfig.REVIEWS_KEY);
      }


                reviewQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
onReviewFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            String reviewerId = documentSnapshot.getId();
                            String dateReviewed = documentSnapshot.get(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY)!=null &&  documentSnapshot.get(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY) instanceof Timestamp ?  documentSnapshot.getTimestamp(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY).toDate().toString() : "Undefined";
                            if(dateReviewed.length()>10){
                                dateReviewed = dateReviewed.substring(0,10);
                            }
                            String finalDateReview = dateReviewed;

                            String reviewBody = "" + documentSnapshot.get(GlobalConfig.REVIEW_COMMENT_KEY);
                            long starLevel = documentSnapshot.get(GlobalConfig.STAR_LEVEL_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.STAR_LEVEL_KEY): 0L;

                            GlobalConfig.getFirebaseFirestoreInstance()
                                    .collection(GlobalConfig.ALL_USERS_KEY)
                                    .document(reviewerId)
                                    .get()
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    })
                                   .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                       @Override
                                       public void onSuccess(DocumentSnapshot documentSnapshot) {

                                           String reviewerName =""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                                           String userProfilePhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);

                                           onReviewFetchListener.onSuccess(new RatingDataModel(reviewerId,reviewerName,libraryId,"library",finalDateReview,reviewBody, (int) starLevel,userProfilePhotoDownloadUrl));

                                       }
                                   });
                        }
                    }
                });
    }

    interface  OnReviewFetchListener{
        void onSuccess(RatingDataModel  ratingDataModel);
        void onFailed(String errorMessage);
    }
}