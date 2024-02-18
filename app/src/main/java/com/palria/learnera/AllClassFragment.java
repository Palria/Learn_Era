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
import com.palria.learnera.adapters.ClassRcvAdapter;
import com.palria.learnera.models.ClassDataModel;
import com.palria.learnera.models.ClassDataModel;

import java.util.ArrayList;

public class AllClassFragment extends Fragment {
ClassRcvAdapter classRCVAdapter;
ArrayList<ClassDataModel> classDataModelArrayList = new ArrayList<>();
RecyclerView recyclerView;


    boolean isShowUserCreatedClass = false;
    boolean isFromStudentProfile = false;
    boolean isFromSearchContext = false;
    String searchKeyword = "";
    String studentId = "";
    String authorId = "";

    boolean isOpenStartedClass = false;
    boolean isOpenClosedClass = false;

    public AllClassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            isShowUserCreatedClass = getArguments().getBoolean(GlobalConfig.IS_SHOW_USER_CREATED_CLASS_KEY,false);
            isFromStudentProfile = getArguments().getBoolean(GlobalConfig.IS_FROM_STUDENT_PROFILE_KEY,false);
            isFromSearchContext = getArguments().getBoolean(GlobalConfig.IS_FROM_SEARCH_CONTEXT_KEY,false);
            isOpenStartedClass = getArguments().getBoolean(GlobalConfig.IS_OPEN_STARTED_CLASS_KEY,false);
            isOpenClosedClass = getArguments().getBoolean(GlobalConfig.IS_OPEN_CLOSED_CLASS_KEY,false);
            searchKeyword = getArguments().getString(GlobalConfig.SEARCH_KEYWORD_KEY,"");
            studentId = getArguments().getString(GlobalConfig.STUDENT_ID_KEY,"");
            authorId = getArguments().getString(GlobalConfig.AUTHOR_ID_KEY,"");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_all_class, container, false);
        initUI(parentView);
        fetchClass(new ClassFetchListener() {
            @Override
            public void onSuccess(ClassDataModel classDataModel) {

                classDataModelArrayList.add(classDataModel);
                classRCVAdapter.notifyItemChanged(classDataModelArrayList.size());

            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });

        return parentView;
    }
//
private void initUI(View parentView){
        recyclerView = parentView.findViewById(R.id.classRecyclerListViewId);
    classRCVAdapter = new ClassRcvAdapter(classDataModelArrayList,getContext(),false);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
    recyclerView.setAdapter(classRCVAdapter);
    recyclerView.setLayoutManager(layoutManager);


}
    private void fetchClass(ClassFetchListener classFetchListener){
//        Query classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereEqualTo(GlobalConfig.IS_USER_AUTHOR_KEY,true).whereArrayContains(GlobalConfig.AUTHOR_CATEGORY_TAG_ARRAY_KEY,categoryTag).orderBy(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY);

        Query classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereEqualTo(GlobalConfig.IS_PUBLIC_KEY,true);

         if (isFromSearchContext){
             classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereArrayContains(GlobalConfig.CLASS_SEARCH_ANY_MATCH_KEYWORD_KEY,searchKeyword);
                if(isOpenStartedClass){
                    classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereEqualTo(GlobalConfig.IS_STARTED_KEY,true).whereArrayContains(GlobalConfig.CLASS_SEARCH_ANY_MATCH_KEYWORD_KEY,searchKeyword);

                }
//                else if(!isOpenStartedClass){
//                    classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereEqualTo(GlobalConfig.IS_STARTED_KEY,false).whereArrayContains(GlobalConfig.CLASS_SEARCH_ANY_MATCH_KEYWORD_KEY,searchKeyword);
//
//                }
             else if(isOpenClosedClass){
                    classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereEqualTo(GlobalConfig.IS_CLOSED_KEY,true).whereArrayContains(GlobalConfig.IS_CLOSED_KEY,searchKeyword);

             }

        }
         else if(isFromStudentProfile){
             classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereArrayContains(GlobalConfig.STUDENTS_LIST_KEY,studentId);

//             if(isOpenStartedClass){
//                 classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereEqualTo(GlobalConfig.IS_STARTED_KEY,true).whereArrayContains(GlobalConfig.STUDENTS_LIST_KEY,studentId);
//
//             }
//             else if(!isOpenStartedClass){
//                 classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereEqualTo(GlobalConfig.IS_STARTED_KEY,false).whereArrayContains(GlobalConfig.STUDENTS_LIST_KEY,studentId);
//
//             }
//             else if(isOpenCompletedClass){
//                 classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereEqualTo(GlobalConfig.IS_CLASS_MARKED_COMPLETED_KEY,true).whereArrayContains(GlobalConfig.STUDENTS_LIST_KEY,studentId);
//
//             }
         }
         else if(isShowUserCreatedClass){
             classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereEqualTo(GlobalConfig.AUTHOR_ID_KEY,authorId);

             if(isOpenStartedClass){
                 classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereEqualTo(GlobalConfig.IS_STARTED_KEY,true).whereEqualTo(GlobalConfig.AUTHOR_ID_KEY,authorId);

             }
             else if(!isOpenStartedClass){
                 classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereEqualTo(GlobalConfig.IS_STARTED_KEY,false).whereEqualTo(GlobalConfig.AUTHOR_ID_KEY,authorId);

             }
             else if(isOpenClosedClass){
                 classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereEqualTo(GlobalConfig.IS_CLOSED_KEY,true).whereEqualTo(GlobalConfig.AUTHOR_ID_KEY,authorId);

             }
         }
         else{
  if(isOpenClosedClass){
                 classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereEqualTo(GlobalConfig.IS_CLOSED_KEY,true).whereEqualTo(GlobalConfig.IS_PUBLIC_KEY,true);

             }
  else if(isOpenStartedClass){
                  classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereEqualTo(GlobalConfig.IS_STARTED_KEY,true).whereEqualTo(GlobalConfig.IS_CLOSED_KEY,false).whereEqualTo(GlobalConfig.IS_PUBLIC_KEY,true);

             }
  else if(!isOpenStartedClass){
                 classQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).whereEqualTo(GlobalConfig.IS_STARTED_KEY,false).whereEqualTo(GlobalConfig.IS_PUBLIC_KEY,true);

             }

         }

        classQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        classFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        classDataModelArrayList.clear();
                        classRCVAdapter.notifyDataSetChanged();


                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String classId = ""+ documentSnapshot.getId();
                            String authorId = ""+ documentSnapshot.get(GlobalConfig.AUTHOR_ID_KEY);
                            String classTitle = ""+ documentSnapshot.get(GlobalConfig.CLASS_TITLE_KEY);
                            String classDescription = ""+ documentSnapshot.get(GlobalConfig.CLASS_DESCRIPTION_KEY);
                            long totalClassFeeCoins =  documentSnapshot.get(GlobalConfig.TOTAL_CLASS_FEE_COINS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_CLASS_FEE_COINS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_CLASS_FEE_COINS_KEY) : 0L;
                            long totalStudents =  documentSnapshot.get(GlobalConfig.TOTAL_STUDENTS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_STUDENTS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_STUDENTS_KEY) : 0L;
                            long totalViews =  documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY) : 0L;
                            boolean isPublic =  documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY) != null && documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY) instanceof Boolean ? documentSnapshot.getBoolean(GlobalConfig.IS_PUBLIC_KEY) : true;
                            boolean isClosed =  documentSnapshot.get(GlobalConfig.IS_CLOSED_KEY) != null && documentSnapshot.get(GlobalConfig.IS_CLOSED_KEY) instanceof Boolean ? documentSnapshot.getBoolean(GlobalConfig.IS_CLOSED_KEY) : false;
                            boolean isStarted =  documentSnapshot.get(GlobalConfig.IS_STARTED_KEY) != null && documentSnapshot.get(GlobalConfig.IS_STARTED_KEY) instanceof Boolean ? documentSnapshot.getBoolean(GlobalConfig.IS_STARTED_KEY) : false;

                            ArrayList startDateList =  documentSnapshot.get(GlobalConfig.CLASS_START_DATE_LIST_KEY) != null && documentSnapshot.get(GlobalConfig.CLASS_START_DATE_LIST_KEY) instanceof ArrayList ? (ArrayList) documentSnapshot.get(GlobalConfig.CLASS_START_DATE_LIST_KEY) : new ArrayList();
                            ArrayList endDateList =  documentSnapshot.get(GlobalConfig.CLASS_END_DATE_LIST_KEY) != null && documentSnapshot.get(GlobalConfig.CLASS_END_DATE_LIST_KEY) instanceof ArrayList ? (ArrayList) documentSnapshot.get(GlobalConfig.CLASS_END_DATE_LIST_KEY) : new ArrayList();
                            ArrayList studentsList =  documentSnapshot.get(GlobalConfig.STUDENTS_LIST_KEY) != null && documentSnapshot.get(GlobalConfig.STUDENTS_LIST_KEY) instanceof ArrayList ? (ArrayList) documentSnapshot.get(GlobalConfig.STUDENTS_LIST_KEY) : new ArrayList();
                            String dateCreated = documentSnapshot.get(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY) != null && documentSnapshot.get(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY) instanceof Timestamp ? "" + documentSnapshot.getTimestamp(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY).toDate() : "Moment ago";
                            if (dateCreated.length() > 10) {
                                dateCreated = dateCreated.substring(0, 10);
                            }
                            String dateEdited = documentSnapshot.get(GlobalConfig.DATE_EDITED_TIME_STAMP_KEY) != null && documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY) instanceof Timestamp ? "" + documentSnapshot.getTimestamp(GlobalConfig.DATE_EDITED_TIME_STAMP_KEY).toDate() : "Moment ago";
                            if (dateEdited.length() > 10) {
                                dateEdited = dateEdited.substring(0, 10);
                            }
//                            boolean isClassMarkedCompleted = documentSnapshot.get(GlobalConfig.IS_CLASS_MARKED_COMPLETED_KEY)!=null? documentSnapshot.getBoolean(GlobalConfig.IS_CLASS_MARKED_COMPLETED_KEY) :false;

                            classFetchListener.onSuccess(new ClassDataModel(
                                    classId,
                                    authorId,
                                    classTitle,
                                    classDescription,
                                    (int)totalClassFeeCoins,
                                    dateCreated,
                                    dateEdited,
                                    totalStudents,
                                    totalViews,
                                    isPublic,
                                    isClosed,
                                    isStarted,
                                    startDateList,
                                    endDateList,
                                    studentsList
                            ));

                        }

                    }
                });
    }

    interface ClassFetchListener{
        void onSuccess(ClassDataModel usersDataModel);
        void onFailed(String errorMessage);
    }

}