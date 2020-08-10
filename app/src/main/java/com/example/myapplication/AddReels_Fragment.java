package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.ArrayList;
import java.util.List;


public class AddReels_Fragment extends Fragment {
    private List<MediaObject> mediaObjectList = new ArrayList<>();
    private DemoAdapter demoAdapter;
    private RecyclerView recyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_reels_, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.reelsRecycler_View);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        mediaObjectList.add(new MediaObject("", "", "", "", "", "", "", "", ""));

        mediaObjectList.add(new MediaObject("", "", "", "", "", "", "", "", ""));

        mediaObjectList.add(new MediaObject("", "", "", "", "", "", "", "", ""));

        mediaObjectList.add(new MediaObject("", "", "", "", "", "", "", "", ""));

        mediaObjectList.add(new MediaObject("", "", "", "", "", "", "", "", ""));

        demoAdapter = new DemoAdapter(mediaObjectList,  getActivity().getApplicationContext());
        recyclerView.setAdapter(demoAdapter);
        demoAdapter.notifyDataSetChanged();

        return v;

    }


}
