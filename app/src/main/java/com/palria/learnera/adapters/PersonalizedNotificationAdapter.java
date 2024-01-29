package com.palria.learnera.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.PersonalizedNotificationDataModel;
import com.palria.learnera.QuizActivity;
import com.palria.learnera.R;
import com.palria.learnera.models.LearnEraNotificationDataModel;

import java.util.ArrayList;
import java.util.HashMap;


public class PersonalizedNotificationAdapter extends RecyclerView.Adapter<PersonalizedNotificationAdapter.ViewHolder> {

    ArrayList<PersonalizedNotificationDataModel> notificationDataModelArrayList;
    Context context;


    public PersonalizedNotificationAdapter(Context context, ArrayList<PersonalizedNotificationDataModel> notificationDataModelArrayList) {
        this.notificationDataModelArrayList = notificationDataModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.notification_item_layout, parent, false);
       ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PersonalizedNotificationDataModel notificationDataModel = notificationDataModelArrayList.get(position);

            holder.notificationTitleTextView.setText(notificationDataModel.getTitle());
            holder.dateNotifiedTextView.setText(notificationDataModel.getDateNotified());
            holder.notificationMessageTextView.setText(notificationDataModel.getMessage());


 if(notificationDataModel.isSeen()){
        holder.notificationMessageTextView.setTextColor(context.getResources().getColor(R.color.light_dark,context.getTheme()));
//        holder.notificationTitleTextView.setTextColor(context.getResources().getColor(R.color.light_dark,context.getTheme()));
        holder.dateNotifiedTextView.setTextColor(context.getResources().getColor(R.color.light_dark,context.getTheme()));

    }else{
        markAsSeen(notificationDataModel);
        holder.notificationMessageTextView.setTextColor(context.getResources().getColor(R.color.black,context.getTheme()));
//        holder.notificationTitleTextView.setTextColor(context.getResources().getColor(R.color.black,context.getTheme()));
        holder.dateNotifiedTextView.setTextColor(context.getResources().getColor(R.color.black,context.getTheme()));
//        holder.itemView.setBackgroundResource(R.color.success_green);

    }

 holder.itemView.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         switch(notificationDataModel.getType()){
             case GlobalConfig.NOTIFICATION_TYPE_QUIZ_KEY:
                 String quizId = notificationDataModel.getNotification_model_info_list().get(0);
                 Intent intent = new Intent(context, QuizActivity.class);
                 intent.putExtra(GlobalConfig.QUIZ_ID_KEY,quizId);
                 intent.putExtra(GlobalConfig.AUTHOR_ID_KEY, notificationDataModel.getSenderId());
                 intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,true);
                 context.startActivity(intent);
                 break;
                 case GlobalConfig.NOTIFICATION_TYPE_QUIZ_COMPLETED_KEY:
                 String quizId1 = notificationDataModel.getNotification_model_info_list().get(0);
                 Intent intent1 = new Intent(context, QuizActivity.class);
                 intent1.putExtra(GlobalConfig.QUIZ_ID_KEY,quizId1);
                 intent1.putExtra(GlobalConfig.AUTHOR_ID_KEY, notificationDataModel.getSenderId());
                 intent1.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,true);
                 context.startActivity(intent1);
                 break;
                 case GlobalConfig.NOTIFICATION_TYPE_QUIZ_ANSWER_SUBMITTED_KEY:
                 String quizId2 = notificationDataModel.getNotification_model_info_list().get(0);
                 Intent intent2 = new Intent(context, QuizActivity.class);
                 intent2.putExtra(GlobalConfig.QUIZ_ID_KEY,quizId2);
                 intent2.putExtra(GlobalConfig.AUTHOR_ID_KEY, notificationDataModel.getSenderId());
                 intent2.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,true);
                 context.startActivity(intent2);
                 break;
         }
     }
 });
    }


    @Override
    public int getItemCount() {
        return notificationDataModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView notificationTitleTextView;
        public TextView dateNotifiedTextView;
        public TextView notificationMessageTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.notificationTitleTextView =  itemView.findViewById(R.id.notificationTitleTextViewId);
            this.dateNotifiedTextView =  itemView.findViewById(R.id.dateNotifiedTextViewId);
            this.notificationMessageTextView =  itemView.findViewById(R.id.notificationMessageTextViewId);

        }
    }

    void markAsSeen(PersonalizedNotificationDataModel notificationDataModel ){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference notifyDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.PERSONALIZED_NOTIFICATIONS_KEY).document(notificationDataModel.getNotificationId());
        HashMap<String, Object> notifyDetails = new HashMap<>();
        notifyDetails.put(GlobalConfig.DATE_SEEN_LAST_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        notifyDetails.put(GlobalConfig.IS_SEEN_KEY, true);
        writeBatch.set(notifyDocumentReference,notifyDetails, SetOptions.merge());
        writeBatch.commit();
    }
}

