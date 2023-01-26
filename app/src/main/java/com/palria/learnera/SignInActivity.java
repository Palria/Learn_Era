package com.palria.learnera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SignInActivity extends AppCompatActivity {
private String email;
private String password;
private EditText emailEditText;
private EditText passwordEditText;
private Button signInActionButton;
//register and forget password link
private TextView register_link_view;
private TextView forget_password_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_sign_in);
        //initializes this activity's views
        initUI();
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
                        // account sign in failed//
                    }

                    @Override
                    public void onEmptyInput(boolean isEmailEmpty, boolean isPasswordEmpty) {
                        // Either email or password is empty
                    }
                });
            }
        });

        //register link button click listener
        register_link_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //regiser|sign up activity starts from here .
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        forget_password_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //forget password activity intent starts from here .
            }
        });
    }


    private void initUI(){


        emailEditText = (EditText) findViewById(R.id.emailInput);
        passwordEditText = (EditText) findViewById(R.id.passwordInput);

        signInActionButton = (Button) findViewById(R.id.loginButton);

        register_link_view = (TextView) findViewById(R.id.register_link);

        forget_password_link = (TextView) findViewById(R.id.forget_password_link);



    }


}