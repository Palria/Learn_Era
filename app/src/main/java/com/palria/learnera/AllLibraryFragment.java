package com.palria.learnera;

        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.QuerySnapshot;
        import com.palria.learnera.models.LibraryDataModel;

public class AllLibraryFragment extends Fragment {

    public AllLibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_all_library, container, false);
initUI(parentView);

fetchAllLibrary(new LibraryFetchListener() {
    @Override
    public void onFailed(String errorMessage) {

    }

    @Override
    public void onSuccess(LibraryDataModel libraryDataModel) {

    }
});

        return parentView;
    }

    private void initUI(View parentView){

    }

    private void fetchAllLibrary(LibraryFetchListener libraryFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_LIBRARY_KEY)
                .get()
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
                            String libraryId = documentSnapshot.getId();
                            long totalNumberOfLibraryVisitor = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY) : 0L;
                            long totalNumberOfLibraryReach = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) : 0L;
                            long totalNumberOfOneStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) : 0L;
                            long totalNumberOfTwoStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) : 0L;
                            long totalNumberOfThreeStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) : 0L;
                            long totalNumberOfFourStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) : 0L;
                            long totalNumberOfFiveStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) : 0L;
                            long totalNumberOfTutorials = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) : 0L;


                            String libraryName = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                            String libraryCategory = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_CATEGORY_KEY);
                            String dateCreated = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_KEY);
                            String authorUserId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);
                            String libraryCoverPhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);



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
                    }
                });

    }

    interface LibraryFetchListener{
        void onFailed(String errorMessage);
        void onSuccess(LibraryDataModel libraryDataModel);
    }

}