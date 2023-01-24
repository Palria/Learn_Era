package com.palria.learnera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {
private String email;
private String password;
private EditText emailEditText;
private EditText passwordEditText;
private Button signInActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                GlobalConfig.signInUserWithEmailAndPassword(email, password, new GlobalConfig.SignInListener() {
                    @Override
                    public void onSuccess(String email, String password) {
                        //user has successfully signed in

                        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                        startActivity(intent);
                        SignInActivity.this.finish();

                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        // account sign in failed
                    }

                    @Override
                    public void onEmptyInput(boolean isEmailEmpty, boolean isPasswordEmpty) {
                        // Either email or password is empty
                    }
                });
            }
        });
    }


    private void initUI(){

    }


}