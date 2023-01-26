package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;

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
private TextView errorMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_sign_in);
        //initializes this activity's views
        initUI();

        signInActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                Toast.makeText(getApplicationContext(), "Sign in progress...!", Toast.LENGTH_SHORT).show();
                errorMessageTextView.setText("Progress...");
                GlobalConfig.signInUserWithEmailAndPassword(SignInActivity.this,email, password, new GlobalConfig.SignInListener() {
                    @Override
                    public void onSuccess(String email, String password) {
                        //user has successfully signed in

                        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "You successfully signed in, go learn more, it is era of learning", Toast.LENGTH_LONG).show();
                        SignInActivity.this.finish();


                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        // account sign in failed//
//                        Toast.makeText(getApplicationContext(), "Sign in failed: "+errorMessage+" please try again!", Toast.LENGTH_SHORT).show();
                                errorMessageTextView.setText(errorMessage+ "  Please try again!");
                                errorMessageTextView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onEmptyInput(boolean isEmailEmpty, boolean isPasswordEmpty) {
                        // Either email or password is empty
//                        Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                        errorMessageTextView.setText("All fields are required, fill the form and try again!");
                        errorMessageTextView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        //register link button click listener
        register_link_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //register|sign up activity starts from here .
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
        errorMessageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorMessageTextView.setVisibility(View.GONE);

            }
        });

    }

    private void initUI(){


        emailEditText = (EditText) findViewById(R.id.emailInput);
        passwordEditText = (EditText) findViewById(R.id.passwordInput);
        errorMessageTextView = (TextView) findViewById(R.id.errorMessage);

        signInActionButton = (Button) findViewById(R.id.loginButton);

        register_link_view = (TextView) findViewById(R.id.register_link);

        forget_password_link = (TextView) findViewById(R.id.forget_password_link);



    }


}