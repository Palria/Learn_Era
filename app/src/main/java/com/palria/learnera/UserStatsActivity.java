package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.models.StatisticsDataModel;
import com.palria.learnera.widgets.RatingBarWidget;

import java.util.ArrayList;

public class UserStatsActivity extends AppCompatActivity {


    AlertDialog alertDialog;
    // Initialize variables
    TabLayout tabLayout;
    ViewPager viewPager;

    LinearLayout ratingContainer;
    TextView profileViewsTextView;
    TextView numOfLibraryTextView;
    TextView numOfReachedTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);
        InitUi();


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




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
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
        // Initialize array list
        ArrayList<String> arrayList=new ArrayList<>(0);

        // Add title in array list
        arrayList.add("Basic");
        arrayList.add("Advance");
        arrayList.add("Pro");

        // Setup tab layout
        tabLayout.setupWithViewPager(viewPager);

        // Prepare view pager
        prepareViewPager(viewPager,arrayList);

        alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();

        //load rating bar
//        int[] ratings = {0,12,55,1,250};
//
//        RatingBarWidget ratingBarWidget = new RatingBarWidget();
//        ratingBarWidget.setContainer(ratingContainer)
//                .setContext(this)
//                .setMax_value(5)
//                .setRatings(ratings)
//                .setText_color(R.color.teal_700)
//                .render();
    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {
        // Initialize main adapter
        MainAdapter adapter=new MainAdapter(getSupportFragmentManager());

        // Initialize main fragment
        TestFragment mainFragment=new TestFragment();

        // Use for loop
        for(int i=0;i<arrayList.size();i++)
        {
            // Initialize bundle
            Bundle bundle=new Bundle();

            // Put title
            bundle.putString("title",arrayList.get(i));

            // set argument
            mainFragment.setArguments(bundle);

            // Add fragment
            adapter.addFragment(mainFragment,arrayList.get(i));
            mainFragment=new TestFragment();
        }
        // set adapter
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
    }

    private class MainAdapter extends PagerAdapter {
        // Initialize arrayList
        ArrayList<Fragment> fragmentArrayList= new ArrayList<>();
        ArrayList<String> stringArrayList=new ArrayList<>();

        int[] imageList={R.drawable.ic_baseline_bookmarks_24,R.drawable.ic_baseline_reviews_24,R.drawable.ic_baseline_photo_camera_24};

        // Create constructor
        public void addFragment(Fragment fragment,String s)
        {
            // Add fragment
            fragmentArrayList.add(fragment);
            // Add title
            stringArrayList.add(s);
        }

        public MainAdapter(FragmentManager supportFragmentManager) {

        }



        @Override
        public int getCount() {
            // Return fragment array list size
            return fragmentArrayList.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View item_view = layoutInflater.inflate(R.layout.welcome_activity_vpadapter_layout_item,container,false);


            ImageView imageView = item_view.findViewById(R.id.image);
            TextView titleTextView = item_view.findViewById(R.id.title);
            TextView contentTextView = item_view.findViewById(R.id.body);


            titleTextView.setText("No Data Found");
            contentTextView.setText(
                    position==0?"Please add Bookmarks to see here." : (
                            position == 1 ? "Please Submit your reviews and visit again." :
                                    "We cannot find any relating reviews for you at this time."
                    )
            );
            imageView.setImageResource(R.drawable.undraw_no_data_re_kwbl);
            imageView.getLayoutParams().height = 150;
            imageView.requestLayout();
            container.addView(item_view);


            //add these three fragments to the statistics
//            switch (position){
//          case 0:   return new BookmarksFragment();
//          case 1:   return new AllReviewsFragment();
//          case 2:   return new UserReviewsFragment();
//            }

            return item_view ;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                        container.removeView((LinearLayout)object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

            return (LinearLayout) object == view;
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            return "";

        }


    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_bookmarks_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_reviews_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_stars_24);
    }

    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
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
                        int[] ratings = {(int) totalNumberOfOneStarRate,(int)totalNumberOfTwoStarRate,(int)totalNumberOfThreeStarRate,(int)totalNumberOfFourStarRate,(int)totalNumberOfFiveStarRate};

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