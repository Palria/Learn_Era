package com.palria.learnera;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.models.QuizDataModel;

import java.util.ArrayList;

public class UserWalletActivity extends AppCompatActivity {
    MaterialToolbar materialToolbar;
    Intent intent;
    TabLayout tabLayout ;

    boolean isQuizEarningsHistoryLinearLayoutOpen = false;
    boolean isWithdrawalsHistoryLinearLayoutOpen = false;
    boolean isReferralRewardLinearLayoutOpen = false;

    LinearLayout quizEarningsHistoryLinearLayout;
    LinearLayout withdrawalsHistoryLinearLayout;
    LinearLayout referralRewardLinearLayout;

    TextView availableCoinEquityTextView;
    TextView withdrawableCoinsTextView;
    TextView withdrawActionTextView;
    TextView getMoreCoinActionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wallet);


        initUI();
        fetchIntentData();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){

                    case 0:
                        setLayoutVisibility(quizEarningsHistoryLinearLayout);
                        setQuizEarningsHistoryLinearLayoutOpen();
                        break;

                    case 1:
                        setLayoutVisibility(withdrawalsHistoryLinearLayout);
                        setWithdrawalsHistoryLinearLayoutOpen();
                        break;

                    case 2:
                        setLayoutVisibility(referralRewardLinearLayout);
                        setReferralRewardLinearLayoutOpen();
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        manageTabLayout();
getWalletInfo();
        withdrawActionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestWithdrawal();

            }
        });
        getMoreCoinActionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to EarnCoinActivity
                Intent intent = new Intent(UserWalletActivity.this, EarnCoinActivity.class);
                intent.putExtra(GlobalConfig.IS_LOAD_IMMEDIATELY_KEY,false);

                startActivity(intent);


            }
        });
    }


    private void initUI(){
        materialToolbar=findViewById(R.id.topBar);
        tabLayout = findViewById(R.id.tabLayoutId);
        availableCoinEquityTextView = findViewById(R.id.availableCoinEquityTextViewId);
        withdrawableCoinsTextView = findViewById(R.id.withdrawableCoinsTextViewId);
        withdrawActionTextView = findViewById(R.id.withdrawActionTextViewId);
        getMoreCoinActionTextView = findViewById(R.id.getMoreCoinActionTextViewId);

        quizEarningsHistoryLinearLayout = findViewById(R.id.quizEarningsHistoryLinearLayoutId);
        withdrawalsHistoryLinearLayout = findViewById(R.id.withdrawalsHistoryLinearLayoutId);
        referralRewardLinearLayout = findViewById(R.id.referralRewardLinearLayoutId);


        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    void fetchIntentData(){
        intent = getIntent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void manageTabLayout(){
        TabLayout.Tab quizEarningsTabItem= tabLayout.newTab();
        quizEarningsTabItem.setText("Quiz Earnings");
        tabLayout.addTab(quizEarningsTabItem,0,true);


        TabLayout.Tab withdrawalsTabItem=tabLayout.newTab();
        withdrawalsTabItem.setText("Withdrawals");
        tabLayout.addTab(withdrawalsTabItem,1);


        TabLayout.Tab referalRewardTabItem = tabLayout.newTab();
        referalRewardTabItem.setText("Referal Rewards");
        tabLayout.addTab(referalRewardTabItem,2);


    }

void getWalletInfo(){
    GlobalConfig.getFirebaseFirestoreInstance()
            .collection(GlobalConfig.ALL_USERS_KEY)
            .document(GlobalConfig.getCurrentUserId())
            .collection(GlobalConfig.USER_WALLET_KEY)
            .document(GlobalConfig.USER_WALLET_KEY)
    .get()
    .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

        }
    })
    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
//            walletDetails.put(GlobalConfig.QUIZ_EARNINGS_HISTORY_LIST_KEY,new ArrayList<>());
//            walletDetails.put(GlobalConfig.TOTAL_QUIZ_REWARD_COINS_EARNED_KEY,0L);
//            walletDetails.put(GlobalConfig.COIN_WITHDRAWAL_HISTORY_LIST_KEY,new ArrayList<>());
//            walletDetails.put(GlobalConfig.REFERAL_REWARD_HISTORY_LIST_KEY,new ArrayList<>());

            String withdrawableCoins = ""+ documentSnapshot.get(GlobalConfig.WITHDRAWABLE_COIN_BALANCE_KEY);
            String totalCoinsEarned = ""+ documentSnapshot.get(GlobalConfig.TOTAL_COINS_EARNED_KEY);
            String totalCoinEquity = ""+ documentSnapshot.get(GlobalConfig.TOTAL_COIN_EQUITY_KEY);

            availableCoinEquityTextView.setText(totalCoinEquity+" COINS");
            withdrawableCoinsTextView.setText(withdrawableCoins+" COINS");

            ArrayList<String> quizEarningsHistoryList =  documentSnapshot.get(GlobalConfig.QUIZ_EARNINGS_HISTORY_LIST_KEY)!=null? (ArrayList<String>) documentSnapshot.get(GlobalConfig.QUIZ_EARNINGS_HISTORY_LIST_KEY):new ArrayList<>();
            for(String history: quizEarningsHistoryList){
                displayHistory(quizEarningsHistoryLinearLayout,history);
            }

            ArrayList<String> coinWithdrawalHistoryList =  documentSnapshot.get(GlobalConfig.COIN_WITHDRAWAL_HISTORY_LIST_KEY)!=null? (ArrayList<String>) documentSnapshot.get(GlobalConfig.COIN_WITHDRAWAL_HISTORY_LIST_KEY):new ArrayList<>();
            for(String history: coinWithdrawalHistoryList){
                displayHistory(withdrawalsHistoryLinearLayout,history);
            }

            ArrayList<String> referalRewardHistoryList =  documentSnapshot.get(GlobalConfig.REFERAL_REWARD_HISTORY_LIST_KEY)!=null? (ArrayList<String>) documentSnapshot.get(GlobalConfig.REFERAL_REWARD_HISTORY_LIST_KEY):new ArrayList<>();
            for(String history: referalRewardHistoryList){
                displayHistory(referralRewardLinearLayout,history);
            }

        }
    });


}

void displayHistory(LinearLayout historyLinearLayout, String description){
    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    View historyView = layoutInflater.inflate(R.layout.history_item_layout,historyLinearLayout, false);
    TextView desc = historyView.findViewById(R.id.descriptionTextViewId);
    desc.setText(description);

    historyLinearLayout.addView(historyView);
            }
    private void requestWithdrawal() {
        Toast.makeText(getApplicationContext(), "Withdrawal in process...", Toast.LENGTH_SHORT).show();
        withdrawActionTextView.setText("Requesting...");
    }


    void setQuizEarningsHistoryLinearLayoutOpen(){
        isWithdrawalsHistoryLinearLayoutOpen = false;
        isReferralRewardLinearLayoutOpen = false;

        isQuizEarningsHistoryLinearLayoutOpen = true;
    }
    void setWithdrawalsHistoryLinearLayoutOpen(){
        isQuizEarningsHistoryLinearLayoutOpen = false;
        isReferralRewardLinearLayoutOpen = false;

        isWithdrawalsHistoryLinearLayoutOpen = true;
    }
    void setReferralRewardLinearLayoutOpen(){
        isQuizEarningsHistoryLinearLayoutOpen = false;
        isWithdrawalsHistoryLinearLayoutOpen = false;

        isReferralRewardLinearLayoutOpen = true;
    }

    void setLayoutVisibility(View viewToReveal){
        quizEarningsHistoryLinearLayout.setVisibility(View.GONE);
        withdrawalsHistoryLinearLayout.setVisibility(View.GONE);
        referralRewardLinearLayout.setVisibility(View.GONE);

        viewToReveal.setVisibility(View.VISIBLE);
    }
}