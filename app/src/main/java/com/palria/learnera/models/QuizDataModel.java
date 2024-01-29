package com.palria.learnera.models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.palria.learnera.GlobalConfig;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class QuizDataModel implements Serializable {

    private String quizId;
    private String authorId;
    private String quizTitle;
    private String quizDescription;
    private int totalQuizFeeCoins;
    private int totalQuizRewardCoins;
    private String dateCreated;
    private String dateEdited;
    private long totalQuestions;
    private long totalTimeLimit;
    private long totalParticipants;
    private long totalViews;
    private boolean isPublic;
    private boolean isClosed;
    ArrayList<ArrayList> questionList;
    ArrayList<Long> startDateList;
    ArrayList<Long> endDateList;
    private ArrayList<String> participantsList;
    //WETHER AUTHOR'S ANSWER IS SAVED
    private boolean isAnswerSaved;
    private boolean isQuizMarkedCompleted;
    private String timeAnswerSubmitted;
    ArrayList<String> savedParticipantScoresList;
    private int totalQuizScore;
    private int totalTheory;
    private int totalObjective;
    private ArrayList<ArrayList<String>> authorSavedAnswersList = new ArrayList<>();
    public QuizDataModel(){}
    public QuizDataModel(
             String quizId,
             String authorId,
             String quizTitle,
             String quizDescription,
             int totalQuizFeeCoins,
             int totalQuizRewardCoins,
             String dateCreated,
             String dateEdited,
             long totalQuestions,
             long totalTimeLimit,
             long totalParticipants,
             long totalViews,
             boolean isPublic,
             boolean isClosed,
             ArrayList<ArrayList> questionList,
             ArrayList<Long> startDateList,
             ArrayList<Long> endDateList,
             ArrayList<String> participantsList,
             boolean isAnswerSaved,
             boolean isQuizMarkedCompleted,
             String timeAnswerSubmitted,
             ArrayList<ArrayList<String>> authorSavedAnswersList,
             ArrayList<String> savedParticipantScoresList,
             int totalQuizScore,
             int totalTheory,
             int totalObjective
    ) {
        this.quizId = quizId;
        this.authorId = authorId;
        this.quizTitle = quizTitle;
        this.quizDescription = quizDescription;
        this.totalQuizFeeCoins = totalQuizFeeCoins;
        this.totalQuizRewardCoins = totalQuizRewardCoins;
        this.dateCreated = dateCreated;
        this.dateEdited = dateEdited;
        this.totalQuestions = totalQuestions;
        this.totalTimeLimit = totalTimeLimit;
        this.totalParticipants = totalParticipants;
        this.totalViews = totalViews;
        this.isPublic = isPublic;
        this.isClosed = isClosed;
        this.questionList = questionList;
        this.startDateList = startDateList;
        this.endDateList = endDateList;
        this.participantsList = participantsList;
        this.isAnswerSaved = isAnswerSaved;
        this.isQuizMarkedCompleted = isQuizMarkedCompleted;
        this.timeAnswerSubmitted = timeAnswerSubmitted;
        this.authorSavedAnswersList = authorSavedAnswersList;
        this.savedParticipantScoresList = savedParticipantScoresList;
        this.totalQuizScore = totalQuizScore;
        this.totalTheory = totalTheory;
        this.totalObjective = totalObjective;
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

    public int getTotalQuizFeeCoins() {
        return totalQuizFeeCoins;
    }

    public int getTotalQuizRewardCoins() {
        return totalQuizRewardCoins;
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

    public ArrayList<Long> getStartDateList() {
        return startDateList;
    }
    public ArrayList<Long> getEndDateList() {
        return endDateList;
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
    public boolean isQuizMarkedCompleted() {
        return isQuizMarkedCompleted;
    }

    public String getTimeAnswerSubmitted() {
        return timeAnswerSubmitted;
    }

    public ArrayList<ArrayList<String>> getAuthorSavedAnswersList() {
        return authorSavedAnswersList;
    }

    public ArrayList<String> getSavedParticipantScoresList() {
if (savedParticipantScoresList!=null){
        savedParticipantScoresList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2) * -1;
            }
        });
    }
        return savedParticipantScoresList;
    }

    public int getTotalQuizScore() {
        return totalQuizScore;
    }
    public int getTotalTheory() {
        return totalTheory;
    }
    public int getTotalObjective() {
        return totalObjective;
    }
}
