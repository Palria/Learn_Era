package com.palria.learnera.models;

import java.util.ArrayList;

public class QuizParticipantDatamodel {
    String participantId;
    String dateJoined;
    boolean isSubmitted;
    boolean isAuthorAnswer;
    String timeSubmitted;
    int totalScores;
    boolean isRewardClaimed;
    ArrayList<ArrayList<String>> answerList;
    boolean isAnswerMarkedByAuthor;


    public QuizParticipantDatamodel(boolean isAuthorAnswer,String participantId,String dateJoined,boolean isSubmitted,String timeSubmitted,int totalScores,boolean isRewardClaimed,ArrayList<ArrayList<String>> answerList, boolean isAnswerMarkedByAuthor){
         this.participantId=participantId;
         this.dateJoined=dateJoined;
         this.isSubmitted=isSubmitted;
         this.isAuthorAnswer=isAuthorAnswer;
         this.timeSubmitted=timeSubmitted;
         this.answerList=answerList;
         this.totalScores=totalScores;
         this.isRewardClaimed=isRewardClaimed;
         this.isAnswerMarkedByAuthor=isAnswerMarkedByAuthor;

    }

    public String getParticipantId() {
        return participantId;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public boolean isAuthorAnswer() {
        return isAuthorAnswer;
    }

    public String getTimeSubmitted() {
        return timeSubmitted;
    }

    public ArrayList<ArrayList<String>> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(ArrayList<ArrayList<String>> answerList) {
        this.answerList = answerList;
    }

    public int getTotalScores() {
        return totalScores;
    }
    public boolean isRewardClaimed() {
        return isRewardClaimed;
    }

    public boolean isAnswerMarkedByAuthor() {
        return isAnswerMarkedByAuthor;
    }
}
