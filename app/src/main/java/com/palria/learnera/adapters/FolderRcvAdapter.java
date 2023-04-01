package com.palria.learnera.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.R;
import com.palria.learnera.TutorialFolderActivity;
import com.palria.learnera.models.AuthorDataModel;
import com.palria.learnera.models.FolderDataModel;

import java.util.ArrayList;
import java.util.Random;

public class FolderRcvAdapter extends RecyclerView.Adapter<FolderRcvAdapter.ViewHolder> {

    ArrayList<FolderDataModel> folderDataModels;
    Context context;

    public FolderRcvAdapter(ArrayList<FolderDataModel> folderDataModels, Context context) {
        this.folderDataModels = folderDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public FolderRcvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.folder_item_layout, parent, false);
        FolderRcvAdapter.ViewHolder viewHolder = new FolderRcvAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FolderRcvAdapter.ViewHolder holder, int position) {

        FolderDataModel folderDataModel = folderDataModels.get(position);
        if (folderDataModel.isPublic() || (GlobalConfig.getCurrentUserId().equals(folderDataModel.getAuthorId() + ""))) {

            holder.folderName.setText(folderDataModel.getFolderName());
            holder.dateCreated.setText(folderDataModel.getDateCreated());
            holder.pagesCountView.setText(folderDataModel.getNumOfPages() + "");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: "+folderDataModel.getFolderName(),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, TutorialFolderActivity.class);
                    intent.putExtra(GlobalConfig.FOLDER_ID_KEY, folderDataModel.getId());
                    intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY, folderDataModel.getTutorialId());
                    intent.putExtra(GlobalConfig.LIBRARY_ID_KEY, folderDataModel.getLibraryId());
                    intent.putExtra(GlobalConfig.AUTHOR_ID_KEY, folderDataModel.getAuthorId());
                    intent.putExtra(GlobalConfig.FOLDER_NAME_KEY, folderDataModel.getFolderName());
                    intent.putExtra(GlobalConfig.IS_FIRST_VIEW_KEY, false);
                    intent.putExtra(GlobalConfig.FOLDER_DATA_MODEL_KEY, folderDataModel);

                    context.startActivity(intent);
                }
            });
        } else {

            holder.folderName.setText("Folder is private");
            holder.itemView.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return folderDataModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView folderName;
        public TextView dateCreated;
        public TextView pagesCountView;


        public ViewHolder(View itemView) {
            super(itemView);
            this.folderName =  itemView.findViewById(R.id.folderName);
            this.dateCreated = (TextView) itemView.findViewById(R.id.dateCreated);
            pagesCountView=itemView.findViewById(R.id.pagesCountView);

        }
    }

}

