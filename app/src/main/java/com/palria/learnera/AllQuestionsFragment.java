package com.palria.learnera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.QuestionRcvAdapter;
import com.palria.learnera.adapters.UsersRCVAdapter;
import com.palria.learnera.models.QuestionDataModel;
import com.palria.learnera.models.UsersDataModel;

import java.util.ArrayList;








public class AllQuestionsFragment extends Fragment {
QuestionRcvAdapter questionRCVAdapter;
ArrayList<QuestionDataModel> questionDataModels = new ArrayList<>();
RecyclerView recyclerView;


    boolean isFromSearchContext = false;
    String searchKeyword = "";

    public AllQuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            isFromSearchContext = getArguments().getBoolean(GlobalConfig.IS_FROM_SEARCH_CONTEXT_KEY,false);
            searchKeyword = getArguments().getString(GlobalConfig.SEARCH_KEYWORD_KEY,"");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_all_questions, container, false);
        initUI(parentView);
        fetchQuestions(new QuestionFetchListener() {
            @Override
            public void onSuccess(QuestionDataModel questionDataModel) {

                questionDataModels.add(questionDataModel);
                questionRCVAdapter.notifyItemChanged(questionDataModels.size());

            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });

        return parentView;
    }
//
private void initUI(View parentView){
        recyclerView = parentView.findViewById(R.id.questionsRecyclerViewId);
        questionRCVAdapter = new QuestionRcvAdapter(questionDataModels,getContext());
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
    recyclerView.setAdapter(questionRCVAdapter);
    recyclerView.setLayoutManager(layoutManager);


}
    private void fetchQuestions(QuestionFetchListener questionFetchListener){
//        Query authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereEqualTo(GlobalConfig.IS_USER_AUTHOR_KEY,true).whereArrayContains(GlobalConfig.AUTHOR_CATEGORY_TAG_ARRAY_KEY,categoryTag).orderBy(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY);

        Query authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUESTIONS_KEY);
        if (isFromSearchContext){
                authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUESTIONS_KEY).whereArrayContains(GlobalConfig.QUESTION_SEARCH_ANY_MATCH_KEYWORD_KEY,searchKeyword);
        }


            authorQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        questionFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){


                            String questionId  = documentSnapshot.getId();
                            final String questionBody = ""+ documentSnapshot.get(GlobalConfig.QUESTION_BODY_KEY);
                            final String photoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.QUESTION_PHOTO_DOWNLOAD_URL_KEY);
                            final String authorId = ""+ documentSnapshot.get(GlobalConfig.AUTHOR_ID_KEY);
                             String dateAsked =  documentSnapshot.get(GlobalConfig.USER_PROFILE_DATE_CREATED_TIME_STAMP_KEY)!=null ?  documentSnapshot.getTimestamp(GlobalConfig.USER_PROFILE_DATE_CREATED_TIME_STAMP_KEY).toDate()+""  :"Moments ago";
                            if(dateAsked.length()>10){
                                dateAsked = dateAsked.substring(0,10);
                            }
                            final String finalDateAsked = dateAsked;
                            final boolean isPublic =  documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_PUBLIC_KEY) :false;
                            final boolean isClosed =  documentSnapshot.get(GlobalConfig.IS_CLOSED_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_CLOSED_KEY) :false;
                            final boolean isPhotoIncluded =  documentSnapshot.get(GlobalConfig.IS_PHOTO_INCLUDED_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_PHOTO_INCLUDED_KEY) :false;
                            long numOfAnswers = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ANSWER_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ANSWER_KEY) : 0L;
                            long numOfViews = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY) : 0L;


                            questionFetchListener.onSuccess(new QuestionDataModel(
                                    questionId,
                                    questionBody,
                                    photoDownloadUrl,
                                    dateAsked,
                                    authorId,
                                    numOfAnswers,
                                    numOfViews,
                                    isPublic,
                                    isClosed,
                                    isPhotoIncluded


    ));

                        }
                    }
                });
    }

    interface QuestionFetchListener{
        void onSuccess(QuestionDataModel questionDataModel);
        void onFailed(String errorMessage);
    }

}