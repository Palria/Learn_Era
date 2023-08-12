package com.palria.learnera.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.palria.learnera.GlobalConfig;
import com.palria.learnera.R;
import com.palria.learnera.TutorialFolderActivity;
import com.palria.learnera.models.FolderDataModel;
import com.palria.learnera.models.QuizDataModel;

import java.util.ArrayList;

public class QuizRcvAdapter extends RecyclerView.Adapter<QuizRcvAdapter.ViewHolder> {

    ArrayList<QuizDataModel> quizDataModels;
    Context context;

    public QuizRcvAdapter(ArrayList<QuizDataModel> quizDataModels, Context context) {
        this.quizDataModels = quizDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public QuizRcvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.folder_item_layout, parent, false);
        QuizRcvAdapter.ViewHolder viewHolder = new QuizRcvAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuizRcvAdapter.ViewHolder holder, int position) {

        QuizDataModel quizDataModel = quizDataModels.get(position);
        if (quizDataModel.isPublic() || (GlobalConfig.getCurrentUserId().equals(quizDataModel.getAuthorId() + ""))) {

            holder.quizTitleView.setText(quizDataModel.getQuizTitle());
            holder.dateCreated.setText(quizDataModel.getDateCreated());
            holder.feeView.setText("Fee "+quizDataModel.getQuizFeeDescription());
            holder.rewardView.setText("Reward "+quizDataModel.getQuizRewardDescription());
            holder.participantsCountView.setText("Joined "+quizDataModel.getTotalParticipants() + "");
            if(quizDataModel.isClosed()){
                holder.isClosedView.setText("Closed");
            }else{
                holder.isClosedView.setText("Open");
            }

            ArrayList<Integer> quizDateList1 = quizDataModel.getDateList();
            if(quizDateList1.size() == 5) {
                int quizYear = quizDateList1.get(0);
                int quizMonth = quizDateList1.get(0);
                int quizDay = quizDateList1.get(0);
                int quizHour = quizDateList1.get(0);
                int quizMinute = quizDateList1.get(0);
                holder.startTimeView.setText("Starting "+quizDay + "/" + quizMonth + "/" + quizYear + " " + quizHour + ":" + quizMinute);
            }

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

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView quizTitleView;
        public TextView dateCreated;
        public TextView participantsCountView;
        public TextView startTimeView;
        public TextView feeView;
        public TextView rewardView;
        public TextView isClosedView;


        public ViewHolder(View itemView) {
            super(itemView);
            /*
            this.quizTitleView =  itemView.findViewById(R.id.quizTitleViewId);
            this.dateCreated = (TextView) itemView.findViewById(R.id.dateCreated);
            this.participantsCountView = (TextView) itemView.findViewById(R.id.participantsCountViewId);
            this.startTimeView = (TextView) itemView.findViewById(R.id.startTimeViewId);
            this.feeView = (TextView) itemView.findViewById(R.id.feeViewId);
            this.rewardView = (TextView) itemView.findViewById(R.id.rewardViewId);
            this.isClosedView = (TextView) itemView.findViewById(R.id.isClosedViewId);
*/
        }
    }

}

