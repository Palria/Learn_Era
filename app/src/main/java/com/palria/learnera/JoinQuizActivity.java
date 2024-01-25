package com.palria.learnera;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.models.QuizDataModel;

import java.util.ArrayList;

public class JoinQuizActivity extends AppCompatActivity {
    Intent intent;
    MaterialButton joinQuizActionTextView;
    TextInputEditText quizTitleTextView;
    TextView quizDescriptionTextView;
    TextView regFeeTextView;
    TextView rewardTextView;
    QuizDataModel quizDataModel;
    MaterialToolbar materialToolbar;
    TextView startTimeTextView;
    TextView endTimeTextView;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_quiz);
        initUI();
        fetchIntentData();
        if(quizDataModel.getParticipantsList().contains(GlobalConfig.getCurrentUserId()) || GlobalConfig.newlyJoinedQuizList.contains(quizDataModel.getQuizId())){

            Toast.makeText(getApplicationContext(), "You have already joined quiz. EXIT", Toast.LENGTH_SHORT).show();
            JoinQuizActivity.super.onBackPressed();

        }
        if(GlobalConfig.getCurrentUserId().equals(quizDataModel.getAuthorId())){

            Toast.makeText(getApplicationContext(), "You can't join a quiz you created. EXIT ", Toast.LENGTH_SHORT).show();
            JoinQuizActivity.super.onBackPressed();

        }
        joinQuizActionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQuizJoinConfirmationDialog();
            }
        });
renderQuizDetails();
    }

    private void initUI(){
        materialToolbar=findViewById(R.id.topBar);
        joinQuizActionTextView=findViewById(R.id.joinQuizActionTextViewId);
        quizTitleTextView=findViewById(R.id.quizTitlePreviewId);
        quizDescriptionTextView=findViewById(R.id.quizDescriptionTextViewId);
        regFeeTextView=findViewById(R.id.regFeeValueTextViewId);
        rewardTextView=findViewById(R.id.rewardValueTextViewId);
        quizDescriptionTextView=findViewById(R.id.quizDescriptionTextViewId);

        startTimeTextView=findViewById(R.id.startTimeTextViewId);
        endTimeTextView=findViewById(R.id.endTimeTextViewId);

        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init progress.
        alertDialog = new AlertDialog.Builder(JoinQuizActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();

    }



    void fetchIntentData(){
         intent = getIntent();
        quizDataModel = (QuizDataModel) intent.getSerializableExtra(GlobalConfig.QUIZ_DATA_MODEL_KEY);
    }


    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }
    void processAndJoinQuiz(){
        toggleProgress(true);

//check if the user has coin equity balance up to the quiz fee
            GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(GlobalConfig.getCurrentUserId())
                    .collection(GlobalConfig.USER_WALLET_KEY)
                    .document(GlobalConfig.USER_WALLET_KEY)
                    .get()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toggleProgress(false);
GlobalConfig.createSnackBar2(JoinQuizActivity.this, joinQuizActionTextView,"Error checking your wallet, please try again" , "Retry", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        processAndJoinQuiz();
    }
});
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            long totalCoinEquity =  documentSnapshot.get(GlobalConfig.TOTAL_COIN_EQUITY_KEY)!=null && documentSnapshot.get(GlobalConfig.TOTAL_COIN_EQUITY_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_COIN_EQUITY_KEY):0L;

                            if(totalCoinEquity>=quizDataModel.getTotalQuizFeeCoins()){
                               //eligible
                                GlobalConfig.joinQuiz(JoinQuizActivity.this, quizDataModel, new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {
                                        //navigate to QuizActivity
                                        Intent intent = new Intent(JoinQuizActivity.this, QuizActivity.class);
                                        intent.putExtra(GlobalConfig.QUIZ_DATA_MODEL_KEY,quizDataModel);
                                        intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,quizDataModel.getAuthorId());
                                        intent.putExtra(GlobalConfig.QUIZ_ID_KEY,quizDataModel.getQuizId());
                                        intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,true);
                                        startActivity(intent);

                                        Toast.makeText(getApplicationContext(), "You successfully joined quiz ", Toast.LENGTH_LONG).show();
                                        toggleProgress(false);
                                        JoinQuizActivity.super.onBackPressed();

                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
                                        toggleProgress(false);
                                        GlobalConfig.createSnackBar2(JoinQuizActivity.this, joinQuizActionTextView,"Error joining quiz, please try again" , "Retry", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                processAndJoinQuiz();
                                            }
                                        });
                                    }
                                });
                            }else{
                                //ineligible . let user know to take action
                                toggleProgress(false);
                                GlobalConfig.createSnackBar2(JoinQuizActivity.this, joinQuizActionTextView,"Error: You have less or no coin equity hence ineligible to join quiz. Click earn coin to earn coins , please try again" , "Earn coin", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //navigate to EarnCoinActivity
                                        Intent intent = new Intent(JoinQuizActivity.this, EarnCoinActivity.class);
                                        intent.putExtra(GlobalConfig.IS_LOAD_IMMEDIATELY_KEY,false);

                                        startActivity(intent);

                                        JoinQuizActivity.super.onBackPressed();

                                    }
                                });
                            }


                        }
                    });


    }

    private void showQuizJoinConfirmationDialog(){

        AlertDialog confirmationDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you ready to join?");
        String message = "You are about to join, please click confirm button once you are ready to join";

        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setIcon(R.drawable.ic_baseline_error_outline_24);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                processAndJoinQuiz();

            }
        })
                .setNegativeButton("Not yet", null);
        confirmationDialog = builder.create();
        confirmationDialog.show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void renderQuizDetails(){
        if(quizDataModel!=null){
            quizTitleTextView.setText(quizDataModel.getQuizTitle());
            quizDescriptionTextView.setText(quizDataModel.getQuizDescription());
            regFeeTextView.setText(quizDataModel.getTotalQuizFeeCoins()+" COINS");
            rewardTextView.setText(quizDataModel.getTotalQuizRewardCoins()+" COINS");


            ArrayList<Long> startTime = quizDataModel.getStartDateList();
            if(startTime.size()>=5) {
                startTimeTextView.setText(startTime.get(2) + "/" + startTime.get(1) + "/" + startTime.get(0) + " :" + startTime.get(3) + ":" + startTime.get(4));
            }
            ArrayList<Long> endTime = quizDataModel.getStartDateList();
            if(endTime.size()>=5) {
                endTimeTextView.setText(endTime.get(2) + "/" + endTime.get(1) + "/" + endTime.get(0) + " :" + endTime.get(3) + ":" + endTime.get(4));
            }

        }
    }

}