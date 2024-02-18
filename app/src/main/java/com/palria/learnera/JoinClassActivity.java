package com.palria.learnera;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.models.ClassDataModel;

import java.util.ArrayList;

public class JoinClassActivity extends AppCompatActivity {
    Intent intent;
    MaterialButton joinClassActionTextView;
    TextInputEditText classTitleTextView;
    TextView classDescriptionTextView;
    TextView regFeeTextView;
    TextView rewardTextView;
    ClassDataModel classDataModel;
    MaterialToolbar materialToolbar;
    TextView startTimeTextView;
    TextView endTimeTextView;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);
        initUI();
        fetchIntentData();
        if(classDataModel.getStudentsList().contains(GlobalConfig.getCurrentUserId()) || GlobalConfig.newlyJoinedClassList.contains(classDataModel.getClassId())){

            Toast.makeText(getApplicationContext(), "You have already joined class. EXIT", Toast.LENGTH_SHORT).show();
            JoinClassActivity.super.onBackPressed();

        }
        if(GlobalConfig.getCurrentUserId().equals(classDataModel.getAuthorId())){

            Toast.makeText(getApplicationContext(), "You can't join a class you created. EXIT ", Toast.LENGTH_SHORT).show();
            JoinClassActivity.super.onBackPressed();

        }
        joinClassActionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClassJoinConfirmationDialog();
            }
        });
renderClassDetails();
    }

    private void initUI(){
        materialToolbar=findViewById(R.id.topBar);
        joinClassActionTextView=findViewById(R.id.joinClassActionTextViewId);
        classTitleTextView=findViewById(R.id.classTitlePreviewId);
        classDescriptionTextView=findViewById(R.id.classDescriptionTextViewId);
        regFeeTextView=findViewById(R.id.regFeeValueTextViewId);
        rewardTextView=findViewById(R.id.rewardValueTextViewId);
        classDescriptionTextView=findViewById(R.id.classDescriptionTextViewId);

        startTimeTextView=findViewById(R.id.startTimeTextViewId);
        endTimeTextView=findViewById(R.id.endTimeTextViewId);

        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init progress.
        alertDialog = new AlertDialog.Builder(JoinClassActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();

    }



    void fetchIntentData(){
         intent = getIntent();
        classDataModel = (ClassDataModel) intent.getSerializableExtra(GlobalConfig.CLASS_DATA_MODEL_KEY);
    }


    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }
    void processAndJoinClass(){
        toggleProgress(true);

//check if the user has coin equity balance up to the class fee
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
GlobalConfig.createSnackBar2(JoinClassActivity.this, joinClassActionTextView,"Error checking your wallet, please try again" , "Retry", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        processAndJoinClass();
    }
});
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            long totalCoinEquity =  documentSnapshot.get(GlobalConfig.TOTAL_COIN_EQUITY_KEY)!=null && documentSnapshot.get(GlobalConfig.TOTAL_COIN_EQUITY_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_COIN_EQUITY_KEY):0L;

                            if(totalCoinEquity>=classDataModel.getTotalClassFeeCoins()){
                               //eligible
                                GlobalConfig.joinClass(JoinClassActivity.this, classDataModel, new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {
                                        //navigate to ClassActivity
                                        Intent intent = new Intent(JoinClassActivity.this, ClassActivity.class);
                                        intent.putExtra(GlobalConfig.CLASS_DATA_MODEL_KEY,classDataModel);
                                        intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,classDataModel.getAuthorId());
                                        intent.putExtra(GlobalConfig.CLASS_ID_KEY,classDataModel.getClassId());
                                        intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,true);
                                        startActivity(intent);

                                        Toast.makeText(getApplicationContext(), "You successfully joined class ", Toast.LENGTH_LONG).show();
                                        toggleProgress(false);
                                        JoinClassActivity.super.onBackPressed();

                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
                                        toggleProgress(false);
                                        GlobalConfig.createSnackBar2(JoinClassActivity.this, joinClassActionTextView,"Error joining class, please try again" , "Retry", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                processAndJoinClass();
                                            }
                                        });
                                    }
                                });
                            }else{
                                //ineligible . let user know to take action
                                toggleProgress(false);
                                GlobalConfig.createSnackBar2(JoinClassActivity.this, joinClassActionTextView,"Error: You have less or no coin equity hence ineligible to join class. Click earn coin to earn coins , please try again" , "Earn coin", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //navigate to EarnCoinActivity
                                        Intent intent = new Intent(JoinClassActivity.this, EarnCoinActivity.class);
                                        intent.putExtra(GlobalConfig.IS_LOAD_IMMEDIATELY_KEY,false);

                                        startActivity(intent);

                                        JoinClassActivity.super.onBackPressed();

                                    }
                                });
                            }


                        }
                    });


    }

    private void showClassJoinConfirmationDialog(){

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
                processAndJoinClass();

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

    void renderClassDetails(){
        if(classDataModel!=null){
            classTitleTextView.setText(classDataModel.getClassTitle());
            classDescriptionTextView.setText(classDataModel.getClassDescription());
            regFeeTextView.setText(classDataModel.getTotalClassFeeCoins()+" COINS");


            ArrayList<Long> startTime = classDataModel.getStartDateList();
            if(startTime.size()>=5) {
                startTimeTextView.setText(startTime.get(2) + "/" + startTime.get(1) + "/" + startTime.get(0) + " :" + startTime.get(3) + ":" + startTime.get(4));
            }
            ArrayList<Long> endTime = classDataModel.getStartDateList();
            if(endTime.size()>=5) {
                endTimeTextView.setText(endTime.get(2) + "/" + endTime.get(1) + "/" + endTime.get(0) + " :" + endTime.get(3) + ":" + endTime.get(4));
            }

        }
    }

}