package com.palria.learnera;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

public class SearchActivity extends AppCompatActivity {
    TabLayout tabLayout ;
    FrameLayout allViewerFrameLayout;

    boolean isAllLibrariesSearchOpen = false;
    boolean isAllTutorialsSearchOpen = false;
    boolean isAllAuthorsSearchOpen = false;

    SearchView searchView;
    ImageButton searchActionButton;
    static String searchKeyword = "";

    MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        tabLayout = findViewById(R.id.tabLayoutId);

        searchView = findViewById(R.id.searchViewId);
        toolbar=findViewById(R.id.topBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        EditText txtSearch = ((EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        txtSearch.setHint("Search");
        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setTextColor(Color.WHITE);

        searchView.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(1,0);
        searchView.requestFocus();

        //        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchKeyword = query;
                if(isAllLibrariesSearchOpen){
                    openFragment(new AllLibraryFragment(),allViewerFrameLayout);

                }
                if(isAllTutorialsSearchOpen){
                    openFragment(new AllTutorialFragment(),allViewerFrameLayout);

                }
                if(isAllAuthorsSearchOpen){
                    openFragment(new AllUsersFragment(),allViewerFrameLayout);

                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchKeyword = newText;

                return true;
            }
        });

//        searchActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isAllLibrariesSearchOpen){
//                    openFragment(new AllLibraryFragment(),allViewerFrameLayout);
//
//                }
//                if(isAllTutorialsSearchOpen){
//                    openFragment(new AllTutorialFragment(),allViewerFrameLayout);
//
//                }
//                if(isAllAuthorsSearchOpen){
//                    openFragment(new AllLibraryFragment(),allViewerFrameLayout);
//
//                }
//            }
//        });


        allViewerFrameLayout = findViewById(R.id.allViewerFrameLayoutId);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){

                    case 0:
                        openFragment(new AllLibraryFragment(),allViewerFrameLayout);
//                        setLayoutVisibility();
                        setAllLibrariesSearchOpen();
                        break;

                    case 1:
                        openFragment(new AllTutorialFragment(),allViewerFrameLayout);
                        setAllTutorialsSearchOpen();
                        break;

                    case 2:
                        openFragment(new AllUsersFragment(),allViewerFrameLayout);
                        setAllAuthorsSearchOpen();
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        manageTabLayout();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        searchKeyword = "";


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void manageTabLayout(){
        TabLayout.Tab libraryTabItem= tabLayout.newTab();
        libraryTabItem.setText("Libraries");
        tabLayout.addTab(libraryTabItem,0,true);


        TabLayout.Tab tutorialTabItem=tabLayout.newTab();
        tutorialTabItem.setText("Tutorials");
        tabLayout.addTab(tutorialTabItem,1);


        TabLayout.Tab authorsTabItem=tabLayout.newTab();
        authorsTabItem.setText("Authors");
        tabLayout.addTab(authorsTabItem,2);


    }

    public void openFragment(Fragment fragment, FrameLayout frameLayoutToBeReplaced){
//        String searchKeyword = searchFieldEditText.getText().toString();
        searchKeyword = searchKeyword.toLowerCase();
        Bundle bundle = new Bundle();
        bundle.putString(GlobalConfig.SEARCH_KEYWORD_KEY, searchKeyword);
        bundle.putBoolean(GlobalConfig.IS_AUTHOR_OPEN_TYPE_KEY, true);
        bundle.putBoolean(GlobalConfig.IS_FROM_SEARCH_CONTEXT_KEY,true);
//        bundle.putString(GlobalValue.FRAGMENT_OPENING_PURPOSE,GlobalValue.FRAGMENT_OPENING_PURPOSE_DEFAULT);

        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(frameLayoutToBeReplaced.getId(),fragment).commit();
    }


    void setAllLibrariesSearchOpen(){
        isAllTutorialsSearchOpen = false;
        isAllAuthorsSearchOpen = false;

        isAllLibrariesSearchOpen = true;
    }
    void setAllTutorialsSearchOpen(){
        isAllLibrariesSearchOpen = false;
        isAllAuthorsSearchOpen = false;

        isAllTutorialsSearchOpen = true;
    }
    void setAllAuthorsSearchOpen(){
        isAllTutorialsSearchOpen = false;
        isAllLibrariesSearchOpen = false;

        isAllAuthorsSearchOpen = true;
    }

    interface  OnSearchTriggered{
        void  onSearch(String whichData);
    }
}