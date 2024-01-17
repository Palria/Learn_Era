package com.palria.learnera.models;

import java.util.ArrayList;

public class QuizParticipantDatamodel {
    String participantId;
    String dateJoined;
    boolean isSubmitted;
    boolean isAuthor;
    String timeSubmitted;
    ArrayList<ArrayList<String>> answerList;

    public QuizParticipantDatamodel(boolean isAuthor,String participantId,String dateJoined,boolean isSubmitted,String timeSubmitted,ArrayList<ArrayList<String>> answerList){
         this.participantId=participantId;
         this.dateJoined=dateJoined;
         this.isSubmitted=isSubmitted;
         this.isAuthor=isAuthor;
         this.timeSubmitted=timeSubmitted;
         this.answerList=answerList;

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

    public boolean isAuthor() {
        return isAuthor;
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
}
