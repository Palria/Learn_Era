package com.palria.learnera;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.LearnEraNotificationAdapter;
import com.palria.learnera.adapters.LearnEraNotificationAdapter;
import com.palria.learnera.models.LearnEraNotificationDataModel;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView notificationRecyclerView;
    LearnEraNotificationAdapter notificationAdapter;
    ArrayList<LearnEraNotificationDataModel> notificationDataModelArrayList = new ArrayList<>();
    ImageButton backButton;
    TabLayout tabLayout;
    FrameLayout personalisedViewerFrameLayout;
    FrameLayout learneraNotesFrameLayout;


    //boolean fragment open stats
    boolean ispersonalisedViewerFrameLayoutOpened=false;
    boolean islearneraNotesFrameLayoutOpened=false;
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
        createTabLayout();

    }

    void initUI(){
        notificationRecyclerView = findViewById(R.id.notificationRecyclerViewId);
        backButton = findViewById(R.id.backButton);
        tabLayout = findViewById(R.id.tabLayoutId);
        personalisedViewerFrameLayout = findViewById(R.id.personalisedViewerFrameLayoutId);
        learneraNotesFrameLayout = findViewById(R.id.learneraNotesFrameLayoutId);

    }

    public void createTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabTitle=tab.getText().toString().trim().toUpperCase();
                if(tabTitle.equalsIgnoreCase("PERSONALIZED")) {
                    if(ispersonalisedViewerFrameLayoutOpened){
                        //Just set the frame layout visibility
                        setFrameLayoutVisibility(personalisedViewerFrameLayout);
                    }else {
                        ispersonalisedViewerFrameLayoutOpened =true;
                        setFrameLayoutVisibility(personalisedViewerFrameLayout);
                        AllPersonalizedNotificationsFragment allPersonalizedNotificationsFragment = new AllPersonalizedNotificationsFragment();
                        Bundle bundle = new Bundle();
                        allPersonalizedNotificationsFragment.setArguments(bundle);
                        initFragment(allPersonalizedNotificationsFragment, personalisedViewerFrameLayout);
                    }
                }
                else if(tabTitle.equalsIgnoreCase("LEARN ERA")){
                    if(islearneraNotesFrameLayoutOpened){
                        //Just set the frame layout visibility
                        setFrameLayoutVisibility(learneraNotesFrameLayout);
                    }else {
                        islearneraNotesFrameLayoutOpened =true;
                        setFrameLayoutVisibility(learneraNotesFrameLayout);

                        AllLearnEraNotificationsFragment allLearnEraNotificationsFragment = new AllLearnEraNotificationsFragment();
                        Bundle bundle = new Bundle();
                        allLearnEraNotificationsFragment.setArguments(bundle);
                        initFragment(allLearnEraNotificationsFragment, learneraNotesFrameLayout);
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



        TabLayout.Tab personalisedTabItem = tabLayout.newTab();
        personalisedTabItem.setText("Personalized");
        tabLayout.addTab(personalisedTabItem, 0,true);

        TabLayout.Tab learnEraTabItem = tabLayout.newTab();
        learnEraTabItem.setText("Learn Era");
        tabLayout.addTab(learnEraTabItem, 1);

    }


    private void initFragment(Fragment fragment, FrameLayout frameLayout){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), fragment)
                .commit();


    }

    private void setFrameLayoutVisibility(FrameLayout frameLayoutToSetVisible){
        personalisedViewerFrameLayout.setVisibility(View.GONE);
        learneraNotesFrameLayout.setVisibility(View.GONE);
        frameLayoutToSetVisible.setVisibility(View.VISIBLE);
    }

}