package com.palria.learnera.adapters;

import android.content.Context;
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
import com.palria.learnera.models.AuthorDataModel;

import java.util.ArrayList;

public class HomeAuthorListViewAdapter extends RecyclerView.Adapter<HomeAuthorListViewAdapter.ViewHolder> {

    ArrayList<AuthorDataModel> authorDataModels;
    Context context;

    public HomeAuthorListViewAdapter(ArrayList<AuthorDataModel> authorDataModels, Context context) {
        this.authorDataModels = authorDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.single_author_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AuthorDataModel authorDataModel = authorDataModels.get(position);

        holder.textView.setText(authorDataModel.getAuthorName());
        Glide.with(context)
                        .load(authorDataModel.getAuthorProfilePhotoDownloadUrl())
                                .centerCrop()
                .placeholder(R.drawable.default_profile)
                                        .into(holder.imageView);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: "+authorDataModel.getAuthorName(),Toast.LENGTH_LONG).show();

                context.startActivity(GlobalConfig.getHostActivityIntent(context,null,GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY,authorDataModel.getAuthorId()));

            }
        });

    }

    @Override
    public int getItemCount() {
        return authorDataModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.author_image_view);
            this.textView = (TextView) itemView.findViewById(R.id.author_name);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.parentItem);
        }
    }

}
