package com.palria.learnera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;

import com.palria.learnera.adapters.UserActivityItemRCVAdapter;
import com.palria.learnera.models.UserActivityDataModel;

import java.util.ArrayList;

public class UserActivityLogActivity extends AppCompatActivity {


    private RecyclerView activityLogContainer;
    ArrayList<UserActivityDataModel> activityDataModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log);

        initUi();

    }

    private void initUi() {
        Toolbar actionBar = (Toolbar)  findViewById(R.id.topBar);
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activityLogContainer = findViewById(R.id.activityLogContainer);

        //date divider model
        activityDataModelArrayList.add(new UserActivityDataModel("",
                "Today", "","",0,"",true));

        activityDataModelArrayList.add(new UserActivityDataModel("User created a new Library.",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=10",1,"library",false));
        activityDataModelArrayList.add(new UserActivityDataModel("User post a review in Tutorials.",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=5",1,"tutorial",false));
        activityDataModelArrayList.add(new UserActivityDataModel("Post a new book in "+ Html.fromHtml("<b style='color:black;'> Java Development </b>") +" .",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=12",1,"post",false));
        activityDataModelArrayList.add(new UserActivityDataModel("Logs a new Rating to the Ram Magar Library.",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=56",1,"library",false));
        activityDataModelArrayList.add(new UserActivityDataModel("Deleted review from Hemans Tutorial.",
                "15 Feb 2023", "Hello This is my first Tutorial...","https://picsum.photos/400/200?random=22",1,"rating",false));
        //date divider model
        activityDataModelArrayList.add(new UserActivityDataModel("",
                "Yesterday", "","",0,"",true));
        activityDataModelArrayList.add(new UserActivityDataModel("Post a new book in "+ Html.fromHtml("<b style='color:black;'> Java Development </b>") +" .",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=12",1,"post",false));
        activityDataModelArrayList.add(new UserActivityDataModel("Logs a new Rating to the Ram Magar Library.",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=56",1,"library",false));
        activityDataModelArrayList.add(new UserActivityDataModel("Deleted review from Hemans Tutorial.",
                "15 Feb 2023", "Hello This is my first Tutorial...","https://picsum.photos/400/200?random=22",1,"rating",false));

        //date divider model
        activityDataModelArrayList.add(new UserActivityDataModel("",
                "Earlier this week", "","",0,"",true));
        activityDataModelArrayList.add(new UserActivityDataModel("Post a new book in "+ Html.fromHtml("<b style='color:black;'> Java Development &#9733; &#9733; &#9733; </b>") +" .",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=12",1,"post",false));
        activityDataModelArrayList.add(new UserActivityDataModel("Logs a new Rating to the Ram Magar Library.",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=56",1,"library",false));
        activityDataModelArrayList.add(new UserActivityDataModel("Deleted review from Hemans Tutorial.",
                "15 Feb 2023", "Hello This is my first Tutorial...","https://picsum.photos/400/200?random=22",1,"rating",false));


        UserActivityItemRCVAdapter userActivityItemRCVAdapter = new UserActivityItemRCVAdapter(activityDataModelArrayList,this);
        activityLogContainer.setHasFixedSize(true);
        activityLogContainer.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        activityLogContainer.setAdapter(userActivityItemRCVAdapter);




    }
}