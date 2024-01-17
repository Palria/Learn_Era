package com.palria.learnera.adapters;

import android.content.Context;
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
import com.palria.learnera.R;
import com.palria.learnera.models.LearnEraNotificationDataModel;
import com.palria.learnera.models.LearnEraNotificationDataModel;

import java.util.ArrayList;
import java.util.HashMap;


public class LearnEraNotificationAdapter extends RecyclerView.Adapter<LearnEraNotificationAdapter.ViewHolder> {

    ArrayList<LearnEraNotificationDataModel> notificationDataModelArrayList;
    Context context;


    public LearnEraNotificationAdapter(Context context, ArrayList<LearnEraNotificationDataModel> notificationDataModelArrayList) {
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
        LearnEraNotificationDataModel notificationDataModel = notificationDataModelArrayList.get(position);

            holder.notificationTitleTextView.setText(notificationDataModel.getTitle());
            holder.dateNotifiedTextView.setText(notificationDataModel.getDateNotified());
            holder.notificationMessageTextView.setText(notificationDataModel.getMessage());


 if(notificationDataModel.getNotificationViewers().contains(GlobalConfig.getCurrentUserId())){
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

    void markAsSeen(LearnEraNotificationDataModel notificationDataModel ){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference notifyDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.PLATFORM_NOTIFICATIONS_KEY).document(notificationDataModel.getNotificationId());
        HashMap<String, Object> notifyDetails = new HashMap<>();
        notifyDetails.put(GlobalConfig.DATE_SEEN_LAST_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        notifyDetails.put(GlobalConfig.NOTIFICATION_VIEWERS_LIST_KEY, FieldValue.arrayUnion(GlobalConfig.getCurrentUserId()));
        writeBatch.set(notifyDocumentReference,notifyDetails, SetOptions.merge());
        writeBatch.commit();
    }
}

