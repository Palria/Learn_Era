package com.palria.learnera.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.palria.learnera.widgets.LEBottomSheetDialog;

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
        holder.moreActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LEBottomSheetDialog leBottomSheetDialog = new LEBottomSheetDialog(context);
                leBottomSheetDialog.addOptionItem("Block User", R.drawable.ic_baseline_error_outline_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        leBottomSheetDialog.hide();
                        Toast.makeText(context,"Blocking",Toast.LENGTH_SHORT).show();
                        int position = authorDataModels.indexOf(authorDataModel);
                        authorDataModels.remove(authorDataModel);
                        HomeAuthorListViewAdapter.this.notifyItemRemoved(position);
                        GlobalConfig.block(GlobalConfig.ACTIVITY_LOG_USER_BLOCK_USER_TYPE_KEY, authorDataModel.getAuthorId(), null, null, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });
                    }
                }, 0);
                leBottomSheetDialog.addOptionItem("Report User", R.drawable.ic_baseline_error_outline_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        leBottomSheetDialog.hide();
                        Toast.makeText(context,"reporting",Toast.LENGTH_SHORT).show();
                        int position = authorDataModels.indexOf(authorDataModel);
                        authorDataModels.remove(authorDataModel);
                        HomeAuthorListViewAdapter.this.notifyItemRemoved(position);
                        GlobalConfig.report(GlobalConfig.ACTIVITY_LOG_USER_REPORT_USER_TYPE_KEY, authorDataModel.getAuthorId(), null, null, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });
                    }
                }, 0);
                leBottomSheetDialog.render().show();
            }
        });

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
        public ImageButton moreActionButton;


        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.author_image_view);
            this.textView = (TextView) itemView.findViewById(R.id.author_name);
            this.moreActionButton = itemView.findViewById(R.id.moreActionButtonId);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.parentItem);
        }
    }

}
