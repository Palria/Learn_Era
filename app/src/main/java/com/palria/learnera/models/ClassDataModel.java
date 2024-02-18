package com.palria.learnera.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class ClassDataModel implements Serializable {

    private String classId;
    private String authorId;
    private String classTitle;
    private String classDescription;
    private int totalClassFeeCoins;
    private String dateCreated;
    private String dateEdited;
    private long totalStudents;
    private long totalViews;
    private boolean isPublic;
    private boolean isClosed;
    private boolean isStarted;
    ArrayList<Long> startDateList;
    ArrayList<Long> endDateList;
    private ArrayList<String> studentsList;
    public ClassDataModel(){}
    public ClassDataModel(
             String classId,
             String authorId,
             String classTitle,
             String classDescription,
             int totalClassFeeCoins,
             String dateCreated,
             String dateEdited,
             long totalStudents,
             long totalViews,
             boolean isPublic,
             boolean isClosed,
             boolean isStarted,
             ArrayList<Long> startDateList,
             ArrayList<Long> endDateList,
             ArrayList<String> studentsList
    ) {
        this.classId = classId;
        this.authorId = authorId;
        this.classTitle = classTitle;
        this.classDescription = classDescription;
        this.totalClassFeeCoins = totalClassFeeCoins;
        this.dateCreated = dateCreated;
        this.dateEdited = dateEdited;
        this.totalStudents = totalStudents;
        this.totalViews = totalViews;
        this.isPublic = isPublic;
        this.isClosed = isClosed;
        this.isStarted = isStarted;
        this.startDateList = startDateList;
        this.endDateList = endDateList;
        this.studentsList = studentsList;
    }

    public String getClassId() {
        return classId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public int getTotalClassFeeCoins() {
        return totalClassFeeCoins;
    }


    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateEdited() {
        return dateEdited;
    }


    public long getTotalStudents() {
        return totalStudents;
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
    public boolean isStarted() {
        return isStarted;
    }


    public ArrayList<Long> getStartDateList() {
        return startDateList;
    }
    public ArrayList<Long> getEndDateList() {
        return endDateList;
    }

    public ArrayList<String> getStudentsList() {
        return studentsList;
    }

    public void setStudentsList(ArrayList<String> studentsList) {
        this.studentsList = studentsList;
    }

}
