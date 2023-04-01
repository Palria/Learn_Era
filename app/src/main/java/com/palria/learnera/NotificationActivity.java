package com.palria.learnera;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.NotificationAdapter;
import com.palria.learnera.models.NotificationDataModel;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView notificationRecyclerView;
    NotificationAdapter notificationAdapter;
    ArrayList<NotificationDataModel> notificationDataModelArrayList = new ArrayList<>();
    ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initUI();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationActivity.super.onBackPressed();
            }
        });

        initRecyclerView();
        getNotifications();
    }

    void initUI(){
        notificationRecyclerView = findViewById(R.id.notificationRecyclerViewId);
        backButton = findViewById(R.id.backButton);

    }

    void initRecyclerView(){
        notificationAdapter = new NotificationAdapter(this,notificationDataModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
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
                    notificationDataModelArrayList.add(new NotificationDataModel( notificationId, title, message, dateNotified,notificationViewers));
                    notificationAdapter.notifyItemChanged(notificationDataModelArrayList.size());

                }
            }
        });
    }
}