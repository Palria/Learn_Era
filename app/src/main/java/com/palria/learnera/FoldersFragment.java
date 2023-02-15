package com.palria.learnera;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.palria.learnera.adapters.FolderRcvAdapter;
import com.palria.learnera.models.FolderDataModel;

import java.util.ArrayList;


public class FoldersFragment extends Fragment {

   RecyclerView foldersRcv;
   ArrayList<FolderDataModel> folderDataModels = new ArrayList<>();


    public FoldersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_folders, container, false);


        initUi(view);

        return view;

    }

    private void initUi(View view) {
        foldersRcv=view.findViewById(R.id.foldersRcv);

        //init oflder rcv here
        folderDataModels.add(new FolderDataModel("","","Design princilples","1 min ago"));
        folderDataModels.add(new FolderDataModel("","","OOps Concept Advanced","45 min ago"));
        folderDataModels.add(new FolderDataModel("","","Ui Guide 2023","1 hours ago"));
        folderDataModels.add(new FolderDataModel("","","Domain Registration P","5 hours ago"));
        folderDataModels.add(new FolderDataModel("","","Lambda Expression with Joy","1 day ago"));
        folderDataModels.add(new FolderDataModel("","","YOutube Policy changes overview","1 week ago"));
        FolderRcvAdapter folderRcvAdapter= new FolderRcvAdapter(folderDataModels,getContext());

        foldersRcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        foldersRcv.setHasFixedSize(false);
        foldersRcv.setAdapter(folderRcvAdapter);



    }
}