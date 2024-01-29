package com.palria.learnera;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class CustomizeTabActivity extends AppCompatActivity {
    ChipGroup categoriesChipGroup;
    MaterialButton saveCategoryActionTextView;
    ArrayList<String> newCustomCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_tab);
        initUI();
        initCategoriesChip();
        saveCategoryActionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalConfig.setCustomizedCategoryList(newCustomCategories,getApplicationContext());
                saveCategoryActionTextView.setEnabled(false);
                saveCategoryActionTextView.setText("Saved");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.quiz_action_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

       if(item.getItemId()==R.id.home){
            super.onBackPressed();

        }
        return true;
    }


    void initUI(){
    categoriesChipGroup=findViewById(R.id.categoriesChipGroup);
        saveCategoryActionTextView=findViewById(R.id.saveCategoryActionTextViewId);

}
    private void initCategoriesChip(){
        ArrayList<String> categories = GlobalConfig.getCategoryList(getApplicationContext());
        ArrayList<String> oldCustomCategories = GlobalConfig.getCustomizedCategoryList(getApplicationContext());

        for(String cat : categories){
            Chip chip = new Chip(this);
            chip.setText(cat);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), chip.getText()+"", Toast.LENGTH_SHORT).show();
                }
            });
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if(isChecked) {
                     newCustomCategories.remove(chip.getText() + "");
                     newCustomCategories.add(chip.getText() + "");
                     saveCategoryActionTextView.setEnabled(true);
                 }else{
                     newCustomCategories.remove(chip.getText() + "");
                 }

                }
            });
            chip.setCheckable(true);
            if(oldCustomCategories.contains(chip.getText()+"")){
                chip.setChecked(true);
            }
            categoriesChipGroup.addView(chip);
        }

    }

}