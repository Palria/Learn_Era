package com.palria.learnera;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CreateQuizActivity extends AppCompatActivity {
    Intent intent;
    String quizId;
    LinearLayout containerLinearLayout;
    LinearLayout topbar;
    MaterialButton addTheoryQuestionActionButton;
    MaterialButton addObjectiveQuestionActionButton;
    FloatingActionButton saveQuizActionButton;
    Switch publishIndicatorSwitch;
    ImageButton backButton;
    TextView textHeaderTextView;
    ImageButton menuButton;
    boolean isQuizEdition = false;
    boolean isPublish = true;
    boolean isDateSet = true;

    TextInputEditText quizTitleInput;
    TextInputEditText quizDescriptionInput;
    TextInputEditText quizFeeDescriptionInput;
    TextInputEditText quizRewardDescriptionInput;
    TextInputEditText quizDateInput;
    AlertDialog  alertDialog;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    long quizDay = 1;
    long quizMonth = 2;
    long quizYear = 2023;
    long quizHour = 12;
    long quizMinute = 30;
    ArrayList<Long> quizDateList = new ArrayList<>();
//    String[] timeLimits = {"5","10","15","20","25","30","35","40","45","50","55","60"};
    ArrayList<String> timeLimits = new ArrayList<>();
    Spinner categorySelector;
    String category;
    boolean isCategorySelected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        initUI();
        fetchIntentData();
        timeLimits.add("5");
        timeLimits.add("10");
        timeLimits.add("15");
        timeLimits.add("20");
        timeLimits.add("25");
        timeLimits.add("30");
        timeLimits.add("35");
        timeLimits.add("40");
        timeLimits.add("50");
        timeLimits.add("55");
        timeLimits.add("60");


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateQuizActivity.super.onBackPressed();
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalConfig.createPopUpMenu(CreateQuizActivity.this, R.menu.quiz_menu, menuButton, new GlobalConfig.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClicked(MenuItem item) {
                        if(item.getItemId() == R.id.closeQuizId){

                        }
                        return true;
                    }
                });
            }
        });
        publishIndicatorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isPublish = b;
            }
        });
        addTheoryQuestionActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTheoryQuestion(containerLinearLayout.getChildCount(),"30","");

            }
        });
        addObjectiveQuestionActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addObjectiveQuestion(containerLinearLayout.getChildCount(),"30","","","","", "");

            }
        });
        saveQuizActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showQuizCompletionConfirmationDialog();

            }
        });
        quizDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        if(isQuizEdition){
            GlobalConfig.getQuiz(this, quizId, new GlobalConfig.QuizCallback() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {


                    String quizDescription = ""+documentSnapshot.get(GlobalConfig.QUIZ_DESCRIPTION_KEY);
                    String quizTitle = ""+documentSnapshot.get(GlobalConfig.QUIZ_TITLE_KEY);
                    boolean isPublic = documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY)!=null?documentSnapshot.getBoolean(GlobalConfig.IS_PUBLIC_KEY):true;
                    long totalQuestions = documentSnapshot.get(GlobalConfig.TOTAL_QUESTIONS_KEY)!=null?documentSnapshot.getLong(GlobalConfig.TOTAL_QUESTIONS_KEY):0L;
                    ArrayList<Long> quizDateList1 = documentSnapshot.get(GlobalConfig.QUIZ_DATE_LIST_KEY)!=null? (ArrayList<Long>) documentSnapshot.get(GlobalConfig.QUIZ_DATE_LIST_KEY):new ArrayList<>();
                    String quizFeeDescription = ""+documentSnapshot.get(GlobalConfig.QUIZ_FEE_DESCRIPTION_KEY);
                    String quizRewardDescription = ""+documentSnapshot.get(GlobalConfig.QUIZ_REWARD_DESCRIPTION_KEY);

                    quizDescriptionInput.setText(quizDescription);
                    quizTitleInput.setText(quizTitle);
                    quizFeeDescriptionInput.setText(quizFeeDescription);
                    quizRewardDescriptionInput.setText(quizRewardDescription);
                    quizDateList = quizDateList1;

                    isDateSet = true;
                    publishIndicatorSwitch.setChecked(isPublic);

                    //set date
                    if(quizDateList1 !=null) {
                        if (quizDateList1.size() == 5) {
                            quizYear = quizDateList1.get(0);
                            quizMonth = quizDateList1.get(1);
                            quizDay = quizDateList1.get(2);
                            quizHour = quizDateList1.get(3);
                            quizMinute = quizDateList1.get(4);
                            quizDateInput.setText(quizDay + "/" + quizMonth + "/" + quizYear + " " + quizHour + ":" + quizMinute);
                        }
                    }

                    //render questions
                    for(long i=0; i<totalQuestions; i++){
                        ArrayList<String> questionArrayList = documentSnapshot.get(GlobalConfig.QUESTION_LIST_KEY+"-"+i)!=null? (ArrayList<String>) documentSnapshot.get(GlobalConfig.QUESTION_LIST_KEY+"-"+i):new ArrayList<>();
                        if(questionArrayList.get(0).equals(GlobalConfig.IS_THEORY_QUESTION_KEY)){
                            int position = Integer.parseInt(questionArrayList.get(1));
                            String timeLimit = questionArrayList.get(2);
                            String question = questionArrayList.get(3);
                            addTheoryQuestion( position, timeLimit, question);



                        }
                        else if(questionArrayList.get(0).equals(GlobalConfig.IS_OBJECTIVE_QUESTION_KEY)){
                            int position = Integer.parseInt(questionArrayList.get(1));
                            String timeLimit = questionArrayList.get(2);
                            String question = questionArrayList.get(3);
                            String option1 = questionArrayList.get(4);
                            String option2 = questionArrayList.get(5);
                            String option3 = questionArrayList.get(6);
                            String option4 = questionArrayList.get(7);
                            addObjectiveQuestion( position, timeLimit, question, option1, option2, option3, option4 );
                        }
                    }
                }

                @Override
                public void onFailed(String errorMessage) {

                }
            });
        }else {
            addTheoryQuestion(containerLinearLayout.getChildCount(),"30","");
            addObjectiveQuestion(containerLinearLayout.getChildCount(),"30","","","","","");
        }
        initCategorySpinner(categorySelector);
        prepareDatePickerDialog();
        prepareTimePickerDialog();
    }

    @Override
    public void onBackPressed() {
       createConfirmExitDialog();
    }

    private void initUI(){
        topbar = findViewById(R.id.topBar);
        backButton = findViewById(R.id.backButtonId);
        textHeaderTextView = findViewById(R.id.textHeaderTextViewId);
        menuButton = findViewById(R.id.menuButtonId);

        quizTitleInput = findViewById(R.id.quizTitleInput1Id);
        categorySelector = findViewById(R.id.categorySelectorSpinnerId);
        quizDateInput = findViewById(R.id.quizDateInputId);
        quizDescriptionInput = findViewById(R.id.quizDescriptionInput1Id);
        quizFeeDescriptionInput = findViewById(R.id.quizFeeInputId);
        quizRewardDescriptionInput = findViewById(R.id.quizRewardInputId);

        addTheoryQuestionActionButton = findViewById(R.id.addTheoryQuestionActionButtonId);
        addObjectiveQuestionActionButton = findViewById(R.id.addObjectiveQuestionActionButtonId);
        containerLinearLayout = findViewById(R.id.containerLinearLayoutId);
        saveQuizActionButton = findViewById(R.id.saveQuizActionButtonId);
        publishIndicatorSwitch = findViewById(R.id.publishIndicatorSwitchId);
//        setSupportActionBar(materialToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init progress.
        alertDialog = new AlertDialog.Builder(CreateQuizActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();

    }



    void fetchIntentData(){
         intent = getIntent();
         isQuizEdition = intent.getBooleanExtra(GlobalConfig.IS_EDITION_KEY,false);
        if(isQuizEdition){
            quizId = intent.getStringExtra(GlobalConfig.QUIZ_ID_KEY);
        }else{
            quizId = GlobalConfig.getRandomString(70);
        }
    }

    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }
    void addObjectiveQuestion( int position,String timeLimit,String question,String option1,String option2,String option3,String option4 ){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View questionView  =  layoutInflater.inflate(R.layout.objective_question_item,containerLinearLayout,false);
        Spinner timeSelectorSpinner = questionView.findViewById(R.id.timeSelectorSpinnerId);
        ImageView removeItemActionImage = questionView.findViewById(R.id.removeItemActionImageId);
        removeItemActionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containerLinearLayout.removeView(questionView);
            }
        });
        initSpinner(timeSelectorSpinner);


        TextInputEditText questionInput = questionView.findViewById(R.id.questionInput1Id);
        questionInput.setText(question);

        TextInputEditText option1Input = questionView.findViewById(R.id.answerInput1Id);
        TextInputEditText option2Input = questionView.findViewById(R.id.answerInput2Id);
        TextInputEditText option3Input = questionView.findViewById(R.id.answerInput3Id);
        TextInputEditText option4Input = questionView.findViewById(R.id.answerInput4Id);
        option1Input.setText(option1);
        option2Input.setText(option2);
        option3Input.setText(option3);
        option4Input.setText(option4);

        timeSelectorSpinner.setSelection(timeLimits.indexOf(timeLimit));

        containerLinearLayout.addView(questionView,position);
    }
    void addTheoryQuestion(int position,String timeLimit,String question){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View questionView  =  layoutInflater.inflate(R.layout.theory_question_item,containerLinearLayout,false);
        Spinner timeSelectorSpinner = questionView.findViewById(R.id.timeSelectorSpinnerId);
        ImageView removeItemActionImage = questionView.findViewById(R.id.removeItemActionImageId);
        TextInputEditText questionInput = questionView.findViewById(R.id.questionInput1Id);
        questionInput.setText(question);
        removeItemActionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containerLinearLayout.removeView(questionView);
            }
        });
        initSpinner(timeSelectorSpinner);


        questionInput.setText(question);
        timeSelectorSpinner.setSelection(timeLimits.indexOf(timeLimit));

        containerLinearLayout.addView(questionView,position);
    }
    private void initSpinner(Spinner timeSelectorSpinner){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,timeLimits);
        timeSelectorSpinner.setAdapter(arrayAdapter);
        timeSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                genderType = String.valueOf(genderTypeSpinner.getSelectedItem());
                //genderTypeEditText.setText(genderType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void initCategorySpinner(Spinner categorySelectorSpinner){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,GlobalConfig.getCategoryList(CreateQuizActivity.this));
        categorySelectorSpinner.setAdapter(arrayAdapter);

        categorySelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isCategorySelected = true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showQuizCompletionConfirmationDialog(){

        AlertDialog confirmationDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm your action");
        builder.setMessage("You are about to save your quiz, you need to deposit 1$ to create quiz, please confirm if you are ready. ");
        builder.setCancelable(true);
        builder.setIcon(R.drawable.ic_baseline_error_outline_24);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                processAndSaveQuiz();
                // todo redirect to payment for quiz deposit for test
//                Intent intent1 = GlobalConfig.getPaypalIntent(CreateQuizActivity.this, "1");
//                CreateQuizActivity.this.startActivityForResult(intent1, GlobalConfig.PAYPAL_PAYMENT_REQUEST_CODE);
                //PROCESS IF PAID OR NOT IN ACTIVITY RESULT
            }
        }).setNegativeButton("Edit more", null);
        confirmationDialog = builder.create();
        confirmationDialog.show();

        // successDialog.show();

    }
    private void createConfirmExitDialog(){
        AlertDialog confirmExitDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exit");
        builder.setMessage("Click exit button to exit the screen");
        builder.setCancelable(true);
        builder.setIcon(R.drawable.baseline_error_outline_red_24);

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CreateQuizActivity.super.onBackPressed();
            }
        })
                .setNegativeButton("Stay back", null);
        confirmExitDialog = builder.create();
        confirmExitDialog.show();

    }

    private void processAndSaveQuiz() {

        if (isCategorySelected) {
            toggleProgress(true);
            category = categorySelector.getSelectedItem() + "";
            ArrayList<ArrayList<String>> questionList = new ArrayList<>();
            int totalTimeLimit = 0;
            for (int i = 0; i < containerLinearLayout.getChildCount(); i++) {
                View questionView = containerLinearLayout.getChildAt(i);

                Spinner timeSelector1 = questionView.findViewById(R.id.timeSelectorSpinnerId);
                totalTimeLimit = totalTimeLimit + (Integer.parseInt(timeSelector1.getSelectedItem() + ""));

                ArrayList<String> theoryQuestionList = new ArrayList<>();

                if (questionView.getId() == R.id.theoryQuestionViewId) {
//                        Toast.makeText(CreateQuizActivity.this, "theory", Toast.LENGTH_SHORT).show();
                    TextInputEditText question = questionView.findViewById(R.id.questionInput1Id);
                    Spinner timeSelector = questionView.findViewById(R.id.timeSelectorSpinnerId);

                    //Indicates that this is an objective question
                    theoryQuestionList.add(0, GlobalConfig.IS_THEORY_QUESTION_KEY);
                    //stores the position of this question
                    theoryQuestionList.add(1, i + "");
                    //stores the time limit of this particular question
                    theoryQuestionList.add(2, timeSelector.getSelectedItem() + "");
                    //This stores the question
                    theoryQuestionList.add(3, question.getText() + "");

                    questionList.add(i, theoryQuestionList);
                } else if (questionView.getId() == R.id.objectiveQuestionViewId) {
//                        Toast.makeText(CreateQuizActivity.this, "objective", Toast.LENGTH_SHORT).show();
                    ArrayList<String> objectiveQuestionList = new ArrayList<>();

                    TextInputEditText question = questionView.findViewById(R.id.questionInput1Id);

                    TextInputEditText option1 = questionView.findViewById(R.id.answerInput1Id);
                    TextInputEditText option2 = questionView.findViewById(R.id.answerInput2Id);
                    TextInputEditText option3 = questionView.findViewById(R.id.answerInput3Id);
                    TextInputEditText option4 = questionView.findViewById(R.id.answerInput4Id);

                    Spinner timeSelector = questionView.findViewById(R.id.timeSelectorSpinnerId);
                    //Indicates that this is an objective question
                    objectiveQuestionList.add(0, GlobalConfig.IS_OBJECTIVE_QUESTION_KEY);
                    //stores the position of this question
                    objectiveQuestionList.add(1, i + "");
                    //stores the time limit of this particular question
                    objectiveQuestionList.add(2, timeSelector.getSelectedItem() + "");
                    //This stores the question
                    objectiveQuestionList.add(3, question.getText() + "");
                    //These are the answer options to the question
                    objectiveQuestionList.add(4, option1.getText() + "");
                    objectiveQuestionList.add(5, option2.getText() + "");
                    objectiveQuestionList.add(6, option3.getText() + "");
                    objectiveQuestionList.add(7, option4.getText() + "");


                    questionList.add(i, objectiveQuestionList);
                }

            }

            String quizTitle = quizTitleInput.getText() + "";
            String quizDescription = quizDescriptionInput.getText() + "";
            String quizFeeDescription = quizFeeDescriptionInput.getText() + "";
            String quizRewardDescription = quizRewardDescriptionInput.getText() + "";
            int totalQuestions = questionList.size();
            int finalTotalTimeLimit = totalTimeLimit;

            GlobalConfig.createQuiz(CreateQuizActivity.this, quizId, quizTitle, category, totalQuestions, totalTimeLimit, quizDescription, quizFeeDescription, quizRewardDescription, questionList, quizDateList, isQuizEdition, isPublish, new GlobalConfig.ActionCallback() {
                @Override
                public void onSuccess() {
                    toggleProgress(false);

                    GlobalConfig.createSnackBar2(CreateQuizActivity.this, quizTitleInput, "Your quiz is successfully posted", "View", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }

                @Override
                public void onFailed(String errorMessage) {

                    toggleProgress(false);

                    GlobalConfig.createSnackBar2(CreateQuizActivity.this, quizTitleInput, "Failed", "Retry", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            processAndSaveQuiz();

                        }
                    });

                }
            });
        }
        else{
          categorySelector.performClick();
            Toast.makeText(this, "Please select category", Toast.LENGTH_SHORT).show();
        }
    }
    void prepareDatePickerDialog(){

        Calendar dateCalendar = Calendar.getInstance();
        final int YEAR = dateCalendar.get(Calendar.YEAR);
        final int MONTH = dateCalendar.get(Calendar.MONTH);
        final int DAY_OF_MONTH = dateCalendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int YEAR, int MONTH, int DAY_OF_MONTH) {
                MONTH += 1 ;
                  quizDay = DAY_OF_MONTH;
                  quizMonth = MONTH;
                  quizYear = YEAR;

                timePickerDialog.show();
            }
        }, YEAR, MONTH, DAY_OF_MONTH);
        datePickerDialog.setCancelable(false);

    }
void prepareTimePickerDialog(){

    Calendar dateCalendar = Calendar.getInstance();
    final int HOUR = dateCalendar.get(Calendar.HOUR_OF_DAY);
    final int MINUTE = dateCalendar.get(Calendar.MINUTE);
    final int SECOND = dateCalendar.get(Calendar.SECOND);

    timePickerDialog = new TimePickerDialog(CreateQuizActivity.this, new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            quizHour = i;
            quizMinute = i1;
            quizDateInput.setText(quizDay  + "/" + quizMonth + "/" + quizYear+" "+i+":"+i1);
            quizDateList.clear();
            quizDateList.add(quizYear);
            quizDateList.add(quizMonth);
            quizDateList.add(quizDay);
            quizDateList.add(quizHour);
            quizDateList.add(quizMinute);
            isDateSet = true;
        }
    }, HOUR, MINUTE, true);
    timePickerDialog.setCancelable(false);
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GlobalConfig.PAYPAL_PAYMENT_REQUEST_CODE){
            if(resultCode == RESULT_OK) {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null) {
                    String paymentDetails = paymentConfirmation.toJSONObject().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(paymentDetails);
                        Log.w(paymentDetails, "createQuizActivity");
                        // payment done now save the quiz...
                        //processAndSaveQuiz();
                    } catch (JSONException e) {
                        Toast.makeText(this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Payment cancelled! please try again.", Toast.LENGTH_SHORT).show();
            }else if(resultCode==PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(this, "Invalid payment request.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

