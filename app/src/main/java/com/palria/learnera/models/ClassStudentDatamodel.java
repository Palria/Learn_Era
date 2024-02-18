package com.palria.learnera.models;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassStudentDatamodel {
    String studentId;
    String dateJoined;

    public ClassStudentDatamodel(String studentId, String dateJoined){
         this.studentId=studentId;
         this.dateJoined=dateJoined;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getDateJoined() {
        return dateJoined;
    }

}
