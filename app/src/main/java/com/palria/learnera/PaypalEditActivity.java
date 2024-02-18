package com.palria.learnera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.Objects;

public class PaypalEditActivity extends AppCompatActivity {

    TextInputEditText paypalEmail;
    TextInputEditText confirmPaypalInput;
    Button cancelButton;
    Button createActionButton;

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

                }
            }
        });

    }

    private void initUI() {
        paypalEmail = findViewById(R.id.paypalEmail);
        confirmPaypalInput = findViewById(R.id.confirmPaypalInput);
        cancelButton =findViewById(R.id.cancelButton);
        createActionButton=findViewById(R.id.createActionButton);

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
}