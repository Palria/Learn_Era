package com.palria.learnera.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.JoinClassActivity;
import com.palria.learnera.ClassActivity;
import com.palria.learnera.R;
import com.palria.learnera.models.ClassDataModel;

import java.util.ArrayList;

public class ClassRcvAdapter extends RecyclerView.Adapter<ClassRcvAdapter.ViewHolder> {

    ArrayList<ClassDataModel> classDataModels;
    Context context;
    boolean isFromHome;

    public ClassRcvAdapter(ArrayList<ClassDataModel> classDataModels, Context context, boolean isFromHome) {
        this.classDataModels = classDataModels;
        this.context = context;
        this.isFromHome = isFromHome;
    }

    @NonNull
    @Override
    public ClassRcvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.class_item_layout, parent, false);
        ClassRcvAdapter.ViewHolder viewHolder = new ClassRcvAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassRcvAdapter.ViewHolder holder, int position) {

        ClassDataModel classDataModel = classDataModels.get(position);
        if (classDataModel.isPublic() || (GlobalConfig.getCurrentUserId().equals(classDataModel.getAuthorId() + ""))) {

            holder.classTitleView.setText(classDataModel.getClassTitle());
//            holder.dateCreated.setText(classDataModel.getDateCreated());
            if(classDataModel.getAuthorId().equals(GlobalConfig.getCurrentUserId())){

                holder.menuButton.setVisibility(View.VISIBLE);
            }

            if(classDataModel.getClassDescription().equals("")){
                holder.descriptionView.setVisibility(View.GONE);
            }else{
                holder.descriptionView.setVisibility(View.VISIBLE);
            }
            holder.descriptionView.setText(""+classDataModel.getClassDescription());
            holder.feeView.setText("Fee "+classDataModel.getTotalClassFeeCoins());
            holder.studentsCountView.setText(""+classDataModel.getTotalStudents() + "");
            holder.joinClassActionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //navigate to JoinClassActivity
                    Intent intent = new Intent(context, JoinClassActivity.class);
                    intent.putExtra(GlobalConfig.CLASS_DATA_MODEL_KEY, classDataModel);
                    context.startActivity(intent);

//                    joinClass(classDataModel);
                }
            });
            if(classDataModel.isClosed()){
                holder.isClosedView.setText("Closed");
                holder.isClosedView.setBackgroundColor(context.getResources().getColor(R.color.error_red,context.getTheme()));
            }else{
                holder.isClosedView.setText("Active");
                holder.isClosedView.setBackground(context.getResources().getDrawable(R.drawable.rounded_border_gray_bg,context.getTheme()));
                holder.isClosedView.setBackgroundColor(context.getResources().getColor(R.color.secondary_app_color,context.getTheme()));
            }

            if(isStarted(classDataModel)){

                holder.isClosedView.setText("Started");
                holder.isClosedView.setBackgroundColor(context.getResources().getColor(R.color.success_green,context.getTheme()));
            }
            holder.startTimeView.setText("Time Undefined");
            holder.endTimeView.setText("Time Undefined");

            ArrayList<Long> classStartDateList1 = classDataModel.getStartDateList();
            if(classStartDateList1.size() >= 5) {
                long classStartYear = classStartDateList1.get(0);
                long classStartMonth = classStartDateList1.get(1);
                long classStartDay = classStartDateList1.get(2);
                long classStartHour = classStartDateList1.get(3);
                long classStartMinute = classStartDateList1.get(4);
                holder.startTimeView.setText("Start Time "+classStartDay + "/" + classStartMonth + "/" + classStartYear + " " + classStartHour + " : " + classStartMinute);
//checks if class has started
                if(GlobalConfig.isClassStarted(classStartYear,classStartMonth,classStartDay,classStartHour,classStartMinute)) {
                    holder.joinClassActionTextView.setText("Started");
                    holder.joinClassActionTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //navigate to ClassActivity
                            Intent intent = new Intent(context, ClassActivity.class);
                            intent.putExtra(GlobalConfig.CLASS_DATA_MODEL_KEY,classDataModel);
                            intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,classDataModel.getAuthorId());
                            intent.putExtra(GlobalConfig.CLASS_ID_KEY,classDataModel.getClassId());
                            intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,false);
                            context.startActivity(intent);

                        }
                    });
                    if(!classDataModel.isStarted()){
                        GlobalConfig.markClassAsStarted(context, classDataModel.getClassId(), null);
                    }
                }
            }


            ArrayList<Long> classEndDateList1 = classDataModel.getEndDateList();
            if(classEndDateList1.size() >= 5) {
                long classEndYear = classEndDateList1.get(0);
                long classEndMonth = classEndDateList1.get(1);
                long classEndDay = classEndDateList1.get(2);
                long classEndHour = classEndDateList1.get(3);
                long classEndMinute = classEndDateList1.get(4);
//checks if the class is expired.
                if(GlobalConfig.isClassExpired(classEndYear,classEndMonth,classEndDay,classEndHour,classEndMinute)) {
                    holder.joinClassActionTextView.setText("Expired");
                    holder.joinClassActionTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            //navigate to ClassActivity
                            Intent intent = new Intent(context, ClassActivity.class);
                            intent.putExtra(GlobalConfig.CLASS_DATA_MODEL_KEY,classDataModel);
                            intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,classDataModel.getAuthorId());
                            intent.putExtra(GlobalConfig.CLASS_ID_KEY,classDataModel.getClassId());
                            intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,false);
                            context.startActivity(intent);

                        }
                    });
                    if(!classDataModel.isClosed()){
//                        //mark the class as closed if it has expired
//
//                        GlobalConfig.markClassAsClosed(context, classDataModel.getClassId(), new GlobalConfig.ActionCallback() {
//                            @Override
//                            public void onSuccess() {
//                                holder.joinClassActionTextView.setText("Closed");
////                                holder.joinClassActionTextView.setOnClickListener(new View.OnClickListener() {
////                                    @Override
////                                    public void onClick(View view) {
////
////
////                                        //navigate to ClassActivity
////                                        Intent intent = new Intent(context, ClassActivity.class);
////                                        intent.putExtra(GlobalConfig.CLASS_DATA_MODEL_KEY,classDataModel);
////                                        intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,classDataModel.getAuthorId());
////                                        intent.putExtra(GlobalConfig.CLASS_ID_KEY,classDataModel.getClassId());
////                                        intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,false);
////                                        context.startActivity(intent);
////
////                                    }
////                                });
//                            }
//
//                            @Override
//                            public void onFailed(String errorMessage) {
//
//                            }
//                        });
                    }
                }
                holder.endTimeView.setText("End Time "+classEndDay + "/" + classEndMonth + "/" + classEndYear + " " + classEndHour + " : " + classEndMinute);
            }


            GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(classDataModel.getAuthorId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            String userDisplayName = "" + documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                            holder.authorNameTextView.setText(userDisplayName);

                            boolean isVerified = documentSnapshot.get(GlobalConfig.IS_ACCOUNT_VERIFIED_KEY) != null ? documentSnapshot.getBoolean(GlobalConfig.IS_ACCOUNT_VERIFIED_KEY) : false;
                            if (isVerified) {
//                                holder.verificationFlagImageView.setVisibility(View.VISIBLE);
                            } else {
//                                holder.verificationFlagImageView.setVisibility(View.INVISIBLE);

                            }
                        }
                    });

                if(classDataModel.isClosed()) {
                    holder.joinClassActionTextView.setText("Closed");
                    holder.joinClassActionTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            //navigate to ClassActivity
                            Intent intent = new Intent(context, ClassActivity.class);
                            intent.putExtra(GlobalConfig.CLASS_DATA_MODEL_KEY, classDataModel);
                            intent.putExtra(GlobalConfig.AUTHOR_ID_KEY, classDataModel.getAuthorId());
                            intent.putExtra(GlobalConfig.CLASS_ID_KEY, classDataModel.getClassId());
                            intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY, false);
                            context.startActivity(intent);

                        }
                    });
                }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //navigate to ClassActivity
                    Intent intent = new Intent(context, ClassActivity.class);
                    intent.putExtra(GlobalConfig.CLASS_DATA_MODEL_KEY,classDataModel);
                    intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,classDataModel.getAuthorId());
                    intent.putExtra(GlobalConfig.CLASS_ID_KEY,classDataModel.getClassId());
                    intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,false);
                    context.startActivity(intent);

                }
            });
            holder.authorNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(GlobalConfig.getHostActivityIntent(context,null,GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY,classDataModel.getAuthorId()));

                }
            });
        } else {

            holder.classTitleView.setText("Class is not published yet");
            holder.itemView.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return classDataModels.size();
    }

    boolean isStarted(ClassDataModel classDataModel){
        //implement an algorithm to detect when class starts

        return false;
    }
    void joinClass(ClassDataModel classDataModel){
        //implement method to join class

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView classTitleView;
        public TextView dateCreated;
        public TextView studentsCountView;
        public TextView startTimeView;
        public TextView endTimeView;
        public TextView descriptionView;
        public TextView feeView;
        public TextView isClosedView;
        public TextView joinClassActionTextView;
        public TextView authorNameTextView;
        public ImageButton menuButton;
//        public ImageView verificationFlagImageView;


        public ViewHolder(View itemView) {
            super(itemView);
if(!isFromHome){

    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(3,3,2,2);
    itemView.setLayoutParams(layoutParams);
}
            this.classTitleView =  itemView.findViewById(R.id.classTitleTextViewId);
//            this.dateCreated = (TextView) itemView.findViewById(R.id.dateCreated);
//            this.verificationFlagImageView =  itemView.findViewById(R.id.verificationFlagImageViewId);
            this.studentsCountView = (TextView) itemView.findViewById(R.id.studentCountViewId);
            this.startTimeView = (TextView) itemView.findViewById(R.id.startingTimeTextViewId);
            this.endTimeView = (TextView) itemView.findViewById(R.id.endingTimeTextViewId);
            this.descriptionView = (TextView) itemView.findViewById(R.id.descriptionTextViewId);
            this.feeView = (TextView) itemView.findViewById(R.id.feeTextViewId);
            this.isClosedView = (TextView) itemView.findViewById(R.id.activeStatusTextViewId);
            this.joinClassActionTextView = (TextView) itemView.findViewById(R.id.joinClassActionTextViewId);
            this.authorNameTextView = (TextView) itemView.findViewById(R.id.authorNameTextViewId);
            this.menuButton =  itemView.findViewById(R.id.menuButtonId);

        }
    }

}

