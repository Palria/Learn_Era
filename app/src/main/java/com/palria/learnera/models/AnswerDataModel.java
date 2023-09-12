package com.palria.learnera.models;

import java.io.Serializable;
import java.util.ArrayList;

public class AnswerDataModel implements Serializable {

    private String answerId;
    private String questionId;
    private String contributorId;
    private String answer;
    private String answerPhotoDownloadUrl;
    private String authorId;
    private String  parentId;
    private boolean  hasParent;
    private boolean  isAnswer;
    private boolean  hasReplies;
    private boolean  isPhotoIncluded;
    private boolean  isHiddenByAuthor;
    private boolean  isHiddenByContributor;
    private String dateCreated;
    private long totalReplies;
    private long totalUpVotes;
    private long totalDownVotes;
    private ArrayList repliersIdList;
    private ArrayList upVotersIdList;
    private ArrayList downVotersIdList;

    public AnswerDataModel(
             String answerId,
             String questionId,
             String contributorId,
             String answer,
             String answerPhotoDownloadUrl,
             String authorId,
             String  parentId,
             boolean  hasParent,
             boolean  isAnswer,
             boolean  hasReplies,
             boolean  isPhotoIncluded,
             boolean  isHiddenByAuthor,
             boolean  isHiddenByContributor,
             String dateCreated,
             long totalReplies,
             long totalUpVotes,
             long totalDownVotes,
             ArrayList repliersIdList,
             ArrayList upVotersIdList,
             ArrayList downVotersIdList
    ){
        this.answerId = answerId;
        this.questionId = questionId;
        this.contributorId = contributorId;
        this.answer = answer;
        this.answerPhotoDownloadUrl = answerPhotoDownloadUrl;
        this.authorId = authorId;
        this.parentId = parentId;
        this.hasParent = hasParent;
        this.isPhotoIncluded = isPhotoIncluded;
        this.isHiddenByAuthor = isHiddenByAuthor;
        this.isHiddenByContributor = isHiddenByContributor;
        this.isAnswer = isAnswer;
        this.hasReplies = hasReplies;
        this.dateCreated = dateCreated;
        this.totalReplies = totalReplies;
        this.totalUpVotes = totalUpVotes;
        this.totalDownVotes = totalDownVotes;
        this.repliersIdList = repliersIdList;
        this.upVotersIdList = upVotersIdList;
        this.downVotersIdList = downVotersIdList;
        this.dateCreated = dateCreated;

    }

    public String getAnswerId() {
        return answerId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getContributorId() {
        return contributorId;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAnswerPhotoDownloadUrl() {
        return answerPhotoDownloadUrl;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getParentId() {
        return parentId;
    }

    public boolean hasParent() {
        return hasParent;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public boolean hasReplies() {
        return hasReplies;
    }

    public boolean isPhotoIncluded() {
        return isPhotoIncluded;
    }

    public boolean isHiddenByAuthor() {
        return isHiddenByAuthor;
    }

    public boolean isHiddenByContributor() {
        return isHiddenByContributor;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public long getTotalReplies() {
        return totalReplies;
    }

    public long getTotalUpVotes() {
        return totalUpVotes;
    }

    public void setTotalUpVotes(long totalUpVotes) {
        this.totalUpVotes = totalUpVotes;
    }

    public long getTotalDownVotes() {
        return totalDownVotes;
    }

    public void setTotalDownVotes(long totalDownVotes) {
        this.totalDownVotes = totalDownVotes;
    }

    public ArrayList<String> getRepliersIdList() {
        return repliersIdList;
    }

    public ArrayList<String> getUpVotersIdList() {
        return upVotersIdList;
    }
    public ArrayList<String> getDownVotersIdList() {
        return downVotersIdList;
    }
    public void recordUpVotes(){

    }
    public void recordDownVotes(){

    }
}
