package com.palria.learnera.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.palria.learnera.R;

import java.text.DecimalFormat;

public class RatingBarWidget {

    /**
     * show rating label like 5,4,3,2,1 & count
     */
    private boolean show_label;
    /**
     * rating max start default=5stars
     */
    private int max_value;
    /**
     * rating label color to be show
     */
    private int text_color;
    /**
     * rating label size
     */
    private int text_size;
    /**
     * rating layout container width
     */
    private int width;
    /**
     * rating layout container height max-
     */
    private int height;
    /**
     * gap between two bars
     */
    private int spaces;
    /**
     * bool wheather the bar is rounded or not
     */
    private boolean rounded;
    /**
     * set bar color int default primary color
     */
    private int bar_color;

    /**
     * total ratings list containing int values which has values of 5 stars
     * like : {25,44,2,3,234} indicates 25=1star, 44=2star, 2=3star count and so on.
     * 25 person rates as 1 star
     * 44 person rates as 2 star
     * ...
     */
    private int[] ratings;

    /**
     * parent context of the application
     */
    private Context context;
    /**
     * container where to load the rating bar widget in.
     */
    private Object container;


    public RatingBarWidget() {
    }

    public RatingBarWidget(int max_value, int[] ratings, Context context, Object container) {
        this.max_value = max_value;
        this.ratings = ratings;
        this.context = context;
        this.container = container;
    }


    @SuppressLint("SetTextI18n")
    private Object create(){

        double average_rate = calculateAverageRating();


        DecimalFormat df = new DecimalFormat("#.#");
        String rating_string_average =  df.format(average_rate);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.rating_bar_default_layout,null);

        TextView averageRatingTextView = view.findViewById(R.id.avgRating);
        TextView totalRatingCountView = view.findViewById(R.id.totalRatings);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);

        LinearProgressIndicator progressIndicatorFive = view.findViewById(R.id.progressFive);
        LinearProgressIndicator progressIndicatorFour = view.findViewById(R.id.progressFour);
        LinearProgressIndicator progressIndicatorThree = view.findViewById(R.id.progressThree);
        LinearProgressIndicator progressIndicatorTwo = view.findViewById(R.id.progressTwo);
        LinearProgressIndicator progressIndicatorOne = view.findViewById(R.id.progressOne);


        //set tint color if set
        if(text_color > 0){

           ColorStateList colorStateList= new ColorStateList(
                    new int[][]{ new int[]{android.R.attr.state_pressed},
                            // pressed
                            new int[]{},
                            // default
                            new int[]{},
                            new int[]{},
                            new int[]{}
                    },
                    new int[]{ context.getColor(text_color),
                            // pressed
                            context.getColor(text_color),
                            // default
                            context.getColor(text_color),
                            context.getColor(text_color),
                            context.getColor(text_color)
                    });

            ratingBar.setProgressTintList(colorStateList);
            progressIndicatorFive.setProgressTintList(ColorStateList.valueOf(text_color));
            progressIndicatorFive.setIndeterminateTintList(ColorStateList.valueOf(Color.RED));
            progressIndicatorFive.getIndeterminateDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            progressIndicatorFour.setProgressTintList(ColorStateList.valueOf(text_color));
            progressIndicatorThree.setProgressTintList(ColorStateList.valueOf(text_color));
            progressIndicatorTwo.setProgressTintList(ColorStateList.valueOf(text_color));
            progressIndicatorOne.setProgressTintList(ColorStateList.valueOf(text_color));
        }

        int total = getTotalRatingcount();

        //set the values here
        averageRatingTextView.setText(rating_string_average.length() == 1 ? rating_string_average.concat(".0"):rating_string_average);
        totalRatingCountView.setText(total+"");
        ratingBar.setProgress((int) average_rate,true);


        double rate_progress_five = (ratings[4]/(total+0.0))*100;
        double rate_progress_four = (ratings[3]/(total+0.0))*100;
        double rate_progress_three = (ratings[2]/(total+0.0))*100;
        double rate_progress_two = (ratings[1]/(total+0.0))*100;
        double rate_progress_one = (ratings[0]/(total+0.0))*100;

        progressIndicatorFive.setProgress((int)rate_progress_five);
        progressIndicatorFour.setProgress((int) rate_progress_four);
        progressIndicatorThree.setProgress((int) rate_progress_three);
        progressIndicatorTwo.setProgress((int)rate_progress_two);
        progressIndicatorOne.setProgress((int)rate_progress_one);
        //return newly created view
        return view;
    }

    private int getTotalRatingcount(){
        int totalStarsCount=0;
        if(ratings==null)return  0;
        for(int r : ratings){
            totalStarsCount+=r;
        }
        return totalStarsCount;
    }

    private double calculateAverageRating(){

        //claculate avg here
        //(1*0 + 2 * 50 + 3 * 3 + 4 * 5 + 5 * 15) / 78


        double average_rate = (ratings[0] + 2 * ratings[1] + 3 * ratings[2] + 4 * ratings[3] + 5 * ratings[4]) / (getTotalRatingcount()+0.0);




        return average_rate;
    }

    public void render(){

        //show now

        View view = (View) this.create();
        //now add the view to container

        LinearLayout c = (LinearLayout) container;

        if(c == null){
            Log.e("error","Exception : null container in ratingWidget.");
            return;
        }

        c.removeAllViews();

        //now add the view
        c.addView(view);
        //now done adding the views.



    }

    public boolean isShow_label() {
        return show_label;
    }

    public RatingBarWidget setShow_label(boolean show_label) {
        this.show_label = show_label;
        return this;
    }

    public int getMax_value() {
        return max_value;
    }

    public RatingBarWidget setMax_value(int max_value) {
        this.max_value = max_value;
        return this;
    }

    public int getText_color() {
        return text_color;
    }

    public RatingBarWidget setText_color(int text_color) {
        this.text_color = text_color;
        return this;
    }

    public int getText_size() {
        return text_size;
    }

    public RatingBarWidget setText_size(int text_size) {
        this.text_size = text_size;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public RatingBarWidget setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public RatingBarWidget setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getSpaces() {
        return spaces;
    }

    public RatingBarWidget setSpaces(int spaces) {
        this.spaces = spaces;
        return this;
    }

    public boolean isRounded() {
        return rounded;
    }

    public RatingBarWidget setRounded(boolean rounded) {
        this.rounded = rounded;
        return this;
    }

    public int getBar_color() {
        return bar_color;
    }

    public RatingBarWidget setBar_color(int bar_color) {
        this.bar_color = bar_color;
        return this;
    }

    public int[] getRatings() {
        return ratings;
    }

    public RatingBarWidget setRatings(int[] ratings) {
        this.ratings = ratings;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public RatingBarWidget setContext(Context context) {
        this.context = context;
        return this;
    }

    public Object getContainer() {
        return container;
    }

    public RatingBarWidget setContainer(Object container) {
        this.container = container;
        return this;
    }
}
