package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.models.CurrentUserProfileDataModel;
import com.palria.learnera.models.StatisticsDataModel;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tp = findViewById(R.id.topBar);
        setSupportActionBar(tp);

        if(getSupportActionBar()!=null){


        }

            initUI();
            initializeApp();

    }

//

    /**<p>initializes this activity's views</p>
     * This method must be invoked first before any initializations
     * to avoid null pointer exception.
     * */
    private void initUI(){

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home_item);



    }
    /**<p>Initializes global variables which will be shared across every activities</p>
     * This method has to be called immediately after the invocation of {@link MainActivity#initUI()}
     * */

    private void initializeApp(){
GlobalConfig.setFirebaseFirestoreInstance();
GlobalConfig.setFirebaseStorageInstance();
if(GlobalConfig.isUserLoggedIn()) {
    GlobalConfig.setCurrentUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
    fetchToken();
}

initUserProfileData();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home_item:
            case R.id.stats_item:
            case R.id.library_item:
                initFragment(new TestFragment());
                return true;
            case R.id.profile_item:
                initFragment(new UserProfileFragment());
                return true;
        }
        return false;
    }

    private void initFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.myFragment, fragment)
                .commit();

    }

void fetchToken(){
    FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
        @Override
        public void onSuccess(GetTokenResult getTokenResult) {
            GlobalConfig.setCurrentUserTokenId(getTokenResult.getToken());

        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
        //retries recursively until it succeeds
            fetchToken();
        }
    });
}
private void initUserProfileData(){
    GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId())
            .get()
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            })
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    long totalNumberOfProfileVisitor = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY) : 0L;
                    long totalNumberOfProfileReach = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_REACH_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_REACH_KEY) : 0L;
                    long totalNumberOfOneStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) : 0L;
                    long totalNumberOfTwoStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) : 0L;
                    long totalNumberOfThreeStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) : 0L;
                    long totalNumberOfFourStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) : 0L;
                    long totalNumberOfFiveStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) : 0L;


                    documentSnapshot.getReference()
                            .collection(GlobalConfig.USER_PROFILE_KEY)
                            .document(GlobalConfig.getCurrentUserId())
                            .get()
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String lastSeen = documentSnapshot.getString(GlobalConfig.LAST_SEEN_KEY);
                                    String userName = documentSnapshot.getString(GlobalConfig.USER_DISPLAY_NAME_KEY);
                                    String userId = documentSnapshot.getString(GlobalConfig.USER_ID_KEY);
                                    String gender = documentSnapshot.getString(GlobalConfig.USER_GENDER_TYPE_KEY);
                                    String dateOfBirth = documentSnapshot.getString(GlobalConfig.USER_DATE_OF_BIRTH_KEY);
                                    String dateRegistered = documentSnapshot.getString(GlobalConfig.USER_PROFILE_DATE_CREATED_KEY);
                                    String userPhoneNumber = documentSnapshot.getString(GlobalConfig.USER_CONTACT_PHONE_NUMBER_KEY);
                                    String userEmail = documentSnapshot.getString(GlobalConfig.USER_CONTACT_EMAIL_ADDRESS_KEY);
                                    String userResidentialAddress = documentSnapshot.getString(GlobalConfig.USER_RESIDENTIAL_ADDRESS_KEY);
                                    String userCountryOfResidence = documentSnapshot.getString(GlobalConfig.USER_COUNTRY_OF_RESIDENCE_KEY);
                                    long age = documentSnapshot.get(GlobalConfig.USER_AGE_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.USER_AGE_KEY):0L;
                                    boolean isUserProfileCompleted = documentSnapshot.get(GlobalConfig.IS_USER_PROFILE_COMPLETED_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_USER_PROFILE_COMPLETED_KEY):false;
                                    String userProfileImageDownloadUrl = documentSnapshot.getString(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                                    long totalNumberOfLibraries = (documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY)!= null) ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY) : 0L;
                                    boolean isAnAuthor = (documentSnapshot.get(GlobalConfig.IS_USER_AUTHOR_KEY)!=null )? (documentSnapshot.getBoolean(GlobalConfig.IS_USER_AUTHOR_KEY)) : false;

                                   new CurrentUserProfileDataModel(
                                                                 userName,
                                                                 userId,
                                                                 userProfileImageDownloadUrl,
                                                                 totalNumberOfLibraries,
                                                                 isAnAuthor,
                                                                 isAnAuthor,
                                                                 gender,
                                                                 age,
                                                                 dateOfBirth,
                                                                 dateRegistered,
                                                                 lastSeen,
                                                                 isUserProfileCompleted,
                                                                 userPhoneNumber,
                                                                 userEmail,
                                                                 userResidentialAddress,
                                                                 userCountryOfResidence,
                                                                 totalNumberOfProfileVisitor,
                                                                 totalNumberOfProfileReach,
                                                                 totalNumberOfOneStarRate,
                                                                 totalNumberOfTwoStarRate,
                                                                 totalNumberOfThreeStarRate,
                                                                 totalNumberOfFourStarRate,
                                                                 totalNumberOfFiveStarRate
                                                                 );
                                }
                            });
                }
            });
}

}
