package com.palria.learnera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllPostsFragment extends Fragment {
    public AllPostsFragment() {
        // Required empty public constructor
    }
//    UpdateFetchListener updateFetchListener;
//    SwipeRefreshLayout refreshLayout;
//    ShimmerFrameLayout shimmerLayout,progressIndicatorShimmerLayout;
//
//    UpdateAdapter adapter;
//    ArrayList<UpdateDataModel> updateDataModels = new ArrayList<>();
//    DocumentSnapshot lastRetrievedUpdateSnapshot = null;
//    LinearLayout containerLinearLayout;
//    boolean isLoadingMoreUpdates = false;
//    boolean isFirstLoad = true;
//    boolean isFromSearchContext = false;
//    String searchKeyword = "";
//    RecyclerView updatesRecyclerView;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (getArguments() != null) {
//            isFromSearchContext = getArguments().getBoolean(GlobalValue.IS_FROM_SEARCH_CONTEXT,false);
//            searchKeyword = getArguments().getString(GlobalValue.SEARCH_KEYWORD,"");
//
//        }
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View parentView = inflater.inflate(R.layout.fragment_all_posts, container, false);
//        adapter = new UpdateAdapter(updateDataModels,getContext());
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
//
//        shimmerLayout = parentView.findViewById(R.id.updatesShimmerLayoutId);
//        containerLinearLayout = parentView.findViewById(R.id.containerLinearLayoutId);
//        refreshLayout = parentView.findViewById(R.id.allUpdatesRefreshLayoutId);
//        updatesRecyclerView = parentView.findViewById(R.id.updatesRecyclerViewId);
//        updatesRecyclerView.setLayoutManager(linearLayoutManager);
//        updatesRecyclerView.setAdapter(adapter);
//
//        updateFetchListener = new UpdateFetchListener() {
//            @Override
//            public void onFailed(String errorMessage) {
//
//            }
//
//            @Override
//            public void onSuccess(UpdateDataModel updateDataModel) {
////                GlobalValue.incrementUpdateViews(updateDataModel.getUpdateId());
//
//                shimmerLayout.stopShimmer();
//                shimmerLayout.setVisibility(View.GONE);
//
//                updateDataModels.add(updateDataModel);
////                productDataModels.add(productDataModel);
////                productDataModels.add(productDataModel);
////                productDataModels.add(productDataModel);
////                productDataModels.add(productDataModel);
//
//                adapter.notifyItemChanged(updateDataModels.size());
//            }
//        };
//        getUpdateData();
//        manageRefreshLayout();
//        listenToScrollChange();
//
//        return parentView;
//    }
//
//    void manageRefreshLayout(){
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh(){
//                refreshLayout.setRefreshing(false);
//
//                isFirstLoad = true;
//                isLoadingMoreUpdates = false;
//                getUpdateData();
//
//            }
//        });
//    }
//    private void getUpdateData() {
//
//        Query query = GlobalValue.getFirebaseFirestoreInstance().collection(GlobalValue.PLATFORM_UPDATES).orderBy(GlobalValue.DATE_POSTED_TIME_STAMP, Query.Direction.DESCENDING).limit(20);
//
//        if (!isLoadingMoreUpdates) {
//
//            if (isFirstLoad) {
//
//                updateDataModels.clear();
//                adapter.notifyDataSetChanged();
//                shimmerLayout.startShimmer();
//                shimmerLayout.setVisibility(View.VISIBLE);
//            } else {
//                progressIndicatorShimmerLayout = GlobalValue.showShimmerLayout(getContext(), containerLinearLayout);
//
//            }
//
//            if (isFromSearchContext) {
//               if(isFirstLoad){
//                   query = GlobalValue.getFirebaseFirestoreInstance().collection(GlobalValue.PLATFORM_UPDATES).whereArrayContains(GlobalValue.SEARCH_ANY_MATCH_KEYWORD, searchKeyword).limit(20);
//            }else {
//                query = GlobalValue.getFirebaseFirestoreInstance().collection(GlobalValue.PLATFORM_UPDATES).whereArrayContains(GlobalValue.SEARCH_ANY_MATCH_KEYWORD, searchKeyword).startAfter(lastRetrievedUpdateSnapshot).limit(20);
//            }
//            }else {
//                if(isFirstLoad){
//                     query = GlobalValue.getFirebaseFirestoreInstance().collection(GlobalValue.PLATFORM_UPDATES).orderBy(GlobalValue.DATE_POSTED_TIME_STAMP, Query.Direction.DESCENDING).limit(20);
//
//                }else{
//                     query = GlobalValue.getFirebaseFirestoreInstance().collection(GlobalValue.PLATFORM_UPDATES).orderBy(GlobalValue.DATE_POSTED_TIME_STAMP, Query.Direction.DESCENDING).startAfter(lastRetrievedUpdateSnapshot).limit(20);
//
//                }
////            if (!categorySelected.equalsIgnoreCase("ALL")) {
////                query = GlobalValue.getFirebaseFirestoreInstance().collection(GlobalValue.PLATFORM_UPDATES).whereEqualTo(GlobalValue.PRODUCT_CATEGORY, categorySelected).whereNotEqualTo(GlobalValue.PRODUCT_CATEGORY, "categorySelected").orderBy(GlobalValue.PRODUCT_CATEGORY, Query.Direction.DESCENDING).orderBy(GlobalValue.DATE_POSTED_TIME_STAMP, Query.Direction.DESCENDING);
////            }
//            }
//
//            isLoadingMoreUpdates = true;
//
//            query.get().addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    updateFetchListener.onFailed(e.getMessage());
//                    isLoadingMoreUpdates = false;
//
//                }
//            }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                @Override
//                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//
//                        String updateId = documentSnapshot.getId();
////                    String productOwnerId = "" + documentSnapshot.get(GlobalValue.PRODUCT_OWNER_USER_ID);
//                        String updateTitle = "" + documentSnapshot.get(GlobalValue.UPDATE_TITLE);
//                        String updateDescription = "" + documentSnapshot.get(GlobalValue.UPDATE_DESCRIPTION);
//                        String datePosted = documentSnapshot.get(GlobalValue.DATE_POSTED_TIME_STAMP) != null ? documentSnapshot.getTimestamp(GlobalValue.DATE_POSTED_TIME_STAMP).toDate() + "" : "Undefined";
//                        if (datePosted.length() > 10) {
//                            datePosted = datePosted.substring(0, 10);
//                        }
//                        long updateViewCount = documentSnapshot.get(GlobalValue.TOTAL_NUMBER_OF_VIEWS) != null ? documentSnapshot.getLong(GlobalValue.TOTAL_NUMBER_OF_VIEWS) : 0L;
//                        ArrayList<String> imageUrlList = documentSnapshot.get(GlobalValue.UPDATE_IMAGE_DOWNLOAD_URL_ARRAY_LIST) != null ? (ArrayList<String>) documentSnapshot.get(GlobalValue.UPDATE_IMAGE_DOWNLOAD_URL_ARRAY_LIST) : new ArrayList<>();
////                    ArrayList<String> viewersIdList = documentSnapshot.get(GlobalValue.UPDATE_VIEWERS_ID_ARRAY_LIST) != null ? (ArrayList<String>) documentSnapshot.get(GlobalValue.UPDATE_VIEWERS_ID_ARRAY_LIST) : new ArrayList<>();
//                        boolean isPrivate = documentSnapshot.get(GlobalValue.IS_PRIVATE) != null ? documentSnapshot.getBoolean(GlobalValue.IS_PRIVATE) : false;
//
//                        updateFetchListener.onSuccess(new UpdateDataModel(updateId, updateTitle, updateDescription, datePosted, imageUrlList, (int) updateViewCount, isPrivate));
//                    }
//                    GlobalValue.removeShimmerLayout(containerLinearLayout,progressIndicatorShimmerLayout);
//                    if (queryDocumentSnapshots.size() == 0) {
//                        if(isFirstLoad) {
//
//                        }
//                    }else{
//                        lastRetrievedUpdateSnapshot = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size()-1);
//                    }
//                    isFirstLoad = false;
//                    isLoadingMoreUpdates = false;
//
//                }
//            });
//        }
//    }
//
//
//    void listenToScrollChange(){
////        if(true){
////            return;
////        }
//
//        updatesRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
////                if (oldScrollY > scrollY) {
////                    topFrameLayout.setVisibility(View.VISIBLE);
////
////                } else {
//////                  topFrameLayout.animate();
////                    topFrameLayout.setVisibility(View.GONE);
////
////                }
//                if(!updatesRecyclerView.canScrollVertically(View.LAYOUT_DIRECTION_RTL)){
////                           Toast.makeText(getContext(), "end", Toast.LENGTH_SHORT).show();
//                    if(!isLoadingMoreUpdates){
//                        getUpdateData();
//                    }
//                }
//
//            }
//        });
////        productRecyclerView.getViewTreeObserver()
////                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
////                    @Override
////                    public void onGlobalLayout() {
////                        productRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
////                            @Override
////                            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
////
////                                    if (oldScrollY > scrollY) {
////                                        topFrameLayout.setVisibility(View.VISIBLE);
////
////                                    } else {
//////                                        topFrameLayout.animate();
////                                        topFrameLayout.setVisibility(View.GONE);
////
////                                    }
////                            }
////                        });
////                    }
////                });
//    }
//
//    interface UpdateFetchListener{
//        void onFailed(String errorMessage);
//        void onSuccess(UpdateDataModel updateDataModel);
//    }

}