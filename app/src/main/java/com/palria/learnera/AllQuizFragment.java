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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.QuizRcvAdapter;
import com.palria.learnera.adapters.UsersRCVAdapter;
import com.palria.learnera.models.QuizDataModel;
import com.palria.learnera.models.UsersDataModel;

import java.util.ArrayList;

public class AllQuizFragment extends Fragment {
QuizRcvAdapter quizRCVAdapter;
ArrayList<QuizDataModel> quizDataModelArrayList = new ArrayList<>();
RecyclerView recyclerView;


    boolean isFromSearchContext = false;
    String searchKeyword = "";

    public AllQuizFragment() {
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
        View parentView = inflater.inflate(R.layout.fragment_all_quiz, container, false);
        initUI(parentView);
        fetchQuiz(new QuizFetchListener() {
            @Override
            public void onSuccess(QuizDataModel quizDataModel) {

                quizDataModelArrayList.add(quizDataModel);
                quizRCVAdapter.notifyItemChanged(quizDataModelArrayList.size());

            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });

        return parentView;
    }
//
private void initUI(View parentView){
        recyclerView = parentView.findViewById(R.id.quizRecyclerListViewId);
    quizRCVAdapter = new QuizRcvAdapter(quizDataModelArrayList,getContext(),false);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
    recyclerView.setAdapter(quizRCVAdapter);
    recyclerView.setLayoutManager(layoutManager);


}
    private void fetchQuiz(QuizFetchListener quizFetchListener){
//        Query authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereEqualTo(GlobalConfig.IS_USER_AUTHOR_KEY,true).whereArrayContains(GlobalConfig.AUTHOR_CATEGORY_TAG_ARRAY_KEY,categoryTag).orderBy(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY);

        Query authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).whereEqualTo(GlobalConfig.IS_PUBLIC_KEY,true);

         if (isFromSearchContext){
                authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).whereArrayContains(GlobalConfig.QUIZ_SEARCH_ANY_MATCH_KEYWORD_KEY,searchKeyword);
        }


            authorQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        quizFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String quizId = ""+ documentSnapshot.getId();
                            String authorId = ""+ documentSnapshot.get(GlobalConfig.AUTHOR_ID_KEY);
                            String quizTitle = ""+ documentSnapshot.get(GlobalConfig.QUIZ_TITLE_KEY);
                            String quizDescription = ""+ documentSnapshot.get(GlobalConfig.QUIZ_DESCRIPTION_KEY);
//                            String quizFeeDescription = ""+ documentSnapshot.get(GlobalConfig.QUIZ_FEE_DESCRIPTION_KEY);
//                            String quizRewardDescription = ""+ documentSnapshot.get(GlobalConfig.QUIZ_REWARD_DESCRIPTION_KEY);
                            long totalQuizFeeCoins =  documentSnapshot.get(GlobalConfig.TOTAL_QUIZ_FEE_COINS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_QUIZ_FEE_COINS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_QUIZ_FEE_COINS_KEY) : 0L;
                            long totalQuizRewardCoins =  documentSnapshot.get(GlobalConfig.TOTAL_QUIZ_REWARD_COINS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_QUIZ_REWARD_COINS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_QUIZ_REWARD_COINS_KEY) : 0L;

                            long totalQuestions =  documentSnapshot.get(GlobalConfig.TOTAL_QUESTIONS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_QUESTIONS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_QUESTIONS_KEY) : 0L;

                            long totalQuizScore =  documentSnapshot.get(GlobalConfig.TOTAL_QUIZ_SCORE_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_QUESTIONS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_QUIZ_SCORE_KEY) : 0L;
                            long totalTheoryQuestions =  documentSnapshot.get(GlobalConfig.TOTAL_THEORY_QUESTIONS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_QUESTIONS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_THEORY_QUESTIONS_KEY) : 0L;
                            long totalObjectiveQuestions =  documentSnapshot.get(GlobalConfig.TOTAL_OBJECTIVE_QUESTIONS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_QUESTIONS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_OBJECTIVE_QUESTIONS_KEY) : 0L;

                            long totalTimeLimit =  documentSnapshot.get(GlobalConfig.TOTAL_TIME_LIMIT_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_TIME_LIMIT_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_TIME_LIMIT_KEY) : 0L;
                            long totalParticipants =  documentSnapshot.get(GlobalConfig.TOTAL_PARTICIPANTS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_PARTICIPANTS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_PARTICIPANTS_KEY) : 0L;
                            long totalViews =  documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY) : 0L;
                            boolean isPublic =  documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY) != null && documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY) instanceof Boolean ? documentSnapshot.getBoolean(GlobalConfig.IS_PUBLIC_KEY) : true;
                            boolean isClosed =  documentSnapshot.get(GlobalConfig.IS_CLOSED_KEY) != null && documentSnapshot.get(GlobalConfig.IS_CLOSED_KEY) instanceof Boolean ? documentSnapshot.getBoolean(GlobalConfig.IS_CLOSED_KEY) : false;
                            ArrayList<ArrayList> questionList = new ArrayList();
                            for(int i=0;i<totalQuestions;i++) {
                                ArrayList questionList1 = documentSnapshot.get(GlobalConfig.QUESTION_LIST_KEY+"-" + i) != null && documentSnapshot.get(GlobalConfig.QUESTION_LIST_KEY+"-"+ i) instanceof ArrayList ? (ArrayList) documentSnapshot.get(GlobalConfig.QUESTION_LIST_KEY+"-"+ i) : new ArrayList();
                                questionList.add(questionList1);
                            }
                            ArrayList<String> savedParticipantScoresList =  documentSnapshot.get(GlobalConfig.PARTICIPANT_SCORES_LIST_KEY) != null && documentSnapshot.get(GlobalConfig.PARTICIPANT_SCORES_LIST_KEY) instanceof ArrayList ? (ArrayList) documentSnapshot.get(GlobalConfig.PARTICIPANT_SCORES_LIST_KEY) : new ArrayList();
                            ArrayList<Long> quizStartDateList1 = documentSnapshot.get(GlobalConfig.QUIZ_START_DATE_LIST_KEY)!=null? (ArrayList<Long>) documentSnapshot.get(GlobalConfig.QUIZ_START_DATE_LIST_KEY):new ArrayList<>();
                            ArrayList<Long> quizEndDateList1 = documentSnapshot.get(GlobalConfig.QUIZ_END_DATE_LIST_KEY)!=null? (ArrayList<Long>) documentSnapshot.get(GlobalConfig.QUIZ_END_DATE_LIST_KEY):new ArrayList<>();
                            ArrayList participantsList =  documentSnapshot.get(GlobalConfig.PARTICIPANTS_LIST_KEY) != null  ? (ArrayList) documentSnapshot.get(GlobalConfig.PARTICIPANTS_LIST_KEY) : new ArrayList();
                            String dateCreated = documentSnapshot.get(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY) != null && documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY) instanceof Timestamp ? "" + documentSnapshot.getTimestamp(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY).toDate() : "Moment ago";
                            if (dateCreated.length() > 10) {
                                dateCreated = dateCreated.substring(0, 10);
                            }
                            String dateEdited = documentSnapshot.get(GlobalConfig.DATE_EDITED_TIME_STAMP_KEY) != null && documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY) instanceof Timestamp ? "" + documentSnapshot.getTimestamp(GlobalConfig.DATE_EDITED_TIME_STAMP_KEY).toDate() : "Moment ago";
                            if (dateEdited.length() > 10) {
                                dateEdited = dateEdited.substring(0, 10);
                            }
                            String timeAnswerSubmitted = documentSnapshot.get(GlobalConfig.ANSWER_SUBMITTED_TIME_STAMP_KEY)!=null? documentSnapshot.getTimestamp(GlobalConfig.ANSWER_SUBMITTED_TIME_STAMP_KEY).toDate()+"" :"Undefined";
                            boolean isAnswerSaved = documentSnapshot.get(GlobalConfig.IS_ANSWER_SUBMITTED_KEY)!=null? documentSnapshot.getBoolean(GlobalConfig.IS_ANSWER_SUBMITTED_KEY) :false;

                            ArrayList<ArrayList<String>> authorSavedAnswersList = new ArrayList<>();
                            for(int i=0; i<questionList.size(); i++) {
                                //list of participant answer
                                ArrayList<String> answerItem = documentSnapshot.get(GlobalConfig.ANSWER_LIST_KEY + "-" + i) != null ? (ArrayList<String>) documentSnapshot.get(GlobalConfig.ANSWER_LIST_KEY + "-" + i) : new ArrayList<>();
                                authorSavedAnswersList.add(answerItem);

                            }
                            boolean isQuizMarkedCompleted = documentSnapshot.get(GlobalConfig.IS_QUIZ_MARKED_COMPLETED_KEY)!=null? documentSnapshot.getBoolean(GlobalConfig.IS_QUIZ_MARKED_COMPLETED_KEY) :false;

                            quizFetchListener.onSuccess(new QuizDataModel(
                                    quizId,
                                    authorId,
                                    quizTitle,
                                    quizDescription,
                                    (int)totalQuizFeeCoins,
                                    (int)totalQuizRewardCoins,
                                    dateCreated,
                                    dateEdited,
                                    totalQuestions,
                                    totalTimeLimit,
                                    totalParticipants,
                                    totalViews,
                                    isPublic,
                                    isClosed,
                                    questionList,
                                    quizStartDateList1,
                                    quizEndDateList1,
                                    participantsList,
                                    isAnswerSaved,
                                    isQuizMarkedCompleted,
                                    timeAnswerSubmitted,
                                    authorSavedAnswersList,
                                    savedParticipantScoresList,
                                    (int)totalQuizScore,
                                    (int)totalTheoryQuestions,
                                    (int)totalObjectiveQuestions
                            ));


                        }

                    }
                });
    }

    interface QuizFetchListener{
        void onSuccess(QuizDataModel usersDataModel);
        void onFailed(String errorMessage);
    }

}