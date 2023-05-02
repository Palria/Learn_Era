package com.palria.learnera.widgets;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.palria.learnera.R;

import org.checkerframework.common.subtyping.qual.Bottom;

import java.util.ArrayList;

public class BottomSheetFormBuilderWidget extends BottomSheetDialog {

    Context context;
    LinearLayout parentLayout;
    LinearLayout formLayout;
    String positiveTitle="Ok";
    String negativeTitle="No";
    String title = "";
    ArrayList<View> inputLists=new ArrayList<>();
    private OnSubmitHandler onSubmitListener;
    View dialogView;
    Activity activity;



    public BottomSheetFormBuilderWidget(@NonNull Context context) {
        super(context);
        this.context=context;
        //init parent container.
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogView = (View) layoutInflater.inflate(R.layout.bottom_sheet_dialog_host_layout,null);



        parentLayout=dialogView.findViewById(R.id.container);


        formLayout = new LinearLayout(context);
        formLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        formLayout.setOrientation(LinearLayout.VERTICAL);


    }

    public BottomSheetFormBuilderWidget setActivity(Activity a){
        this.activity=a;
        return this;
    }

    public BottomSheetFormBuilderWidget render(){

        //now render the view

        //
        if(title!=null){
            //remove the title
            TextView titleText = new TextView(context);
            titleText.setText(title);
            titleText.setTextSize(20);
            titleText.setPadding(0,15,0,40);
            parentLayout.addView(titleText);
        }

        parentLayout.addView(formLayout);

        //add buttons at last

        Button positiveButton = new Button(context);
        positiveButton.setText(positiveTitle);
        positiveButton.setBackground(context.getDrawable(R.drawable.default_auth_btn));
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(0,70,0,15);
        positiveButton.setLayoutParams(buttonLayoutParams);
        positiveButton.setClickable(true);
        positiveButton.setFocusable(true);

        positiveButton.setTextColor(context.getColor(R.color.white));

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String[] values = new String[inputLists.size()];
              int i=0;
              for(View input : inputLists){
                  if(input instanceof EditTextInput){
                      values[i]=((EditTextInput) input).getText().toString();
                      i++;
                  }
              }
              if(values[0]!=null && !values[0].equals("")){
                  BottomSheetFormBuilderWidget.this.dismiss();
              }
                onSubmitListener.onSubmit(values);

            }
        });


      parentLayout.addView(positiveButton);

      setContentView(dialogView);



        return this;
    }

    public BottomSheetFormBuilderWidget setPositiveTitle(String name){
        this.positiveTitle=name;
        return this;
    }

    public BottomSheetFormBuilderWidget setTitle(String name){
        this.title=name;
        return this;
    }


    public BottomSheetFormBuilderWidget addFolderVisibilitySwitch(SwitchCompat switchView){

        //add the item to linear layout
        formLayout.addView(switchView);
//        inputLists.add(input);

        return this;
    }
    public BottomSheetFormBuilderWidget addInputField(View input){

        //add the item to linear layout
        formLayout.addView(input);
        inputLists.add(input);

        return this;
    }

    public static class EditTextInput extends TextInputEditText {

        public EditTextInput(@NonNull Context context) {
            super(context);
            super.setBackground(context.getDrawable(R.drawable.default_input_bg));
            super.setPadding(20,30,20,30);
        }

        public EditTextInput onEmpty(){

            return this;
        }

        public EditTextInput required(boolean b){
            if(b){

            }else{

            }
            return this;
        }


        public EditTextInput setHint(String hint){
            super.setHint(hint);
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            buttonLayoutParams.setMargins(0,15,0,15);
            setLayoutParams(buttonLayoutParams);

            return this;
        }

        public EditTextInput setText(String text){
            super.setText(text);
            return this;
        }


        public EditTextInput autoFocus(){
            super.requestFocus();
            return this;
        }

    }

    public BottomSheetFormBuilderWidget setOnSubmit(OnSubmitHandler onSubmitListener){
        this.onSubmitListener=onSubmitListener;
        return this;
    }



    public static class OnSubmitHandler implements onSubmit{
        @Override
        public void onSubmit(String[] values) {


        }
    }


    interface onSubmit{
        void onSubmit(String[] values);
    }


}
