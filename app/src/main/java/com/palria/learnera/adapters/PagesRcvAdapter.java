package com.palria.learnera.adapters;

import static com.palria.learnera.GlobalConfig.LIBRARY_AUTHOR_ID_KEY;
import static com.palria.learnera.GlobalConfig.LIBRARY_ID_KEY;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.GlobalHelpers;
import com.palria.learnera.LibraryActivity;
import com.palria.learnera.PageActivity;
import com.palria.learnera.R;
import com.palria.learnera.models.LibraryDataModel;
import com.palria.learnera.models.PageDataModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PagesRcvAdapter extends RecyclerView.Adapter<PagesRcvAdapter.ViewHolder> {

    ArrayList<PageDataModel> pageDataModels;
    Context context;

    public PagesRcvAdapter(ArrayList<PageDataModel> pageDataModels, Context context) {
        this.pageDataModels = pageDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public PagesRcvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.all_library_fragment_list_item_library, parent, false);
        PagesRcvAdapter.ViewHolder viewHolder = new PagesRcvAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PagesRcvAdapter.ViewHolder holder, int position) {

        PageDataModel pageDataModel = pageDataModels.get(position);

        holder.tutorialsContainer.setVisibility(View.GONE);
        holder.dateContainer.setVisibility(View.VISIBLE);
        holder.dateCreated.setText("2 m ago");//view counts here

        holder.pageTitle.setText(pageDataModel.getTitle());
        try {
            Glide.with(context)
                    .load(pageDataModel.getCoverDownloadUrl())
                    .centerCrop()
                    .placeholder(R.drawable.book_cover)
                    .into(holder.cover);
        }catch(Exception ignored){}


        holder.pageDescription.setText(pageDataModel.getDescription());
        holder.pageViewCount.setText(45+"");

//        GlobalConfig.getFirebaseFirestoreInstance()
//                .collection(GlobalConfig.ALL_USERS_KEY)
//                .document(pageDataModel.getAuthorUserId())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                        String userDisplayName = "" + documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
//                        holder.authorName.setText(userDisplayName);
//                    }
//                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                Intent intent = new Intent(context, PageActivity.class);
                intent.putExtra(GlobalConfig.PAGE_ID_KEY,pageDataModel.getPageId());
                intent.putExtra(GlobalConfig.FOLDER_ID_KEY,pageDataModel.getFolderId());
                intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,pageDataModel.getTutorialId());
                intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,pageDataModel.getAuthorId());
                intent.putExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,pageDataModel.isTutorialPage());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return pageDataModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView cover;
        public TextView pageTitle;
        public TextView authorName;
        public TextView pageViewCount;
        public TextView ratingCount;
        public TextView pageDescription;

        public LinearLayout dateContainer;
        public LinearLayout tutorialsContainer;
        public TextView dateCreated;


        public ViewHolder(View itemView) {
            super(itemView);
            this.cover = (ImageView) itemView.findViewById(R.id.cover);
            this.pageTitle = (TextView) itemView.findViewById(R.id.libraryName);
            authorName=itemView.findViewById(R.id.authorName);
            pageViewCount=itemView.findViewById(R.id.libraryViewCount);
            ratingCount=itemView.findViewById(R.id.ratingCount);
            pageDescription=itemView.findViewById(R.id.libraryDescription);
            tutorialsContainer=itemView.findViewById(R.id.tutorialsContainer);
            dateContainer=itemView.findViewById(R.id.dateContainer);
            dateCreated=itemView.findViewById(R.id.dateCreated);


        }
    }



}
