package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class Home_Fragment extends Fragment
{

    private View PostView;

    private RecyclerView    recyclerView;
    private DatabaseReference  UsersRef, PostRef;
    private FirebaseAuth mAuth;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        PostView = inflater.inflate(R.layout.fragment_home_, container, false);




        recyclerView =(RecyclerView) PostView.findViewById(R.id.RecyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return PostView;




    }



}

