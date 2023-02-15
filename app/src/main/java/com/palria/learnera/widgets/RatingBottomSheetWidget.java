package com.palria.learnera.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.palria.learnera.R;

public class RatingBottomSheetWidget extends BottomSheetDialog {


    private Context context;

    private OnRatingPosted onPost;


    public RatingBottomSheetWidget(@NonNull Context context) {
        super(context);
        this.context=context;

    }





    public void render(){
        //initzlize the view for the bottom sheet

        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.rating_widget_bottom_sheet_default,null);


        EditText editText = view.findViewById(R.id.ratingBodyEditText);
        Button button = view.findViewById(R.id.postRatingActionButton);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
onPost.onPost(ratingBar.getProgress(),editText.getText().toString().trim());
                    //onRatingPosted.onPost(ratingBar.getProgress(), editText.getText().toString().trim());
            }
        });



        this.setContentView(view);

    }


    public RatingBottomSheetWidget setRatingPostListener(OnRatingPosted onRatingPosted){
        this.onPost=onRatingPosted;
        return this;
    }


    public static class OnRatingPosted implements OnPostRating{

        @Override
        public void onPost(int star, String message) {

        }
    }



    interface OnPostRating{
        void onPost(int star, String message);
    }

}
