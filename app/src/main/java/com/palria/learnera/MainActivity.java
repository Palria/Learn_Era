package com.palria.learnera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initializeApp();
    }

//

    /**<p>initializes this activity's views</p>
     * This method must be invoked first before any initializations
     * to avoid null pointer exception.
     * */
    private void initUI(){

    }
    /**<p>Initializes global variables which will be shared across every activities</p>
     * This method has to be called immediately after the invocation of {@link MainActivity#initUI()}
     * */
    private void initializeApp(){

    }



}
