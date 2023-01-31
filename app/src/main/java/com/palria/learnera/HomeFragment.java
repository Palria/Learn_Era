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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.models.LibraryDataModel;
import com.palria.learnera.models.TutorialDataModel;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
            }
String categorySelected = "";
        LinearLayout popularAuthorLinearLayout;
        LinearLayout libraryLinearLayout;
        LinearLayout tutorialLinearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View parentView = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(parentView);
        fetchPopularAuthor(categorySelected, new PopularAuthorFetchListener() {
            @Override
            public void onSuccess(String authorName, String authorProfilePhotoDownloadUrl, long totalNumberOfLibrary) {
                displayPopularAuthor(authorName,authorProfilePhotoDownloadUrl,totalNumberOfLibrary);
            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });
        fetchLibrary(categorySelected, new LibraryFetchListener() {
            @Override
            public void onSuccess(LibraryDataModel libraryDataModel) {
                displayLibrary(libraryDataModel);
            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });
        fetchTutorial(categorySelected, new TutorialFetchListener() {
            @Override
            public void onSuccess(TutorialDataModel tutorialDataModel) {
                displayTutorial(tutorialDataModel);
            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });
       return parentView;
    }


    private void initUI(View parentView){

    }

private void changeCategory(String categorySelected){
    popularAuthorLinearLayout.removeAllViews();
    libraryLinearLayout.removeAllViews();
    tutorialLinearLayout.removeAllViews();

    fetchPopularAuthor(categorySelected, new PopularAuthorFetchListener() {
        @Override
        public void onSuccess(String authorName, String authorProfilePhotoDownloadUrl, long totalNumberOfLibrary) {
            displayPopularAuthor(authorName,authorProfilePhotoDownloadUrl,totalNumberOfLibrary);
        }

        @Override
        public void onFailed(String errorMessage) {

        }
    });
    fetchLibrary(categorySelected, new LibraryFetchListener() {
        @Override
        public void onSuccess(LibraryDataModel libraryDataModel) {
            displayLibrary(libraryDataModel);
        }

        @Override
        public void onFailed(String errorMessage) {

        }
    });
    fetchTutorial(categorySelected, new TutorialFetchListener() {
        @Override
        public void onSuccess(TutorialDataModel tutorialDataModel) {
            displayTutorial(tutorialDataModel);
        }

        @Override
        public void onFailed(String errorMessage) {

        }
    });

}

    private void fetchPopularAuthor(String categoryTag, PopularAuthorFetchListener popularAuthorFetchListener){
        Query authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereEqualTo(GlobalConfig.IS_USER_AUTHOR_KEY,true).whereArrayContains(GlobalConfig.AUTHOR_CATEGORY_TAG_ARRAY_KEY,categoryTag).orderBy(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY);
        authorQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                                popularAuthorFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            documentSnapshot.getReference()
                                    .collection(GlobalConfig.USER_PROFILE_KEY)
                                    .document(GlobalConfig.USER_ID_KEY)
                                    .get()
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            final String authorName = ""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                                           final String authorProfilePhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                                           final long totalNumberOfLibrary = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY)!= null ?documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY)  :0L ;
                                        popularAuthorFetchListener.onSuccess(authorName,authorProfilePhotoDownloadUrl,totalNumberOfLibrary );
                                        }
                                    });

                        }
                    }
                });
    }

    private void fetchLibrary(String categoryTag,LibraryFetchListener libraryFetchListener){
        Query libraryQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).whereEqualTo(GlobalConfig.LIBRARY_CATEGORY_KEY,categoryTag).orderBy(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY);

        libraryQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        libraryFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String authorId =""+ documentSnapshot.get(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);
                            String libraryId =""+ documentSnapshot.get(GlobalConfig.LIBRARY_ID_KEY);
                            GlobalConfig.getFirebaseFirestoreInstance()
                                    .collection(GlobalConfig.ALL_USERS_KEY)
                                    .document(authorId)
                                    .collection(GlobalConfig.ALL_LIBRARY_KEY)
                                    .document(libraryId)
                                    .get()
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    long totalNumberOfLibraryVisitor = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY) : 0L;
                                    long totalNumberOfLibraryReach = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) : 0L;
                                    long totalNumberOfOneStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) : 0L;
                                    long totalNumberOfTwoStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) : 0L;
                                    long totalNumberOfThreeStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) : 0L;
                                    long totalNumberOfFourStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) : 0L;
                                    long totalNumberOfFiveStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) : 0L;

                                    documentSnapshot.getReference()
                                            .collection(GlobalConfig.LIBRARY_PROFILE_KEY)
                                            .document(libraryId)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {


                                                    String libraryName = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                                                    String libraryCategory = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_CATEGORY_KEY);
                                                    String dateCreated = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_KEY);
                                                    String authorUserId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);
                                                    String libraryCoverPhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);
                                                    long totalNumberOfTutorials = 0L;
                                                    if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) != null){
                                                        totalNumberOfTutorials =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY);
                                                    }


                                                    libraryFetchListener.onSuccess(new LibraryDataModel(
                                                            libraryName,
                                                            libraryId,
                                                            libraryCategory,
                                                            libraryCoverPhotoDownloadUrl,
                                                            dateCreated,
                                                            totalNumberOfTutorials,
                                                            totalNumberOfLibraryVisitor,
                                                            totalNumberOfLibraryReach,
                                                            authorUserId,
                                                            totalNumberOfOneStarRate,
                                                            totalNumberOfTwoStarRate,
                                                            totalNumberOfThreeStarRate,
                                                            totalNumberOfFourStarRate,
                                                            totalNumberOfFiveStarRate
                                                    ));
                                                }
                                            });
                                }
                            });

                        }
                    }
                });
    }

    private void fetchTutorial(String libraryCategoryTag,TutorialFetchListener tutorialFetchListener){
        Query libraryQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).whereEqualTo(GlobalConfig.LIBRARY_CONTAINER_CATEGORY_KEY,libraryCategoryTag).orderBy(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY);

        libraryQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tutorialFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String authorId =""+ documentSnapshot.get(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY);
                            String libraryId =""+ documentSnapshot.get(GlobalConfig.LIBRARY_CONTAINER_ID_KEY);
                            String tutorialId =""+ documentSnapshot.get(GlobalConfig.TUTORIAL_ID_KEY);
                            GlobalConfig.getFirebaseFirestoreInstance()
                                    .collection(GlobalConfig.ALL_USERS_KEY)
                                    .document(authorId)
                                    .collection(GlobalConfig.ALL_LIBRARY_KEY)
                                    .document(libraryId)
                                    .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                                    .document(tutorialId)
                                    .get()
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    long totalNumberOfTutorialVisitor = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY) : 0L;
                                    long totalNumberOfTutorialReach = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REACH_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REACH_KEY) : 0L;
                                    long totalNumberOfOneStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) : 0L;
                                    long totalNumberOfTwoStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) : 0L;
                                    long totalNumberOfThreeStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) : 0L;
                                    long totalNumberOfFourStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) : 0L;
                                    long totalNumberOfFiveStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) : 0L;

                                    documentSnapshot.getReference()
                                            .collection(GlobalConfig.TUTORIAL_PROFILE_KEY)
                                            .document(tutorialId)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {


                                                    String tutorialName = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY);
                                                    String libraryCategory = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_CONTAINER_CATEGORY_KEY);
                                                    String dateCreated = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_DATE_CREATED_KEY);
                                                    String authorUserId = ""+ documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_PAGES_CREATED_KEY);
                                                    long totalNumberOfPages = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY)!=null ?documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY) :0L;
                                                    long totalNumberOfFolders =  documentSnapshot.get(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY)!=null ?documentSnapshot.getLong(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY) :0L;
                                                    String tutorialCoverPhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY);



                                                    tutorialFetchListener.onSuccess(new TutorialDataModel(
                                                                                     tutorialName,
                                                                                     tutorialId,
                                                                                     dateCreated,
                                                                                     totalNumberOfPages,
                                                                                     totalNumberOfFolders,
                                                                                     totalNumberOfTutorialVisitor,
                                                                                     totalNumberOfTutorialReach,
                                                                                     authorUserId,
                                                                                     libraryId,
                                                                                     tutorialCoverPhotoDownloadUrl,
                                                                                     totalNumberOfOneStarRate,
                                                                                     totalNumberOfTwoStarRate,
                                                                                     totalNumberOfThreeStarRate,
                                                                                     totalNumberOfFourStarRate,
                                                                                     totalNumberOfFiveStarRate
                                                                                     ));
                                                }
                                            });
                                }
                            });

                        }
                    }
                });
    }

    private  void displayTutorial(TutorialDataModel tutorialDataModel){
        //<Uncomment and implement>
        /*
        if(getContext() != null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View tutorialView = layoutInflater.inflate(R.layout.tutorial_custom_view, null);
            TextView tutorialNameTextView = tutorialView.findViewById(R.id.tutorialNameTextViewId);
            TextView authorNameTextView = tutorialView.findViewById(R.id.authorNameTextViewId);
            ImageView tutorialCoverPhotoImageView = tutorialView.findViewById(R.id.tutorialCoverPhotoImageViewId);
            tutorialNameTextView.setText(tutorialDataModel.getTutorialName());
            Glide.with(getContext())
                    .load(tutorialDataModel.getTutorialCoverPhotoDownloadUrl())
                    .centerCrop()
                    .into(tutorialCoverPhotoImageView);

            //get author name
            GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(tutorialDataModel.getAuthorId())
                    .collection(GlobalConfig.USER_PROFILE_KEY)
                    .document(tutorialDataModel.getAuthorId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String authorName =""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
//                            String authorProfilePhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);

                                authorNameTextView.setText(authorName);


                        }
                    });
            tutorialLinearLayout.addView(tutorialView);
        }
        */
        }

    private  void displayLibrary(LibraryDataModel libraryDataModel){
        //<Uncomment and implement>
        /*
        if(getContext() != null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View libraryView = layoutInflater.inflate(R.layout.library_custom_view, null);
            TextView libraryNameTextView = libraryView.findViewById(R.id.libraryNameTextViewId);
            TextView authorNameTextView = libraryView.findViewById(R.id.authorNameTextViewId);
            ImageView libraryCoverPhotoImageView = libraryView.findViewById(R.id.libraryCoverPhotoImageViewId);
            libraryNameTextView.setText(libraryDataModel.getLibraryName());
            Glide.with(getContext())
                    .load(libraryDataModel.getLibraryCoverPhotoDownloadUrl())
                    .centerCrop()
                    .into(libraryCoverPhotoImageView);

            //get author name
            GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(libraryDataModel.getAuthorUserId())
                    .collection(GlobalConfig.USER_PROFILE_KEY)
                    .document(libraryDataModel.getAuthorUserId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String authorName =""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
//                            String authorProfilePhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);

                                authorNameTextView.setText(authorName);


                        }
                    });
            libraryLinearLayout.addView(libraryView);
        }
        */
        }

    private void displayPopularAuthor(final String authorName,final String authorProfilePhotoDownloadUrl,final long totalNumberOfLibrary ){
        //<Uncomment and implement>
        /*
        if(getContext() != null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View authorView = layoutInflater.inflate(R.layout.popular_author_custom_view, null);
            TextView authorNameTextView = authorView.findViewById(R.id.authorNameTextViewId);
            TextView totalNumberOfLibraryTextView = authorView.findViewById(R.id.totalNumberOfLibraryTextViewId);
            ImageView authorProfilePhotoImageView = authorView.findViewById(R.id.authorProfilePhotoImageViewId);
            authorNameTextView.setText(authorName);
            totalNumberOfLibraryTextView.setText(String.valueOf(totalNumberOfLibrary));
            Glide.with(getContext())
                    .load(authorProfilePhotoDownloadUrl)
                    .centerCrop()
                    .into(authorProfilePhotoImageView);
            popularAuthorLinearLayout.addView(authorView);
        }
        */
    }

    interface PopularAuthorFetchListener{
        void onSuccess(final String authorName,final String authorProfilePhotoDownloadUrl,final long totalNumberOfLibrary );
        void onFailed(String errorMessage);
    }

    interface LibraryFetchListener{
        void onSuccess(LibraryDataModel libraryDataModel);
        void onFailed(String errorMessage);
    }

    interface TutorialFetchListener{
        void onSuccess(TutorialDataModel tutorialDataModel);
        void onFailed(String errorMessage);
    }


}