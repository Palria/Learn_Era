package com.palria.learnera;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.PopularTutorialsListViewAdapter;
import com.palria.learnera.models.TutorialDataModel;

import java.util.ArrayList;

public class AllTutorialFragment extends Fragment {


    RecyclerView tutorialsRecyclerListView;
    LinearLayout topContents;
    LinearLayout mainContents;
    PopularTutorialsListViewAdapter popularTutorialsListViewAdapter;
    ArrayList<TutorialDataModel> tutorialDataModels = new ArrayList<>();
    BottomAppBar bottomAppBar;

    NestedScrollView parentScrollView;

    public AllTutorialFragment() {
        // Required empty public constructor
    }
    public AllTutorialFragment(BottomAppBar bottomAppBar) {
         this.bottomAppBar =  bottomAppBar;
    }


    public static String OPEN_TYPE_KEY = "OPEN_TYPE";
    public static String OPEN_TYPE_USER_TUTORIAL = "OPEN_TYPE_USER_TUTORIAL";
    public static String OPEN_TYPE_ALL_TUTORIAL = "OPEN_TYPE_ALL_TUTORIAL";
    String open_type = "ALL";

String tutorialCategory = "";
String libraryId = "";
String authorId = "";
boolean isFromLibraryActivityContext = false;

    boolean isLoadingMoreTutorial = false;
    boolean isFirstLoad = true;
    DocumentSnapshot lastRetrievedTutorialSnapshot = null;
    ShimmerFrameLayout  progressIndicatorShimmerLayout;
    LinearLayout containerLinearLayout;
    TutorialFetchListener tutorialFetchListener;

    boolean isFromSearchContext = false;
    String searchKeyword = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
if(getArguments() != null){
    isFromLibraryActivityContext =  getArguments().getBoolean(GlobalConfig.IS_FROM_LIBRARY_ACTIVITY_CONTEXT_KEY,false);
    libraryId =  getArguments().getString(GlobalConfig.LIBRARY_CONTAINER_ID_KEY,"");
    authorId =  getArguments().getString(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY,"");
    open_type =  getArguments().getString(OPEN_TYPE_KEY,"");
    isFromSearchContext = getArguments().getBoolean(GlobalConfig.IS_FROM_SEARCH_CONTEXT_KEY,false);
    searchKeyword = getArguments().getString(GlobalConfig.SEARCH_KEYWORD_KEY,"");
}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View parentView = inflater.inflate(R.layout.fragment_all_tutorial, container, false);

      initView(parentView);
        tutorialFetchListener =  new TutorialFetchListener() {
            @Override
            public void onSuccess(TutorialDataModel tutorialDataModel) {
                tutorialDataModels.add(tutorialDataModel);
                popularTutorialsListViewAdapter.notifyItemChanged(tutorialDataModels.size());
            }

            @Override
            public void onFailed(String errorMessage) {

            }
        };
        fetchTutorial(tutorialCategory);
        listenToScrollChange();
        return parentView;
    }

    private void initView(View parentView) {

        topContents=parentView.findViewById(R.id.topContents);
        tutorialsRecyclerListView=parentView.findViewById(R.id.tutorialsRecyclerListView);
        parentScrollView=parentView.findViewById(R.id.scrollView);
        mainContents=parentView.findViewById(R.id.mainContents);
        containerLinearLayout=parentView.findViewById(R.id.containerLinearLayoutId);

        popularTutorialsListViewAdapter = new PopularTutorialsListViewAdapter(tutorialDataModels,getContext());




        //hide if from context of library or search
        if(isFromLibraryActivityContext || isFromSearchContext){
            topContents.setVisibility(View.GONE);
        }

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
//                        "https://api.lorem.space/image/furniture?w=300&h=150",
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
//                        "https://api.lorem.space/image/drink?w=350&h=150",
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
//                        "https://api.lorem.space/image/burger?w=350&h=150",
//                        0l,
//                        0l,
//                        0l,
//                        0l,
//                        0l));

//        popularTutorialsContainerRcv.setHasFixedSize(true);
        tutorialsRecyclerListView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        tutorialsRecyclerListView.setAdapter(popularTutorialsListViewAdapter);


    }

    private void fetchTutorial(String tutorialCategoryTag) {
        Query tutorialQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).whereEqualTo(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY, GlobalConfig.getCurrentUserId());

        if (!isLoadingMoreTutorial) {

            if (isFirstLoad) {
            } else {
                progressIndicatorShimmerLayout = GlobalConfig.showShimmerLayout(getContext(), containerLinearLayout);

            }


            if (isFromLibraryActivityContext) {
                tutorialQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).whereEqualTo(GlobalConfig.LIBRARY_CONTAINER_ID_KEY, libraryId);

            } else {

//            tutorialQuery =   GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).whereEqualTo(GlobalConfig.TUTORIAL_CATEGORY_KEY, tutorialCategoryTag);

                if (open_type.equals(OPEN_TYPE_USER_TUTORIAL)) {
                    if(lastRetrievedTutorialSnapshot == null) {
                        tutorialQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).whereEqualTo(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY, authorId);
                    }else{
                        tutorialQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).whereEqualTo(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY, authorId).startAfter(lastRetrievedTutorialSnapshot);

                    }
                } else if (open_type.equals(OPEN_TYPE_ALL_TUTORIAL)) {
                    if(lastRetrievedTutorialSnapshot == null) {
                        tutorialQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY);
                    }else {
                        tutorialQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).startAfter(lastRetrievedTutorialSnapshot);
                        ;
                    }
                } else if (isFromSearchContext) {
                    if(lastRetrievedTutorialSnapshot == null) {
                        tutorialQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).whereArrayContains(GlobalConfig.TUTORIAL_SEARCH_VERBATIM_KEYWORD_KEY, searchKeyword);
                    }else{
                        tutorialQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).whereArrayContains(GlobalConfig.TUTORIAL_SEARCH_VERBATIM_KEYWORD_KEY, searchKeyword).startAfter(lastRetrievedTutorialSnapshot);

                    }

                }
            }
            isLoadingMoreTutorial = true;


            tutorialQuery.get()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            tutorialFetchListener.onFailed(e.getMessage());
                            isLoadingMoreTutorial = false;
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

//                            tutorialDataModels.clear();
//                            popularTutorialsListViewAdapter.notifyDataSetChanged();

                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String authorId = "" + documentSnapshot.get(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY);
                                String libraryId = "" + documentSnapshot.get(GlobalConfig.LIBRARY_CONTAINER_ID_KEY);
                                String tutorialId = "" + documentSnapshot.get(GlobalConfig.TUTORIAL_ID_KEY);
                                String tutorialName = "" + documentSnapshot.get(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY);
                                String tutorialCategory = "" + documentSnapshot.get(GlobalConfig.TUTORIAL_CATEGORY_KEY);
                                String tutorialDescription = "" + documentSnapshot.get(GlobalConfig.TUTORIAL_DESCRIPTION_KEY);

                                String dateCreated = documentSnapshot.get(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY) != null && documentSnapshot.get(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY) instanceof Timestamp ? documentSnapshot.getTimestamp(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY).toDate().toString() : "Undefined";
                                if (dateCreated.length() > 10) {
                                    dateCreated = dateCreated.substring(0, 10);
                                }
                                long totalNumberOfFolders = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY) != null ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY) : 0L;
                                long totalNumberOfPages = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_PAGES_CREATED_KEY) != null ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_PAGES_CREATED_KEY) : 0L;
                                String tutorialCoverPhotoDownloadUrl = "" + documentSnapshot.get(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY);


                                long totalNumberOfTutorialVisitor = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY) : 0L;
                                long totalNumberOfTutorialReach = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REACH_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REACH_KEY) : 0L;
                                long totalNumberOfOneStarRate = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) : 0L;
                                long totalNumberOfTwoStarRate = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) : 0L;
                                long totalNumberOfThreeStarRate = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) : 0L;
                                long totalNumberOfFourStarRate = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) : 0L;
                                long totalNumberOfFiveStarRate = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) : 0L;

                            if(!(GlobalConfig.getBlockedItemsList().contains(authorId+"")) &&!(GlobalConfig.getBlockedItemsList().contains(libraryId+"")) && !(GlobalConfig.getBlockedItemsList().contains(tutorialId+"")))  {

                                tutorialFetchListener.onSuccess(new TutorialDataModel(
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

                                isLoadingMoreTutorial = false;
                                GlobalConfig.removeShimmerLayout(containerLinearLayout, progressIndicatorShimmerLayout);

                                isFirstLoad = false;
                            }
                            }

                            if(queryDocumentSnapshots.isEmpty()){
                                isLoadingMoreTutorial = false;
                                GlobalConfig.removeShimmerLayout(containerLinearLayout,progressIndicatorShimmerLayout);
                                isFirstLoad =false;

                            }else{
                                lastRetrievedTutorialSnapshot = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size()-1);

                            }

                        }
                    });
        }
    }
    private void listenToScrollChange(){
        parentScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parentScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                        if(bottomAppBar!=null) {
                            if (oldScrollY > scrollY) {
                                bottomAppBar.performShow();

                            } else {
                                bottomAppBar.performHide();

                            }
                        }
                        if(!v.canScrollVertically(View.LAYOUT_DIRECTION_RTL)){
                            if(!isLoadingMoreTutorial){
                                fetchTutorial(tutorialCategory);
                            }
                        }

//                        if(mainContents.getBottom()==(parentScrollView.getHeight()+parentScrollView.getScrollY())){
//
//                            if(!isLoadingMoreTutorial){
//                                fetchTutorial(tutorialCategory);
//                            }
//
//                        }
                    }
                });
            }
        });
    }

    interface TutorialFetchListener{
        void onSuccess(TutorialDataModel tutorialDataModel);
        void onFailed(String errorMessage);
    }
}