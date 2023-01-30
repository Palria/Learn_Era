package com.palria.learnera;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TutorialsReviewedFragment extends Fragment {

    public TutorialsReviewedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View parentView = inflater.inflate(R.layout.fragment_tutorials_reviewed, container, false);

        return parentView;
    }
}