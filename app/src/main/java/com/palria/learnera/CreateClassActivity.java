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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateClassActivity extends AppCompatActivity {
    Intent intent;
    String classId;
    LinearLayout containerLinearLayout;
    LinearLayout topbar;
    FloatingActionButton saveClassActionButton;
    Switch publishIndicatorSwitch;
    ImageButton backButton;
    TextView textHeaderTextView;
    ImageButton menuButton;
    boolean isClassEdition = false;
    boolean isPublish = true;
    boolean isStartDateSet = true;
    boolean isEndDateSet = true;

    TextInputEditText classTitleInput;
    TextInputEditText classDescriptionInput;
    TextInputEditText classFeeDescriptionInput;
    TextInputEditText classStartDateInput;
    TextInputEditText classEndDateInput;
    AlertDialog  alertDialog;
    DatePickerDialog startDatePickerDialog;
    DatePickerDialog endDatePickerDialog;
    TimePickerDialog startTimePickerDialog;
    TimePickerDialog endTimePickerDialog;

    long classStartDay = 1;
    long classStartMonth = 2;
    long classStartYear = 2023;
    long classStartHour = 12;
    long classStartMinute = 30;
    ArrayList<Long> classStartDateList = new ArrayList<>();

    long classEndDay = 1;
    long classEndMonth = 2;
    long classEndYear = 2023;
    long classEndHour = 12;
    long classEndMinute = 30;
    ArrayList<Long> classEndDateList = new ArrayList<>();

    Spinner categorySelector;
    String category;
    boolean isCategorySelected = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        initUI();
        fetchIntentData();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateClassActivity.super.onBackPressed();
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalConfig.createPopUpMenu(CreateClassActivity.this, R.menu.class_menu, menuButton, new GlobalConfig.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClicked(MenuItem item) {
                        if(item.getItemId() == R.id.closeClassId){

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
        saveClassActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showClassCompletionConfirmationDialog();

            }
        });
        classStartDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDatePickerDialog.show();
            }
        });
        classEndDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDatePickerDialog.show();
            }
        });
        if(isClassEdition){
            GlobalConfig.getClass(this, classId, new GlobalConfig.ClassCallback() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    String classDescription = ""+documentSnapshot.get(GlobalConfig.CLASS_DESCRIPTION_KEY);
                    String classTitle = ""+documentSnapshot.get(GlobalConfig.CLASS_TITLE_KEY);
                    boolean isPublic = documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY)!=null?documentSnapshot.getBoolean(GlobalConfig.IS_PUBLIC_KEY):true;
                    long totalQuestions = documentSnapshot.get(GlobalConfig.TOTAL_QUESTIONS_KEY)!=null?documentSnapshot.getLong(GlobalConfig.TOTAL_QUESTIONS_KEY):0L;

                    ArrayList<Long> classStartDateList1 = documentSnapshot.get(GlobalConfig.CLASS_START_DATE_LIST_KEY)!=null? (ArrayList<Long>) documentSnapshot.get(GlobalConfig.CLASS_START_DATE_LIST_KEY):new ArrayList<>();
                    ArrayList<Long> classEndDateList1 = documentSnapshot.get(GlobalConfig.CLASS_END_DATE_LIST_KEY)!=null? (ArrayList<Long>) documentSnapshot.get(GlobalConfig.CLASS_END_DATE_LIST_KEY):new ArrayList<>();

//                    String classFeeDescription = ""+documentSnapshot.get(GlobalConfig.CLASS_FEE_DESCRIPTION_KEY);
//                    String classRewardDescription = ""+documentSnapshot.get(GlobalConfig.CLASS_REWARD_DESCRIPTION_KEY);
                    long totalClassFeeCoins =  documentSnapshot.get(GlobalConfig.TOTAL_CLASS_FEE_COINS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_CLASS_FEE_COINS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_CLASS_FEE_COINS_KEY) : 0L;

                    classDescriptionInput.setText(classDescription);
                    classTitleInput.setText(classTitle);
                    classFeeDescriptionInput.setText(totalClassFeeCoins+"");
                    classStartDateList = classStartDateList1;
                    classEndDateList = classEndDateList1;

                    isStartDateSet = true;
                    isEndDateSet = true;
                    publishIndicatorSwitch.setChecked(isPublic);

                    //set start date
                    if(classStartDateList1 !=null) {
                        if (classStartDateList1.size() == 5) {
                            classStartYear = classStartDateList1.get(0);
                            classStartMonth = classStartDateList1.get(1);
                            classStartDay = classStartDateList1.get(2);
                            classStartHour = classStartDateList1.get(3);
                            classStartMinute = classStartDateList1.get(4);
                            classStartDateInput.setText(classStartDay + "/" + classStartMonth + "/" + classStartYear + " " + classStartHour + ":" + classStartMinute);
                        }
                    }

                    //set end date
                    if(classEndDateList1 !=null) {
                        if (classEndDateList1.size() == 5) {
                            classEndYear = classEndDateList1.get(0);
                            classEndMonth = classEndDateList1.get(1);
                            classEndDay = classEndDateList1.get(2);
                            classEndHour = classEndDateList1.get(3);
                            classEndMinute = classEndDateList1.get(4);
                            classEndDateInput.setText(classEndDay + "/" + classEndMonth + "/" + classEndYear + " " + classEndHour + ":" + classEndMinute);
                        }
                    }

                }

                @Override
                public void onFailed(String errorMessage) {

                }
            });
        }else {


        }
        initCategorySpinner(categorySelector);
        prepareStartDatePickerDialog();
        prepareEndDatePickerDialog();
        prepareStartTimePickerDialog();
        prepareEndTimePickerDialog();
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

        classTitleInput = findViewById(R.id.classTitleInput1Id);
        categorySelector = findViewById(R.id.categorySelectorSpinnerId);
        classStartDateInput = findViewById(R.id.classStartDateInputId);
        classEndDateInput = findViewById(R.id.classEndDateInputId);
        classDescriptionInput = findViewById(R.id.classDescriptionInput1Id);
        classFeeDescriptionInput = findViewById(R.id.classFeeInputId);

        containerLinearLayout = findViewById(R.id.containerLinearLayoutId);
        saveClassActionButton = findViewById(R.id.saveClassActionButtonId);
        publishIndicatorSwitch = findViewById(R.id.publishIndicatorSwitchId);
//        setSupportActionBar(materialToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init progress.
        alertDialog = new AlertDialog.Builder(CreateClassActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();

    }



    void fetchIntentData(){
         intent = getIntent();
         isClassEdition = intent.getBooleanExtra(GlobalConfig.IS_EDITION_KEY,false);
        if(isClassEdition){
            classId = intent.getStringExtra(GlobalConfig.CLASS_ID_KEY);
        }else{
            classId = GlobalConfig.getRandomString(70);
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
    private void initCategorySpinner(Spinner categorySelectorSpinner){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,GlobalConfig.getCategoryList(CreateClassActivity.this));
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

    private void showClassCompletionConfirmationDialog(){

        AlertDialog confirmationDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm your action");
        builder.setMessage("You are about to save your class, please confirm if you are ready. ");
        builder.setCancelable(true);
        builder.setIcon(R.drawable.ic_baseline_error_outline_24);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                processAndSaveClass();
                // todo redirect to payment for class deposit for test
//                Intent intent1 = GlobalConfig.getPaypalIntent(CreateClassActivity.this, "1");
//                CreateClassActivity.this.startActivityForResult(intent1, GlobalConfig.PAYPAL_PAYMENT_REQUEST_CODE);
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
                CreateClassActivity.super.onBackPressed();
            }
        })
                .setNegativeButton("Stay back", null);
        confirmExitDialog = builder.create();
        confirmExitDialog.show();

    }

    private void processAndSaveClass() {

        if ((classTitleInput.getText()+"").isEmpty()){
            classTitleInput.performClick();
            classTitleInput.requestFocus();
            Toast.makeText(this, "Please enter class title", Toast.LENGTH_SHORT).show();
        return;
        }
        if (isCategorySelected){
        if (!(classStartDateInput.getText()+"").isEmpty()) {
            if (!(classEndDateInput.getText() + "").isEmpty()) {
                category = categorySelector.getSelectedItem() + "";

                String classTitle = classTitleInput.getText() + "";
                String classDescription = classDescriptionInput.getText() + "";
                int totalClassFeeCoins = Integer.parseInt(!(classFeeDescriptionInput.getText() + "").isEmpty()? classFeeDescriptionInput.getText() + "":"0");

                GlobalConfig.createClass(CreateClassActivity.this, classId, classTitle, category, classDescription, totalClassFeeCoins, classStartDateList, classEndDateList, isClassEdition, isPublish, new GlobalConfig.ActionCallback() {
                    @Override
                    public void onSuccess() {
                        toggleProgress(false);
                        GlobalConfig.createSnackBar2(CreateClassActivity.this, classTitleInput, "Your class is successfully posted", "View", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CreateClassActivity.super.finish();
                                Intent intent = new Intent(CreateClassActivity.this, ClassActivity.class);
                                intent.putExtra(GlobalConfig.CLASS_ID_KEY, classId);
                                intent.putExtra(GlobalConfig.AUTHOR_ID_KEY, GlobalConfig.getCurrentUserId());
                                intent.putExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY, true);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onFailed(String errorMessage) {

                        toggleProgress(false);

                        GlobalConfig.createSnackBar2(CreateClassActivity.this, classTitleInput, "Failed", "Retry", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                processAndSaveClass();

                            }
                        });

                    }
                });
                toggleProgress(true);

            } else {
                classEndDateInput.performClick();
                Toast.makeText(this, "Please select end date of class", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            classStartDateInput.performClick();
            Toast.makeText(this, "Please select start date of class", Toast.LENGTH_SHORT).show();
        }
        }
        else{
          categorySelector.performClick();
            Toast.makeText(this, "Please select category", Toast.LENGTH_SHORT).show();
        }
    }
    void prepareStartDatePickerDialog(){

        Calendar dateCalendar = Calendar.getInstance();
        final int YEAR = dateCalendar.get(Calendar.YEAR);
        final int MONTH = dateCalendar.get(Calendar.MONTH);
        final int DAY_OF_MONTH = dateCalendar.get(Calendar.DAY_OF_MONTH);

        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int YEAR, int MONTH, int DAY_OF_MONTH) {
                MONTH += 1 ;
                  classStartDay = DAY_OF_MONTH;
                  classStartMonth = MONTH;
                  classStartYear = YEAR;

                startTimePickerDialog.show();
            }
        }, YEAR, MONTH, DAY_OF_MONTH);
        startDatePickerDialog.setCancelable(false);

    }
    void prepareEndDatePickerDialog(){

        Calendar dateCalendar = Calendar.getInstance();
        final int YEAR = dateCalendar.get(Calendar.YEAR);
        final int MONTH = dateCalendar.get(Calendar.MONTH);
        final int DAY_OF_MONTH = dateCalendar.get(Calendar.DAY_OF_MONTH);

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int YEAR, int MONTH, int DAY_OF_MONTH) {
                MONTH += 1 ;
                  classEndDay = DAY_OF_MONTH;
                  classEndMonth = MONTH;
                  classEndYear = YEAR;

                endTimePickerDialog.show();
            }
        }, YEAR, MONTH, DAY_OF_MONTH);
        endDatePickerDialog.setCancelable(false);

    }
void prepareStartTimePickerDialog(){

    Calendar dateCalendar = Calendar.getInstance();
    final int HOUR = dateCalendar.get(Calendar.HOUR_OF_DAY);
    final int MINUTE = dateCalendar.get(Calendar.MINUTE);
    final int SECOND = dateCalendar.get(Calendar.SECOND);
    final int AM_PM = dateCalendar.get(Calendar.AM_PM);

//    final int timeFormat = dateCalendar.get(Calendar.SECOND);
//    final int timeFormat = LocalTime.now().get(Calendar.SECOND);

    startTimePickerDialog = new TimePickerDialog(CreateClassActivity.this, new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            classStartHour = i;
            classStartMinute = i1;
            classStartDateInput.setText(classStartDay  + "/" + classStartMonth + "/" + classStartYear+" "+i+":"+i1);
            classStartDateList.clear();
            classStartDateList.add(classStartYear);
            classStartDateList.add(classStartMonth);
            classStartDateList.add(classStartDay);
            if(AM_PM == Calendar.AM) {
                classStartDateList.add(classStartHour +12);
                Toast.makeText(getApplicationContext(), classStartHour +12+" AM", Toast.LENGTH_SHORT).show();
            }else{
                classStartDateList.add(classStartHour);
                Toast.makeText(getApplicationContext(), classStartHour +" PM", Toast.LENGTH_SHORT).show();

            }
            classStartDateList.add(classStartMinute);
//            classStartDateList.add(timeFormat);
            isStartDateSet = true;
        }
    }, HOUR, MINUTE, false);
    startTimePickerDialog.setCancelable(false);
}

void prepareEndTimePickerDialog(){

    Calendar dateCalendar = Calendar.getInstance();
    final int HOUR = dateCalendar.get(Calendar.HOUR_OF_DAY);
    final int MINUTE = dateCalendar.get(Calendar.MINUTE);
    final int SECOND = dateCalendar.get(Calendar.SECOND);
    final int AM_PM = dateCalendar.get(Calendar.AM_PM);

    endTimePickerDialog = new TimePickerDialog(CreateClassActivity.this, new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            classEndHour = i;
            classEndMinute = i1;
            classEndDateInput.setText(classEndDay  + "/" + classEndMonth + "/" + classEndYear+" "+i+":"+i1);
            classEndDateList.clear();
            classEndDateList.add(classEndYear);
            classEndDateList.add(classEndMonth);
            classEndDateList.add(classEndDay);
            if(AM_PM == Calendar.AM) {
                classEndDateList.add(classEndHour +12);
            }else{
                classEndDateList.add(classEndHour);

            }
            classEndDateList.add(classEndMinute);
            isEndDateSet = true;
        }
    }, HOUR, MINUTE, false);
    endTimePickerDialog.setCancelable(false);
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
                        Log.w(paymentDetails, "createClassActivity");
                        // payment done now save the class...
                        //processAndSaveClass();
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

