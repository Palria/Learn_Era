package com.palria.learnera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palria.learnera.adapters.WelcomeScreenvpAdapter;
import com.palria.learnera.models.WelcomeScreenItemModal;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    ViewPager viewPager;
    TextView nextBtn;
    TextView prevBtn;
    LinearLayout dotsContainer;
    WelcomeScreenvpAdapter welcomeScreenvpAdapter;
    int activePosition=0;

    ArrayList<WelcomeScreenItemModal> screens_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);

        initUI();

    }

    /**
     * initialized the ui for welcome screenhere
     */
    private void initUI() {

        viewPager = findViewById(R.id.viewPager);
        nextBtn = findViewById(R.id.skip_next);
        prevBtn =findViewById(R.id.skip_prev);
        dotsContainer = findViewById(R.id.dots);


        screens_items = GlobalConfig.getWelcomeScreenItemsList();

        welcomeScreenvpAdapter = new WelcomeScreenvpAdapter(this,screens_items);

        viewPager.setAdapter(welcomeScreenvpAdapter);

        prevBtn.setText(Html.fromHtml("&#8592;"));
        nextBtn.setText(Html.fromHtml("&#8594;"));

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(activePosition == screens_items.size()-1){
                    //now move to next
                    Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }
                if(activePosition<screens_items.size()){
                    activePosition++;
                    viewPager.setCurrentItem(activePosition,true);
                }

                initIndicators(activePosition);
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activePosition>0){
                    activePosition--;
                    viewPager.setCurrentItem(activePosition,true);
                    initIndicators(activePosition);
                }
            }
        });

        initIndicators(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                activePosition=position;
                initIndicators(position);
                if(position>0) prevBtn.setVisibility(View.VISIBLE);
                else prevBtn.setVisibility(View.INVISIBLE);

                if(position==screens_items.size()-1){
                    nextBtn.setText("Finish");
                }else{
                    nextBtn.setText(Html.fromHtml("&#8594;"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initIndicators(int i) {

        //remove alldots if exits
        dotsContainer.removeAllViews();
        //add new dots now
        for(int j = 0; j < screens_items.size();j++){
            TextView dot_item = new TextView(this);
            dot_item.setText(Html.fromHtml("&#8226"));
            dot_item.setTextSize(35);
            if(i == j){
                //active position
                dot_item.setTextColor(getResources().getColor(R.color.teal_700,getApplicationContext().getTheme()));

            }else{
                //default position
                dot_item.setTextColor(getResources().getColor(R.color.gray,getApplicationContext().getTheme()));

            }
            dotsContainer.addView(dot_item);


        }

    }



}