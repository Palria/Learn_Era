package com.palria.learnera.widgets;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.palria.learnera.R;

import java.util.ArrayList;

public class LEBottomSheetDialog extends BottomSheetDialog  {

    Context context;
    ArrayList<OptionItem> optionItems = new ArrayList<>();


    public LEBottomSheetDialog(@NonNull Context context) {
        super(context);
        this.context=context;
        optionItems.clear();
    }


    public LEBottomSheetDialog addOptionItem(String title, int icon, View.OnClickListener clickListener, int endIcon){

        OptionItem optionItem = new OptionItem(title, icon,null);
        optionItem.setClickListener(clickListener);
        optionItems.add(optionItem);
        return this;

    }



    public LEBottomSheetDialog render(){
        this.initialize();
        return this;
    }

    private void initialize(){

        //now initialize the items

        LayoutInflater lf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parent = lf.inflate(R.layout.default_bottom_sheet_dialog,null);
        LinearLayout view = parent.findViewById(R.id.contentView);
        view.removeAllViews();
        for(OptionItem optionItem : optionItems){

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View item = layoutInflater.inflate(R.layout.bottom_sheet_list_item,null);

            ImageView iconView =item.findViewById(R.id.leadingIcon);
            TextView titleView = item.findViewById(R.id.title);
            ImageView endIconView = item.findViewById(R.id.trailingIcon);

            iconView.setImageResource(optionItem.getIconResId());
            titleView.setText(optionItem.getTitle());
//            if(>0){
//                endIconView.setBackgroundResource(optionItem.IconResId);
//                endIconView.setVisibility(View.VISIBLE);
//            }

            optionItem.setView(item);
            view.addView(item);
        }


        setContentView(parent);

    }



    public class OptionItem {
        private String title;
        private int IconResId;
        private View view;
        private View.OnClickListener listener;

        public OptionItem(String title, int iconResId, View view) {
            this.title = title;
            IconResId = iconResId;
            this.view = view;

        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIconResId() {
            return IconResId;
        }

        public void setIconResId(int iconResId) {
            IconResId = iconResId;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
            view.setOnClickListener(listener);

        }

        public void setClickListener(View.OnClickListener clickListener) {
            this.listener =clickListener;
        }
    }



}
