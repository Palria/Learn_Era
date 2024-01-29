package com.palria.learnera.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.JoinQuizActivity;
import com.palria.learnera.QuizActivity;
import com.palria.learnera.R;
import com.palria.learnera.TutorialFolderActivity;
import com.palria.learnera.models.FolderDataModel;
import com.palria.learnera.models.QuizDataModel;

import java.util.ArrayList;

public class QuizRcvAdapter extends RecyclerView.Adapter<QuizRcvAdapter.ViewHolder> {

    ArrayList<QuizDataModel> quizDataModels;
    Context context;
    boolean isFromHome;

    public QuizRcvAdapter(ArrayList<QuizDataModel> quizDataModels, Context context, boolean isFromHome) {
        this.quizDataModels = quizDataModels;
        this.context = context;
        this.isFromHome = isFromHome;
    }

    @NonNull
    @Override
    public QuizRcvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.quiz_item_layout, parent, false);
        QuizRcvAdapter.ViewHolder viewHolder = new QuizRcvAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuizRcvAdapter.ViewHolder holder, int position) {

        QuizDataModel quizDataModel = quizDataModels.get(position);
        if (quizDataModel.isPublic() || (GlobalConfig.getCurrentUserId().equals(quizDataModel.getAuthorId() + ""))) {

            holder.quizTitleView.setText(quizDataModel.getQuizTitle());
//            holder.dateCreated.setText(quizDataModel.getDateCreated());
            if(quizDataModel.getAuthorId().equals(GlobalConfig.getCurrentUserId())){

                holder.menuButton.setVisibility(View.VISIBLE);
            }

            if(quizDataModel.getQuizDescription().equals("")){
                holder.descriptionView.setVisibility(View.GONE);
            }else{
                holder.descriptionView.setVisibility(View.VISIBLE);
            }
            holder.descriptionView.setText(""+quizDataModel.getQuizDescription());
            holder.timeLimitTimeView.setText("Time Limit "+quizDataModel.getTotalTimeLimit()+"s");
            holder.questionsCountTimeView.setText("Questions "+quizDataModel.getTotalQuestions());
            holder.feeView.setText("Fee "+quizDataModel.getTotalQuizFeeCoins());
            holder.rewardView.setText("Reward "+quizDataModel.getTotalQuizRewardCoins());
            holder.participantsCountView.setText(""+quizDataModel.getTotalParticipants() + "");
            holder.joinQuizActionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //navigate to JoinQuizActivity
                    Intent intent = new Intent(context, JoinQuizActivity.class);
                    intent.putExtra(GlobalConfig.QUIZ_DATA_MODEL_KEY, quizDataModel);
                    context.startActivity(intent);

//                    joinQuiz(quizDataModel);
                }
            });
            if(quizDataModel.isClosed()){
                holder.isClosedView.setText("Closed");
                holder.isClosedView.setBackgroundColor(context.getResources().getColor(R.color.error_red,context.getTheme()));
            }else{
                holder.isClosedView.setText("Active");
                holder.isClosedView.setBackground(context.getResources().getDrawable(R.drawable.rounded_border_gray_bg,context.getTheme()));
                holder.isClosedView.setBackgroundColor(context.getResources().getColor(R.color.secondary_app_color,context.getTheme()));
            }

            if(isStarted(quizDataModel)){

                holder.isClosedView.setText("Started");
                holder.isClosedView.setBackgroundColor(context.getResources().getColor(R.color.success_green,context.getTheme()));
            }
            holder.startTimeView.setText("Time Undefined");
            holder.endTimeView.setText("Time Undefined");

            ArrayList<Long> quizStartDateList1 = quizDataModel.getStartDateList();
            if(quizStartDateList1.size() >= 5) {
                long quizStartYear = quizStartDateList1.get(0);
                long quizStartMonth = quizStartDateList1.get(1);
                long quizStartDay = quizStartDateList1.get(2);
                long quizStartHour = quizStartDateList1.get(3);
                long quizStartMinute = quizStartDateList1.get(4);
                holder.startTimeView.setText("Start Time "+quizStartDay + "/" + quizStartMonth + "/" + quizStartYear + " " + quizStartHour + " : " + quizStartMinute);
//checks if quiz has started
                if(GlobalConfig.isQuizStarted(quizStartYear,quizStartMonth,quizStartDay,quizStartHour,quizStartMinute)) {
                    holder.joinQuizActionTextView.setText("Started");
                    holder.joinQuizActionTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            //navigate to QuizActivity
                            Intent intent = new Intent(context, QuizActivity.class);
                            intent.putExtra(GlobalConfig.QUIZ_DATA_MODEL_KEY,quizDataModel);
                            intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,quizDataModel.getAuthorId());
                            intent.putExtra(GlobalConfig.QUIZ_ID_KEY,quizDataModel.getQuizId());
                            intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,false);
                            context.startActivity(intent);

                        }
                    });

                }
            }


            ArrayList<Long> quizEndDateList1 = quizDataModel.getEndDateList();
            if(quizEndDateList1.size() >= 5) {
                long quizEndYear = quizEndDateList1.get(0);
                long quizEndMonth = quizEndDateList1.get(1);
                long quizEndDay = quizEndDateList1.get(2);
                long quizEndHour = quizEndDateList1.get(3);
                long quizEndMinute = quizEndDateList1.get(4);
//checks if the quiz is expired.
                if(GlobalConfig.isQuizExpired(quizEndYear,quizEndMonth,quizEndDay,quizEndHour,quizEndMinute)) {
                    holder.joinQuizActionTextView.setText("Expired");
                    holder.joinQuizActionTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            //navigate to QuizActivity
                            Intent intent = new Intent(context, QuizActivity.class);
                            intent.putExtra(GlobalConfig.QUIZ_DATA_MODEL_KEY,quizDataModel);
                            intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,quizDataModel.getAuthorId());
                            intent.putExtra(GlobalConfig.QUIZ_ID_KEY,quizDataModel.getQuizId());
                            intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,false);
                            context.startActivity(intent);

                        }
                    });
                    if(!quizDataModel.isClosed()){
                        //mark the quiz as closed if it has expired

                        GlobalConfig.markQuizAsClosed(context, quizDataModel.getQuizId(), new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {
                                holder.joinQuizActionTextView.setText("Closed");
                                holder.joinQuizActionTextView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        //navigate to QuizActivity
                                        Intent intent = new Intent(context, QuizActivity.class);
                                        intent.putExtra(GlobalConfig.QUIZ_DATA_MODEL_KEY,quizDataModel);
                                        intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,quizDataModel.getAuthorId());
                                        intent.putExtra(GlobalConfig.QUIZ_ID_KEY,quizDataModel.getQuizId());
                                        intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,false);
                                        context.startActivity(intent);

                                    }
                                });
                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });
                    }
                }
                holder.endTimeView.setText("End Time "+quizEndDay + "/" + quizEndMonth + "/" + quizEndYear + " " + quizEndHour + " : " + quizEndMinute);
            }


            GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(quizDataModel.getAuthorId())
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

                if(quizDataModel.isClosed()) {
                    holder.joinQuizActionTextView.setText("Closed");
                    holder.joinQuizActionTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            //navigate to QuizActivity
                            Intent intent = new Intent(context, QuizActivity.class);
                            intent.putExtra(GlobalConfig.QUIZ_DATA_MODEL_KEY, quizDataModel);
                            intent.putExtra(GlobalConfig.AUTHOR_ID_KEY, quizDataModel.getAuthorId());
                            intent.putExtra(GlobalConfig.QUIZ_ID_KEY, quizDataModel.getQuizId());
                            intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY, false);
                            context.startActivity(intent);

                        }
                    });
                }

//checks if the quiz is completed
            if(quizDataModel.isQuizMarkedCompleted()) {
                holder.joinQuizActionTextView.setText("Completed");
                holder.isClosedView.setText("Completed");
                holder.joinQuizActionTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        //navigate to QuizActivity
                        Intent intent = new Intent(context, QuizActivity.class);
                        intent.putExtra(GlobalConfig.QUIZ_DATA_MODEL_KEY,quizDataModel);
                        intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,quizDataModel.getAuthorId());
                        intent.putExtra(GlobalConfig.QUIZ_ID_KEY,quizDataModel.getQuizId());
                        intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,false);
                        context.startActivity(intent);

                    }
                });

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //navigate to QuizActivity
                    Intent intent = new Intent(context, QuizActivity.class);
                    intent.putExtra(GlobalConfig.QUIZ_DATA_MODEL_KEY,quizDataModel);
                    intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,quizDataModel.getAuthorId());
                    intent.putExtra(GlobalConfig.QUIZ_ID_KEY,quizDataModel.getQuizId());
                    intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,false);
                    context.startActivity(intent);

                }
            });
            holder.authorNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(GlobalConfig.getHostActivityIntent(context,null,GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY,quizDataModel.getAuthorId()));

                }
            });
        } else {

            holder.quizTitleView.setText("Quiz is not published yet");
            holder.itemView.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return quizDataModels.size();
    }

    boolean isStarted(QuizDataModel quizDataModel){
        //implement an algorithm to detect when quiz starts

        return false;
    }
    void joinQuiz(QuizDataModel quizDataModel){
        //implement method to join quiz

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView quizTitleView;
        public TextView dateCreated;
        public TextView participantsCountView;
        public TextView startTimeView;
        public TextView endTimeView;
        public TextView timeLimitTimeView;
        public TextView questionsCountTimeView;
        public TextView descriptionView;
        public TextView feeView;
        public TextView rewardView;
        public TextView isClosedView;
        public TextView joinQuizActionTextView;
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
            this.quizTitleView =  itemView.findViewById(R.id.quizTitleTextViewId);
//            this.dateCreated = (TextView) itemView.findViewById(R.id.dateCreated);
//            this.verificationFlagImageView =  itemView.findViewById(R.id.verificationFlagImageViewId);
            this.participantsCountView = (TextView) itemView.findViewById(R.id.participantCountViewId);
            this.startTimeView = (TextView) itemView.findViewById(R.id.startingTimeTextViewId);
            this.endTimeView = (TextView) itemView.findViewById(R.id.endingTimeTextViewId);
            this.timeLimitTimeView = (TextView) itemView.findViewById(R.id.timeLimitTextViewId);
            this.questionsCountTimeView = (TextView) itemView.findViewById(R.id.questionCountTextViewId);
            this.descriptionView = (TextView) itemView.findViewById(R.id.descriptionTextViewId);
            this.feeView = (TextView) itemView.findViewById(R.id.feeTextViewId);
            this.rewardView = (TextView) itemView.findViewById(R.id.rewardTextViewId);
            this.isClosedView = (TextView) itemView.findViewById(R.id.activeStatusTextViewId);
            this.joinQuizActionTextView = (TextView) itemView.findViewById(R.id.joinQuizActionTextViewId);
            this.authorNameTextView = (TextView) itemView.findViewById(R.id.authorNameTextViewId);
            this.menuButton =  itemView.findViewById(R.id.menuButtonId);

        }
    }

}

