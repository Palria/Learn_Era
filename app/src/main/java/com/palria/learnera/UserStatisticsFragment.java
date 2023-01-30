package com.palria.learnera;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.models.StatisticsDataModel;

public class UserStatisticsFragment extends Fragment {
    AlertDialog alertDialog;

    public UserStatisticsFragment() {
        // Required empty public constructor
    }

/*
 *This fragment will also contain child fragments
 * There should be three tabs which are: (Bookmarks tab, My reviews tab, and All reviews tab)
 *
 *  Bookmarks tab  :                  (This tab shows other libraries and tutorials bookmarked by this user  which will be contained in a fragment),
 * Total number of libraries :       (A view that reveals the number of libraries this author created),
 * is an author flag         :       (A view that reveals whether this user is an author or not),
 * Star rating chat layout   :       (This is a layout such like playstore review layout that shows the chat bars of star rating for this user.),
 * My reviews tab        :           (This tab shows a list of this user's reviews reviewed by other users which will be contained in a fragment),
 * All reviews  tab   :              (This tab shows other authors, libraries and tutorials this user reviewed which will be contained in a fragment),
 * Total number of profile visitors: (The number of times users visited this user's profile),
 * Total number of profile reach:    (The number of times  this user's profile reached users),
 * Last seen                     :   (The last day this user visited the Learn Era platform)
 *
 *
 *
 * //if there are more, then  include them//
 * */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_user_statistics, container, false);
        initUI(parentView);

        toggleProgress(true);
initStatistics(new InitStatsListener() {
    @Override
    public void onSuccess(StatisticsDataModel statisticsDataModel) {
        //access the public methods of StatisticsDataModel class
        toggleProgress(false);

    }

    @Override
    public void onFailed(String errorMessage) {
        //it failed to load the statistics
        toggleProgress(false);


    }
});
        return parentView;
    }

    private void initUI(View parentView){
        //use the parentView to find the by Id as in : parentView.findViewById(...);

        alertDialog = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();
    }

    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.hide();
        }
    }



    private  void initStatistics(InitStatsListener initStatsListener){
        GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId())
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        initStatsListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        long totalNumberOfProfileVisitor = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY) : 0L;
                        long totalNumberOfProfileReach = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_REACH_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_REACH_KEY) : 0L;
                        long totalNumberOfOneStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) : 0L;
                        long totalNumberOfTwoStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) : 0L;
                        long totalNumberOfThreeStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) : 0L;
                        long totalNumberOfFourStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) : 0L;
                        long totalNumberOfFiveStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) : 0L;


                                         documentSnapshot.getReference()
                                                 .collection(GlobalConfig.USER_PROFILE_KEY)
                                                 .document(GlobalConfig.getCurrentUserId())
                                                 .get()
                                                .addOnFailureListener(new OnFailureListener() {
                                                  @Override
                                                  public void onFailure(@NonNull Exception e) {
                                                      initStatsListener.onFailed(e.getMessage());
                                                 }
                                                })
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                 @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                     String lastSeen = documentSnapshot.getString(GlobalConfig.LAST_SEEN_KEY);
                                                     long totalNumberOfLibraries = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY)!= null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY) : 0L;
                                                     boolean isAnAuthor = (documentSnapshot.get(GlobalConfig.IS_USER_AUTHOR_KEY)!=null )? (documentSnapshot.getBoolean(GlobalConfig.IS_USER_AUTHOR_KEY)) : false;

                                                     initStatsListener.onSuccess(new StatisticsDataModel(
                                                                                                          totalNumberOfLibraries,
                                                                                                          totalNumberOfProfileVisitor,
                                                                                                          totalNumberOfProfileReach,
                                                                                                          isAnAuthor,
                                                                                                          lastSeen,
                                                                                                          totalNumberOfOneStarRate,
                                                                                                          totalNumberOfTwoStarRate,
                                                                                                          totalNumberOfThreeStarRate,
                                                                                                          totalNumberOfFourStarRate,
                                                                                                          totalNumberOfFiveStarRate
                                                                                                         ));
                                                 }
                           });
                    }
                });
    }

    interface InitStatsListener{
        void onSuccess(StatisticsDataModel statisticsDataModel);
        void onFailed(String errorMessage);
    }

}