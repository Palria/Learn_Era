package com.palria.learnera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

public class HostActivity extends AppCompatActivity {
    Intent intent;
    String FRAGMENT_TYPE = "";
    FrameLayout hostFrameLayout;
    String userId = "";
    String libraryOpenType = "";
    String libraryCategory = "";
    String authorId = "";

    MaterialToolbar materialToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        initUI();
        fetchIntentData();
        openIncomingFragment();
    }

    private void initUI(){
        materialToolbar=findViewById(R.id.topBar);
        hostFrameLayout = findViewById(R.id.hostFrameLayoutId);
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    void fetchIntentData(){
         intent = getIntent();
        FRAGMENT_TYPE = intent.getStringExtra(GlobalConfig.FRAGMENT_TYPE_KEY);
        userId = intent.getStringExtra(GlobalConfig.USER_ID_KEY);
        libraryOpenType = intent.getStringExtra(AllLibraryFragment.OPEN_TYPE_KEY);
        libraryCategory = intent.getStringExtra(GlobalConfig.SINGLE_CATEGORY_KEY);
        authorId = intent.getStringExtra(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openIncomingFragment(){
        Bundle bundle = new Bundle();

        switch(FRAGMENT_TYPE) {
            case GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY:
                bundle = new Bundle();
                bundle.putString(GlobalConfig.USER_ID_KEY, userId);
                materialToolbar.setTitle("Profile");
                initFragment(bundle, new UserProfileFragment(null));
                break;
            case GlobalConfig.AUTHORS_FRAGMENT_TYPE_KEY:
                bundle = new Bundle();
                bundle.putBoolean(GlobalConfig.IS_AUTHOR_OPEN_TYPE_KEY, true);
                if(getIntent().getBooleanExtra(GlobalConfig.IS_ACCOUNT_VERIFICATION_KEY,false)) {
                    materialToolbar.setTitle("Verify accounts");

                    bundle.putBoolean(GlobalConfig.IS_AUTHOR_OPEN_TYPE_KEY, false);
                    bundle.putBoolean(GlobalConfig.IS_ACCOUNT_VERIFICATION_KEY, getIntent().getBooleanExtra(GlobalConfig.IS_ACCOUNT_VERIFICATION_KEY,false));

                }else{
                    materialToolbar.setTitle("Authors");
                }
                initFragment(bundle, new AllUsersFragment());
                break;
            case GlobalConfig.LIBRARY_FRAGMENT_TYPE_KEY:
                if (userId != null && userId.equals(GlobalConfig.getCurrentUserId())) {
                    materialToolbar.setTitle("My Libraries");
                } else {
                    materialToolbar.setTitle("Libraries");
                }
                bundle = new Bundle();
                bundle.putString(GlobalConfig.LIBRARY_AUTHOR_ID_KEY, userId);
                if (AllLibraryFragment.OPEN_TYPE_SINGLE_CATEGORY.equals(libraryOpenType+"")){
                    bundle.putString(AllLibraryFragment.OPEN_TYPE_KEY, AllLibraryFragment.OPEN_TYPE_SINGLE_CATEGORY);
                    bundle.putString(GlobalConfig.SINGLE_CATEGORY_KEY, libraryCategory);
                    materialToolbar.setTitle(materialToolbar.getTitle()+" ("+libraryCategory+")");
                } else{ if (userId == null) {
                        bundle.putString(AllLibraryFragment.OPEN_TYPE_KEY, AllLibraryFragment.OPEN_TYPE_ALL_LIBRARY);
                        bundle.putString(AllLibraryFragment.OPEN_TYPE_KEY, AllLibraryFragment.OPEN_TYPE_ALL_LIBRARY);
                    } else {
                        bundle.putString(AllLibraryFragment.OPEN_TYPE_KEY, AllLibraryFragment.OPEN_TYPE_USER_LIBRARY);
                    }
        }
                bundle.putString(GlobalConfig.LIBRARY_AUTHOR_ID_KEY, authorId);
                initFragment(bundle,new AllLibraryFragment());
                break;
            case GlobalConfig.TUTORIAL_FRAGMENT_TYPE_KEY:
                if(userId!=null && userId.equals(GlobalConfig.getCurrentUserId())) {
                    materialToolbar.setTitle("My Tutorials");
                }else{
                    materialToolbar.setTitle("Tutorials");
                }
                bundle = new Bundle();
                bundle.putString(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY,userId);
                if(userId==null){
                    bundle.putString(AllTutorialFragment.OPEN_TYPE_KEY, AllTutorialFragment.OPEN_TYPE_ALL_TUTORIAL);

                }else {
                    bundle.putString(AllTutorialFragment.OPEN_TYPE_KEY, AllTutorialFragment.OPEN_TYPE_USER_TUTORIAL);
                }
                initFragment(bundle,new AllTutorialFragment());
                break;
            case GlobalConfig.CATEGORY_FRAGMENT_TYPE_KEY:
                bundle = new Bundle();
                materialToolbar.setTitle("Category");
                initFragment(bundle, new AddCategoryFragment());
                break;
            case GlobalConfig.DISCUSSION_FRAGMENT_TYPE_KEY:
                bundle = new Bundle();
                bundle.putBoolean(GlobalConfig.IS_FROM_PAGE_CONTEXT_KEY,getIntent().getBooleanExtra(GlobalConfig.IS_FROM_PAGE_CONTEXT_KEY,false));
                bundle.putBoolean(GlobalConfig.IS_VIEW_ALL_DISCUSSIONS_FOR_SINGLE_PAGE_KEY,getIntent().getBooleanExtra(GlobalConfig.IS_VIEW_ALL_DISCUSSIONS_FOR_SINGLE_PAGE_KEY,false));
                bundle.putBoolean(GlobalConfig.IS_VIEW_SINGLE_DISCUSSION_REPLY_KEY,getIntent().getBooleanExtra(GlobalConfig.IS_VIEW_SINGLE_DISCUSSION_REPLY_KEY,false));
                bundle.putBoolean(GlobalConfig.IS_TUTORIAL_PAGE_KEY,getIntent().getBooleanExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,false));
                bundle.putString(GlobalConfig.AUTHOR_ID_KEY,getIntent().getStringExtra(GlobalConfig.AUTHOR_ID_KEY));
                bundle.putString(GlobalConfig.TUTORIAL_ID_KEY,getIntent().getStringExtra(GlobalConfig.TUTORIAL_ID_KEY));
                bundle.putString(GlobalConfig.FOLDER_ID_KEY,getIntent().getStringExtra(GlobalConfig.FOLDER_ID_KEY));
                bundle.putString(GlobalConfig.PAGE_ID_KEY,getIntent().getStringExtra(GlobalConfig.PAGE_ID_KEY));
                bundle.putString(GlobalConfig.PARENT_DISCUSSION_ID_KEY,getIntent().getStringExtra(GlobalConfig.PARENT_DISCUSSION_ID_KEY));

                materialToolbar.setTitle("Discussions");
                initFragment(bundle, new AllPageDiscussionsFragment());
                break;
            case GlobalConfig.ANSWER_FRAGMENT_TYPE_KEY:
                bundle = new Bundle();
                bundle.putBoolean(GlobalConfig.IS_FROM_QUESTION_CONTEXT_KEY,getIntent().getBooleanExtra(GlobalConfig.IS_FROM_QUESTION_CONTEXT_KEY,false));
                bundle.putBoolean(GlobalConfig.IS_VIEW_ALL_ANSWERS_FOR_SINGLE_QUESTION_KEY,getIntent().getBooleanExtra(GlobalConfig.IS_VIEW_ALL_ANSWERS_FOR_SINGLE_QUESTION_KEY,false));
                bundle.putBoolean(GlobalConfig.IS_VIEW_SINGLE_ANSWER_REPLY_KEY,getIntent().getBooleanExtra(GlobalConfig.IS_VIEW_SINGLE_ANSWER_REPLY_KEY,false));
                bundle.putString(GlobalConfig.AUTHOR_ID_KEY,getIntent().getStringExtra(GlobalConfig.AUTHOR_ID_KEY));
                bundle.putString(GlobalConfig.TUTORIAL_ID_KEY,getIntent().getStringExtra(GlobalConfig.TUTORIAL_ID_KEY));
                bundle.putString(GlobalConfig.ANSWER_ID_KEY,getIntent().getStringExtra(GlobalConfig.ANSWER_ID_KEY));
                bundle.putString(GlobalConfig.QUESTION_ID_KEY,getIntent().getStringExtra(GlobalConfig.QUESTION_ID_KEY));

                materialToolbar.setTitle("Replies");
                initFragment(bundle, new AllAnswersFragment());
                break;
            case GlobalConfig.QUIZ_FRAGMENT_TYPE_KEY:
                bundle = new Bundle();
                materialToolbar.setTitle("Join Ongoing Quiz");
                initFragment(bundle, new AllQuizFragment());
                break;
        }
    }

    void initFragment(Bundle bundle,Fragment fragment){
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(hostFrameLayout.getId(),fragment)
                .commit();
    }
}