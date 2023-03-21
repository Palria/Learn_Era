package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.models.StatisticsDataModel;
import com.palria.learnera.widgets.RatingBarWidget;

import java.util.ArrayList;
import java.util.Objects;

public class UserStatsActivity extends AppCompatActivity {


    AlertDialog alertDialog;
    // Initialize variables
    TabLayout tabLayout;
    ViewPager viewPager;

    LinearLayout ratingContainer;
    TextView profileViewsTextView;
    TextView numOfLibraryTextView;
    TextView numOfReachedTextView;
    String userId;
    FrameLayout bookmarksFrameLayout;
    FrameLayout allRatingsFrameLayout;
    FrameLayout myRatingsFrameLayout;

    boolean isBookmarksFragmentOpened=false;
    boolean isAllReviewsFragmentOpened=false;
    boolean ismyReviewsFragmentOpened=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);
        fetchIntentData();
        InitUi();
        initTabLayout();
        toggleProgress(true);
        initStatistics(new InitStatsListener() {
            @Override
            public void onSuccess(StatisticsDataModel statisticsDataModel) {
                //access the public methods of StatisticsDataModel class
                toggleProgress(false);
                profileViewsTextView.setText(statisticsDataModel.getTotalNumberOfProfileVisitor() +"");
                numOfLibraryTextView.setText(statisticsDataModel.getTotalNumberOfLibrariesCreated() +"");

            }

            @Override
            public void onFailed(String errorMessage) {
                //it failed to load the statistics
                toggleProgress(false);


            }
        });




        //set first frame layout init and visible
        //or change here
//        isBookmarksFragmentOpened =true;
//        setFrameLayoutVisibility(bookmarksFrameLayout);
//        BookmarksFragment bookmarksFragment = new BookmarksFragment();
//        initFragment(bookmarksFragment, bookmarksFrameLayout);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }


  private void fetchIntentData(){
        Intent intent = getIntent();
        userId = intent.getStringExtra(GlobalConfig.USER_ID_KEY);
  }


    private void initFragment(Fragment fragment, FrameLayout frameLayout){
        Bundle bundle = new Bundle();
        bundle.putString(GlobalConfig.USER_ID_KEY,userId);
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), fragment)
                .commit();


    }

    private void setFrameLayoutVisibility(FrameLayout frameLayoutToSetVisible){
        bookmarksFrameLayout.setVisibility(View.GONE);
        allRatingsFrameLayout.setVisibility(View.GONE);
        myRatingsFrameLayout.setVisibility(View.GONE);
        frameLayoutToSetVisible.setVisibility(View.VISIBLE);
    }

    private void InitUi() {
        Toolbar actionBar = (Toolbar)  findViewById(R.id.topBar);
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // assign variable
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.layout_view_pager);

        ratingContainer = findViewById(R.id.ratingBarContainer);
        profileViewsTextView = findViewById(R.id.profileViewsTextViewId);
        numOfLibraryTextView = findViewById(R.id.numOfLibraryTextViewId);
        numOfReachedTextView = findViewById(R.id.numOfReachedTextViewId);

         bookmarksFrameLayout=findViewById(R.id.bookmarksFrameLayout);
         allRatingsFrameLayout=findViewById(R.id.allRatingsFrameLayout);
         myRatingsFrameLayout=findViewById(R.id.myRatingsFrameLayout);


        alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();

    }

    private void toggleProgress(boolean show){
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }

    private void  initTabLayout(){
          tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
              @Override
              public void onTabSelected(TabLayout.Tab tab) {

                  if(tab.getPosition()==0){
                      if(ismyReviewsFragmentOpened){
                          //Just set the frame layout visibility
                          setFrameLayoutVisibility(myRatingsFrameLayout);
                      }else {
                          ismyReviewsFragmentOpened =true;
                          setFrameLayoutVisibility(myRatingsFrameLayout);
                          UserReviewsFragment userReviewsFragment = new UserReviewsFragment();

                          initFragment(userReviewsFragment, myRatingsFrameLayout);
                      }

                  }else if(tab.getPosition()==1)
                  {
                      if(isAllReviewsFragmentOpened){
                          //Just set the frame layout visibility
                          setFrameLayoutVisibility(allRatingsFrameLayout);
                      }else {
                          isAllReviewsFragmentOpened =true;
                          setFrameLayoutVisibility(allRatingsFrameLayout);
                          AllReviewsFragment allReviewsFragment = new AllReviewsFragment();

                          initFragment(allReviewsFragment, allRatingsFrameLayout);
                      }
                  }else if(tab.getPosition()==2){

                      if(isBookmarksFragmentOpened){
                          //Just set the frame layout visibility
                          setFrameLayoutVisibility(bookmarksFrameLayout);
                      }else {
                          isBookmarksFragmentOpened =true;
                          setFrameLayoutVisibility(bookmarksFrameLayout);

                          BookmarksFragment bookmarksFragment = new BookmarksFragment();

                          initFragment(bookmarksFragment, bookmarksFrameLayout);
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

          tabLayout.addTab(tabLayout.newTab(),0,true);
          tabLayout.addTab(tabLayout.newTab(),1,false);
          if(userId.equals(GlobalConfig.getCurrentUserId())) {
              tabLayout.addTab(tabLayout.newTab(), 2, false);
          }

          tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_stars_24);
          tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_reviews_24);

          if(userId.equals(GlobalConfig.getCurrentUserId())) {
              tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_bookmarks_24);
          }

      }

    private  void initStatistics(InitStatsListener initStatsListener){
        GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(userId)
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
                        int[] ratings = {
                                Integer.parseInt(Long.toString(totalNumberOfOneStarRate)),
                                Integer.parseInt(Long.toString(totalNumberOfTwoStarRate)),
                                Integer.parseInt(Long.toString(totalNumberOfThreeStarRate)),
                                Integer.parseInt(Long.toString(totalNumberOfFourStarRate)),
                                Integer.parseInt(Long.toString(totalNumberOfFiveStarRate))
                        };

                        RatingBarWidget ratingBarWidget = new RatingBarWidget();
                        if(getApplicationContext()!=null) {
                            ratingBarWidget.setContainer(ratingContainer)
                                    .setContext(UserStatsActivity.this)
                                    .setMax_value(5)
                                    .setRatings(ratings)
                                    .setText_color(R.color.teal_700)
                                    .render();
                        }
                    }
                });
    }

    interface InitStatsListener{
        void onSuccess(StatisticsDataModel statisticsDataModel);
        void onFailed(String errorMessage);
    }
}