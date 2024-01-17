package com.palria.learnera.models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.palria.learnera.GlobalConfig;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class QuizDataModel implements Serializable {

    private String quizId;
    private String authorId;
    private String quizTitle;
    private String quizDescription;
    private String quizFeeDescription;
    private String quizRewardDescription;
    private String dateCreated;
    private String dateEdited;
    private long totalQuestions;
    private long totalTimeLimit;
    private long totalParticipants;
    private long totalViews;
    private boolean isPublic;
    private boolean isClosed;
    ArrayList<ArrayList> questionList;
    ArrayList<Long> dateList;
    private ArrayList<String> participantsList;
    private boolean isAnswerSaved;
    private String timeAnswerSubmitted;
    private ArrayList<ArrayList<String>> authorSavedAnswersList = new ArrayList<>();
    public QuizDataModel(){}
    public QuizDataModel(
             String quizId,
             String authorId,
             String quizTitle,
             String quizDescription,
             String quizFeeDescription,
             String quizRewardDescription,
             String dateCreated,
             String dateEdited,
             long totalQuestions,
             long totalTimeLimit,
             long totalParticipants,
             long totalViews,
             boolean isPublic,
             boolean isClosed,
             ArrayList<ArrayList> questionList,
             ArrayList<Long> dateList,
             ArrayList<String> participantsList,
             boolean isAnswerSaved,
             String timeAnswerSubmitted,
             ArrayList<ArrayList<String>> authorSavedAnswersList
            ) {
        this.quizId = quizId;
        this.authorId = authorId;
        this.quizTitle = quizTitle;
        this.quizDescription = quizDescription;
        this.quizFeeDescription = quizFeeDescription;
        this.quizRewardDescription = quizRewardDescription;
        this.dateCreated = dateCreated;
        this.dateEdited = dateEdited;
        this.totalQuestions = totalQuestions;
        this.totalTimeLimit = totalTimeLimit;
        this.totalParticipants = totalParticipants;
        this.totalViews = totalViews;
        this.isPublic = isPublic;
        this.isClosed = isClosed;
        this.questionList = questionList;
        this.dateList = dateList;
        this.participantsList = participantsList;
        this.isAnswerSaved = isAnswerSaved;
        this.timeAnswerSubmitted = timeAnswerSubmitted;
        this.authorSavedAnswersList = authorSavedAnswersList;
    }

    public String getQuizId() {
        return quizId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public String getQuizDescription() {
        return quizDescription;
    }

    public String getQuizFeeDescription() {
        return quizFeeDescription;
    }

    public String getQuizRewardDescription() {
        return quizRewardDescription;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateEdited() {
        return dateEdited;
    }

    public long getTotalQuestions() {
        return totalQuestions;
    }

    public long getTotalTimeLimit() {
        return totalTimeLimit;
    }

    public long getTotalParticipants() {
        return totalParticipants;
    }

    public long getTotalViews() {
        return totalViews;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public ArrayList<ArrayList> getQuestionList() {
        return questionList;
    }

    public ArrayList<Long> getDateList() {
        return dateList;
    }

    public ArrayList<String> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(ArrayList<String> participantsList) {
        this.participantsList = participantsList;
    }

    public boolean isAnswerSaved() {
        return isAnswerSaved;
    }

    public String getTimeAnswerSubmitted() {
        return timeAnswerSubmitted;
    }

    public ArrayList<ArrayList<String>> getAuthorSavedAnswersList() {
        return authorSavedAnswersList;
    }
}
