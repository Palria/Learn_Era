package com.palria.learnera;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;

public class EditBankAccountDetailsActivity extends AppCompatActivity {

    Spinner bankSpinner;
    String selectedBank = "NONE";
    TextView accountNameEditText;
    TextView accountNumberEditText;
    Spinner countrySpinner;
    String selectedCountry = "Nigeria";
    AlertDialog alertDialog;
    Button saveActionButton;
    ArrayList<String> banks = new ArrayList<>();
    ArrayList<String> countryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bank_account_details);
        initUI();
        prepareBankSpinner();
        prepareCountrySpinner();
        getAccountInfo();
        saveActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccountDetails();
            }
        });
    }

void initUI(){
    bankSpinner = findViewById(R.id.bankSpinnerId);
    countrySpinner = findViewById(R.id.countrySpinnerId);
    accountNameEditText = findViewById(R.id.accountNameEditTextId);
    accountNumberEditText = findViewById(R.id.accountNumberEditTextId);
    saveActionButton = findViewById(R.id.saveActionButtonId);

    alertDialog = new AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
            .create();
}

    private void toggleProgress(boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(show){
                    alertDialog.show();
                }else{
                    alertDialog.cancel();
                }
            }
        });

    }
    void getAccountInfo(){
        toggleProgress(true);
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

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        toggleProgress(false);

                        String bankName = ""+ documentSnapshot.get(GlobalConfig.BANK_NAME_KEY);
                        String accountName = ""+ documentSnapshot.get(GlobalConfig.ACCOUNT_NAME_KEY);
                        String accountNumber = ""+ documentSnapshot.get(GlobalConfig.ACCOUNT_NUMBER_KEY);
                        String country = ""+ documentSnapshot.get(GlobalConfig.COUNTRY_KEY);

                        bankSpinner.setSelection(banks.indexOf(bankName));
                        accountNumberEditText.setText(accountNumber);
                        accountNameEditText.setText(accountName);
                        countrySpinner.setSelection(countryList.indexOf(country));



                    }
                });


    }

    void prepareBankSpinner(){
        banks.add("None");
        banks.add("Opay");
        banks.add("MoniePoint");
        banks.add("PayPal");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,banks);
        bankSpinner.setAdapter(adapter);
        bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBank = adapterView.getSelectedItem()+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void prepareCountrySpinner(){

        countryList = GlobalConfig.getCountryArrayList(null);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,countryList);
        countrySpinner.setAdapter(adapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCountry = adapterView.getSelectedItem()+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void saveAccountDetails(){
        String accountNumber = accountNumberEditText.getText()+"";
        String accountName = accountNameEditText.getText()+"";

        if(!selectedBank.equalsIgnoreCase("NONE")) {
        if(!accountNumber.isEmpty() && !accountName.isEmpty()) {
            toggleProgress(true);

            WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
            DocumentReference walletReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.USER_WALLET_KEY).document(GlobalConfig.USER_WALLET_KEY);
            HashMap<String, Object> walletDetails = new HashMap<>();
            walletDetails.put(GlobalConfig.DATE_ACCOUNT_DETAILS_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
            walletDetails.put(GlobalConfig.BANK_NAME_KEY, selectedBank);
            walletDetails.put(GlobalConfig.ACCOUNT_NUMBER_KEY, accountNumber);
            walletDetails.put(GlobalConfig.ACCOUNT_NAME_KEY, accountName);
            walletDetails.put(GlobalConfig.COUNTRY_KEY, selectedCountry);
            writeBatch.set(walletReference, walletDetails, SetOptions.merge());

            writeBatch.commit()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toggleProgress(false);
                            GlobalConfig.createSnackBar2(EditBankAccountDetailsActivity.this, saveActionButton, "Error: Account details failed to save. Please retry the process", "Retry", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    saveAccountDetails();
                                }
                            });
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            toggleProgress(false);
                            GlobalConfig.createSnackBar(EditBankAccountDetailsActivity.this, saveActionButton, "Success! When you request withdrawal, you will receive your payment through this bank account provided. Be sure they're correct", Snackbar.LENGTH_INDEFINITE);
                        }
                    });
        }else{
            Toast.makeText(this, "Please fill both your account name and account number", Toast.LENGTH_SHORT).show();
        }
        }else{
            Toast.makeText(this, "Please select the bank you operate", Toast.LENGTH_SHORT).show();
        }
    }
}