package com.palria.learnera;

        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.cardview.widget.CardView;
        import androidx.core.widget.NestedScrollView;
        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.text.Editable;
        import android.text.TextWatcher;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.ViewTreeObserver;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.Toast;

        import com.facebook.shimmer.ShimmerFrameLayout;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.material.bottomappbar.BottomAppBar;
        import com.google.android.material.textfield.TextInputEditText;
        import com.google.firebase.Timestamp;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.Query;
        import com.google.firebase.firestore.QuerySnapshot;
        import com.palria.learnera.adapters.AllLibraryFragmentRcvAdapter;
        import com.palria.learnera.models.LibraryDataModel;

        import java.util.ArrayList;

public class AllLibraryFragment extends Fragment {


    RecyclerView libraryListRecyclerView;
    TextInputEditText searchKeywordInput;

    AllLibraryFragmentRcvAdapter adapter;
    ArrayList<LibraryDataModel> libraryDataModels;
    ArrayList<LibraryDataModel> libraryDataModelsBackup;

    ShimmerFrameLayout shimmerFrameLayout, progressIndicatorShimmerLayout;
    LinearLayout libraryContents;
    LinearLayout notFoundView;
    LinearLayout containerLinearLayout;
    LibraryFetchListener libraryFetchListener;

    LinearLayout searchLinearLayout;
    CardView searchCardView;
    CardView cardView;
    ImageView imageView;


    public static String OPEN_TYPE_KEY = "OPEN_TYPE";
   public static String OPEN_TYPE_USER_LIBRARY = "OPEN_TYPE_USER_LIBRARY";
   public static String OPEN_TYPE_ALL_LIBRARY = "OPEN_TYPE_ALL_LIBRARY";
   public static String OPEN_TYPE_SINGLE_CATEGORY = "OPEN_TYPE_SINGLE_CATEGORY";

    String open_type = "ALL";
    String authorId = "";

    boolean isFromSearchContext = false;
    boolean isLoadingMoreLibrary = false;
    boolean isFirstLoad = true;
    String searchKeyword = "";
    String singleCategory = "";
    BottomAppBar bottomAppBar;

    NestedScrollView parentScrollView;

    DocumentSnapshot lastRetrievedLibrarySnapshot = null;

    public AllLibraryFragment() {
        // Required empty public constructor
    }
    public AllLibraryFragment(BottomAppBar bottomAppBar) {
        this.bottomAppBar =  bottomAppBar;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //create
        libraryDataModels=new ArrayList<>();
        libraryDataModelsBackup=new ArrayList<>();
if(getArguments() != null){
    open_type = getArguments().getString(OPEN_TYPE_KEY,"");
    authorId = getArguments().getString(GlobalConfig.LIBRARY_AUTHOR_ID_KEY,"");
    isFromSearchContext = getArguments().getBoolean(GlobalConfig.IS_FROM_SEARCH_CONTEXT_KEY,false);
    searchKeyword = getArguments().getString(GlobalConfig.SEARCH_KEYWORD_KEY,"");
    singleCategory = getArguments().getString(GlobalConfig.SINGLE_CATEGORY_KEY,"");
}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_all_library, container, false);
initUI(parentView);
        toggleContentsVisibility(false);
         libraryFetchListener = new LibraryFetchListener() {
             @Override
             public void onFailed(String errorMessage) {

             }

             @Override
             public void onSuccess(LibraryDataModel libraryDataModel) {
                 //add to library data models.
                 libraryDataModels.add(libraryDataModel);
                 libraryDataModelsBackup.add(libraryDataModel);
                 adapter.notifyItemChanged(libraryDataModels.size());
             }
         };
fetchAllLibrary();

searchKeywordInput.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        toggleContentsVisibility(false);
    }

    @Override
    public void onTextChanged(CharSequence query, int i, int i1, int i2) {

        //fetch the library from server if requires here

//        fetchLibraryFromServer();

        ArrayList<LibraryDataModel> filteredList = new ArrayList<>();
        for(LibraryDataModel libraryDataModel : libraryDataModelsBackup){
            if(libraryDataModel.getLibraryName().toLowerCase().contains(query.toString().trim().toLowerCase())){
                filteredList.add(libraryDataModel);
            }
        }
        toggleContentsVisibility(true);
        if(filteredList.size()==0){
            libraryListRecyclerView.setVisibility(View.GONE);
            notFoundView.setVisibility(View.VISIBLE);
        }else{
            notFoundView.setVisibility(View.GONE);
            libraryListRecyclerView.setVisibility(View.VISIBLE);
            adapter.setDataList(filteredList);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
});
//        parentScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            float y = 0;
//            @Override
//            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//                if(bottomAppBar!=null) {
//                    if (oldScrollY > scrollY) {
//                        bottomAppBar.performShow();
//
//                    } else {
//                        bottomAppBar.performHide();
//
//                    }
//                }
//
//
//            }
//        });


//progressIndicatorShimmerLayout = GlobalConfig.showShimmerLayout(getContext(),containerLinearLayout);
        listenToScrollChange();
        return parentView;
    }

    private void toggleContentsVisibility(boolean show){
        if(!show){
            libraryContents.setVisibility(View.GONE);
            shimmerFrameLayout.startShimmer();
            shimmerFrameLayout.setVisibility(View.VISIBLE);

        }else{
            libraryContents.setVisibility(View.VISIBLE);
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }
    }

    private void initUI(View parentView){
        libraryListRecyclerView=parentView.findViewById(R.id.libraryListRecyclerView);
        searchKeywordInput=parentView.findViewById(R.id.searchKeywordInput);

        parentScrollView=parentView.findViewById(R.id.scrollView);
        shimmerFrameLayout=parentView.findViewById(R.id.shimmerLayout);
        libraryContents=parentView.findViewById(R.id.libraryContents);
        notFoundView=parentView.findViewById(R.id.notFoundView);
        containerLinearLayout=parentView.findViewById(R.id.containerLinearLayoutId);

        //these views are search view which have to be hidden if we are searching libraries from the search activity
        searchLinearLayout=parentView.findViewById(R.id.searchLinearLayoutId);
        searchCardView=parentView.findViewById(R.id.searchCardViewId);
        cardView=parentView.findViewById(R.id.cardViewId);
        imageView=parentView.findViewById(R.id.imageViewId);


        //hide if from context of search
        if(isFromSearchContext){
            searchLinearLayout.setVisibility(View.GONE);
            searchCardView.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }
        adapter = new AllLibraryFragmentRcvAdapter(libraryDataModels,getContext());
        libraryListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        libraryListRecyclerView.setHasFixedSize(false);
        libraryListRecyclerView.setAdapter(adapter);

    }

    private void fetchAllLibrary(){

        Query libraryQuery = null;
        if(!isLoadingMoreLibrary) {

            if(isFirstLoad){

            }else{
                progressIndicatorShimmerLayout = GlobalConfig.showShimmerLayout(getContext(),containerLinearLayout);

            }

            if (open_type.equals(OPEN_TYPE_USER_LIBRARY)) {
                if(lastRetrievedLibrarySnapshot == null) {
                    libraryQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).whereEqualTo(GlobalConfig.LIBRARY_AUTHOR_ID_KEY, authorId).limit(15L);
                }else{
                    libraryQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).whereEqualTo(GlobalConfig.LIBRARY_AUTHOR_ID_KEY, authorId).startAfter(lastRetrievedLibrarySnapshot).limit(15L);

                }
            } else if (open_type.equals(OPEN_TYPE_ALL_LIBRARY)) {
                if(lastRetrievedLibrarySnapshot == null) {
                    libraryQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).limit(5L);
                }else{
                    libraryQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).startAfter(lastRetrievedLibrarySnapshot).limit(5L);
                }

            }  else if (open_type.equals(OPEN_TYPE_SINGLE_CATEGORY)) {
                if(lastRetrievedLibrarySnapshot == null) {
                    libraryQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).whereArrayContains(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY,singleCategory).limit(5L);
                }else{
                    libraryQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).whereArrayContains(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY,singleCategory).startAfter(lastRetrievedLibrarySnapshot).limit(5L);
                }

            } else if (isFromSearchContext) {
                searchKeyword = searchKeyword.toLowerCase();
                if(lastRetrievedLibrarySnapshot == null) {
                    libraryQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).whereArrayContains(GlobalConfig.LIBRARY_SEARCH_VERBATIM_KEYWORD_KEY, searchKeyword).limit(15L);
                }else{
                    libraryQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).whereArrayContains(GlobalConfig.LIBRARY_SEARCH_VERBATIM_KEYWORD_KEY, searchKeyword).startAfter(lastRetrievedLibrarySnapshot).limit(15L);

                }

            }
            isLoadingMoreLibrary = true;

            libraryQuery.get()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            libraryFetchListener.onFailed(e.getMessage());
                            isLoadingMoreLibrary = false;

                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

//                            libraryDataModels.clear();
//                            adapter.notifyDataSetChanged();

                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String libraryId = documentSnapshot.getId();
                                long totalNumberOfLibraryVisitor = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY) : 0L;
                                long totalNumberOfLibraryReach = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) : 0L;
                                long totalNumberOfOneStarRate = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) : 0L;
                                long totalNumberOfTwoStarRate = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) : 0L;
                                long totalNumberOfThreeStarRate = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) : 0L;
                                long totalNumberOfFourStarRate = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) : 0L;
                                long totalNumberOfFiveStarRate = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) : 0L;
                                long totalNumberOfTutorials = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) != null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) : 0L;


                                String libraryName = "" + documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                                String libraryDescription = "" + documentSnapshot.get(GlobalConfig.LIBRARY_DESCRIPTION_KEY);
                                ArrayList<String> libraryCategoryArray = (ArrayList<String>) documentSnapshot.get(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY);
                                String dateCreated = documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY) != null && documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY) instanceof Timestamp ? "" + documentSnapshot.getTimestamp(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY).toDate() : "Undefined";
                                if (dateCreated.length() > 10) {
                                    dateCreated = dateCreated.substring(0, 10);
                                }
                                String authorUserId = "" + documentSnapshot.get(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);
                                String libraryCoverPhotoDownloadUrl = "" + documentSnapshot.get(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);

                                if(!(GlobalConfig.getBlockedItemsList().contains(authorUserId+"")) &&!(GlobalConfig.getBlockedItemsList().contains(libraryId+""))) {
                                libraryFetchListener.onSuccess(new LibraryDataModel(
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
//                                    documentSnapshot
                                ));
                            }
                            }
                            //willl reload rcv
                            toggleContentsVisibility(true);
                             GlobalConfig.removeShimmerLayout(containerLinearLayout,progressIndicatorShimmerLayout);
                            if (queryDocumentSnapshots.size() == 0) {
                                if(isFirstLoad) {
                                    libraryListRecyclerView.setVisibility(View.GONE);
                                    notFoundView.setVisibility(View.VISIBLE);
                                }
                            }else{
                                lastRetrievedLibrarySnapshot = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size()-1);
                            }
                            isFirstLoad = false;
                            isLoadingMoreLibrary = false;

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

//                       if(scrollY == -10){
                       if(!v.canScrollVertically(View.LAYOUT_DIRECTION_RTL)){
//                           Toast.makeText(getContext(), "end", Toast.LENGTH_SHORT).show();
                           if(!isLoadingMoreLibrary){
                               fetchAllLibrary();
                           }
                       }
                        if(bottomAppBar!=null) {
                            if (oldScrollY > scrollY) {
                                bottomAppBar.performShow();

                            } else {
                                bottomAppBar.performHide();

                            }
                        }
//                        if(libraryListRecyclerView.getBottom()==(parentScrollView.getHeight()+parentScrollView.getScrollY())){
////                            if(!isLoadingMoreLibrary){
////                                fetchAllLibrary();
////                            }
//
//                        }
                    }
                });
            }
        });
    }

    interface LibraryFetchListener{
        void onFailed(String errorMessage);
        void onSuccess(LibraryDataModel libraryDataModel);
    }

}