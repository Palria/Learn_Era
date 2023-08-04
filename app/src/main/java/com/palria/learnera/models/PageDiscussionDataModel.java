package com.palria.learnera.models;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class PageDiscussionDataModel implements Serializable {

    private String discussionId;
    private String discussionPosterId;
    private String description;
    private String coverDownloadUrl;
    private String pageId;
    private String authorId;
    private String parentDiscussionId;
    private String tutorialId;
    private String folderId;
    private boolean  isTutorialPage;
    private boolean  hasParentDiscussion;
    private boolean  hasReplies;
    private boolean  isHiddenByAuthor;
    private boolean  isHiddenByPoster;
    private String dateCreated;
    private long totalReplies;
    private long totalLikes;
//    private long totalDisLikes;
    private ArrayList repliersIdList;
    private ArrayList likersIdList;
//    private ArrayList disLikersIdList;

    public PageDiscussionDataModel(
             String discussionId,
             String discussionPosterId,
             String description,
             String coverDownloadUrl,
             String pageId,
             String authorId,
             String parentDiscussionId,
             String tutorialId,
             String folderId,
             boolean  isTutorialPage,
             boolean  hasParentDiscussion,
             boolean  hasReplies,
             boolean  isHiddenByAuthor,
             boolean  isHiddenByPoster,
             String dateCreated,
             long totalReplies,
             long totalLikes,
//             long totalDisLikes,
             ArrayList repliersIdList,
             ArrayList likersIdList
//             ArrayList disLikersIdList
    ){
        this.discussionId = discussionId;
        this.discussionPosterId = discussionPosterId;
        this.description = description;
        this.coverDownloadUrl = coverDownloadUrl;
        this.pageId = pageId;
        this.authorId = authorId;
        this.parentDiscussionId = parentDiscussionId;
        this.tutorialId = tutorialId;
        this.folderId = folderId;
        this.isTutorialPage = isTutorialPage;
        this.hasParentDiscussion = hasParentDiscussion;
        this.hasReplies = hasReplies;
        this.isHiddenByAuthor = isHiddenByAuthor;
        this.isHiddenByPoster = isHiddenByPoster;
        this.totalReplies = totalReplies;
        this.totalLikes = totalLikes;
//        this.totalDisLikes = totalDisLikes;
        this.repliersIdList = repliersIdList;
        this.likersIdList = likersIdList;
//        this.disLikersIdList = disLikersIdList;
        this.dateCreated = dateCreated;

    }

    public String getDiscussionId() {
        return discussionId;
    }
    public String getDiscussionPosterId() {
        return discussionPosterId;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverDownloadUrl() {
        return coverDownloadUrl;
    }

    public String getPageId() {
        return pageId;
    }
    public String getAuthorId() {
        return authorId;
    }
    public String getParentDiscussionId() {
        return parentDiscussionId;
    }

    public String getTutorialId() {
        return tutorialId;
    }
    public String getFolderId() {
        return folderId;
    }
    public String getDateCreated() {
        return dateCreated;
    }
    public boolean isTutorialPage() {
        return isTutorialPage;
    }

    public boolean hasParentDiscussion() {
        return hasParentDiscussion;
    }
    public boolean hasReplies() {
        return hasReplies;
    }
    public boolean isHiddenByAuthor() {
        return isHiddenByAuthor;
    }

    public boolean isHiddenByPoster() {
        return isHiddenByPoster ;
    }

    public long getTotalReplies() {
        return totalReplies;
    }

    public long getTotalLikes() {
        return totalLikes;
    }

//    public long getTotalDisLikes() {
//        return totalDisLikes;
//    }

    public ArrayList getRepliersIdList() {
        return repliersIdList;
    }

    public ArrayList getLikersIdList() {
        return likersIdList;
    }

//    public ArrayList getDisLikersIdList() {
//        return disLikersIdList;
//    }
}
