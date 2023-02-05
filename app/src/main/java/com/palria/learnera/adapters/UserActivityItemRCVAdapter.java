package com.palria.learnera.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.auth.User;
import com.palria.learnera.R;
import com.palria.learnera.models.TutorialDataModel;
import com.palria.learnera.models.UserActivityDataModel;

import java.util.ArrayList;

public class UserActivityItemRCVAdapter extends RecyclerView.Adapter<UserActivityItemRCVAdapter.ViewHolder> {

    ArrayList<UserActivityDataModel> userActivityDataModels;
    Context context;

    public UserActivityItemRCVAdapter(ArrayList<UserActivityDataModel> userActivityDataModels, Context context) {
        this.userActivityDataModels = userActivityDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public UserActivityItemRCVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.user_activity_log_item, parent, false);
        UserActivityItemRCVAdapter.ViewHolder viewHolder = new UserActivityItemRCVAdapter.ViewHolder(listItem);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull UserActivityItemRCVAdapter.ViewHolder holder, int position) {

        UserActivityDataModel userActivityDataModel  = userActivityDataModels.get(position);

        holder.title.setText(userActivityDataModel.getTitle());
        holder.date.setText(userActivityDataModel.getDate());


        if(userActivityDataModel.isAsDateDivider()){
           holder.linearLayout.removeAllViews();
           TextView textView = new TextView(context);
           textView.setText(userActivityDataModel.getDate());
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(17);

            textView.setPadding(10,20,0,10);
            holder.linearLayout.setBackground(null);
           holder.linearLayout.addView(textView);
           return;
        }

       if(!userActivityDataModel.getBody().equals("")){
           holder.body.setText(userActivityDataModel.getBody());
       }else{
           holder.body.setVisibility(View.GONE);
       }

       int res_icon_id;

       if(userActivityDataModel.getModelType().equals("library")){
           res_icon_id=R.drawable.ic_baseline_library_books_24;
       }else if(userActivityDataModel.getModelType().equals("tutorial")){
           res_icon_id=R.drawable.ic_baseline_dynamic_feed_24;
       }else if(userActivityDataModel.getModelType().equals("rating")){
           res_icon_id=R.drawable.ic_baseline_stars_24;
       }else{
           res_icon_id=R.drawable.ic_baseline_local_activity_24;
       }





      if(userActivityDataModel.getIcon().equals("")){
          Glide.with(context)
                  .load(res_icon_id)
                  .fitCenter()
                  .placeholder(R.drawable.ic_baseline_local_activity_24)
                  .into(holder.icon);
      }else{
          Glide.with(context)
                  .load(userActivityDataModel.getIcon())
                  .centerCrop()
                  .placeholder(R.drawable.ic_baseline_local_activity_24)
                  .into(holder.icon);
      }

    }

    @Override
    public int getItemCount() {
        return userActivityDataModels.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView icon;
        public TextView title;
        public TextView body;
        public TextView date;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.title = itemView.findViewById(R.id.title);
            this.body = itemView.findViewById(R.id.body);
            this.date = itemView.findViewById(R.id.date);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.parentItem);
        }
    }

}