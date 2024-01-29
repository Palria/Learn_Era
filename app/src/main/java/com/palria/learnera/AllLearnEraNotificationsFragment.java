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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.palria.learnera.adapters.FolderRcvAdapter;
import com.palria.learnera.adapters.LearnEraNotificationAdapter;
import com.palria.learnera.adapters.LearnEraNotificationAdapter;
import com.palria.learnera.models.FolderDataModel;
import com.palria.learnera.models.LearnEraNotificationDataModel;

import java.util.ArrayList;
import java.util.HashMap;


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
                WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
                DocumentReference userReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId());
                HashMap<String,Object> userInfo = new HashMap<>();
                userInfo.put(GlobalConfig.DATE_PLATFORM_NOTIFICATION_LAST_SEEN_TIME_STAMP_KEY, FieldValue.serverTimestamp());
                userInfo.put(GlobalConfig.THERE_IS_NEW_PLATFORM_NOTIFICATION_KEY,false);
                writeBatch.set(userReference,userInfo, SetOptions.merge());
                writeBatch.commit();

            }
        });
    }


}