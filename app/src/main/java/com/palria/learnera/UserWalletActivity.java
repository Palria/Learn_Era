package com.palria.learnera;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.palria.learnera.models.QuizDataModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class UserWalletActivity extends AppCompatActivity {
    MaterialToolbar materialToolbar;
    Intent intent;
    TabLayout tabLayout ;

    boolean isQuizEarningsHistoryLinearLayoutOpen = false;
    boolean isWithdrawalsHistoryLinearLayoutOpen = false;
    boolean isReferralRewardLinearLayoutOpen = false;
    boolean isWithdrawalRequestLinearLayoutOpen = false;

    LinearLayout quizEarningsHistoryLinearLayout;
    LinearLayout withdrawalsHistoryLinearLayout;
    LinearLayout referralRewardLinearLayout;
    LinearLayout withdrawalRequestLayout;

    TextView availableCoinEquityTextView;
    TextView withdrawableCoinsTextView;
    TextView withdrawActionTextView;
    TextView getMoreCoinActionTextView;
int totalWithdrawableCoins = 0;

TextView bankNameTextView,accountNumberTextView,accountNameTextView,editAccountActionTextView,countryTextView;

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

                    case 3:
                        withdrawalRequestLayout.removeAllViews();
                        for(String desc: GlobalConfig.getWithdrawalRequestList()) {
                            displayHistory(withdrawalRequestLayout, desc);
                        }
                        setLayoutVisibility(withdrawalRequestLayout);
                        setWithdrawalRequestLinearLayoutOpen();
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
                //pop up form dialog

                showRequestConfirmationDialog();
            }
        });

        editAccountActionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to EditBankAccountDetailsActivity
                Intent intent = new Intent(UserWalletActivity.this, EditBankAccountDetailsActivity.class);

                startActivity(intent);
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
        withdrawalRequestLayout = findViewById(R.id.withdrawalRequestLayoutId);

        bankNameTextView = findViewById(R.id.bankNameTextViewId);
        accountNumberTextView = findViewById(R.id.accountNumberTextViewId);
        accountNameTextView = findViewById(R.id.accountNameTextViewId);
        editAccountActionTextView = findViewById(R.id.editAccountActionTextViewId);
        countryTextView = findViewById(R.id.countryNameTextViewId);

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

    private void showRequestConfirmationDialog(){

        Dialog confirmationDialog = new Dialog(this);


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View requestView = layoutInflater.inflate(R.layout.withdarawal_request_dialog_layout,withdrawalRequestLayout,false);
        TextView introTextView = requestView.findViewById(R.id.introId);
        TextView cancel = requestView.findViewById(R.id.cancelId);
        TextView confirm = requestView.findViewById(R.id.confirmId);
        TextView coinInput = requestView.findViewById(R.id.coinInputId);
        coinInput.setText(totalWithdrawableCoins+"");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog.cancel();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((coinInput.getText()+"").isEmpty()){
                    introTextView.setText("Enter valid number of coin. it must be equal to or less than your withdrawable coins");
                    introTextView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.error_red,getTheme()));

                    return;
                }
                int coins = Integer.parseInt(coinInput.getText()+"");

                if(coins<1 || coins>totalWithdrawableCoins){

//                    Toast.makeText(getApplicationContext(), "Enter valid number of coin. it must be equal to or less than your withdrawable coins", Toast.LENGTH_SHORT).show();
                    introTextView.setText("Enter valid number of coin. it must be equal to or less than your withdrawable coins");
                    introTextView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.error_red,getTheme()));
                    return;
                }else{
                    requestWithdrawal(coins);
                    confirmationDialog.cancel();
                }

            }
        });
        confirmationDialog.setContentView(requestView);
        confirmationDialog.setCancelable(true);

        confirmationDialog.create();
        confirmationDialog.show();

    }

    public void manageTabLayout(){
        TabLayout.Tab quizEarningsTabItem= tabLayout.newTab();
        quizEarningsTabItem.setText("Quiz Earnings");
        tabLayout.addTab(quizEarningsTabItem,0,true);


        TabLayout.Tab withdrawalsTabItem=tabLayout.newTab();
        withdrawalsTabItem.setText("Withdrawals");
        tabLayout.addTab(withdrawalsTabItem,1);


        TabLayout.Tab referalRewardTabItem = tabLayout.newTab();
        referalRewardTabItem.setText("Referral Rewards");
        tabLayout.addTab(referalRewardTabItem,2);


        TabLayout.Tab withdrawalRequestTabItem = tabLayout.newTab();
        withdrawalRequestTabItem.setText("Withdrawal Requests");
        tabLayout.addTab(withdrawalRequestTabItem,3);


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
            long withdrawableCoins =  documentSnapshot.get(GlobalConfig.WITHDRAWABLE_COIN_BALANCE_KEY)!=null?  documentSnapshot.getLong(GlobalConfig.WITHDRAWABLE_COIN_BALANCE_KEY):0L;
            String totalCoinsEarned = ""+ documentSnapshot.get(GlobalConfig.TOTAL_COINS_EARNED_KEY);
            String totalCoinEquity = ""+ documentSnapshot.get(GlobalConfig.TOTAL_COIN_EQUITY_KEY);

            String bankName = ""+ documentSnapshot.get(GlobalConfig.BANK_NAME_KEY);
            String accountName = ""+ documentSnapshot.get(GlobalConfig.ACCOUNT_NAME_KEY);
            String accountNumber = ""+ documentSnapshot.get(GlobalConfig.ACCOUNT_NUMBER_KEY);
            String country = ""+ documentSnapshot.get(GlobalConfig.COUNTRY_KEY);

            availableCoinEquityTextView.setText(totalCoinEquity+" Coins");
            withdrawableCoinsTextView.setText(withdrawableCoins+" Coins");
            totalWithdrawableCoins =(int) withdrawableCoins;

            bankNameTextView.setText("Bank Name : " +bankName);
            accountNumberTextView.setText("Account Number : " +accountNumber);
            accountNameTextView.setText("Account Name : " +accountName);
            countryTextView.setText("Country : " +country);


            ArrayList<String> quizEarningsHistoryList =  documentSnapshot.get(GlobalConfig.QUIZ_EARNINGS_HISTORY_LIST_KEY)!=null? (ArrayList<String>) documentSnapshot.get(GlobalConfig.QUIZ_EARNINGS_HISTORY_LIST_KEY):new ArrayList<>();
            for(String history: quizEarningsHistoryList){
                displayHistory(quizEarningsHistoryLinearLayout,history);
            }

            ArrayList<String> coinWithdrawalHistoryList =  documentSnapshot.get(GlobalConfig.COIN_WITHDRAWAL_HISTORY_LIST_KEY)!=null? (ArrayList<String>) documentSnapshot.get(GlobalConfig.COIN_WITHDRAWAL_HISTORY_LIST_KEY):new ArrayList<>();
            for(String history: coinWithdrawalHistoryList){
                displayHistory(withdrawalsHistoryLinearLayout,history);
            }

            ArrayList<String> referralRewardHistoryList =  documentSnapshot.get(GlobalConfig.REFERAL_REWARD_HISTORY_LIST_KEY)!=null? (ArrayList<String>) documentSnapshot.get(GlobalConfig.REFERAL_REWARD_HISTORY_LIST_KEY):new ArrayList<>();
            for(String history: referralRewardHistoryList){
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
    private void requestWithdrawal(int coinsToWithdraw) {
        if(coinsToWithdraw<1)return;

        Toast.makeText(getApplicationContext(), "Withdrawal request in process...", Toast.LENGTH_SHORT).show();
        withdrawActionTextView.setText("Requesting...");

        String withdrawalId = GlobalConfig.getRandomString(60);
        String requestInfo = coinsToWithdraw+"-ID-"+withdrawalId+"-DATE-"+GlobalConfig.getDate();

        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
        DocumentReference walletReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.USER_WALLET_KEY).document(GlobalConfig.USER_WALLET_KEY);
        HashMap<String,Object> walletDetails = new HashMap<>();
        walletDetails.put(GlobalConfig.WITHDRAWABLE_COIN_BALANCE_KEY,FieldValue.increment(-coinsToWithdraw));
        walletDetails.put(GlobalConfig.COIN_WITHDRAWAL_HISTORY_LIST_KEY, FieldValue.arrayUnion("COIN-"+coinsToWithdraw+"-DESC-"+"You withdrew "+coinsToWithdraw +" coins "+"-DATE-"+GlobalConfig.getDate()));

        writeBatch.set(walletReference,walletDetails, SetOptions.merge());

        DocumentReference userReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId());
        HashMap<String,Object>userDetails = new HashMap<>();
        userDetails.put(GlobalConfig.IS_WITHDRAWAL_REQUESTED_KEY,true);
        userDetails.put(GlobalConfig.WITHDRAWAL_REQUESTS_LIST_KEY,FieldValue.arrayUnion(requestInfo));
        writeBatch.set(userReference,userDetails, SetOptions.merge());


        writeBatch.commit()
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Withdrawal failed please try again later", Toast.LENGTH_SHORT).show();
                withdrawActionTextView.setText("Retry");

            }
        })
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Withdrawal requested", Toast.LENGTH_SHORT).show();
                withdrawActionTextView.setText("Request withdrawal");
                withdrawableCoinsTextView.setText(totalWithdrawableCoins-coinsToWithdraw+" Coins");


                String notificationId = GlobalConfig.getRandomString(100);
                //carries the info about the withdrawal
                ArrayList<String> modelInfo = new ArrayList<>();
                modelInfo.add(requestInfo);

                //carries the receipient id
                ArrayList<String> recipientId = new ArrayList<>();
                recipientId.add(GlobalConfig.getLearnEraId());
                //fires the notification
                GlobalConfig.sendNotificationToUsers(GlobalConfig.NOTIFICATION_TYPE_WITHDRAWAL_REQUEST_KEY,notificationId,recipientId,modelInfo,"New Withdrawal Request",requestInfo,null);

            }
        });

    }


    void setQuizEarningsHistoryLinearLayoutOpen(){
        isWithdrawalsHistoryLinearLayoutOpen = false;
        isReferralRewardLinearLayoutOpen = false;
        isWithdrawalRequestLinearLayoutOpen = false;

        isQuizEarningsHistoryLinearLayoutOpen = true;
    }
    void setWithdrawalsHistoryLinearLayoutOpen(){
        isQuizEarningsHistoryLinearLayoutOpen = false;
        isReferralRewardLinearLayoutOpen = false;
        isWithdrawalRequestLinearLayoutOpen = false;

        isWithdrawalsHistoryLinearLayoutOpen = true;
    }
    void setReferralRewardLinearLayoutOpen(){
        isQuizEarningsHistoryLinearLayoutOpen = false;
        isWithdrawalsHistoryLinearLayoutOpen = false;
        isWithdrawalRequestLinearLayoutOpen = false;

        isReferralRewardLinearLayoutOpen = true;
    }

    void setWithdrawalRequestLinearLayoutOpen(){
        isQuizEarningsHistoryLinearLayoutOpen = false;
        isWithdrawalsHistoryLinearLayoutOpen = false;
        isReferralRewardLinearLayoutOpen = false;

        isWithdrawalRequestLinearLayoutOpen = true;
    }

    void setLayoutVisibility(View viewToReveal){
        quizEarningsHistoryLinearLayout.setVisibility(View.GONE);
        withdrawalsHistoryLinearLayout.setVisibility(View.GONE);
        referralRewardLinearLayout.setVisibility(View.GONE);
        withdrawalRequestLayout.setVisibility(View.GONE);

        viewToReveal.setVisibility(View.VISIBLE);
    }

}