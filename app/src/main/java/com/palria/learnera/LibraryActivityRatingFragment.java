package com.palria.learnera;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.palria.learnera.adapters.RatingItemRecyclerViewAdapter;
import com.palria.learnera.models.RatingDataModel;
import com.palria.learnera.widgets.RatingBarWidget;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibraryActivityRatingFragment#} factory method to
 * create an instance of this fragment.
 */

public class LibraryActivityRatingFragment extends Fragment {

    LinearLayout ratingBarContainer;
    RecyclerView ratingsRecyclerListView;

    public LibraryActivityRatingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_library_activity_rating, container, false);
        initView(view);


        return view;
    }

    private void initView(View parentView) {

        ratingBarContainer=parentView.findViewById(R.id.ratingBarContainer);
        ratingsRecyclerListView=parentView.findViewById(R.id.ratingsRecyclerListView);

        ArrayList<RatingDataModel> ratingDataModels = new ArrayList<>();

        ratingDataModels.add(new RatingDataModel("lasdjf","Karmalu Sherpa",
                "123",
                "library",
                "Sat 01",
                "Hello i love this app so much dude.",
                4,
                "https://api.lorem.space/image/face?w=150&h=150"));

        ratingDataModels.add(new RatingDataModel("lasdjf","Hira Kamu",
                "123",
                "library",
                "June 01",
                "Wow perficet.",
                4,
                "https://api.lorem.space/image/face?w=150&h=150"));

        ratingDataModels.add(new RatingDataModel("lasdjf","Nima Shrestha",
                "123",
                "library",
                "Feb 04",
                "Everytihing is good but have some bugs in this tutorial. please fix it fast now .",
                4,
                "https://api.lorem.space/image/face?w=150&h=150"));


        RatingItemRecyclerViewAdapter ratingItemRecyclerViewAdapter = new RatingItemRecyclerViewAdapter(ratingDataModels,getContext());


        ratingsRecyclerListView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        ratingsRecyclerListView.setHasFixedSize(true);
        ratingsRecyclerListView.setAdapter(ratingItemRecyclerViewAdapter);

        //load rating bar
        int[] ratings = {40,12,155,1,15};

        RatingBarWidget ratingBarWidget = new RatingBarWidget();
        ratingBarWidget.setContainer(ratingBarContainer)
                .setContext(getContext())
                .setMax_value(5)
                .setRatings(ratings)
                .setText_color(R.color.teal_700)
                .render();



    }
}