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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.GlobalConfig;
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
            holder.feeView.setText("Fee "+quizDataModel.getQuizFeeDescription());
            holder.rewardView.setText("Reward "+quizDataModel.getQuizRewardDescription());
            holder.participantsCountView.setText(""+quizDataModel.getTotalParticipants() + "");
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

            ArrayList<Long> quizDateList1 = quizDataModel.getDateList();
            if(quizDateList1.size() == 5) {
                long quizYear = quizDateList1.get(0);
                long quizMonth = quizDateList1.get(1);
                long quizDay = quizDateList1.get(2);
                long quizHour = quizDateList1.get(3);
                long quizMinute = quizDateList1.get(4);
                holder.startTimeView.setText("Time "+quizDay + "/" + quizMonth + "/" + quizYear + " " + quizHour + ":" + quizMinute);
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
                        }
                    });


            holder.joinQuizActionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //implement method to join quiz
                    joinQuiz(quizDataModel);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //go to quiz activity
//                    Intent intent = new Intent(context, ViewQuizActivity.class);
//                    intent.putExtra(GlobalConfig.QUIZ_ID_KEY, quizDataModel.getQuizId());

//                    context.startActivity(intent);
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
        public TextView timeLimitTimeView;
        public TextView questionsCountTimeView;
        public TextView descriptionView;
        public TextView feeView;
        public TextView rewardView;
        public TextView isClosedView;
        public TextView joinQuizActionTextView;
        public TextView authorNameTextView;
        public ImageButton menuButton;


        public ViewHolder(View itemView) {
            super(itemView);
if(!isFromHome){

    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(3,3,2,2);
    itemView.setLayoutParams(layoutParams);
}
            this.quizTitleView =  itemView.findViewById(R.id.quizTitleTextViewId);
//            this.dateCreated = (TextView) itemView.findViewById(R.id.dateCreated);
            this.participantsCountView = (TextView) itemView.findViewById(R.id.participantCountViewId);
            this.startTimeView = (TextView) itemView.findViewById(R.id.startingTimeTextViewId);
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

