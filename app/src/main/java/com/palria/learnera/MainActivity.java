package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.models.CurrentUserProfileDataModel;
import com.palria.learnera.models.StatisticsDataModel;
import com.palria.learnera.widgets.LEBottomSheetDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, Serializable {

    BottomNavigationView bottomNavigationView;
    BottomAppBar bottomAppBar;
    boolean isHomeFragmentOpen = false;
    boolean isLibraryFragmentOpen = false;
    boolean isAllTutorialFragmentOpen = false;
    boolean isUserProfileFragmentOpen = false;
    FrameLayout homeFrameLayout;
    FrameLayout libraryFrameLayout;
    FrameLayout allTutorialFrameLayout;
    FrameLayout userProfileFrameLayout;

    FloatingActionButton fab;

    //learn era bottom sheet dialog
    LEBottomSheetDialog leBottomSheetDialog;
    RoundedImageView currentUserProfile;

    /**
     * loading alert dialog
     *
     */
    AlertDialog alertDialog;
    GlobalConfig.OnCurrentUserProfileFetchListener onCurrentUserProfileFetchListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar tp = findViewById(R.id.topBar);
        setSupportActionBar(tp);



            initUI();
            initializeApp();


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    leBottomSheetDialog.show();

                }
            });

    }

//

    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }

    /**<p>initializes this activity's views</p>
     * This method must be invoked first after the inVocation of {@link AppCompatActivity#setContentView(View)} and  before any initializations
     * to avoid null pointer exception.
     * */
    private void initUI(){


        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        homeFrameLayout = findViewById(R.id.homeFragment);
        libraryFrameLayout = findViewById(R.id.libraryFragment);
        allTutorialFrameLayout = findViewById(R.id.allTutorialFragment);
        userProfileFrameLayout = findViewById(R.id.userProfileFragment);
        currentUserProfile=findViewById(R.id.currentUserProfile);


        fab = findViewById(R.id.fab);

        bottomNavigationView.setBackground(null);
//        bottomNavigationView.getIt
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home_item);
        alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();

        leBottomSheetDialog = new LEBottomSheetDialog(this);

        leBottomSheetDialog.addOptionItem("New Library", R.drawable.ic_baseline_library_add_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this, CreateNewLibraryActivity.class);
                        //creating new

                        i.putExtra(GlobalConfig.IS_CREATE_NEW_LIBRARY_KEY,true);
                        leBottomSheetDialog.hide();
                        startActivity(i);
                    }
                },0)
                .addOptionItem("New Tutorial", R.drawable.ic_baseline_post_add_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        leBottomSheetDialog.hide();
                        toggleProgress(true);

                        GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY)
                                .whereEqualTo(GlobalConfig.LIBRARY_AUTHOR_ID_KEY,GlobalConfig.getCurrentUserId())
                                .get()
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        toggleProgress(false);

                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        toggleProgress(false);

                                        ArrayList<String>libraryIdArrayList= new ArrayList<>();
                                        ArrayList<String>libraryNameArrayList= new ArrayList<>();
                                        ArrayList<ArrayList<String>>libraryCategoryArrayList = new ArrayList<>();
                                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                       String libraryId = documentSnapshot.getId();
                                       String libraryName = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                                       ArrayList<String> libraryCategory = (ArrayList<String>) documentSnapshot.get(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY);
                                        libraryIdArrayList.add(libraryId);
                                        libraryNameArrayList.add(libraryName);
                                        libraryCategoryArrayList.add(libraryCategory);


                                    }
                                        // Initialize alert dialog
                                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);

                                        // set title
                                        builder.setTitle("Select Library to add tutorial.");

                                        // set dialog non cancelable
                                        builder.setCancelable(true);

                                        String[] arr = new String[libraryNameArrayList.size()];
                                        for(int i=0; i<arr.length;i++){
                                            arr[i]=libraryNameArrayList.get(i);
                                        }

                                        builder.setSingleChoiceItems(arr, 0, (dialog, which) -> {

                                            Intent i = new Intent(MainActivity.this, CreateNewTutorialActivity.class);
                                            //creating new

                                            i.putExtra(GlobalConfig.IS_CREATE_NEW_TUTORIAL_KEY,true);
                                            i.putExtra(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY,libraryCategoryArrayList.get(which));
                                            i.putExtra(GlobalConfig.LIBRARY_ID_KEY,libraryIdArrayList.get(which));
                                            i.putExtra(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY, libraryNameArrayList.get(which));

                                            startActivity(i);
                                            // when selected an item the dialog should be closed with the dismiss method
                                            dialog.dismiss();
                                        });

                                        // show dialog
                                        builder.show();

                                    }

                                });


                    }
                }, 0)
                .addOptionItem("New Post", R.drawable.ic_baseline_add_circle_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                }, 0)

                .render();


        currentUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalConfig.createPopUpMenu(MainActivity.this,R.menu.home_overflow_menu , currentUserProfile, new GlobalConfig.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClicked(MenuItem item) {

                        if(item.getItemId()== R.id.login_item){
                            Intent intent =new Intent(MainActivity.this, SignInActivity.class);
                            startActivity(intent);
                        }
                        else if(item.getItemId() == R.id.notification_item){
                            Toast.makeText(MainActivity.this, "notification clicked.", Toast.LENGTH_SHORT).show();
                        }
                        else if(item.getItemId() == R.id.settings_item){
                         //settings activity starts here 
                            Toast.makeText(MainActivity.this, "Setting clicked", Toast.LENGTH_SHORT).show();
                            
                        }else if(item.getItemId() == R.id.log_out_item){
                        //log out user code here 
                           MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                           builder.setTitle("Log out?")
                                   .setMessage("Are you sure want to log out now?")
                                   .setCancelable(false)
                                   .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int i) {
                                           Toast.makeText(MainActivity.this, "log out clicked", Toast.LENGTH_SHORT).show();
                                            //log out code goes here ]



                                       }
                                   })
                                   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int i) {
                                           Toast.makeText(MainActivity.this, "cancel log out.", Toast.LENGTH_SHORT).show();
                                       }
                                   })
                                   .show();
                        }
                        return true;
                    }
                });
            }
        });
        


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
    initUserProfileData();

}

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.tutorials_item:
                if(isAllTutorialFragmentOpen){
                    //Just set the frame layout visibility
                    setFrameLayoutVisibility(allTutorialFrameLayout);

                }else {
                    isAllTutorialFragmentOpen =true;
                    setFrameLayoutVisibility(allTutorialFrameLayout);
                    initFragment(new AllTutorialFragment(), allTutorialFrameLayout);
                }
                return true;
            case R.id.home_item:
                if(isHomeFragmentOpen){
                    //Just set the frame layout visibility
                    setFrameLayoutVisibility(homeFrameLayout);

                }else {
                    isHomeFragmentOpen =true;

                    setFrameLayoutVisibility(homeFrameLayout);
                    initFragment(new HomeFragment(), homeFrameLayout);
                }
                return true;
            case R.id.library_item:
                if(isLibraryFragmentOpen){
                    //Just set the frame layout visibility
                    setFrameLayoutVisibility(libraryFrameLayout);

                }else {
                    isLibraryFragmentOpen =true;

                    setFrameLayoutVisibility(libraryFrameLayout);
                    initFragment(new AllLibraryFragment(), libraryFrameLayout);
                }
                return true;
            case R.id.profile_item:
                if(isUserProfileFragmentOpen){
                    //Just set the frame layout visibility
                    setFrameLayoutVisibility(userProfileFrameLayout);
                }else {
                    isUserProfileFragmentOpen =true;

                    setFrameLayoutVisibility(userProfileFrameLayout);
                    initFragment(new UserProfileFragment(bottomAppBar), userProfileFrameLayout);
                }
                return true;
        }
        return false;
    }

    private void initFragment(Fragment fragment,FrameLayout frameLayout){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), fragment)
                .commit();

    }

    private void setFrameLayoutVisibility(FrameLayout frameLayoutToSetVisible){
        homeFrameLayout.setVisibility(View.GONE);
        allTutorialFrameLayout.setVisibility(View.GONE);
        libraryFrameLayout.setVisibility(View.GONE);
        userProfileFrameLayout.setVisibility(View.GONE);
        frameLayoutToSetVisible.setVisibility(View.VISIBLE);
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
                    GlobalConfig.onCurrentUserProfileFetchListener.onFailed(e.getMessage());
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

                    //show current user profile there .
                    Glide.with(MainActivity.this)
                            .load(userProfileImageDownloadUrl)
                            .centerCrop()
                            .placeholder(R.drawable.default_profile)
                            .into(currentUserProfile);

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

}
