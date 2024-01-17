package com.palria.learnera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.FolderRcvAdapter;
import com.palria.learnera.adapters.LearnEraNotificationAdapter;
import com.palria.learnera.adapters.LearnEraNotificationAdapter;
import com.palria.learnera.models.FolderDataModel;
import com.palria.learnera.models.LearnEraNotificationDataModel;

import java.util.ArrayList;


public class AllLearnEraNotificationsFragment extends Fragment {

//    LinearLayout loadingLayout;
//    LinearLayout noDataFoundLayout;
RecyclerView notificationRecyclerView;
    LearnEraNotificationAdapter notificationAdapter;
    ArrayList<LearnEraNotificationDataModel> notificationDataModelArrayList = new ArrayList<>();

    public AllLearnEraNotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){

             }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_all_learn_era_notifications, container, false);


        initUi(view);


        initRecyclerView();
        getNotifications();

        return view;

    }

//    private void toggleContentVisibility(boolean show){
//        if(!show){
//            loadingLayout.setVisibility(View.VISIBLE);
//            foldersRcv.setVisibility(View.GONE);
//        }else{
//            loadingLayout.setVisibility(View.GONE);
//            foldersRcv.setVisibility(View.VISIBLE);
//        }
//    }

    private void initUi(View view) {
//        noDataFoundLayout=view.findViewById(R.id.noDataFound);
//        loadingLayout=view.findViewById(R.id.loadingLayout);
//        toggleContentVisibility(false);
        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerViewId);



    }





    void initRecyclerView(){
        notificationAdapter = new LearnEraNotificationAdapter(getContext(),notificationDataModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        notificationRecyclerView.setLayoutManager(linearLayoutManager);
        notificationRecyclerView.setAdapter(notificationAdapter);
    }

    void getNotifications(){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.PLATFORM_NOTIFICATIONS_KEY)
                .get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String notificationId = documentSnapshot.getId();
                    String title = ""+ documentSnapshot.get(GlobalConfig.NOTIFICATION_TITLE_KEY);
                    String message = ""+ documentSnapshot.get(GlobalConfig.NOTIFICATION_MESSAGE_KEY);
                    ArrayList<String> notificationViewers =  documentSnapshot.get(GlobalConfig.NOTIFICATION_VIEWERS_LIST_KEY)!=null? (ArrayList<String>) documentSnapshot.get(GlobalConfig.NOTIFICATION_VIEWERS_LIST_KEY): new ArrayList<>();
                    String dateNotified =  documentSnapshot.get(GlobalConfig.DATE_NOTIFIED_TIME_STAMP_KEY)!=null? documentSnapshot.getTimestamp(GlobalConfig.DATE_NOTIFIED_TIME_STAMP_KEY).toDate()+"": "Undefined";
                    if(dateNotified.length()>10){
                        dateNotified = dateNotified.substring(0,10);
                    }
                    notificationDataModelArrayList.add(new LearnEraNotificationDataModel( notificationId, title, message, dateNotified,notificationViewers));
                    notificationAdapter.notifyItemChanged(notificationDataModelArrayList.size());

                }
            }
        });
    }


}