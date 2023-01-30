package com.palria.learnera;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class BookmarksFragment extends Fragment {
    public BookmarksFragment() {
        // Required empty public constructor
    }
    LinearLayout containerLinearLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView =  inflater.inflate(R.layout.fragment_bookmarks, container, false);
        initUI();
fetchBookmarks(new BookmarkFetchListener() {
    @Override
    public void onSuccess(boolean isLibraryBookmark, boolean isTutorialBookmark, String authorId, String libraryId, String tutorialId, String dateBookmarked) {
        displayBookmark(isLibraryBookmark,isTutorialBookmark,authorId,libraryId ,tutorialId ,dateBookmarked );
    }

    @Override
    public void onFailed(String errorMessage) {
//it fails to get bookmarks

    }
});
        return parentView;
    }

    private void initUI(){
        //use the parentView to find the by Id as in : parentView.findViewById(...);


    }

    private void fetchBookmarks(BookmarkFetchListener bookmarkFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId())
                .collection(GlobalConfig.BOOK_MARKS_KEY)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                                bookmarkFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                           final boolean isLibraryBookmark = (documentSnapshot.get(GlobalConfig.IS_LIBRARY_BOOK_MARK_KEY))!=null ? (documentSnapshot.getBoolean(GlobalConfig.IS_LIBRARY_BOOK_MARK_KEY))  :false;
                           final boolean isTutorialBookmark = (documentSnapshot.get(GlobalConfig.IS_TUTORIAL_BOOK_MARK_KEY))!=null ? (documentSnapshot.getBoolean(GlobalConfig.IS_TUTORIAL_BOOK_MARK_KEY))  :false;
                           final String authorId = ""+ documentSnapshot.get(GlobalConfig.AUTHOR_ID_KEY);
                           final String libraryId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_ID_KEY);
                           final String tutorialId = ""+  documentSnapshot.get(GlobalConfig.TUTORIAL_ID_KEY);
                           final String dateBookmarked = ""+  documentSnapshot.get(GlobalConfig.DATE_BOOK_MARKED_KEY);

                            bookmarkFetchListener.onSuccess(isLibraryBookmark,isTutorialBookmark,authorId,libraryId ,tutorialId ,dateBookmarked );

                        }
                    }
                });
    }
    private void displayBookmark(final boolean isLibraryBookmark,final boolean isTutorialBookmark,final String authorId,final String libraryId ,final String tutorialId ,final String dateBookmarked ){
//<Uncomment and implement>
        /*
        if(getContext() != null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View bookmarkView = layoutInflater.inflate(R.layout.bookmark_custom_view, null);
            TextView dateBookmarkedTextView = bookmarkView.findViewById(R.id.dateBookmarkedTextViewId);
            TextView viewTypeIndicatorTextView = bookmarkView.findViewById(R.id.viewTypeIndicatorTextViewId);
            TextView library_tutorial_titleTextView = bookmarkView.findViewById(R.id.titleTextViewId);
            TextView authorNameTextView = bookmarkView.findViewById(R.id.authorNameTextViewId);
            ImageView library_tutorial_CoverPhoto =bookmarkView.findViewById(R.id.library_tutorial_CoverPhotoId);
            dateBookmarkedTextView.setText(dateBookmarked);
if(isLibraryBookmark){
    GlobalConfig.getFirebaseFirestoreInstance()
            .collection(GlobalConfig.ALL_USERS_KEY)
            .document(authorId)
            .collection(GlobalConfig.ALL_LIBRARY_KEY)
            .document(libraryId)
            .collection(GlobalConfig.LIBRARY_PROFILE_KEY)
            .document(libraryId)
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String libraryName =""+ documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                    String libraryCoverPhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);
                    library_tutorial_titleTextView.setText(libraryName);

                    Glide.with(BookmarksFragment.this)
                            .load(libraryCoverPhotoDownloadUrl)
                            .centerCrop()
                            .into(library_tutorial_CoverPhoto);
                }
            });

}
else if(isTutorialBookmark){
    GlobalConfig.getFirebaseFirestoreInstance()
            .collection(GlobalConfig.ALL_USERS_KEY)
            .document(authorId)
            .collection(GlobalConfig.ALL_LIBRARY_KEY)
            .document(libraryId)
            .collection(GlobalConfig.ALL_TUTORIAL_KEY)
            .document(tutorialId)
            .collection(GlobalConfig.TUTORIAL_PROFILE_KEY)
            .document(tutorialId)
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String tutorialName =""+ documentSnapshot.get(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY);
                    String tutorialCoverPhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY);
                    library_tutorial_titleTextView.setText(tutorialName);

                    Glide.with(BookmarksFragment.this)
                            .load(tutorialCoverPhotoDownloadUrl)
                            .centerCrop()
                            .into(library_tutorial_CoverPhoto);
                }
            });
}

//get the author's name
            GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(authorId)
                    .collection(GlobalConfig.USER_PROFILE_KEY)
                    .document(authorId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String authorName  = ""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                            authorNameTextView.setText(authorName);
                        }
                    });
                    containerLinearLayout.addView(bookmarkView);
        }
        */
    }

    interface BookmarkFetchListener{
        void onSuccess(final boolean isLibraryBookmark,final boolean isTutorialBookmark,final String authorId,final String libraryId ,final String tutorialId ,final String dateBookmarked );
        void onFailed(String errorMessage);
    }
}