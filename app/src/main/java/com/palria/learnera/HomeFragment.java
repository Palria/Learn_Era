package com.palria.learnera;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.HomeAuthorListViewAdapter;
import com.palria.learnera.adapters.HomeBooksRecyclerListViewAdapter;
import com.palria.learnera.adapters.PopularTutorialsListViewAdapter;
import com.palria.learnera.models.AuthorDataModel;
import com.palria.learnera.models.CurrentUserProfileDataModel;
import com.palria.learnera.models.LibraryDataModel;
import com.palria.learnera.models.TutorialDataModel;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import io.grpc.okhttp.internal.framed.FrameReader;

public class HomeFragment extends Fragment {
//going to add a category check flag to avoid multiple fetch
    BottomAppBar bottomAppBar;
    NestedScrollView parentScrollView;

    public HomeFragment() {
        // Required empty public constructor
            }
            public HomeFragment(BottomAppBar bottomAppBar) {
                this.bottomAppBar =  bottomAppBar;

            }
            TextView helloUserTextView;
        String categorySelected = "";
        LinearLayout popularAuthorLinearLayout;
        LinearLayout libraryLinearLayout;
        LinearLayout tutorialLinearLayout;

        TextView seeAllLibraryTextView;
        TextView seeAllAuthorTextView;
        TextView seeAllTutorialTextView;

        LinearLayout categoryTabsContainer;

        RecyclerView popularAuthorRecyclerView;
        RecyclerView booksItemRecyclerListView;
        RecyclerView popularTutorialsContainerRcv;
        TabLayout tabLayout;

        ShimmerFrameLayout shimmerLayout;
        LinearLayout homeContents;
        TextView greetingTextView;

        boolean isLibraryFound = false;
        boolean isTutorialFound = false;
        boolean isAuthorFound = false;

    ArrayList<AuthorDataModel> modelArrayList = new ArrayList<AuthorDataModel>();
    HomeAuthorListViewAdapter popularAuthorAdapter;
    ArrayList<LibraryDataModel> libraryArrayList = new ArrayList<>();
    HomeBooksRecyclerListViewAdapter homeBooksRecyclerListViewAdapter ;
    ArrayList<TutorialDataModel> tutorialDataModels = new ArrayList<>();
    PopularTutorialsListViewAdapter popularTutorialsListViewAdapter;
    //test categories
    final String[] categories = {"Java", "Android Dev", "Python", "Data Learning", "OOPs Concept", "Artificial Intelligence"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View parentView = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(parentView);

        //geret the curent user Good morning, evening etc.

//        System.out.println(java.time.LocalTime.now());
        LocalTime currentTime = LocalTime.now();
        String greeting="";
        int current_hour = Integer.parseInt(currentTime.toString().substring(0,2));
        if (current_hour < 12)
            greeting = "Good Morning";
        else if (current_hour >= 12 && current_hour <= 17)
            greeting = "Good Afternoon";
        else if (current_hour >= 17 && current_hour <= 24)
            greeting = "Good Evening";

        //show the greeting to
        greetingTextView.setText(greeting);

        createTabLayout(new OnNewCategorySelectedListener() {
            @Override
            public void onSelected(String categoryName) {
                modelArrayList.clear();
                libraryArrayList.clear();
                tutorialDataModels.clear();
                fetchPopularAuthor(categoryName, new PopularAuthorFetchListener() {
                    @Override
                    public void onSuccess(String authorName, String authorId, String authorProfilePhotoDownloadUrl, long totalNumberOfLibrary) {
//                        displayPopularAuthor(authorName,authorProfilePhotoDownloadUrl,totalNumberOfLibrary);
                        modelArrayList.add(new AuthorDataModel(authorName,authorId,authorProfilePhotoDownloadUrl, (int) totalNumberOfLibrary,0,0,0,0,0));
                        popularAuthorAdapter.notifyItemChanged(modelArrayList.size());
                        toggleContentsVisibility(true);

                    }
                    @Override
                    public void onFailed(String errorMessage) {

                    }
                });
                fetchLibrary(categoryName, new LibraryFetchListener() {
                    @Override
                    public void onSuccess(LibraryDataModel libraryDataModel) {
//                        displayLibrary(libraryDataModel);
                        if(libraryDataModel.isPublic() || (GlobalConfig.getCurrentUserId().equals(libraryDataModel.getAuthorUserId()+""))) {
                            libraryArrayList.add(libraryDataModel);
//                        libraryArrayList.add(new LibraryDataModel(libraryDataModel.getLibraryName(),libraryDataModel.getLibraryId(),libraryDataModel.getLibraryCategoryArrayList(),libraryDataModel.getLibraryCoverPhotoDownloadUrl(),libraryDataModel.getLibraryDescription(),libraryDataModel.getDateCreated(),libraryDataModel.getTotalNumberOfTutorials(),libraryDataModel.getTotalNumberOfLibraryViews(),libraryDataModel.getTotalNumberOfLibraryReach(),libraryDataModel.getAuthorUserId(),libraryDataModel.getTotalNumberOfOneStarRate(),libraryDataModel.getTotalNumberOfTwoStarRate(),libraryDataModel.getTotalNumberOfThreeStarRate(),libraryDataModel.getTotalNumberOfFourStarRate(),libraryDataModel.getTotalNumberOfFiveStarRate()));
                            homeBooksRecyclerListViewAdapter.notifyItemChanged(libraryArrayList.size());
//                        Toast.makeText(getContext(), libraryDataModel.getDateCreated()+"--"+libraryDataModel.getLibraryName()+"---"+libraryDataModel.getLibraryId(), Toast.LENGTH_SHORT).show();
                        }
                        toggleContentsVisibility(true);

                    }

                    @Override
                    public void onFailed(String errorMessage) {

                    }
                });
                fetchTutorial(categoryName, new TutorialFetchListener() {
                    @Override
                    public void onSuccess(TutorialDataModel tutorialDataModel) {
//                        displayTutorial(tutorialDataModel);
                        if(tutorialDataModel.isPublic() || (GlobalConfig.getCurrentUserId().equals(tutorialDataModel.getAuthorId()+""))) {
                            tutorialDataModels.add(tutorialDataModel);

//                        tutorialDataModels.add(new TutorialDataModel(
//                                tutorialDataModel.getTutorialName(),
//                                tutorialDataModel.getTutorialCategory(),
//                                tutorialDataModel.getTutorialDescription(),
//                                tutorialDataModel.getTutorialId(),
//                                tutorialDataModel.getDateCreated(),
//                                tutorialDataModel.getTotalNumberOfPages(),
//                                tutorialDataModel.getTotalNumberOfFolders(),
//                                tutorialDataModel.getTotalNumberOfTutorialViews(),
//                                tutorialDataModel.getTotalNumberOfTutorialReach(),
//                                tutorialDataModel.getAuthorId(),
//                                tutorialDataModel.getLibraryId(),
//                                tutorialDataModel.getTutorialCoverPhotoDownloadUrl(),
//                                tutorialDataModel.getTotalNumberOfOneStarRate(),
//                                tutorialDataModel.getTotalNumberOfTwoStarRate(),
//                                tutorialDataModel.getTotalNumberOfThreeStarRate(),
//                                tutorialDataModel.getTotalNumberOfFourStarRate(),
//                                tutorialDataModel.getTotalNumberOfFiveStarRate()));
                            popularTutorialsListViewAdapter.notifyItemChanged(tutorialDataModels.size());
                        }
                        toggleContentsVisibility(true);
                    }

                    @Override
                    public void onFailed(String errorMessage) {

                    }
                });
//                Toast.makeText(getContext(), categoryName, Toast.LENGTH_SHORT).show();

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(CurrentUserProfileDataModel.getUserName()!=null && !CurrentUserProfileDataModel.getUserName().equals("null") && !CurrentUserProfileDataModel.getUserName().isEmpty()) {
                    helloUserTextView.setText("Hello, "+CurrentUserProfileDataModel.getUserName().split(" ")[0]);
                }
            }
        },12000);

//        initCategoriesTab(0);
        parentScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            float y = 0;
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if(bottomAppBar!=null) {
                    if (oldScrollY > scrollY) {
                        bottomAppBar.performShow();

                    } else {
                        bottomAppBar.performHide();

                    }
                }


            }
        });
        seeAllLibraryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(GlobalConfig.getHostActivityIntent(getContext(),null,GlobalConfig.LIBRARY_FRAGMENT_TYPE_KEY,null));

            }
        });
        seeAllAuthorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(GlobalConfig.getHostActivityIntent(getContext(),null,GlobalConfig.AUTHORS_FRAGMENT_TYPE_KEY,null));

            }
        });
        seeAllTutorialTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(GlobalConfig.getHostActivityIntent(getContext(),null,GlobalConfig.TUTORIAL_FRAGMENT_TYPE_KEY,null));

            }
        });
       return parentView;
    }


    class CategoryTabOnClickListener implements View.OnClickListener {

        int position;

        public CategoryTabOnClickListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            initCategoriesTab(position);
            //reload all books and authors here
//            fetchLibrary();
//            fetchTutorial();
//            fetchPopularAuthor();
        }
    }

    private void initCategoriesTab(int currentActivePosition) {


        //remove all views from the tab container
        categoryTabsContainer.removeAllViews();
        int j=0;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(String c : categories){

            View category_tab_item = layoutInflater.inflate(R.layout.home_book_category_tab_item,null);
            TextView category_name = category_tab_item.findViewById(R.id.categoryName);
            TextView active_indicator = category_tab_item.findViewById(R.id.activeIndicator);
            category_name.setText(c);
            if(currentActivePosition==j){
                //item is active now
                active_indicator.setVisibility(View.VISIBLE);
                category_name.setTextColor(getContext().getResources().getColor(R.color.teal_700));
            }else{
                //item is not active
                active_indicator.setVisibility(View.INVISIBLE);
            }
                category_tab_item.setOnClickListener(new CategoryTabOnClickListener(j));
            categoryTabsContainer.addView(category_tab_item);

            j++;
        }

    }


    private void initUI(View parentView){
//        categoryTabsContainer = parentView.findViewById(R.id.categoryTabContainer);
        helloUserTextView = parentView.findViewById(R.id.helloUserTextViewId);
        booksItemRecyclerListView = parentView.findViewById(R.id.booksItemContainer);
        popularAuthorRecyclerView = parentView.findViewById(R.id.popular_authors_listview);
        seeAllAuthorTextView = parentView.findViewById(R.id.seeAllAuthorTextViewId);
        seeAllLibraryTextView = parentView.findViewById(R.id.seeAllLibraryTextViewId);
        seeAllTutorialTextView = parentView.findViewById(R.id.seeAllTutorialTextViewId);
        popularTutorialsContainerRcv = parentView.findViewById(R.id.popularTutorialsContainer);
        tabLayout = parentView.findViewById(R.id.categoryTabLayoutId);
        popularAuthorAdapter = new HomeAuthorListViewAdapter(modelArrayList,getContext());
        homeBooksRecyclerListViewAdapter = new HomeBooksRecyclerListViewAdapter(libraryArrayList,getContext());
        popularTutorialsListViewAdapter = new PopularTutorialsListViewAdapter(tutorialDataModels,getContext());


        parentScrollView=parentView.findViewById(R.id.scrollView);

        shimmerLayout=parentView.findViewById(R.id.shimmerLayout);
        homeContents=parentView.findViewById(R.id.homeContents);
        greetingTextView=parentView.findViewById(R.id.greetingTextView);
/*
        //init and show some dummny authors.
        modelArrayList.add(new AuthorDataModel("Palria","lasdjf","url",0,0,0,0,0,0));
        modelArrayList.add(new AuthorDataModel("Ramesh Yadav","lsd","url",0,0,0,0,0,0));
        modelArrayList.add(new AuthorDataModel("Simran Sah","lsd","url",0,0,0,0,0,0));
        modelArrayList.add(new AuthorDataModel("Kalidevi Man","lsd","url",0,0,0,0,0,0));
        modelArrayList.add(new AuthorDataModel("Some longest name that trails","lsd","url",0,0,0,0,0,0));
*/

//        popularAuthorRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        popularAuthorRecyclerView.setLayoutManager(layoutManager);
        popularAuthorRecyclerView.setAdapter(popularAuthorAdapter);

        //init and show some dummy libraries
//        libraryArrayList.add(new LibraryDataModel("Chown Town","lasdjf",null,"","",0l,0l,0l,"",0l,0l,0l,0l,0l));
//        libraryArrayList.add(new LibraryDataModel("Palria The Learning way","lasdjf",null,"","",0l,0l,0l,"",0l,0l,0l,0l,0l));
//        libraryArrayList.add(new LibraryDataModel("Paraka Tendi","lasdjf",null,"","",0l,0l,0l,"",0l,0l,0l,0l,0l));

//        booksItemRecyclerListView.setHasFixedSize(true);
        booksItemRecyclerListView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        booksItemRecyclerListView.setAdapter(homeBooksRecyclerListViewAdapter);


/**
 *  String tutorialName,
 *             String tutorialId,
 *             String dateCreated,
 *             long totalNumberOfPages,
 *             long totalNumberOfFolders,
 *             long totalNumberOfTutorialViews,
 *             long totalNumberOfTutorialReach,
 *             String authorId,
 *             String libraryId,
 *             String tutorialCoverPhotoDownloadUrl,
 *             long totalNumberOfOneStarRate,
 *             long totalNumberOfTwoStarRate,
 *             long totalNumberOfThreeStarRate,
 *             long totalNumberOfFourStarRate,
 *             long totalNumberOfFiveStarRate
 */

        //init and show some dummy tutorials
//        tutorialDataModels.add(
//                new TutorialDataModel("How to connect to mysql database for free. in 2012 for Users to get it.",
//                        "category",
//                        "description",
//                        "__id__02151",
//                        "1 days ago",
//                        2l,
//                        1l,
//                        0l,
//                        0l,
//                        "Kamaensi",
//                        "",
//                        "__",
//                        0l,
//                        0l,
//                        0l,
//                        0l,
//                        0l));
//
//        tutorialDataModels.add(
//                new TutorialDataModel("The protest was organised against the Kathmandu Metropolitan City mayor’s recent move to demolish a part of private property in Sankhamul",
//                        "Category",
//                        "description",
//                        "Jeevan",
//                        "32 mins ago",
//                        2l,
//                        1l,
//                        0l,
//                        0l,
//                        "Jeevan",
//                        "",
//                        "__",
//                        0l,
//                        0l,
//                        0l,
//                        0l,
//                        0l));
//
//        tutorialDataModels.add(
//                new TutorialDataModel("According to him, the metropolis demolished the house compound without any letter or notice.",
//                        "Category",
//                        "description",
//                        "Palria",
//                        "32 mins ago",
//                        2l,
//                        1l,
//                        0l,
//                        0l,
//                        "Palria",
//                        "",
//                        "__",
//                        0l,
//                        0l,
//                        0l,
//                        0l,
//                        0l));

//        popularTutorialsContainerRcv.setHasFixedSize(true);
        popularTutorialsContainerRcv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        popularTutorialsContainerRcv.setAdapter(popularTutorialsListViewAdapter);


    }


    /*Changes to be made when new category is selected will be triggered in this method.
    Manipulate the changes here
    */
    public void createTabLayout(OnNewCategorySelectedListener onNewCategorySelectedListener){

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //change categories here
                toggleContentsVisibility(false);
                onNewCategorySelectedListener.onSelected(tab.getText().toString());


                 }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


//String[] categories = getResources().getStringArray(R.array.category);
ArrayList<String> categories = GlobalConfig.getCategoryList(getContext());
for(int i=0; i<categories.size(); i++) {
    if(i==0) {
        TabLayout.Tab firstTabItem = tabLayout.newTab();
        firstTabItem.setText(categories.get(0));
        tabLayout.addTab(firstTabItem, 0, true);
//        Toast.makeText(getContext(),categories.get(i)+" ---1", Toast.LENGTH_SHORT).show();

    }else {
        TabLayout.Tab otherTabItem = tabLayout.newTab();
        otherTabItem.setText(categories.get(i));
        tabLayout.addTab(otherTabItem, i);
//        Toast.makeText(getContext(),categories.get(i)+" ---2", Toast.LENGTH_SHORT).show();

    }
//    Toast.makeText(getContext(),categories.get(i), Toast.LENGTH_SHORT).show();

}
        /*
        TabLayout.Tab graphicDesignTabItem= tabLayout.newTab();
        graphicDesignTabItem.setText("Graphic Design");
        tabLayout.addTab(graphicDesignTabItem,2);


        TabLayout.Tab uiDesignTabItem= tabLayout.newTab();
        uiDesignTabItem.setText("Ui Design");
        tabLayout.addTab(uiDesignTabItem,3);


        TabLayout.Tab ethicalHackingTabItem= tabLayout.newTab();
        ethicalHackingTabItem.setText("Ethical Hacking");
        tabLayout.addTab(ethicalHackingTabItem,4);


        TabLayout.Tab gameDevTabItem= tabLayout.newTab();
        gameDevTabItem.setText("Game Development");
        tabLayout.addTab(gameDevTabItem,5);


        TabLayout.Tab prototypingTabItem= tabLayout.newTab();
        prototypingTabItem.setText("Prototyping");
        tabLayout.addTab(prototypingTabItem,6);

        TabLayout.Tab SEOTabItem= tabLayout.newTab();
        SEOTabItem.setText("SEO");
        tabLayout.addTab(SEOTabItem,7);

        TabLayout.Tab androidDevTabItem=tabLayout.newTab();
        androidDevTabItem.setText("Android Dev");
        tabLayout.addTab(androidDevTabItem,8);

        TabLayout.Tab javaTabItem=tabLayout.newTab();
        javaTabItem.setText("Java");
        tabLayout.addTab(javaTabItem,9);

        TabLayout.Tab pythonTabItem=tabLayout.newTab();
        pythonTabItem.setText("Python");
        tabLayout.addTab(pythonTabItem,10);

        TabLayout.Tab dataLearningTabItem =tabLayout.newTab();
        dataLearningTabItem.setText("Data Learning");
        tabLayout.addTab(dataLearningTabItem,11);


        TabLayout.Tab OOPsConceptTabItem =tabLayout.newTab();
        OOPsConceptTabItem.setText("OOPs Concept");
        tabLayout.addTab(OOPsConceptTabItem,12);


        TabLayout.Tab artificialIntelligenceTabItem =tabLayout.newTab();
        artificialIntelligenceTabItem.setText("Artificial Intelligence");
        tabLayout.addTab(artificialIntelligenceTabItem,13);
*/

    }

    private void toggleContentsVisibility(boolean show){
        if(!show){
            homeContents.setVisibility(View.GONE);
            shimmerLayout.startShimmer();
            shimmerLayout.setVisibility(View.VISIBLE);

        }else{
            homeContents.setVisibility(View.VISIBLE);
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
        }
    }

private void changeCategory(String categorySelected){
    popularAuthorLinearLayout.removeAllViews();
    libraryLinearLayout.removeAllViews();
    tutorialLinearLayout.removeAllViews();



    fetchPopularAuthor(categorySelected, new PopularAuthorFetchListener() {
        @Override
        public void onSuccess(String authorName,String authorId, String authorProfilePhotoDownloadUrl, long totalNumberOfLibrary) {
            displayPopularAuthor(authorName,authorProfilePhotoDownloadUrl,totalNumberOfLibrary);
        }

        @Override
        public void onFailed(String errorMessage) {

        }
    });
    fetchLibrary(categorySelected, new LibraryFetchListener() {
        @Override
        public void onSuccess(LibraryDataModel libraryDataModel) {
            displayLibrary(libraryDataModel);
        }

        @Override
        public void onFailed(String errorMessage) {

        }
    });
    fetchTutorial(categorySelected, new TutorialFetchListener() {
        @Override
        public void onSuccess(TutorialDataModel tutorialDataModel) {
            displayTutorial(tutorialDataModel);
            toggleContentsVisibility(true);
        }

        @Override
        public void onFailed(String errorMessage) {

        }
    });

}

    private void fetchPopularAuthor(String categoryTag, PopularAuthorFetchListener popularAuthorFetchListener){
//        Query authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereEqualTo(GlobalConfig.IS_USER_AUTHOR_KEY,true).whereArrayContains(GlobalConfig.AUTHOR_CATEGORY_TAG_ARRAY_KEY,categoryTag).orderBy(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY);
//        Query authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereArrayContains(GlobalConfig.AUTHOR_CATEGORY_TAG_ARRAY_KEY,categoryTag).whereNotIn(GlobalConfig.USER_ID_KEY,GlobalConfig.getBlockedItemsList()).whereNotIn(GlobalConfig.USER_ID_KEY,GlobalConfig.getReportedItemsList()).limit(10L);
        Query authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereArrayContains(GlobalConfig.AUTHOR_CATEGORY_TAG_ARRAY_KEY,categoryTag).limit(10L);
        authorQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                                popularAuthorFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        modelArrayList.clear();
                        popularAuthorAdapter.notifyDataSetChanged();

                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            String authorId = documentSnapshot.getId();
                            final String authorName = "" + documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                            final String authorProfilePhotoDownloadUrl = "" + documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                            final long totalNumberOfLibrary = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY) != null ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY) : 0L;
                            if(!(GlobalConfig.getBlockedItemsList().contains(authorId+""))) {
                                popularAuthorFetchListener.onSuccess(authorName, authorId, authorProfilePhotoDownloadUrl, totalNumberOfLibrary);
                        }
//                            for(int i=0; i<GlobalConfig.getBlockedItemsList().size();i++) {
//                                Toast.makeText(getContext(), "" +GlobalConfig.getBlockedItemsList().get(i), Toast.LENGTH_SHORT).show();
//                            }
                        }
                        toggleContentsVisibility(true);

                    }
                });
    }

    private void fetchLibrary(String categoryTag,LibraryFetchListener libraryFetchListener){
//        Query libraryQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).whereArrayContains(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY,categoryTag).orderBy(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY);
//        Query libraryQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).whereArrayContains(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY,categoryTag).whereNotIn(GlobalConfig.LIBRARY_ID_KEY,GlobalConfig.getBlockedItemsList()).whereNotIn(GlobalConfig.LIBRARY_ID_KEY,GlobalConfig.getReportedItemsList()).limit(10L);
        Query libraryQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).whereArrayContains(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY,categoryTag).limit(10L);
        libraryQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        libraryFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        libraryArrayList.clear();
                        homeBooksRecyclerListViewAdapter.notifyDataSetChanged();


                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//                            String authorId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);
                            String libraryId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_ID_KEY);
                            boolean isPublic =  documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY)!=null?  documentSnapshot.getBoolean(GlobalConfig.IS_PUBLIC_KEY)  :true;

                                    long totalNumberOfLibraryVisitor = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY) : 0L;
                                    long totalNumberOfLibraryReach = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) : 0L;
                                    long totalNumberOfOneStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) : 0L;
                                    long totalNumberOfTwoStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) : 0L;
                                    long totalNumberOfThreeStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) : 0L;
                                    long totalNumberOfFourStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) : 0L;
                                    long totalNumberOfFiveStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) : 0L;


                                                    String libraryName = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                                                    ArrayList<String> libraryCategoryArray = (ArrayList<String>) documentSnapshot.get(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY);
                                                    String libraryDescription = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DESCRIPTION_KEY);
                                                    String dateCreated = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_KEY);
                                                    String authorUserId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);
                                                    String libraryCoverPhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);
                                                    long totalNumberOfTutorials = 0L;
                                                    if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) != null){
                                                        totalNumberOfTutorials =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY);
                                                    }

                            if(!(GlobalConfig.getBlockedItemsList().contains(authorUserId+"")) &&!(GlobalConfig.getBlockedItemsList().contains(libraryId+""))) {

                                libraryFetchListener.onSuccess(new LibraryDataModel(
                                        isPublic,
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
//                                                            documentSnapshot
                                ));
                            }


                        }
                        toggleContentsVisibility(true);

                    }
                });
    }

    private void fetchTutorial(String tutorialCategoryTag,TutorialFetchListener tutorialFetchListener){
//        Query libraryQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).whereEqualTo(GlobalConfig.TUTORIAL_CATEGORY_KEY,tutorialCategoryTag).orderBy(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY);
//        Query tutorialQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).whereEqualTo(GlobalConfig.TUTORIAL_CATEGORY_KEY,tutorialCategoryTag).whereNotIn(GlobalConfig.TUTORIAL_ID_KEY,GlobalConfig.getBlockedItemsList()).whereNotIn(GlobalConfig.TUTORIAL_ID_KEY,GlobalConfig.getReportedItemsList()).limit(10L);
        Query tutorialQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).whereEqualTo(GlobalConfig.TUTORIAL_CATEGORY_KEY,tutorialCategoryTag).limit(10L);
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
//
//                        tutorialDataModels.clear();
//                        popularTutorialsListViewAdapter.notifyDataSetChanged();

                        tutorialDataModels.clear();
                        popularTutorialsListViewAdapter.notifyDataSetChanged();

                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String authorId =""+ documentSnapshot.get(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY);
                            boolean isPublic =  documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY)!=null?  documentSnapshot.getBoolean(GlobalConfig.IS_PUBLIC_KEY)  :true;

                            String libraryId =""+ documentSnapshot.get(GlobalConfig.LIBRARY_CONTAINER_ID_KEY);
                            String tutorialId =""+ documentSnapshot.get(GlobalConfig.TUTORIAL_ID_KEY);
                            String tutorialName = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY);
                            String tutorialCategory = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_CATEGORY_KEY);
                            String tutorialDescription = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_DESCRIPTION_KEY);
                            String dateCreated = documentSnapshot.get(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY)!=null && documentSnapshot.get(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY) instanceof Timestamp ? ""+ documentSnapshot.getTimestamp(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY).toDate():"Undefined";
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
                                    if(!(GlobalConfig.getBlockedItemsList().contains(authorId+"")) &&!(GlobalConfig.getBlockedItemsList().contains(libraryId+"")) && !(GlobalConfig.getBlockedItemsList().contains(tutorialId+"")))  {

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
//                                                            documentSnapshot
                                        ));
                                    }



                        }
                        toggleContentsVisibility(true);

                    }
                });
    }

    private  void displayTutorial(TutorialDataModel tutorialDataModel){
        //<Uncomment and implement>
        /*
        if(getContext() != null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View tutorialView = layoutInflater.inflate(R.layout.tutorial_custom_view, null);
            TextView tutorialNameTextView = tutorialView.findViewById(R.id.tutorialNameTextViewId);
            TextView authorNameTextView = tutorialView.findViewById(R.id.authorNameTextViewId);
            ImageView tutorialCoverPhotoImageView = tutorialView.findViewById(R.id.tutorialCoverPhotoImageViewId);
            tutorialNameTextView.setText(tutorialDataModel.getTutorialName());
            Glide.with(getContext())
                    .load(tutorialDataModel.getTutorialCoverPhotoDownloadUrl())
                    .centerCrop()
                    .into(tutorialCoverPhotoImageView);

            //get author name
            GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(tutorialDataModel.getAuthorId())
                    .collection(GlobalConfig.USER_PROFILE_KEY)
                    .document(tutorialDataModel.getAuthorId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String authorName =""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
//                            String authorProfilePhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);

                                authorNameTextView.setText(authorName);


                        }
                    });
            tutorialLinearLayout.addView(tutorialView);
        }
        */
        }

    private  void displayLibrary(LibraryDataModel libraryDataModel){
        //<Uncomment and implement>
        /*
        if(getContext() != null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View libraryView = layoutInflater.inflate(R.layout.library_custom_view, null);
            TextView libraryNameTextView = libraryView.findViewById(R.id.libraryNameTextViewId);
            TextView authorNameTextView = libraryView.findViewById(R.id.authorNameTextViewId);
            ImageView libraryCoverPhotoImageView = libraryView.findViewById(R.id.libraryCoverPhotoImageViewId);
            libraryNameTextView.setText(libraryDataModel.getLibraryName());
            Glide.with(getContext())
                    .load(libraryDataModel.getLibraryCoverPhotoDownloadUrl())
                    .centerCrop()
                    .into(libraryCoverPhotoImageView);

            //get author name
            GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(libraryDataModel.getAuthorUserId())
                    .collection(GlobalConfig.USER_PROFILE_KEY)
                    .document(libraryDataModel.getAuthorUserId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String authorName =""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
//                            String authorProfilePhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);

                                authorNameTextView.setText(authorName);


                        }
                    });
            libraryLinearLayout.addView(libraryView);
        }
        */
        }

    private void displayPopularAuthor(final String authorName,final String authorProfilePhotoDownloadUrl,final long totalNumberOfLibrary ){
        //<Uncomment and implement>
        /*
        if(getContext() != null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View authorView = layoutInflater.inflate(R.layout.popular_author_custom_view, null);
            TextView authorNameTextView = authorView.findViewById(R.id.authorNameTextViewId);
            TextView totalNumberOfLibraryTextView = authorView.findViewById(R.id.totalNumberOfLibraryTextViewId);
            ImageView authorProfilePhotoImageView = authorView.findViewById(R.id.authorProfilePhotoImageViewId);
            authorNameTextView.setText(authorName);
            totalNumberOfLibraryTextView.setText(String.valueOf(totalNumberOfLibrary));
            Glide.with(getContext())
                    .load(authorProfilePhotoDownloadUrl)
                    .centerCrop()
                    .into(authorProfilePhotoImageView);
            popularAuthorLinearLayout.addView(authorView);
        }
        */
    }

    interface PopularAuthorFetchListener{
        void onSuccess(final String authorName, final String authorId,final String authorProfilePhotoDownloadUrl,final long totalNumberOfLibrary );
        void onFailed(String errorMessage);
    }

    interface LibraryFetchListener{
        void onSuccess(LibraryDataModel libraryDataModel);
        void onFailed(String errorMessage);
    }

    interface TutorialFetchListener{
        void onSuccess(TutorialDataModel tutorialDataModel);
        void onFailed(String errorMessage);
    }

    interface OnNewCategorySelectedListener{
        void onSelected(String categoryName);
    }


}