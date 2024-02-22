package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Objects;

public class PaypalEditActivity extends AppCompatActivity {

    TextInputEditText paypalEmail;
    TextInputEditText confirmPaypalInput;
    Button cancelButton;
    Button createActionButton;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_edit);

        initUI();

        //listener for click
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaypalEditActivity.this.onBackPressed();

            }
        });

        createActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add logic here
                if(validateForm()){
                    //success validated now save to firebase

                    String email = Objects.requireNonNull(paypalEmail.getText()).toString();

                    savePayPalEmail(email);
                }
            }
        });

    }

    private void initUI() {
        paypalEmail = findViewById(R.id.paypalEmail);
        confirmPaypalInput = findViewById(R.id.confirmPaypalInput);
        cancelButton =findViewById(R.id.cancelButton);
        createActionButton=findViewById(R.id.createActionButton);

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
    private boolean validateForm(){

        String email = Objects.requireNonNull(paypalEmail.getText()).toString();
        String confirmEmail = Objects.requireNonNull(confirmPaypalInput.getText()).toString();

        if(!GlobalHelpers.isValidEmail(email)){
            paypalEmail.setError("Enter a valid email.");
            return false;
        }else{
            paypalEmail.setError(null);
        }

        if(TextUtils.isEmpty(confirmEmail)){
            confirmPaypalInput.setError("Please confirm paypal email");
            return false;
        }

        if(!email.equals(confirmEmail)){
            confirmPaypalInput.setError("Type same email as above");
            return false;
        }else{
            confirmPaypalInput.setError(null);
        }


        return true;
    }

    void savePayPalEmail(String paypalEmail){
        toggleProgress(true);

        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
        DocumentReference walletReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.USER_WALLET_KEY).document(GlobalConfig.USER_WALLET_KEY);
        HashMap<String, Object> walletDetails = new HashMap<>();
        walletDetails.put(GlobalConfig.DATE_ACCOUNT_DETAILS_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        walletDetails.put(GlobalConfig.PAYPAL_ACCOUNT_EMAIL_KEY, paypalEmail);
        writeBatch.set(walletReference, walletDetails, SetOptions.merge());

        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toggleProgress(false);
                        GlobalConfig.createSnackBar2(PaypalEditActivity.this, confirmPaypalInput, "Error: Paypal email failed to save. Please retry the process", "Retry", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               if(validateForm()) {
                                   savePayPalEmail(paypalEmail);
                               }
                            }
                        });
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        toggleProgress(false);
                        GlobalConfig.createSnackBar(PaypalEditActivity.this, confirmPaypalInput, "Success! When you request withdrawal, you will receive your payment through this paypal email account provided. Be sure it's correct", Snackbar.LENGTH_INDEFINITE);
                    }
                });
    }
}