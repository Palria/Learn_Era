package com.palria.learnera;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;

public class EarnCoinActivity extends AppCompatActivity {
    MaterialToolbar materialToolbar;
    MaterialButton watchAndEarnActionTextView;
    boolean isAdsInitialized = false;
    boolean isVideoAdsLoaded = false;
    RewardedAd rewardedAd;
    FullScreenContentCallback fullScreenContentCallback;
    OnUserEarnedRewardListener onUserEarnedRewardListener;
//    TextView numberOfCoinsTextView;
    TextView statusTextView;
    boolean isLoadImmediately = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn_coin);
        initUI();
        fetchIntentData();
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
try {
    configureAds();
}catch(Exception e){
    statusTextView.setText("Error occurred. Please exit and open screen again");
}

if(isLoadImmediately){
    statusTextView.setText("Loading...");

}
       }


    void initUI(){
        materialToolbar=findViewById(R.id.topBar);
        watchAndEarnActionTextView=findViewById(R.id.watchAndEarnActionTextViewId);
//        numberOfCoinsTextView=findViewById(R.id.numberOfCoinsTextViewId);
        statusTextView=findViewById(R.id.statusTextViewId);

    }



    void fetchIntentData(){
       Intent intent = getIntent();
       isLoadImmediately = intent.getBooleanExtra(GlobalConfig.IS_LOAD_IMMEDIATELY_KEY,false);
    }



    void configureAds(){
        fullScreenContentCallback = new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
            }
        };
        onUserEarnedRewardListener = new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                Toast.makeText(getApplicationContext(), "Reward earned: "+rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
                statusTextView.setText("1-Coin earned;");
                watchAndEarnActionTextView.setText("Earn more coin");
                //Deposit the coins to his wallet
                depositCoin();
                //Let new activity be opened to avoid crashing due to some unfavourable configurations in admob ads
                watchAndEarnActionTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EarnCoinActivity.super.onBackPressed();

                        //navigate to QuizActivity
                        Intent intent = new Intent(EarnCoinActivity.this, EarnCoinActivity.class);
                        intent.putExtra(GlobalConfig.IS_LOAD_IMMEDIATELY_KEY,true);
                        startActivity(intent);

                    }
                });
            }
        };

        watchAndEarnActionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusTextView.setText("Loading..." );

                if(isVideoAdsLoaded){
                    GlobalConfig.showVideoAd(EarnCoinActivity.this,EarnCoinActivity.this, rewardedAd, fullScreenContentCallback,onUserEarnedRewardListener );
                }else{
                    GlobalConfig.loadVideoAd(EarnCoinActivity.this, "ca-app-pub-3940256099942544/5224354917", new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            isVideoAdsLoaded = false;
                            GlobalConfig.loadVideoAd(EarnCoinActivity.this, "ca-app-pub-3940256099942544/5224354917", this);
                            statusTextView.setText("Error occurred. failed to load ads, please try again" );

                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            super.onAdLoaded(rewardedAd);
                            isVideoAdsLoaded = true;
                            GlobalConfig.showVideoAd(EarnCoinActivity.this,EarnCoinActivity.this, rewardedAd, fullScreenContentCallback,onUserEarnedRewardListener );

                        }
                    });
                }
            }
        });
        MobileAds.initialize(EarnCoinActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                isAdsInitialized = true;
                Toast.makeText(getApplicationContext(), "initialized", Toast.LENGTH_SHORT).show();
                GlobalConfig.loadVideoAd(EarnCoinActivity.this, "ca-app-pub-3940256099942544/5224354917", new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        isVideoAdsLoaded = false;
                        GlobalConfig.loadVideoAd(EarnCoinActivity.this, "ca-app-pub-3940256099942544/5224354917", this);

                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        super.onAdLoaded(rewardedAd);
                        isVideoAdsLoaded = true;
                        if(isLoadImmediately){
                            GlobalConfig.showVideoAd(EarnCoinActivity.this,EarnCoinActivity.this, rewardedAd, fullScreenContentCallback,onUserEarnedRewardListener );

                        }

                        }
                });
            }
        });

    }
    void depositCoin(){

        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
        DocumentReference walletReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.USER_WALLET_KEY).document(GlobalConfig.USER_WALLET_KEY);
        HashMap<String, Object> walletDetails = new HashMap<>();
        walletDetails.put(GlobalConfig.TOTAL_COINS_EARNED_KEY,FieldValue.increment(1L));
        walletDetails.put(GlobalConfig.TOTAL_COIN_EQUITY_KEY,FieldValue.increment(1L));
        walletDetails.put(GlobalConfig.QUIZ_EARNINGS_HISTORY_LIST_KEY,FieldValue.arrayUnion("COIN-"+1+"-DESC-"+"You earned 1 coin for watching ads"+"-DATE-"+GlobalConfig.getDate()));

        writeBatch.set(walletReference, walletDetails, SetOptions.merge());


        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       depositCoin();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }

}